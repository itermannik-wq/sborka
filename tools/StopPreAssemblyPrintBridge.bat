@echo off
cd /d "%~dp0"

echo Stopping PreAssembly print bridge on port 8787...
powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "$pids = @(); if (Test-Path '%~dp0PreAssemblyPrintBridge.pid') { $pidText = Get-Content -LiteralPath '%~dp0PreAssemblyPrintBridge.pid' -ErrorAction SilentlyContinue | Select-Object -First 1; if ($pidText -match '^\d+$') { $pids += [int]$pidText } }; $listeners = Get-NetTCPConnection -LocalPort 8787 -State Listen -ErrorAction SilentlyContinue; $pids += @($listeners | Select-Object -ExpandProperty OwningProcess -Unique); $pids | Select-Object -Unique | ForEach-Object { Write-Host ('Stopping PID ' + $_); Stop-Process -Id $_ -Force -ErrorAction SilentlyContinue }; Remove-Item -LiteralPath '%~dp0PreAssemblyPrintBridge.pid' -Force -ErrorAction SilentlyContinue"

pause
