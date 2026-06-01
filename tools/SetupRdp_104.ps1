param(
    [string]$UserName = "Office",
    [int]$Port = 3389
)

$ErrorActionPreference = "Continue"
$logPath = Join-Path $PSScriptRoot "rdp_setup_result.txt"

function Write-Log {
    param([string]$Message)
    $line = "$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss') $Message"
    Write-Host $line
    Add-Content -LiteralPath $logPath -Value $line -Encoding UTF8
}

function Write-Section {
    param([string]$Title)
    Write-Log ""
    Write-Log "=== $Title ==="
}

Set-Content -LiteralPath $logPath -Value "==== RDP setup started $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss') ====" -Encoding UTF8

try {
    Write-Section "Windows"
    $os = Get-CimInstance Win32_OperatingSystem
    $editionId = (Get-ItemProperty "HKLM:\SOFTWARE\Microsoft\Windows NT\CurrentVersion" -ErrorAction SilentlyContinue).EditionID
    Write-Log "Caption: $($os.Caption)"
    Write-Log "Version: $($os.Version), Build: $($os.BuildNumber), ProductType: $($os.ProductType), EditionID: $editionId"
    if ($editionId -match "Core|Home") {
        Write-Log "WARNING: This looks like Windows Home/Core. Built-in incoming RDP may be unavailable on this edition."
    }

    Write-Section "Network profile"
    Get-NetConnectionProfile | ForEach-Object {
        Write-Log "Before: $($_.Name), InterfaceIndex=$($_.InterfaceIndex), Category=$($_.NetworkCategory), IPv4=$($_.IPv4Connectivity)"
        if ($_.IPv4Connectivity -ne "NoTraffic" -and $_.NetworkCategory -ne "DomainAuthenticated") {
            try {
                Set-NetConnectionProfile -InterfaceIndex $_.InterfaceIndex -NetworkCategory Private -ErrorAction Stop
                Write-Log "Set Private: $($_.Name)"
            } catch {
                Write-Log "Could not set Private for $($_.Name): $($_.Exception.Message)"
            }
        }
    }

    Write-Section "Enable RDP registry"
    reg add "HKLM\SYSTEM\CurrentControlSet\Control\Terminal Server" /v fDenyTSConnections /t REG_DWORD /d 0 /f | ForEach-Object { Write-Log $_ }
    reg add "HKLM\SYSTEM\CurrentControlSet\Control\Terminal Server\WinStations\RDP-Tcp" /v UserAuthentication /t REG_DWORD /d 0 /f | ForEach-Object { Write-Log $_ }
    reg add "HKLM\SYSTEM\CurrentControlSet\Control\Terminal Server\WinStations\RDP-Tcp" /v SecurityLayer /t REG_DWORD /d 1 /f | ForEach-Object { Write-Log $_ }

    Write-Section "Services"
    foreach ($serviceName in @("TermService", "SessionEnv", "UmRdpService")) {
        $service = Get-Service -Name $serviceName -ErrorAction SilentlyContinue
        if ($service) {
            try {
                Set-Service -Name $serviceName -StartupType Automatic -ErrorAction SilentlyContinue
                Start-Service -Name $serviceName -ErrorAction SilentlyContinue
            } catch {
                Write-Log "Service $serviceName start warning: $($_.Exception.Message)"
            }
            $service.Refresh()
            Write-Log "${serviceName}: $($service.Status), StartType=$((Get-CimInstance Win32_Service -Filter "Name='$serviceName'").StartMode)"
        } else {
            Write-Log "${serviceName}: not found"
        }
    }

    Write-Section "Firewall"
    foreach ($group in @("@FirewallAPI.dll,-28752", "Remote Desktop", "Remote Assistance")) {
        try {
            Enable-NetFirewallRule -Group $group -ErrorAction SilentlyContinue | Out-Null
            Write-Log "Enabled firewall group: $group"
        } catch {
            Write-Log "Could not enable firewall group ${group}: $($_.Exception.Message)"
        }
    }
    try {
        $rule = Get-NetFirewallRule -DisplayName "Allow RDP 3389 TCP" -ErrorAction SilentlyContinue
        if ($rule) {
            $rule | Set-NetFirewallRule -Enabled True -Profile Any -Action Allow | Out-Null
        } else {
            New-NetFirewallRule -DisplayName "Allow RDP 3389 TCP" -Direction Inbound -Action Allow -Protocol TCP -LocalPort $Port -Profile Any | Out-Null
        }
        Write-Log "Enabled explicit TCP rule for port $Port"
    } catch {
        Write-Log "Could not create explicit TCP rule: $($_.Exception.Message)"
    }

    Write-Section "User/group"
    $groupNames = New-Object "System.Collections.Generic.List[string]"
    $sidGroup = Get-CimInstance Win32_Group -Filter "SID='S-1-5-32-555'" -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($sidGroup) {
        $groupNames.Add($sidGroup.Name)
    }
    foreach ($candidate in @("Remote Desktop Users", "Пользователи удаленного рабочего стола")) {
        if (-not $groupNames.Contains($candidate)) {
            $groupNames.Add($candidate)
        }
    }
    foreach ($groupName in $groupNames) {
        try {
            Write-Log "Trying to add $UserName to local group: $groupName"
            $output = net localgroup "$groupName" "$UserName" /add 2>&1
            foreach ($line in $output) { Write-Log $line }
        } catch {
            Write-Log "Could not add user to ${groupName}: $($_.Exception.Message)"
        }
    }

    Write-Section "Listening check"
    Start-Sleep -Seconds 2
    $connections = Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue
    if ($connections) {
        foreach ($connection in $connections) {
            Write-Log "LISTENING: LocalAddress=$($connection.LocalAddress), LocalPort=$($connection.LocalPort), PID=$($connection.OwningProcess)"
        }
    } else {
        Write-Log "NOT LISTENING on TCP $Port"
        Write-Log "If Windows edition is Home/Core, this is expected unless RDP host is installed separately."
        Write-Log "If edition is Pro/Enterprise, reboot this PC and run this BAT again."
    }

    Write-Section "Firewall rules snapshot"
    Get-NetFirewallRule -Enabled True -Direction Inbound -Action Allow -ErrorAction SilentlyContinue |
        Where-Object { $_.DisplayName -match "Remote|RDP|3389|PreAssembly" -or $_.Group -match "28752" } |
        Select-Object DisplayName,DisplayGroup,Profile,Enabled,Action |
        Format-Table -AutoSize |
        Out-String |
        ForEach-Object { $_.TrimEnd() } |
        Where-Object { $_ } |
        ForEach-Object { Write-Log $_ }

    Write-Section "Result"
    if ($connections) {
        Write-Log "OK: RDP appears to be listening on TCP $Port."
    } else {
        Write-Log "FAILED: RDP is still not listening on TCP $Port."
    }
} catch {
    Write-Section "Fatal error"
    Write-Log $_.Exception.Message
    $_ | Format-List * -Force | Out-String | Add-Content -LiteralPath $logPath -Encoding UTF8
}

Write-Log "==== RDP setup finished $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss') ===="
