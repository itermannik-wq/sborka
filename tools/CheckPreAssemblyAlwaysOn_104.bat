@echo off
setlocal EnableExtensions
cd /d "%~dp0"

echo Checking bridge health...
powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "try { $h = Invoke-WebRequest -Uri 'http://127.0.0.1:8787/health' -UseBasicParsing -TimeoutSec 8; Write-Host ('LOCAL HTTP ' + $h.StatusCode + ': ' + $h.Content) } catch { Write-Host ('LOCAL ERROR: ' + $_.Exception.Message) }; try { Get-ScheduledTask -TaskName 'PreAssemblyPrintBridgeWatchdog*' | Select-Object TaskName,State | Format-Table -AutoSize } catch { Write-Host ('TASK ERROR: ' + $_.Exception.Message) }; Get-Process powershell -ErrorAction SilentlyContinue | Where-Object { $_.Path -or $_.ProcessName } | Select-Object Id,ProcessName,StartTime | Format-Table -AutoSize"

echo.
echo Last watchdog log lines:
powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "$p = Join-Path '%~dp0' 'PreAssemblyPrintBridgeWatchdog.log'; if (Test-Path -LiteralPath $p) { Get-Content -LiteralPath $p -Tail 40 } else { Write-Host 'No watchdog log yet.' }"

pause
