$ErrorActionPreference = "Continue"

$RemoteHost = "192.168.10.104"
$PrinterName = "HP LaserJet MFP M129-M134"
$RemoteDir = "\\$RemoteHost\C$\PreAssemblyPrintBridge"
$RemoteLocalDir = "C:\PreAssemblyPrintBridge"
$CredentialPath = Join-Path $PSScriptRoot "remote_office_credential.xml"
$LogPath = Join-Path $PSScriptRoot "ConfigureRemote104Bridge.log"

function Write-Step {
    param([string]$Message)
    $line = "$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss') $Message"
    Write-Host $line
    Add-Content -LiteralPath $LogPath -Value $line -Encoding UTF8
}

function Run-Net {
    param([string[]]$ArgsList)
    Write-Step ("net.exe " + ($ArgsList -join " "))
    & net.exe @ArgsList 2>&1 | ForEach-Object { Write-Step ("  " + $_) }
    Write-Step "  exit=$LASTEXITCODE"
    return $LASTEXITCODE
}

Write-Step "=== Configure remote PreAssembly bridge started ==="

$principal = New-Object Security.Principal.WindowsPrincipal([Security.Principal.WindowsIdentity]::GetCurrent())
if (-not $principal.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)) {
    Write-Step "ERROR: script is not elevated."
    exit 1
}

if (-not (Test-Path -LiteralPath $CredentialPath)) {
    Write-Step "ERROR: credential file not found: $CredentialPath"
    exit 1
}

$cred = Import-Clixml -LiteralPath $CredentialPath
$password = $cred.GetNetworkCredential().Password
$user = "192.168.10.104\Office"

$before = Get-SmbClientConfiguration | Select-Object RequireSecuritySignature, EnableSecuritySignature
Write-Step "Initial SMB RequireSecuritySignature=$($before.RequireSecuritySignature)"

try {
    $eth = Get-NetIPInterface -AddressFamily IPv4 | Where-Object { $_.InterfaceAlias -eq "Ethernet" } | Select-Object -First 1
    if ($eth) {
        route delete $RemoteHost | Out-Null
        route add $RemoteHost mask 255.255.255.255 192.168.0.1 if $($eth.InterfaceIndex) metric 1 | Out-Null
        Write-Step "Added host route to $RemoteHost via Ethernet interface $($eth.InterfaceIndex)"
    } else {
        Write-Step "WARN: Ethernet interface not found, route unchanged"
    }

    Write-Step "Temporarily disabling SMB client required signing"
    Set-SmbClientConfiguration -RequireSecuritySignature $false -Force | Out-Null
    try {
        Restart-Service LanmanWorkstation -Force -ErrorAction Stop
        Start-Sleep -Seconds 2
        Write-Step "LanmanWorkstation restarted"
    } catch {
        Write-Step "WARN: could not restart LanmanWorkstation: $($_.Exception.Message)"
    }

    Run-Net @("use", "\\$RemoteHost\C$", "/delete", "/y") | Out-Null
    $connectExit = Run-Net @("use", "\\$RemoteHost\C$", $password, "/user:$user", "/persistent:no")
    if ($connectExit -ne 0) {
        Write-Step "ERROR: cannot connect to \\$RemoteHost\C$ as $user"
        exit 2
    }

    Write-Step "Connected to \\$RemoteHost\C$"
    New-Item -ItemType Directory -Path $RemoteDir -Force | Out-Null
    Copy-Item -LiteralPath (Join-Path $PSScriptRoot "PreAssemblyPrintBridge.ps1") -Destination $RemoteDir -Force
    Copy-Item -LiteralPath (Join-Path $PSScriptRoot "CheckPreAssemblyPrintBridge.bat") -Destination $RemoteDir -Force
    Copy-Item -LiteralPath (Join-Path $PSScriptRoot "StopPreAssemblyPrintBridge.bat") -Destination $RemoteDir -Force
    Copy-Item -LiteralPath (Join-Path $PSScriptRoot "StartPreAssemblyPrintBridgeBackground.bat") -Destination $RemoteDir -Force
    Write-Step "Bridge files copied to $RemoteLocalDir"

    $remoteStart = @"
`$ErrorActionPreference = "Stop"
Set-Location "$RemoteLocalDir"
`$rule = Get-NetFirewallRule -DisplayName "PreAssembly Print Bridge 8787" -ErrorAction SilentlyContinue
if (-not `$rule) {
    New-NetFirewallRule -DisplayName "PreAssembly Print Bridge 8787" -Direction Inbound -Action Allow -Protocol TCP -LocalPort 8787 | Out-Null
} else {
    Set-NetFirewallRule -DisplayName "PreAssembly Print Bridge 8787" -Enabled True -Action Allow | Out-Null
}
`$listeners = Get-NetTCPConnection -LocalPort 8787 -State Listen -ErrorAction SilentlyContinue
foreach (`$listener in `$listeners) {
    Stop-Process -Id `$listener.OwningProcess -Force -ErrorAction SilentlyContinue
}
`$script = Join-Path "$RemoteLocalDir" "PreAssemblyPrintBridge.ps1"
`$out = Join-Path "$RemoteLocalDir" "PreAssemblyPrintBridge.stdout.log"
`$err = Join-Path "$RemoteLocalDir" "PreAssemblyPrintBridge.stderr.log"
`$pidFile = Join-Path "$RemoteLocalDir" "PreAssemblyPrintBridge.pid"
`$q = [char]34
`$argsList = "-NoProfile -ExecutionPolicy Bypass -File " + `$q + `$script + `$q + " -PrinterName " + `$q + "$PrinterName" + `$q + " -Port 8787"
`$proc = Start-Process -FilePath powershell.exe -ArgumentList `$argsList -WindowStyle Hidden -RedirectStandardOutput `$out -RedirectStandardError `$err -PassThru
Set-Content -LiteralPath `$pidFile -Value `$proc.Id -Encoding ASCII
Start-Sleep -Seconds 2
try {
    `$health = Invoke-WebRequest -Uri "http://127.0.0.1:8787/health" -UseBasicParsing -TimeoutSec 8
    "HTTP " + `$health.StatusCode + ": " + `$health.Content | Set-Content -LiteralPath (Join-Path "$RemoteLocalDir" "last_health.txt") -Encoding UTF8
} catch {
    "ERROR: " + `$_.Exception.Message | Set-Content -LiteralPath (Join-Path "$RemoteLocalDir" "last_health.txt") -Encoding UTF8
}
"@
    $remoteStartPath = Join-Path $RemoteDir "RemoteStartBridge.ps1"
    Set-Content -LiteralPath $remoteStartPath -Value $remoteStart -Encoding UTF8
    Write-Step "Remote start script written"

    $taskName = "PreAssemblyPrintBridge"
    $taskCommand = "powershell.exe -NoProfile -ExecutionPolicy Bypass -WindowStyle Hidden -File `"$RemoteLocalDir\RemoteStartBridge.ps1`""

    Write-Step "Creating remote scheduled task"
    & schtasks.exe /Create /S $RemoteHost /U $user /P $password /TN $taskName /TR $taskCommand /SC ONSTART /RL HIGHEST /F 2>&1 |
        ForEach-Object { Write-Step ("  " + $_) }
    Write-Step "schtasks create exit=$LASTEXITCODE"

    Write-Step "Running remote scheduled task"
    & schtasks.exe /Run /S $RemoteHost /U $user /P $password /TN $taskName 2>&1 |
        ForEach-Object { Write-Step ("  " + $_) }
    Write-Step "schtasks run exit=$LASTEXITCODE"

    Start-Sleep -Seconds 5
    try {
        $health = Invoke-WebRequest -Uri "http://$RemoteHost:8787/health" -UseBasicParsing -TimeoutSec 8
        Write-Step "Remote health from this PC: HTTP $($health.StatusCode): $($health.Content)"
    } catch {
        Write-Step "Remote health failed from this PC: $($_.Exception.Message)"
    }

    $remoteHealthFile = Join-Path $RemoteDir "last_health.txt"
    if (Test-Path -LiteralPath $remoteHealthFile) {
        Write-Step ("Remote local health file: " + (Get-Content -LiteralPath $remoteHealthFile -Raw))
    } else {
        Write-Step "Remote local health file not found"
    }

    Run-Net @("use", "\\$RemoteHost\C$", "/delete", "/y") | Out-Null
} finally {
    Write-Step "Restoring SMB client required signing to $($before.RequireSecuritySignature)"
    Set-SmbClientConfiguration -RequireSecuritySignature $before.RequireSecuritySignature -Force | Out-Null
    try {
        Restart-Service LanmanWorkstation -Force -ErrorAction Stop
        Start-Sleep -Seconds 2
        Write-Step "LanmanWorkstation restored"
    } catch {
        Write-Step "WARN: could not restart LanmanWorkstation during restore: $($_.Exception.Message)"
    }
    Remove-Item -LiteralPath $CredentialPath -Force -ErrorAction SilentlyContinue
    Write-Step "Credential file removed"
}

Write-Step "=== Configure remote PreAssembly bridge finished ==="
