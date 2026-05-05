# PostavkiMarketplaceApp

Android-приложение для сборки поставок товаров по городам и коробкам.

## Что реализовано

- Kotlin + Jetpack Compose, без XML-экранов.
- Основной рукописный Kotlin-код: 8 файлов.
- Локальная база Room, offline-first.
- Поставки Ozon / Wildberries.
- Города, коробки с автонумерацией `ГОРОД-001`.
- Справочник товаров, ручное добавление, добавление при неизвестном штрихкоде.
- Сканирование EAN-13, QR, DataMatrix и внутренних кодов через CameraX + ML Kit.
- Excel `.xlsx` без тяжёлой POI-библиотеки: файл собирается как OOXML zip.
- CSV-экспорт.
- Импорт CSV, простого XLSX и XML справочника товаров.
- Отправка отчёта через стандартное Android-меню «Поделиться».

## Открытие в Android Studio

1. Откройте папку `PostavkiMarketplaceApp` в Android Studio.
2. Дождитесь Gradle Sync.
3. Соберите проект: `Build > Make Project`.
4. Запустите на телефоне Android 10+ или эмуляторе.

## Справочник товаров

Пример CSV находится в `test-data/products_sample.csv`.
Поддерживаемые колонки: `article`, `name`, `barcode` или русские аналоги `артикул`, `название`, `штрихкод`.

## Важно

В архив не включён настоящий `gradle-wrapper.jar`, поэтому при необходимости Android Studio сама использует установленный Gradle. Если нужен полностью автономный wrapper, выполните в установленном Gradle: `gradle wrapper --gradle-version 9.1.0`.
