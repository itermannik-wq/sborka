package com.boldrex.postavki

import java.io.File

enum class AppScreen { SHIPMENTS, CITIES, BOXES, BOX, SCANNER, SETTINGS }

data class ShipmentCardData(
    val id: Long,
    val title: String,
    val date: String,
    val marketplace: String,
    val cityCount: Int,
    val boxCount: Int,
    val positionCount: Int,
    val itemCount: Long,
    val isArchived: Boolean
)

data class ShipmentCityCard(
    val id: Long,
    val cityName: String,
    val boxCount: Int,
    val itemCount: Long
)

data class BoxCardData(
    val id: Long,
    val boxNumber: String,
    val cityName: String,
    val comment: String?,
    val positionCount: Int,
    val itemCount: Long
)

data class BoxItemData(
    val id: Long,
    val productId: Long,
    val article: String,
    val name: String,
    val barcode: String?,
    val quantity: Int
)

data class ProductSearchData(
    val id: Long,
    val article: String,
    val name: String,
    val barcode: String?
)

data class ShipmentInfo(
    val id: Long,
    val title: String,
    val date: String,
    val marketplace: String
)

data class ReportRow(
    val marketplace: String,
    val shipment: String,
    val shipmentDate: String,
    val city: String,
    val boxNumber: String,
    val boxComment: String?,
    val article: String,
    val productName: String,
    val barcode: String?,
    val quantity: Int,
    val productCreatedAt: Long,
    val isCreatedFromScan: Boolean
)

data class ReportBox(
    val city: String,
    val boxNumber: String,
    val marketplace: String,
    val comment: String?,
    val positionCount: Int,
    val itemCount: Long
)


data class ImportColumnPreview(
    val role: String,
    val source: String,
    val found: Boolean
)

data class ImportRowPreview(
    val rowNumber: Int,
    val article: String,
    val name: String,
    val barcode: String?,
    val errors: List<String> = emptyList(),
    val duplicateInFile: Boolean = false,
    val willUpdate: Boolean = false
) {
    val canImport: Boolean get() = errors.isEmpty() && !duplicateInFile
}

data class ProductImportPreview(
    val fileName: String,
    val fileType: String,
    val rowsTotal: Int,
    val rowsForImport: Int,
    val errorRows: Int,
    val duplicateBarcodeRows: Int,
    val updateRows: Int,
    val addRows: Int,
    val columns: List<ImportColumnPreview>,
    val rows: List<ImportRowPreview>
)

data class ProductImportResult(
    val fileName: String,
    val rowsTotal: Int,
    val added: Int,
    val updated: Int,
    val skipped: Int,
    val errors: Int,
    val duplicateBarcodes: Int
)

data class AppUiState(
    val screen: AppScreen = AppScreen.SHIPMENTS,
    val shipments: List<ShipmentCardData> = emptyList(),
    val shipmentCities: List<ShipmentCityCard> = emptyList(),
    val boxes: List<BoxCardData> = emptyList(),
    val boxItems: List<BoxItemData> = emptyList(),
    val productSearch: List<ProductSearchData> = emptyList(),
    val selectedShipmentId: Long? = null,
    val selectedCityId: Long? = null,
    val selectedBoxId: Long? = null,
    val selectedShipmentTitle: String = "",
    val selectedCityName: String = "",
    val selectedBoxNumber: String = "",
    val pendingBarcode: String? = null,
    val openNewShipmentForm: Boolean = false,
    val lastFile: File? = null,
    val importPreview: ProductImportPreview? = null,
    val importResult: ProductImportResult? = null,
    val isBusy: Boolean = false,
    val message: String? = null
)
