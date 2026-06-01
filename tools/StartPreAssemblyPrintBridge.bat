@echo off
cd /d "%~dp0"

net session >nul 2>&1
if not "%errorlevel%"=="0" (
    echo Requesting administrator rights to free port 8787 and allow firewall access...
    powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "Start-Process -FilePath '%~f0' -Verb RunAs"
    exit /b
)

echo For the Android emulator, start this bridge on this computer.
echo For a real Android device, start it on the printer computer: 192.168.10.104
echo The Android app sends the exact Windows printer name. Current default target: HP LaserJet MFP M129-M134
echo Current computer IPv4 addresses:
powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "Get-NetIPAddress -AddressFamily IPv4 | Where-Object { $_.IPAddress -ne '127.0.0.1' -and $_.IPAddress -notlike '169.254*' } | ForEach-Object { Write-Host ('  ' + $_.IPAddress + '  ' + $_.InterfaceAlias) }"
echo.

echo Allowing inbound TCP 8787 in Windows Firewall...
powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "$rule = Get-NetFirewallRule -DisplayName 'PreAssembly Print Bridge 8787' -ErrorAction SilentlyContinue; if (-not $rule) { New-NetFirewallRule -DisplayName 'PreAssembly Print Bridge 8787' -Direction Inbound -Action Allow -Protocol TCP -LocalPort 8787 | Out-Null; Write-Host 'Firewall rule created.' } else { Write-Host 'Firewall rule already exists.' }"
if errorlevel 1 (
    echo Failed to configure firewall rule.
    pause
    exit /b 1
)
echo.

echo Stopping old print bridge on port 8787 if it is running...
powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "$listeners = Get-NetTCPConnection -LocalPort 8787 -State Listen -ErrorAction SilentlyContinue; $pids = $listeners | Select-Object -ExpandProperty OwningProcess -Unique; foreach ($pidValue in $pids) { Write-Host ('Stopping PID ' + $pidValue); Stop-Process -Id $pidValue -Force -ErrorAction SilentlyContinue }; Start-Sleep -Seconds 1; $left = Get-NetTCPConnection -LocalPort 8787 -State Listen -ErrorAction SilentlyContinue; if ($left) { Write-Host 'Port 8787 is still busy:'; $left | ForEach-Object { $process = Get-Process -Id $_.OwningProcess -ErrorAction SilentlyContinue; Write-Host ('  PID ' + $_.OwningProcess + ' ' + $process.ProcessName) }; exit 1 } else { Write-Host 'Port 8787 is free.' }"
if errorlevel 1 (
    echo Cannot start print bridge because port 8787 is still busy.
    pause
    exit /b 1
)

echo.
echo Starting print bridge for printer HP LaserJet MFP M129-M134 on port 8787.
echo Keep this window open while printing from the Android app.
echo Log file: %~dp0PreAssemblyPrintBridge.log
echo After this window starts, run TestPreAssemblyPrintBridge.bat in another window to verify printing.
echo.
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0PreAssemblyPrintBridge.ps1" -PrinterName "HP LaserJet MFP M129-M134" -Port 8787
pause
