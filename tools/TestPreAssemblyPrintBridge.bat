@echo off
cd /d "%~dp0"

echo Testing local bridge on this computer: http://127.0.0.1:8787
echo Start StartPreAssemblyPrintBridge.bat first and keep it open.
echo.

powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; Write-Host 'GET /health'; $health = Invoke-WebRequest -Uri 'http://127.0.0.1:8787/health' -UseBasicParsing -TimeoutSec 5; Write-Host ('  HTTP ' + $health.StatusCode + ' ' + $health.Content); Write-Host 'POST /print'; $body = @{ printer = 'HP LaserJet MFP M129-M134'; title = 'Bridge test'; text = ('Test page from PreAssembly bridge ' + (Get-Date -Format 'yyyy-MM-dd HH:mm:ss')) } | ConvertTo-Json -Compress; $print = Invoke-WebRequest -Uri 'http://127.0.0.1:8787/print' -Method Post -ContentType 'application/json; charset=utf-8' -Body $body -UseBasicParsing -TimeoutSec 60; Write-Host ('  HTTP ' + $print.StatusCode + ' ' + $print.Content)"

if errorlevel 1 (
    echo.
    echo Bridge test failed. Check PreAssemblyPrintBridge.log in this folder.
) else (
    echo.
    echo Bridge test finished.
)

pause
