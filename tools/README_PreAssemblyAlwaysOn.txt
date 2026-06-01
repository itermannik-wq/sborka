PreAssembly print bridge always-on setup
=======================================

Run on the printer computer 192.168.10.104.

1. Extract the whole zip folder. Do not copy only one BAT file.
2. Run InstallPreAssemblyBridgeAlwaysOn_104_SAFE.bat as Administrator.
3. The script opens alwayson_setup_result.txt. The end should contain:
   OK: always-on watchdog installed
4. To check later, run CheckPreAssemblyAlwaysOn_104.bat.

What it installs:
- PreAssemblyPrintBridgeWatchdog task at user logon.
- PreAssemblyPrintBridgeWatchdogStartup one-shot task at Windows startup.
- PreAssemblyPrintBridgeWatchdogMinute one-shot check every minute.
- Startup-folder fallback StartPreAssemblyWatchdog.vbs for the current user.
- Firewall rule for TCP 8787.
- Main logon watchdog checks http://127.0.0.1:8787/health every 20 seconds.
- If the bridge is down or unhealthy, watchdog restarts it.

Android app settings:
- Bridge IP: 192.168.10.104
- Printer: HP LaserJet MFP M129-M134

Useful logs:
- alwayson_setup_result.txt
- PreAssemblyPrintBridgeWatchdog.log
- PreAssemblyPrintBridge.log
- PreAssemblyPrintBridge.stdout.log
- PreAssemblyPrintBridge.stderr.log
