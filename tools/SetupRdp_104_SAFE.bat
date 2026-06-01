@echo off
setlocal EnableExtensions
cd /d "%~dp0"
title Setup RDP 104 SAFE

set "SETUP_PS1=%~dp0SetupRdp_104.ps1"
set "SETUP_LOG=%~dp0rdp_setup_result.txt"

net session >nul 2>&1
if not "%errorlevel%"=="0" (
    echo Requesting administrator rights...
    powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "Start-Process -FilePath '%~f0' -Verb RunAs"
    echo If the administrator window closes, run this BAT again and send rdp_setup_result.txt.
    pause
    exit /b
)

if not exist "%SETUP_PS1%" (
    echo ERROR: SetupRdp_104.ps1 was not found near this BAT.
    echo Put SetupRdp_104_SAFE.bat and SetupRdp_104.ps1 into the same folder.
    pause
    exit /b 1
)

echo Configuring Remote Desktop...
echo Log: %SETUP_LOG%
echo.

powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%SETUP_PS1%" -UserName "Office" -Port 3389
set "SETUP_EXIT=%errorlevel%"

echo.
echo Opening log in Notepad...
start notepad.exe "%SETUP_LOG%"

echo.
if "%SETUP_EXIT%"=="0" (
    echo Setup command finished. Check the log for OK or FAILED.
) else (
    echo Setup command returned an error. Send me the log file.
)
echo.
pause
exit /b %SETUP_EXIT%
