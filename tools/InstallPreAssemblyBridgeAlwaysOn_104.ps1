param(
    [string]$PrinterName = "HP LaserJet MFP M129-M134",
    [int]$Port = 8787,
    [int]$IntervalSeconds = 20
)

$ErrorActionPreference = "Continue"
$LogPath = Join-Path $PSScriptRoot "alwayson_setup_result.txt"
$BridgeScript = Join-Path $PSScriptRoot "PreAssemblyPrintBridge.ps1"
$WatchdogScript = Join-Path $PSScriptRoot "PreAssemblyPrintBridgeWatchdog.ps1"
$WatchdogVbs = Join-Path $PSScriptRoot "StartPreAssemblyWatchdog.vbs"
$WatchdogOnceVbs = Join-Path $PSScriptRoot "StartPreAssemblyWatchdogOnce.vbs"
$TaskName = "PreAssemblyPrintBridgeWatchdog"
$StartupTaskName = "PreAssemblyPrintBridgeWatchdogStartup"
$MinuteTaskName = "PreAssemblyPrintBridgeWatchdogMinute"

function Write-SetupLog {
    param([string]$Message)
    $line = "$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss') $Message"
    Write-Host $line
    Add-Content -LiteralPath $LogPath -Value $line -Encoding UTF8
}

function New-BridgeTaskSettings {
    param([bool]$LongRunning)

    try {
        if ($LongRunning) {
            return New-ScheduledTaskSettingsSet -MultipleInstances IgnoreNew -RestartCount 999 -RestartInterval (New-TimeSpan -Minutes 1) -ExecutionTimeLimit (New-TimeSpan -Seconds 0)
        }
        return New-ScheduledTaskSettingsSet -MultipleInstances IgnoreNew -ExecutionTimeLimit (New-TimeSpan -Minutes 5)
    } catch {
        Write-SetupLog "Task settings fallback: $($_.Exception.Message)"
        return New-ScheduledTaskSettingsSet -MultipleInstances IgnoreNew
    }
}

Set-Content -LiteralPath $LogPath -Value "==== PreAssembly always-on setup started $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss') ====" -Encoding UTF8

try {
    Write-SetupLog "Folder: $PSScriptRoot"
    Write-SetupLog "Printer: $PrinterName"
    Write-SetupLog "Port: $Port"

    if (-not (Test-Path -LiteralPath $BridgeScript)) {
        throw "Missing $BridgeScript"
    }
    if (-not (Test-Path -LiteralPath $WatchdogScript)) {
        throw "Missing $WatchdogScript"
    }
    if (-not (Test-Path -LiteralPath $WatchdogVbs)) {
        throw "Missing $WatchdogVbs"
    }
    if (-not (Test-Path -LiteralPath $WatchdogOnceVbs)) {
        throw "Missing $WatchdogOnceVbs"
    }

    Write-SetupLog "Checking printer..."
    $printerInfo = Get-Printer -Name $PrinterName -ErrorAction SilentlyContinue
    if (-not $printerInfo) {
        $printerInfo = Get-Printer | Where-Object { $_.ShareName -eq "bx-proizv" } | Select-Object -First 1
    }
    if (-not $printerInfo) {
        Write-SetupLog "ERROR: printer was not found. Available printers:"
        Get-Printer | Select-Object Name,ShareName,PrinterStatus | Format-Table -AutoSize | Out-String | ForEach-Object { Write-SetupLog $_.TrimEnd() }
        throw "Printer was not found"
    }
    $PrinterName = $printerInfo.Name
    Write-SetupLog "Found printer: $($printerInfo.Name), share: $($printerInfo.ShareName)"

    Write-SetupLog "Setting active network profile to Private when possible..."
    Get-NetConnectionProfile |
        Where-Object { $_.IPv4Connectivity -ne "NoTraffic" -and $_.NetworkCategory -ne "DomainAuthenticated" } |
        ForEach-Object {
            try {
                Set-NetConnectionProfile -InterfaceIndex $_.InterfaceIndex -NetworkCategory Private -ErrorAction Stop
                Write-SetupLog "Private: $($_.Name)"
            } catch {
                Write-SetupLog "Could not set Private for $($_.Name): $($_.Exception.Message)"
            }
        }

    Write-SetupLog "Opening firewall for TCP $Port..."
    $rule = Get-NetFirewallRule -DisplayName "PreAssembly Print Bridge 8787" -ErrorAction SilentlyContinue
    if ($rule) {
        $rule | Set-NetFirewallRule -Enabled True -Profile Any -Action Allow | Out-Null
    } else {
        New-NetFirewallRule -DisplayName "PreAssembly Print Bridge 8787" -Direction Inbound -Action Allow -Protocol TCP -LocalPort $Port -Profile Any | Out-Null
    }

    Write-SetupLog "Stopping old bridge/watchdog processes..."
    foreach ($pattern in @("*PreAssemblyPrintBridgeWatchdog.ps1*", "*PreAssemblyPrintBridge.ps1*")) {
        Get-CimInstance Win32_Process |
            Where-Object { $_.ProcessId -ne $PID -and $_.CommandLine -like $pattern } |
            ForEach-Object {
                try {
                    Stop-Process -Id $_.ProcessId -Force -ErrorAction Stop
                    Write-SetupLog "Stopped PID $($_.ProcessId): $($_.CommandLine)"
                } catch {
                    Write-SetupLog "Could not stop PID $($_.ProcessId): $($_.Exception.Message)"
                }
            }
    }

    Write-SetupLog "Registering logon watchdog task..."
    $action = New-ScheduledTaskAction -Execute "wscript.exe" -Argument "`"$WatchdogVbs`""
    $trigger = New-ScheduledTaskTrigger -AtLogOn
    $settings = New-BridgeTaskSettings -LongRunning $true
    Register-ScheduledTask -TaskName $TaskName -Action $action -Trigger $trigger -Settings $settings -Description "Keeps PreAssembly Android print bridge alive" -RunLevel Highest -Force | Out-Null
    Write-SetupLog "Registered task: $TaskName"

    Write-SetupLog "Registering one-shot startup task as SYSTEM..."
    $startupPrincipal = New-ScheduledTaskPrincipal -UserId "SYSTEM" -RunLevel Highest
    $startupTrigger = New-ScheduledTaskTrigger -AtStartup
    $startupAction = New-ScheduledTaskAction -Execute "wscript.exe" -Argument "`"$WatchdogOnceVbs`""
    $startupSettings = New-BridgeTaskSettings -LongRunning $false
    Register-ScheduledTask -TaskName $StartupTaskName -Action $startupAction -Trigger $startupTrigger -Settings $startupSettings -Principal $startupPrincipal -Description "Checks PreAssembly print bridge once at Windows startup" -Force | Out-Null
    Write-SetupLog "Registered task: $StartupTaskName"

    Write-SetupLog "Registering every-minute one-shot watchdog task..."
    try {
        $minuteCommand = 'wscript.exe "' + $WatchdogOnceVbs + '"'
        $minuteOutput = schtasks.exe /Create /TN $MinuteTaskName /TR $minuteCommand /SC MINUTE /MO 1 /RL HIGHEST /F 2>&1
        foreach ($line in $minuteOutput) { Write-SetupLog $line }
    } catch {
        Write-SetupLog "Minute task registration warning: $($_.Exception.Message)"
    }

    Write-SetupLog "Installing startup-folder fallback..."
    try {
        $startupFolder = [Environment]::GetFolderPath([Environment+SpecialFolder]::Startup)
        $startupCopy = Join-Path $startupFolder "StartPreAssemblyWatchdog.vbs"
        $startupVbs = @"
Set shell = CreateObject("WScript.Shell")
cmd = "powershell.exe -NoProfile -ExecutionPolicy Bypass -WindowStyle Hidden -File ""$WatchdogScript"" -PrinterName ""$PrinterName"" -Port $Port -IntervalSeconds $IntervalSeconds"
shell.Run cmd, 0, True
"@
        Set-Content -LiteralPath $startupCopy -Value $startupVbs -Encoding ASCII
        Write-SetupLog "Startup fallback installed: $startupCopy"
    } catch {
        Write-SetupLog "Startup fallback warning: $($_.Exception.Message)"
    }

    Write-SetupLog "Starting watchdog now via VBS launcher..."
    Start-Process -FilePath "wscript.exe" -ArgumentList "`"$WatchdogVbs`"" -WindowStyle Hidden -ErrorAction SilentlyContinue
    Start-ScheduledTask -TaskName $TaskName -ErrorAction SilentlyContinue
    Start-Sleep -Seconds 8

    Write-SetupLog "Checking local bridge health..."
    try {
        $health = Invoke-WebRequest -Uri "http://127.0.0.1:$Port/health" -UseBasicParsing -TimeoutSec 8
        Write-SetupLog "HTTP $($health.StatusCode): $($health.Content)"
    } catch {
        Write-SetupLog "Local health failed: $($_.Exception.Message)"
    }

    Write-SetupLog "Scheduled task state:"
    Get-ScheduledTask -TaskName "$TaskName*" -ErrorAction SilentlyContinue |
        Select-Object TaskName,State |
        Format-Table -AutoSize |
        Out-String |
        ForEach-Object { $_.TrimEnd() } |
        Where-Object { $_ } |
        ForEach-Object { Write-SetupLog $_ }

    Write-SetupLog "OK: always-on watchdog installed. From phone use bridge IP 192.168.10.104."
} catch {
    Write-SetupLog "SETUP ERROR: $($_.Exception.Message)"
    $_ | Format-List * -Force | Out-String | Add-Content -LiteralPath $LogPath -Encoding UTF8
    try {
        Write-SetupLog "Emergency bridge start after setup error..."
        $emergencyArgs = @(
            "-NoProfile",
            "-ExecutionPolicy", "Bypass",
            "-WindowStyle", "Hidden",
            "-File", $BridgeScript,
            "-PrinterName", $PrinterName,
            "-Port", "$Port"
        )
        $out = Join-Path $PSScriptRoot "PreAssemblyPrintBridge.stdout.log"
        $err = Join-Path $PSScriptRoot "PreAssemblyPrintBridge.stderr.log"
        $proc = Start-Process -FilePath powershell.exe -ArgumentList $emergencyArgs -WindowStyle Hidden -RedirectStandardOutput $out -RedirectStandardError $err -PassThru
        Set-Content -LiteralPath (Join-Path $PSScriptRoot "PreAssemblyPrintBridge.pid") -Value $proc.Id -Encoding ASCII
        Start-Sleep -Seconds 3
        $health = Invoke-WebRequest -Uri "http://127.0.0.1:$Port/health" -UseBasicParsing -TimeoutSec 8
        Write-SetupLog "Emergency bridge started PID $($proc.Id): HTTP $($health.StatusCode): $($health.Content)"
    } catch {
        Write-SetupLog "Emergency bridge start failed: $($_.Exception.Message)"
    }
    exit 1
}

Write-SetupLog "==== PreAssembly always-on setup finished $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss') ===="
exit 0
