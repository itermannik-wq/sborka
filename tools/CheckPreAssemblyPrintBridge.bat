@echo off
cd /d "%~dp0"

echo Checking PreAssembly print bridge...
powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "Write-Host 'Local health:'; try { $local = Invoke-WebRequest -Uri 'http://127.0.0.1:8787/health' -UseBasicParsing -TimeoutSec 5; Write-Host ('  HTTP ' + $local.StatusCode + ': ' + $local.Content) } catch { Write-Host ('  ERROR: ' + $_.Exception.Message) }; Write-Host ''; Write-Host 'Windows printers:'; Get-Printer | Select-Object Name,ShareName,PrinterStatus,Default | Format-Table -AutoSize; Write-Host ''; Write-Host 'Listeners on 8787:'; $listeners = Get-NetTCPConnection -LocalPort 8787 -State Listen -ErrorAction SilentlyContinue; if ($listeners) { $listeners | Select-Object LocalAddress,LocalPort,OwningProcess | Format-Table -AutoSize } else { Write-Host '  none' }; Write-Host ''; Write-Host 'Recent bridge log:'; if (Test-Path '%~dp0PreAssemblyPrintBridge.log') { Get-Content -LiteralPath '%~dp0PreAssemblyPrintBridge.log' -Tail 20 } else { Write-Host '  no log file' }"

pause
