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
    val lastFile: File? = null,
    val isBusy: Boolean = false,
    val message: String? = null
)
