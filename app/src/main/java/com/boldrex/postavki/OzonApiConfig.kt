package com.boldrex.postavki

/**
 * Конфигурация для Ozon API.
 *
 * Заполните значения своими данными из кабинета Ozon Seller API.
 */
object OzonApiConfig {
    // Базовый URL Ozon Seller API
    const val BASE_URL: String = "https://api-seller.ozon.ru/"

    // Client-Id из local.properties или переменной окружения OZON_CLIENT_ID
    val CLIENT_ID: String = BuildConfig.OZON_CLIENT_ID

    // API Key из local.properties или переменной окружения OZON_API_KEY
    val API_KEY: String = BuildConfig.OZON_API_KEY

    const val ORDER_LOOKBACK_DAYS: Long = 30
    const val ORDER_LOOKAHEAD_DAYS: Long = 30
}
