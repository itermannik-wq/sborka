param(
    [string]$PrinterName = "HP LaserJet MFP M129-M134",
    [int]$Port = 8787,
    [switch]$AllowDefaultPrinterFallback
)

$ErrorActionPreference = "Stop"
$LogPath = Join-Path $PSScriptRoot "PreAssemblyPrintBridge.log"

function Write-BridgeLog {
    param([string]$Message)
    $line = "$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss') $Message"
    Write-Host $line
    Add-Content -LiteralPath $LogPath -Value $line -Encoding UTF8
}

function Find-HeaderEnd {
    param([byte[]]$Bytes, [int]$Count)
    for ($i = 0; $i -le $Count - 4; $i++) {
        if ($Bytes[$i] -eq 13 -and $Bytes[$i + 1] -eq 10 -and $Bytes[$i + 2] -eq 13 -and $Bytes[$i + 3] -eq 10) {
            return $i
        }
    }
    return -1
}

function Read-HttpRequest {
    param([System.Net.Sockets.NetworkStream]$Stream)

    $buffer = New-Object byte[] 4096
    $storage = New-Object "System.Collections.Generic.List[byte]"
    $headerEnd = -1

    while ($headerEnd -lt 0) {
        $read = $Stream.Read($buffer, 0, $buffer.Length)
        if ($read -le 0) {
            throw "Empty HTTP request"
        }
        for ($i = 0; $i -lt $read; $i++) {
            $storage.Add($buffer[$i])
        }
        $bytes = $storage.ToArray()
        $headerEnd = Find-HeaderEnd -Bytes $bytes -Count $bytes.Length
    }

    $bytes = $storage.ToArray()
    $headerText = [System.Text.Encoding]::ASCII.GetString($bytes, 0, $headerEnd)
    $headerLines = $headerText -split "`r`n"
    $firstLine = $headerLines[0]
    $contentLength = 0
    foreach ($line in $headerLines) {
        if ($line -match '^Content-Length:\s*(\d+)') {
            $contentLength = [int]$matches[1]
        }
    }

    $bodyStart = $headerEnd + 4
    $available = [Math]::Max(0, $bytes.Length - $bodyStart)
    $bodyBytes = New-Object byte[] $contentLength
    $offset = 0
    if ($available -gt 0 -and $contentLength -gt 0) {
        $toCopy = [Math]::Min($available, $contentLength)
        [Array]::Copy($bytes, $bodyStart, $bodyBytes, 0, $toCopy)
        $offset = $toCopy
    }
    while ($offset -lt $contentLength) {
        $read = $Stream.Read($bodyBytes, $offset, $contentLength - $offset)
        if ($read -le 0) {
            break
        }
        $offset += $read
    }

    [pscustomobject]@{
        FirstLine = $firstLine
        Body = [System.Text.Encoding]::UTF8.GetString($bodyBytes, 0, $offset)
    }
}

function Send-HttpResponse {
    param(
        [System.Net.Sockets.NetworkStream]$Stream,
        [int]$Code,
        [string]$Status,
        [string]$Body
    )

    $bodyBytes = [System.Text.Encoding]::UTF8.GetBytes($Body)
    $header = "HTTP/1.1 $Code $Status`r`nContent-Type: text/plain; charset=utf-8`r`nContent-Length: $($bodyBytes.Length)`r`nConnection: close`r`n`r`n"
    $headerBytes = [System.Text.Encoding]::ASCII.GetBytes($header)
    $Stream.Write($headerBytes, 0, $headerBytes.Length)
    $Stream.Write($bodyBytes, 0, $bodyBytes.Length)
    $Stream.Flush()
}

function Split-PrintableLines {
    param([string]$Text, [int]$MaxChars = 95)
    $result = New-Object "System.Collections.Generic.List[string]"
    foreach ($line in ($Text -split "`r?`n")) {
        if ($line.Length -le $MaxChars) {
            $result.Add($line)
            continue
        }
        for ($i = 0; $i -lt $line.Length; $i += $MaxChars) {
            $result.Add($line.Substring($i, [Math]::Min($MaxChars, $line.Length - $i)))
        }
    }
    return $result.ToArray()
}

function Get-AvailablePrintersText {
    return (Get-Printer | ForEach-Object {
        if ($_.ShareName) { "$($_.Name) [share: $($_.ShareName)]" } else { $_.Name }
    }) -join "; "
}

function Resolve-Printer {
    param(
        [string]$Printer,
        [bool]$AllowFallback = $false
    )

    $printerInfo = Get-Printer -Name $Printer -ErrorAction SilentlyContinue
    if (-not $printerInfo) {
        $printerInfo = Get-Printer | Where-Object { $_.ShareName -eq $Printer } | Select-Object -First 1
    }
    if (-not $printerInfo -and $AllowFallback) {
        $defaultPrinter = Get-CimInstance Win32_Printer | Where-Object { $_.Default } | Select-Object -First 1
        if ($defaultPrinter) {
            $printerInfo = Get-Printer -Name $defaultPrinter.Name -ErrorAction SilentlyContinue
            if ($printerInfo) {
                Write-BridgeLog "printer '$Printer' was not found, using default printer '$($printerInfo.Name)'"
            }
        }
    }
    if (-not $printerInfo) {
        throw "Printer or share '$Printer' was not found in Windows. Available: $(Get-AvailablePrintersText). Set the exact Windows printer name in the Android app or start bridge with -PrinterName."
    }

    return $printerInfo
}

function Print-Text {
    param(
        [string]$Printer,
        [string]$Title,
        [string]$Text
    )

    $printerInfo = Resolve-Printer -Printer $Printer -AllowFallback:$AllowDefaultPrinterFallback.IsPresent

    $printLines = New-Object "System.Collections.Generic.List[string]"
    $printLines.Add($Title)
    $printLines.Add(("=" * [Math]::Min([Math]::Max($Title.Length, 12), 80)))
    $printLines.Add("")
    foreach ($line in (Split-PrintableLines -Text $Text)) {
        $printLines.Add($line)
    }

    $printLines.ToArray() | Out-Printer -Name $printerInfo.Name
    return $printerInfo.Name
}

$listener = [System.Net.Sockets.TcpListener]::new([System.Net.IPAddress]::Any, $Port)
$listener.Start()
Write-BridgeLog "PreAssembly print bridge started on port $Port. Printer: $PrinterName"
Write-BridgeLog "Leave this window open while printing from the Android app."

try {
    while ($true) {
        $client = $listener.AcceptTcpClient()
        $remote = $client.Client.RemoteEndPoint.ToString()
        Write-BridgeLog "connection from $remote"
        try {
            $stream = $client.GetStream()
            $stream.ReadTimeout = 10000
            $stream.WriteTimeout = 10000
            $request = Read-HttpRequest -Stream $stream
            Write-BridgeLog "request: $($request.FirstLine), body bytes/chars: $($request.Body.Length)"
            if ($request.FirstLine -match '^GET\s+/health\s+HTTP/') {
                $printerText = try { (Resolve-Printer -Printer $PrinterName -AllowFallback:$AllowDefaultPrinterFallback.IsPresent).Name } catch { "NOT FOUND: $PrinterName. $(Get-AvailablePrintersText)" }
                Send-HttpResponse -Stream $stream -Code 200 -Status "OK" -Body "PreAssemblyPrintBridge OK. Printer: $printerText"
                Write-BridgeLog "health check from $remote. Printer: $printerText"
                continue
            }

            if ($request.FirstLine -notmatch '^POST\s+/print\s+HTTP/') {
                Send-HttpResponse -Stream $stream -Code 404 -Status "Not Found" -Body "Use GET /health or POST /print"
                continue
            }

            $payload = $request.Body | ConvertFrom-Json
            $printer = if ($payload.printer) { [string]$payload.printer } else { $PrinterName }
            $title = if ($payload.title) { [string]$payload.title } else { "Pre-assembly print job" }
            $text = [string]$payload.text
            if ([string]::IsNullOrWhiteSpace($text)) {
                throw "Empty print text"
            }
            Write-BridgeLog "print request: title='$title', requested printer='$printer'"

            try {
                $actualPrinter = Print-Text -Printer $printer -Title $title -Text $text
                Send-HttpResponse -Stream $stream -Code 200 -Status "OK" -Body "Printed on $actualPrinter"
                Write-BridgeLog "printed '$title' on '$actualPrinter'"
            } catch {
                $printError = $_.Exception.Message
                Send-HttpResponse -Stream $stream -Code 500 -Status "Print Error" -Body $printError
                Write-BridgeLog "print error: $printError"
            }
            continue
        } catch {
            $message = $_.Exception.Message
            try {
                Send-HttpResponse -Stream $stream -Code 500 -Status "Print Error" -Body $message
            } catch {
                # Client may already be closed.
            }
            Write-BridgeLog "print error: $message"
        } finally {
            $client.Close()
        }
    }
} finally {
    $listener.Stop()
}
