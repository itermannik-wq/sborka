@echo off
setlocal EnableExtensions
cd /d "%~dp0"
title Install PreAssembly Bridge Always On

set "SETUP_PS1=%~dp0InstallPreAssemblyBridgeAlwaysOn_104.ps1"
set "WATCHDOG_PS1=%~dp0PreAssemblyPrintBridgeWatchdog.ps1"
set "BRIDGE_PS1=%~dp0PreAssemblyPrintBridge.ps1"
set "SETUP_LOG=%~dp0alwayson_setup_result.txt"

net session >nul 2>&1
if not "%errorlevel%"=="0" (
    echo Requesting administrator rights...
    powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "Start-Process -FilePath '%~f0' -Verb RunAs"
    echo If the administrator window closes, run this BAT again and send alwayson_setup_result.txt.
    pause
    exit /b
)

if not exist "%BRIDGE_PS1%" (
    echo ERROR: PreAssemblyPrintBridge.ps1 was not found near this BAT.
    pause
    exit /b 1
)
if not exist "%WATCHDOG_PS1%" (
    echo ERROR: PreAssemblyPrintBridgeWatchdog.ps1 was not found near this BAT.
    pause
    exit /b 1
)
if not exist "%SETUP_PS1%" (
    echo ERROR: InstallPreAssemblyBridgeAlwaysOn_104.ps1 was not found near this BAT.
    pause
    exit /b 1
)

echo Installing always-on PreAssembly print bridge...
echo Log: %SETUP_LOG%
echo.

powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%SETUP_PS1%" -PrinterName "HP LaserJet MFP M129-M134" -Port 8787 -IntervalSeconds 20
set "SETUP_EXIT=%errorlevel%"

echo.
echo Opening log in Notepad...
start notepad.exe "%SETUP_LOG%"

echo.
if "%SETUP_EXIT%"=="0" (
    echo Setup command finished. Check the log for OK.
) else (
    echo Setup command returned an error. Send me the log file.
)
echo.
pause
exit /b %SETUP_EXIT%
