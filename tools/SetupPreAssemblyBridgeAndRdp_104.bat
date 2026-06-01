@echo off
setlocal
cd /d "%~dp0"

set "PRINTER=HP LaserJet MFP M129-M134"
set "PORT=8787"
set "TASK_NAME=PreAssemblyPrintBridge"

net session >nul 2>&1
if not "%errorlevel%"=="0" (
    echo Requesting administrator rights...
    powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "Start-Process -FilePath '%~f0' -Verb RunAs"
    exit /b
)

if not exist "%~dp0PreAssemblyPrintBridge.ps1" (
    echo ERROR: PreAssemblyPrintBridge.ps1 was not found near this BAT file.
    echo Put this BAT into the same folder as PreAssemblyPrintBridge.ps1.
    pause
    exit /b 1
)

echo Configuring PreAssembly print bridge on this computer...
echo Printer: %PRINTER%
echo Port: %PORT%
echo.

powershell.exe -NoProfile -ExecutionPolicy Bypass -Command ^
"$ErrorActionPreference='Stop';" ^
"$script = Join-Path '%~dp0' 'PreAssemblyPrintBridge.ps1';" ^
"$printer = '%PRINTER%';" ^
"$port = %PORT%;" ^
"$taskName = '%TASK_NAME%';" ^
"Write-Host '1/7 Setting active network profile to Private when possible...';" ^
"Get-NetConnectionProfile | Where-Object { $_.IPv4Connectivity -ne 'NoTraffic' -and $_.NetworkCategory -ne 'DomainAuthenticated' } | ForEach-Object { try { Set-NetConnectionProfile -InterfaceIndex $_.InterfaceIndex -NetworkCategory Private -ErrorAction Stop; Write-Host ('   Private: ' + $_.Name) } catch { Write-Host ('   Could not change: ' + $_.Name + ' - ' + $_.Exception.Message) } };" ^
"Write-Host '2/7 Opening firewall for bridge port...';" ^
"$rule = Get-NetFirewallRule -DisplayName 'PreAssembly Print Bridge 8787' -ErrorAction SilentlyContinue;" ^
"if ($rule) { $rule | Set-NetFirewallRule -Enabled True -Profile Any -Action Allow | Out-Null } else { New-NetFirewallRule -DisplayName 'PreAssembly Print Bridge 8787' -Direction Inbound -Action Allow -Protocol TCP -LocalPort $port -Profile Any | Out-Null };" ^
"Write-Host '3/7 Enabling Remote Desktop firewall and service...';" ^
"Set-ItemProperty 'HKLM:\System\CurrentControlSet\Control\Terminal Server' -Name fDenyTSConnections -Value 0;" ^
"Enable-NetFirewallRule -Group '@FirewallAPI.dll,-28752' -ErrorAction SilentlyContinue | Out-Null;" ^
"Set-Service -Name TermService -StartupType Automatic -ErrorAction SilentlyContinue;" ^
"Start-Service -Name TermService -ErrorAction SilentlyContinue;" ^
"try { $rdpGroup = ([System.Security.Principal.SecurityIdentifier]'S-1-5-32-555').Translate([System.Security.Principal.NTAccount]).Value.Split('\')[-1]; net localgroup $rdpGroup Office /add | Out-Host } catch { Write-Host ('   Could not add Office to RDP group: ' + $_.Exception.Message) };" ^
"Write-Host '4/7 Checking printer exists...';" ^
"$printerInfo = Get-Printer -Name $printer -ErrorAction SilentlyContinue;" ^
"if (-not $printerInfo) { $printerInfo = Get-Printer | Where-Object { $_.ShareName -eq 'bx-proizv' } | Select-Object -First 1 };" ^
"if (-not $printerInfo) { Write-Host 'ERROR: HP printer was not found. Available printers:'; Get-Printer | Select-Object Name,ShareName,PrinterStatus | Format-Table -AutoSize; exit 2 };" ^
"Write-Host ('   Found printer: ' + $printerInfo.Name + ' / share: ' + $printerInfo.ShareName);" ^
"Write-Host '5/7 Stopping old bridge process if it exists...';" ^
"Get-CimInstance Win32_Process | Where-Object { $_.CommandLine -like '*PreAssemblyPrintBridge.ps1*' } | ForEach-Object { try { Stop-Process -Id $_.ProcessId -Force -ErrorAction Stop; Write-Host ('   Stopped PID ' + $_.ProcessId) } catch { Write-Host ('   Could not stop PID ' + $_.ProcessId + ': ' + $_.Exception.Message) } };" ^
"Start-Sleep -Seconds 1;" ^
"Write-Host '6/7 Creating autostart task at user logon...';" ^
"$argument = '-NoProfile -ExecutionPolicy Bypass -WindowStyle Hidden -File ' + [char]34 + $script + [char]34 + ' -PrinterName ' + [char]34 + $printerInfo.Name + [char]34 + ' -Port ' + $port;" ^
"$action = New-ScheduledTaskAction -Execute 'powershell.exe' -Argument $argument;" ^
"$trigger = New-ScheduledTaskTrigger -AtLogOn;" ^
"Register-ScheduledTask -TaskName $taskName -Action $action -Trigger $trigger -Description 'PreAssembly Android print bridge' -RunLevel Highest -Force | Out-Null;" ^
"Write-Host '7/7 Starting bridge now...';" ^
"$out = Join-Path '%~dp0' 'PreAssemblyPrintBridge.stdout.log';" ^
"$err = Join-Path '%~dp0' 'PreAssemblyPrintBridge.stderr.log';" ^
"$proc = Start-Process -FilePath powershell.exe -ArgumentList $argument -WindowStyle Hidden -RedirectStandardOutput $out -RedirectStandardError $err -PassThru;" ^
"Set-Content -LiteralPath (Join-Path '%~dp0' 'PreAssemblyPrintBridge.pid') -Value $proc.Id -Encoding ASCII;" ^
"Start-Sleep -Seconds 2;" ^
"$health = Invoke-WebRequest -Uri ('http://127.0.0.1:' + $port + '/health') -UseBasicParsing -TimeoutSec 6;" ^
"Write-Host ('OK: bridge PID ' + $proc.Id);" ^
"Write-Host ('HTTP ' + $health.StatusCode + ': ' + $health.Content);" ^
"Write-Host '';" ^
"Write-Host 'From phone use bridge IP: 192.168.10.104';"

if errorlevel 1 (
    echo.
    echo Setup failed. Check the messages above and PreAssemblyPrintBridge.log.
) else (
    echo.
    echo Setup finished. Keep this folder on this computer.
)

pause
