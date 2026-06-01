@echo off
setlocal

set "BRIDGE=http://192.168.10.104:8787"
set "PRINTER=HP LaserJet MFP M129-M134"

echo Checking bridge health: %BRIDGE%/health
powershell.exe -NoProfile -ExecutionPolicy Bypass -Command ^
"$ErrorActionPreference='Stop';" ^
"$bridge='%BRIDGE%';" ^
"$printer='%PRINTER%';" ^
"$health = Invoke-WebRequest -Uri ($bridge + '/health') -UseBasicParsing -TimeoutSec 8;" ^
"Write-Host ('HTTP ' + $health.StatusCode + ': ' + $health.Content);" ^
"$payload = @{ printer = $printer; title = 'Test print from Windows BAT'; text = ('Test print from 192.168.10.104 bridge' + [Environment]::NewLine + 'Time: ' + (Get-Date -Format 'yyyy-MM-dd HH:mm:ss') + [Environment]::NewLine + 'Printer: ' + $printer) } | ConvertTo-Json -Compress;" ^
"$response = Invoke-WebRequest -Uri ($bridge + '/print') -Method Post -ContentType 'application/json; charset=utf-8' -Body ([System.Text.Encoding]::UTF8.GetBytes($payload)) -UseBasicParsing -TimeoutSec 20;" ^
"Write-Host ('PRINT HTTP ' + $response.StatusCode + ': ' + $response.Content);"

if errorlevel 1 (
    echo.
    echo Test failed. The bridge is not reachable or the printer returned an error.
) else (
    echo.
    echo Test sent successfully.
)

pause
