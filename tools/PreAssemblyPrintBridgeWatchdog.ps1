param(
    [string]$PrinterName = "HP LaserJet MFP M129-M134",
    [int]$Port = 8787,
    [int]$IntervalSeconds = 20,
    [int]$HealthTimeoutSeconds = 5,
    [switch]$Once
)

$ErrorActionPreference = "Continue"
$BridgeScript = Join-Path $PSScriptRoot "PreAssemblyPrintBridge.ps1"
$LogPath = Join-Path $PSScriptRoot "PreAssemblyPrintBridgeWatchdog.log"
$PidPath = Join-Path $PSScriptRoot "PreAssemblyPrintBridge.pid"
$StdoutPath = Join-Path $PSScriptRoot "PreAssemblyPrintBridge.stdout.log"
$StderrPath = Join-Path $PSScriptRoot "PreAssemblyPrintBridge.stderr.log"
$Mutex = New-Object System.Threading.Mutex($false, "Global\PreAssemblyPrintBridgeWatchdog")

function Write-WatchdogLog {
    param([string]$Message)
    $line = "$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss') $Message"
    Write-Host $line
    Add-Content -LiteralPath $LogPath -Value $line -Encoding UTF8
}

function Ensure-FirewallRule {
    try {
        $rule = Get-NetFirewallRule -DisplayName "PreAssembly Print Bridge 8787" -ErrorAction SilentlyContinue
        if ($rule) {
            $rule | Set-NetFirewallRule -Enabled True -Profile Any -Action Allow | Out-Null
        } else {
            New-NetFirewallRule -DisplayName "PreAssembly Print Bridge 8787" -Direction Inbound -Action Allow -Protocol TCP -LocalPort $Port -Profile Any | Out-Null
        }
    } catch {
        Write-WatchdogLog "firewall warning: $($_.Exception.Message)"
    }
}

function Test-BridgeHealth {
    try {
        $response = Invoke-WebRequest -Uri "http://127.0.0.1:$Port/health" -UseBasicParsing -TimeoutSec $HealthTimeoutSeconds
        $content = [string]$response.Content
        if ($response.StatusCode -eq 200 -and $content -like "*PreAssemblyPrintBridge OK*" -and $content -notlike "*NOT FOUND*") {
            return [pscustomobject]@{ Ok = $true; Message = $content }
        }
        return [pscustomobject]@{ Ok = $false; Message = "bad health response: HTTP $($response.StatusCode): $content" }
    } catch {
        return [pscustomobject]@{ Ok = $false; Message = $_.Exception.Message }
    }
}

function Stop-OldBridge {
    $pids = New-Object "System.Collections.Generic.HashSet[int]"
    $pidFromFile = $null

    if (Test-Path -LiteralPath $PidPath) {
        $pidText = (Get-Content -LiteralPath $PidPath -Raw -ErrorAction SilentlyContinue).Trim()
        if ($pidText -match "^\d+$") {
            $pidFromFile = [int]$pidText
            [void]$pids.Add($pidFromFile)
        }
    }

    try {
        Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue |
            Select-Object -ExpandProperty OwningProcess -Unique |
            ForEach-Object { if ($_ -and $_ -ne $PID) { [void]$pids.Add([int]$_) } }
    } catch {
        Write-WatchdogLog "port owner lookup warning: $($_.Exception.Message)"
    }

    foreach ($processId in $pids) {
        if ($processId -eq $PID) {
            continue
        }
        $process = Get-CimInstance Win32_Process -Filter "ProcessId=$processId" -ErrorAction SilentlyContinue
        $commandLine = if ($process) { [string]$process.CommandLine } else { "" }
        if ($commandLine -like "*PreAssemblyPrintBridge.ps1*" -or ($pidFromFile -ne $null -and $processId -eq $pidFromFile)) {
            try {
                Stop-Process -Id $processId -Force -ErrorAction Stop
                Write-WatchdogLog "stopped stale bridge PID $processId"
            } catch {
                Write-WatchdogLog "could not stop PID ${processId}: $($_.Exception.Message)"
            }
        } else {
            Write-WatchdogLog "port $Port is used by PID $processId, not stopping unknown process: $commandLine"
        }
    }
}

function Start-Bridge {
    if (-not (Test-Path -LiteralPath $BridgeScript)) {
        throw "Bridge script not found: $BridgeScript"
    }

    Ensure-FirewallRule
    Stop-OldBridge
    Start-Sleep -Seconds 1

    $argsList = @(
        "-NoProfile",
        "-ExecutionPolicy", "Bypass",
        "-File", $BridgeScript,
        "-PrinterName", $PrinterName,
        "-Port", "$Port"
    )

    $process = Start-Process -FilePath powershell.exe -ArgumentList $argsList -WindowStyle Hidden -RedirectStandardOutput $StdoutPath -RedirectStandardError $StderrPath -PassThru
    Set-Content -LiteralPath $PidPath -Value $process.Id -Encoding ASCII
    Write-WatchdogLog "started bridge PID $($process.Id), printer '$PrinterName', port $Port"
}

if (-not $Mutex.WaitOne(0)) {
    Write-WatchdogLog "another watchdog instance is already running; exiting. Once=$($Once.IsPresent)"
    exit 0
}

Write-WatchdogLog "watchdog started. Printer='$PrinterName', Port=$Port, IntervalSeconds=$IntervalSeconds, Once=$($Once.IsPresent)"
Ensure-FirewallRule
$lastHealthyLog = [datetime]::MinValue

try {
    do {
        try {
            $health = Test-BridgeHealth
            if ($health.Ok) {
                if (((Get-Date) - $lastHealthyLog).TotalMinutes -ge 10) {
                    Write-WatchdogLog "bridge healthy: $($health.Message)"
                    $lastHealthyLog = Get-Date
                }
            } else {
                Write-WatchdogLog "bridge unhealthy: $($health.Message)"
                Start-Bridge
                Start-Sleep -Seconds 3
                $afterStart = Test-BridgeHealth
                if ($afterStart.Ok) {
                    Write-WatchdogLog "bridge recovered: $($afterStart.Message)"
                    $lastHealthyLog = Get-Date
                } else {
                    Write-WatchdogLog "bridge still unhealthy after restart: $($afterStart.Message)"
                }
            }
        } catch {
            Write-WatchdogLog "watchdog cycle error: $($_.Exception.Message)"
        }

        if ($Once) {
            break
        }
        Start-Sleep -Seconds $IntervalSeconds
    } while ($true)
} finally {
    try {
        $Mutex.ReleaseMutex()
    } catch {
    }
    $Mutex.Dispose()
}
