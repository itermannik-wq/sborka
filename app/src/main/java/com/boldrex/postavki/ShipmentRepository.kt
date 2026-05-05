package com.boldrex.postavki

import kotlin.math.max

class ShipmentRepository(private val dao: AppDao) {
    suspend fun ensureBaseData() {
        if (dao.marketplaceCount() == 0) {
            dao.insertMarketplace(MarketplaceEntity(name = "Ozon"))
            dao.insertMarketplace(MarketplaceEntity(name = "Wildberries"))
            listOf("Москва", "Санкт-Петербург", "Казань", "Екатеринбург", "Новосибирск").forEach {
                dao.insertCity(CityEntity(name = it))
            }
        }
    }

    suspend fun listShipments(): List<ShipmentCardData> = dao.listShipmentCards()
    suspend fun listShipmentCities(shipmentId: Long): List<ShipmentCityCard> = dao.listShipmentCities(shipmentId)
    suspend fun listBoxes(shipmentCityId: Long): List<BoxCardData> = dao.listBoxes(shipmentCityId)
    suspend fun listBoxItems(boxId: Long): List<BoxItemData> = dao.listBoxItems(boxId)
    suspend fun shipmentInfo(shipmentId: Long): ShipmentInfo? = dao.shipmentInfo(shipmentId)
    suspend fun reportRows(shipmentId: Long): List<ReportRow> = dao.reportRows(shipmentId)
    suspend fun reportBoxes(shipmentId: Long): List<ReportBox> = dao.reportBoxes(shipmentId)

    suspend fun createShipment(title: String, date: String, marketplace: String): Long {
        val cleanTitle = title.trim()
        require(cleanTitle.isNotBlank()) { "Укажите название поставки" }
        val marketName = marketplace.ifBlank { "Ozon" }
        val marketId = dao.marketplaceIdByName(marketName) ?: dao.insertMarketplace(MarketplaceEntity(name = marketName))
        val now = now()
        return dao.insertShipment(
            ShipmentEntity(title = cleanTitle, date = date.trim().ifBlank { todayText() }, marketplaceId = marketId, createdAt = now, updatedAt = now)
        )
    }

    suspend fun archiveShipment(shipmentId: Long, archived: Boolean) {
        val s = dao.shipmentById(shipmentId) ?: return
        dao.updateShipment(s.copy(isArchived = archived, updatedAt = now()))
    }

    suspend fun addCityToShipment(shipmentId: Long, cityName: String): Long {
        val clean = cityName.trim()
        require(clean.isNotBlank()) { "Укажите город" }
        val cityId = dao.cityIdByName(clean) ?: dao.insertCity(CityEntity(name = clean)).takeIf { it > 0 }
        val result = dao.insertShipmentCity(ShipmentCityEntity(shipmentId = shipmentId, cityId = cityId, cityName = clean))
        return if (result > 0) result else dao.listShipmentCities(shipmentId).first { it.cityName == clean }.id
    }

    suspend fun createBox(shipmentId: Long, shipmentCityId: Long, comment: String? = null): Long {
        val city = dao.shipmentCityById(shipmentCityId) ?: error("Город поставки не найден")
        val nextNumber = nextBoxNumber(shipmentId, shipmentCityId, city.cityName)
        val now = now()
        return dao.insertBox(
            BoxEntity(
                shipmentId = shipmentId,
                shipmentCityId = shipmentCityId,
                cityName = city.cityName,
                boxNumber = nextNumber,
                comment = comment?.trim()?.takeIf { it.isNotBlank() },
                createdAt = now,
                updatedAt = now
            )
        )
    }

    suspend fun renameBox(boxId: Long, newNumber: String) {
        val box = dao.boxById(boxId) ?: return
        val clean = newNumber.trim()
        require(clean.isNotBlank()) { "Номер коробки не может быть пустым" }
        val duplicates = dao.countBoxesWithNumber(box.shipmentId, box.shipmentCityId, clean)
        require(duplicates == 0 || clean == box.boxNumber) { "Такой номер уже есть в этом городе" }
        dao.updateBox(box.copy(boxNumber = clean, updatedAt = now()))
    }

    suspend fun deleteBox(boxId: Long) {
        val box = dao.boxById(boxId) ?: return
        dao.deleteItemsForBox(boxId)
        dao.deleteBox(box)
    }

    suspend fun searchProducts(query: String): List<ProductSearchData> {
        val clean = query.trim()
        if (clean.isBlank()) return emptyList()
        return dao.searchProducts(clean)
    }

    suspend fun findProductByBarcode(barcode: String): ProductEntity? = dao.productByBarcode(barcode.trim())

    suspend fun createOrUpdateProduct(article: String, name: String, barcode: String?, createdFromScan: Boolean): Long {
        val cleanName = name.trim()
        val cleanArticle = article.trim().ifBlank { "БЕЗ-АРТИКУЛА" }
        val cleanBarcode = barcode?.trim()?.takeIf { it.isNotBlank() }
        require(cleanName.isNotBlank()) { "Укажите название товара" }
        if (cleanBarcode != null) {
            val existing = dao.productByBarcode(cleanBarcode)
            if (existing != null) {
                dao.updateProduct(existing.copy(article = cleanArticle, name = cleanName))
                return existing.id
            }
        }
        val inserted = dao.insertProduct(
            ProductEntity(article = cleanArticle, name = cleanName, barcode = cleanBarcode, createdAt = now(), isCreatedFromScan = createdFromScan)
        )
        return if (inserted > 0) inserted else cleanBarcode?.let { dao.productByBarcode(it)?.id } ?: error("Не удалось сохранить товар")
    }

    suspend fun addProductToBox(boxId: Long, productId: Long, quantity: Int) {
        val qty = max(1, quantity)
        val old = dao.boxItemByBoxAndProduct(boxId, productId)
        if (old == null) {
            dao.insertBoxItem(BoxItemEntity(boxId = boxId, productId = productId, quantity = qty, addedAt = now()))
        } else {
            dao.updateBoxItem(old.copy(quantity = old.quantity + qty))
        }
    }

    suspend fun setItemQuantity(itemId: Long, quantity: Int) {
        val item = dao.boxItemById(itemId) ?: return
        if (quantity <= 0) dao.deleteBoxItem(item) else dao.updateBoxItem(item.copy(quantity = quantity))
    }

    suspend fun addScannedCodeToBox(boxId: Long, code: String): Boolean {
        val product = findProductByBarcode(code) ?: return false
        addProductToBox(boxId, product.id, 1)
        return true
    }

    suspend fun logReport(shipmentId: Long, fileName: String, filePath: String) {
        dao.insertReportLog(ReportLogEntity(shipmentId = shipmentId, fileName = fileName, filePath = filePath, generatedAt = now()))
    }

    suspend fun logImport(fileName: String, type: String, rowsTotal: Int, rowsImported: Int, errors: Int) {
        dao.insertImportLog(ImportLogEntity(fileName = fileName, type = type, date = now(), rowsTotal = rowsTotal, rowsImported = rowsImported, errorsCount = errors))
    }

    private suspend fun nextBoxNumber(shipmentId: Long, shipmentCityId: Long, cityName: String): String {
        val prefix = cityPrefix(cityName)
        val existing = dao.boxNumbersForCity(shipmentId, shipmentCityId).toSet()
        var n = existing.size + 1
        while (true) {
            val candidate = "$prefix-${n.toString().padStart(3, '0')}"
            if (candidate !in existing) return candidate
            n++
        }
    }

    private fun cityPrefix(cityName: String): String = cityName.trim()
        .uppercase()
        .replace(Regex("[^А-ЯA-Z0-9]+"), "-")
        .trim('-')
        .ifBlank { "ГОРОД" }

    private fun now(): Long = System.currentTimeMillis()
    private fun todayText(): String = java.time.LocalDate.now().toString()
}
