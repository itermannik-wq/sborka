Set shell = CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")
folder = fso.GetParentFolderName(WScript.ScriptFullName)
script = folder & "\PreAssemblyPrintBridgeWatchdog.ps1"
cmd = "powershell.exe -NoProfile -ExecutionPolicy Bypass -WindowStyle Hidden -File " & Chr(34) & script & Chr(34) & " -PrinterName " & Chr(34) & "HP LaserJet MFP M129-M134" & Chr(34) & " -Port 8787 -IntervalSeconds 20 -Once"
shell.Run cmd, 0, True
