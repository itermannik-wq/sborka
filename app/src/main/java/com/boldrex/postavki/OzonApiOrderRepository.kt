package com.boldrex.postavki

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class OzonApiOrderRepository(
    private val baseUrl: String = OzonApiConfig.BASE_URL,
    private val clientId: String = OzonApiConfig.CLIENT_ID,
    private val apiKey: String = OzonApiConfig.API_KEY,
    private val lookbackDays: Long = OzonApiConfig.ORDER_LOOKBACK_DAYS,
    private val lookaheadDays: Long = OzonApiConfig.ORDER_LOOKAHEAD_DAYS
) : OzonOrderRepository {
    override var lastWarning: String? = null
        private set

    override suspend fun loadOrders(): List<OzonOrderItem> = withContext(Dispatchers.IO) {
        val normalizedClientId = clientId.trim()
        val normalizedApiKey = apiKey.trim()
        lastWarning = null
        if (normalizedClientId.isBlank() || normalizedApiKey.isBlank()) {
            throw IllegalStateException("Укажите Ozon Client-Id и API Key в local.properties")
        }

        val orders = mutableListOf<OzonOrderItem>()
        var offset = 0
        var page = 0
        var totalCount: Long? = null

        do {
            val response = postJson(
                path = "/v3/posting/fbs/unfulfilled/list",
                body = buildRequest(offset),
                clientId = normalizedClientId,
                apiKey = normalizedApiKey
            )
            val result = response.optJSONObject("result")
                ?: throw IOException(response.optApiMessage() ?: "Ozon API вернул ответ без блока result")
            val postings = result.optJSONArray("postings") ?: JSONArray()

            orders += parsePostings(postings)
            totalCount = result.optLong("count", -1L).takeIf { it >= 0L }
            offset += postings.length()
            page++
        } while (
            postings.length() == PAGE_LIMIT &&
                page < MAX_PAGES &&
                (totalCount == null || offset.toLong() < totalCount)
        )

        val imageByOfferId = runCatching {
            loadProductImages(
                orderItems = orders,
                clientId = normalizedClientId,
                apiKey = normalizedApiKey
            )
        }.getOrElse { error ->
            lastWarning = when {
                error.message?.contains("required role", ignoreCase = true) == true ->
                    "Заказы загружены, но фото Ozon недоступны: API-ключу нужна роль для методов товаров."
                else ->
                    "Заказы загружены, но фото Ozon временно не подгрузились."
            }
            emptyMap()
        }

        orders.map { item -> item.copy(imageUrl = imageByOfferId[item.offerId]) }
    }

    private fun buildRequest(offset: Int): JSONObject {
        val now = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS)
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        return JSONObject()
            .put("dir", "ASC")
            .put(
                "filter",
                JSONObject()
                    .put("cutoff_from", now.minusDays(lookbackDays).format(formatter))
                    .put("cutoff_to", now.plusDays(lookaheadDays).format(formatter))
                    .put("status", "awaiting_packaging")
            )
            .put("limit", PAGE_LIMIT)
            .put("offset", offset)
            .put(
                "with",
                JSONObject()
                    .put("analytics_data", false)
                    .put("barcodes", false)
                    .put("financial_data", false)
                    .put("translit", false)
            )
    }

    private fun postJson(path: String, body: JSONObject, clientId: String, apiKey: String): JSONObject {
        val endpoint = baseUrl.trimEnd('/') + "/" + path.trimStart('/')
        val connection = (URL(endpoint).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = CONNECT_TIMEOUT_MS
            readTimeout = READ_TIMEOUT_MS
            doOutput = true
            setRequestProperty("Client-Id", clientId)
            setRequestProperty("Api-Key", apiKey)
            setRequestProperty("Content-Type", "application/json; charset=utf-8")
            setRequestProperty("Accept", "application/json")
        }

        return try {
            connection.outputStream.use { stream ->
                stream.write(body.toString().toByteArray(Charsets.UTF_8))
            }

            val responseCode = connection.responseCode
            val responseText = (if (responseCode in 200..299) connection.inputStream else connection.errorStream)
                ?.bufferedReader(Charsets.UTF_8)
                ?.use { it.readText() }
                .orEmpty()

            if (responseCode !in 200..299) {
                throw IOException(buildHttpError(responseCode, responseText))
            }

            runCatching { JSONObject(responseText) }
                .getOrElse { throw IOException("Ozon API вернул некорректный JSON", it) }
        } finally {
            connection.disconnect()
        }
    }

    private fun parsePostings(postings: JSONArray): List<OzonOrderItem> {
        val items = mutableListOf<OzonOrderItem>()
        for (postingIndex in 0 until postings.length()) {
            val posting = postings.optJSONObject(postingIndex) ?: continue
            val orderId = posting.optCleanString("posting_number")
                ?: posting.optCleanString("order_number")
                ?: posting.optCleanString("order_id")
                ?: "posting-${postingIndex + 1}"
            val products = posting.optJSONArray("products") ?: JSONArray()

            for (productIndex in 0 until products.length()) {
                val product = products.optJSONObject(productIndex) ?: continue
                val quantity = product.optInt("quantity", 0)
                if (quantity <= 0) continue

                val sku = product.optCleanString("sku")
                val offerId = product.optCleanString("offer_id")
                    ?: sku?.let { "SKU-$it" }
                    ?: "$orderId-${productIndex + 1}"
                val name = product.optCleanString("name") ?: offerId

                items += OzonOrderItem(
                    orderId = orderId,
                    offerId = offerId,
                    sku = sku,
                    name = name,
                    quantity = quantity
                )
            }
        }
        return items
    }

    private fun loadProductImages(orderItems: List<OzonOrderItem>, clientId: String, apiKey: String): Map<String, String> {
        if (orderItems.isEmpty()) return emptyMap()

        val images = mutableMapOf<String, String>()
        val distinctItems = orderItems.distinctBy { it.offerId }

        distinctItems
            .map { it.offerId }
            .distinct()
            .chunked(PRODUCT_INFO_LIMIT)
            .forEach { offerIds ->
                val response = postJson(
                    path = "/v3/product/info/list",
                    body = JSONObject().put("offer_id", JSONArray().apply { offerIds.forEach { put(it) } }),
                    clientId = clientId,
                    apiKey = apiKey
                )
                collectProductImages(response.productInfoItems(), distinctItems, images)
            }

        val missingSkuItems = distinctItems
            .filter { it.offerId !in images && !it.sku.isNullOrBlank() }
            .distinctBy { it.sku }
        missingSkuItems
            .mapNotNull { it.sku }
            .chunked(PRODUCT_INFO_LIMIT)
            .forEach { skus ->
                val response = postJson(
                    path = "/v3/product/info/list",
                    body = JSONObject().put("sku", JSONArray().apply { skus.forEach { put(it) } }),
                    clientId = clientId,
                    apiKey = apiKey
                )
                collectProductImages(response.productInfoItems(), distinctItems, images)
            }
        return images
    }

    private fun collectProductImages(
        products: JSONArray,
        orderItems: List<OzonOrderItem>,
        images: MutableMap<String, String>
    ) {
        for (index in 0 until products.length()) {
            val product = products.optJSONObject(index) ?: continue
            val imageUrl = product.optFirstCleanString("primary_image")
                ?: product.optFirstCleanString("images")
                ?: continue
            val offerId = product.optCleanString("offer_id")
            if (!offerId.isNullOrBlank()) {
                images[offerId] = imageUrl
            }
            val productSkus = product.optSkuStrings()
            if (productSkus.isNotEmpty()) {
                orderItems.filter { item -> item.sku in productSkus }.forEach { item ->
                    images[item.offerId] = imageUrl
                }
            }
        }
    }

    private fun buildHttpError(responseCode: Int, responseText: String): String {
        val apiMessage = runCatching { JSONObject(responseText).optApiMessage() }.getOrNull()
        return "Ozon API вернул HTTP $responseCode${apiMessage?.let { ": $it" }.orEmpty()}"
    }

    private fun JSONObject.optApiMessage(): String? {
        val message = optCleanString("message")
            ?: optCleanString("error")
            ?: optCleanString("details")
        val code = optCleanString("code")
        return listOfNotNull(code, message).joinToString(": ").takeIf { it.isNotBlank() }
    }

    private fun JSONObject.optCleanString(name: String): String? {
        if (!has(name) || isNull(name)) return null
        return opt(name)
            ?.toString()
            ?.trim()
            ?.takeIf { it.isNotBlank() && !it.equals("null", ignoreCase = true) }
    }

    private fun JSONObject.optFirstCleanString(name: String): String? {
        if (!has(name) || isNull(name)) return null
        return when (val value = opt(name)) {
            is JSONArray -> {
                for (index in 0 until value.length()) {
                    val item = value.opt(index)
                        ?.toString()
                        ?.trim()
                        ?.takeIf { it.isNotBlank() && !it.equals("null", ignoreCase = true) }
                    if (item != null) return item
                }
                null
            }
            else -> value
                ?.toString()
                ?.trim()
                ?.takeIf { it.isNotBlank() && !it.equals("null", ignoreCase = true) }
        }
    }

    private fun JSONObject.productInfoItems(): JSONArray {
        return optJSONObject("result")?.optJSONArray("items")
            ?: optJSONArray("items")
            ?: JSONArray()
    }

    private fun JSONObject.optSkuStrings(): Set<String> {
        val skus = mutableSetOf<String>()
        optCleanString("sku")?.let { skus += it }
        val sources = optJSONArray("sources") ?: JSONArray()
        for (index in 0 until sources.length()) {
            sources.optJSONObject(index)?.optCleanString("sku")?.let { skus += it }
        }
        return skus
    }

    private companion object {
        const val PAGE_LIMIT = 100
        const val PRODUCT_INFO_LIMIT = 1000
        const val MAX_PAGES = 50
        const val CONNECT_TIMEOUT_MS = 15_000
        const val READ_TIMEOUT_MS = 20_000
    }
}
