@echo off
cd /d "%~dp0"

net session >nul 2>&1
if not "%errorlevel%"=="0" (
    echo Requesting administrator rights to allow phone access to port 8787...
    powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "Start-Process -FilePath '%~f0' -Verb RunAs"
    exit /b
)

echo Starting PreAssembly print bridge in background on port 8787...
powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $rule = Get-NetFirewallRule -DisplayName 'PreAssembly Print Bridge 8787' -ErrorAction SilentlyContinue; if (-not $rule) { New-NetFirewallRule -DisplayName 'PreAssembly Print Bridge 8787' -Direction Inbound -Action Allow -Protocol TCP -LocalPort 8787 | Out-Null; Write-Host 'Firewall rule created.' } else { Write-Host 'Firewall rule already exists.' }; $existing = Get-NetTCPConnection -LocalPort 8787 -State Listen -ErrorAction SilentlyContinue; if ($existing) { Write-Host 'Port 8787 is already listening:'; $existing | Format-Table -AutoSize; exit 0 }; $printer = 'HP LaserJet MFP M129-M134'; $script = Join-Path '%~dp0' 'PreAssemblyPrintBridge.ps1'; $out = Join-Path '%~dp0' 'PreAssemblyPrintBridge.stdout.log'; $err = Join-Path '%~dp0' 'PreAssemblyPrintBridge.stderr.log'; $q = [char]34; $argsList = '-NoProfile -ExecutionPolicy Bypass -File ' + $q + $script + $q + ' -PrinterName ' + $q + $printer + $q + ' -Port 8787'; $proc = Start-Process -FilePath powershell.exe -ArgumentList $argsList -WindowStyle Minimized -RedirectStandardOutput $out -RedirectStandardError $err -PassThru; Set-Content -LiteralPath (Join-Path '%~dp0' 'PreAssemblyPrintBridge.pid') -Value $proc.Id -Encoding ASCII; Start-Sleep -Seconds 2; $health = Invoke-WebRequest -Uri 'http://127.0.0.1:8787/health' -UseBasicParsing -TimeoutSec 5; Write-Host ('Started PID ' + $proc.Id); Write-Host ('HTTP ' + $health.StatusCode + ': ' + $health.Content)"

if errorlevel 1 (
    echo.
    echo Bridge failed to start. Check PreAssemblyPrintBridge.log and PreAssemblyPrintBridge.stderr.log.
) else (
    echo.
    echo Bridge is running in background. You can close this window.
)

pause
