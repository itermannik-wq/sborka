Запуск моста печати на компьютере с принтером HP LaserJet MFP M129-M134
=====================================================================

1. Скопируйте всю папку tools на компьютер 192.168.10.104.

2. На компьютере 192.168.10.104 запустите:
   StopPreAssemblyPrintBridge.bat

3. Потом запустите от имени администратора:
   StartPreAssemblyPrintBridgeBackground.bat

4. Проверьте на этом же компьютере:
   CheckPreAssemblyPrintBridge.bat

   В строке Local health должно быть:
   PreAssemblyPrintBridge OK. Printer: HP LaserJet MFP M129-M134

5. В Android-приложении в окне "..." укажите:
   IP моста печати: 192.168.10.104
   Имя принтера Windows: HP LaserJet MFP M129-M134

6. Нажмите "Тестовая печать".

Если в логах будет "Printer or share ... was not found", значит имя принтера в Windows отличается.
Скопируйте точное имя из "Параметры Windows -> Bluetooth и устройства -> Принтеры и сканеры" и вставьте его в поле "Имя принтера Windows" в приложении.
