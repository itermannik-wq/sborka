package com.boldrex.postavki

enum class PreAssemblyStatus(val title: String) {
    NOT_CHECKED("Не проверено"),
    AVAILABLE("Есть"),
    NOT_AVAILABLE("Нет"),
    NEED_TRANSFER("Нужно переместить")
}

enum class PreAssemblySortOrder(val title: String) {
    ATTENTION("Что проверять дальше"),
    NOT_CHECKED_FIRST("Сначала непроверенные"),
    NOT_AVAILABLE_FIRST("Сначала нет в наличии"),
    NEED_TRANSFER_FIRST("Сначала нужно переместить"),
    ARTICLE_ASC("По артикулу"),
    NAME_ASC("По названию"),
    QUANTITY_DESC("По количеству")
}

data class OzonOrderItem(
    val orderId: String,
    val offerId: String,
    val sku: String?,
    val name: String,
    val quantity: Int,
    val imageUrl: String? = null,
    val labelCodes: Set<String> = emptySet()
)

data class PreAssemblyItem(
    val id: String,
    val orderId: String,
    val offerId: String,
    val sku: String?,
    val name: String,
    val requiredQuantity: Int,
    val status: PreAssemblyStatus = PreAssemblyStatus.NOT_CHECKED,
    val transferQuantity: String = "",
    val comment: String = "",
    val imageUrl: String? = null
)

data class PreAssemblyReportRow(
    val offerId: String,
    val name: String,
    val transferQuantity: Int,
    val comment: String?
)

data class PreAssemblyArchiveEntry(
    val id: Long = 0,
    val title: String,
    val completedAt: Long,
    val completedAtText: String,
    val resultTitle: String,
    val total: Int,
    val checked: Int,
    val available: Int,
    val toTransfer: Int,
    val notChecked: Int,
    val comments: Int,
    val items: List<PreAssemblyItem> = emptyList()
)

data class PreAssemblyArchiveSummaryData(
    val id: Long,
    val title: String,
    val completedAt: Long,
    val completedAtText: String,
    val resultTitle: String,
    val total: Int,
    val checked: Int,
    val available: Int,
    val toTransfer: Int,
    val notChecked: Int,
    val comments: Int
)

data class PreAssemblyArchiveItemData(
    val id: Long,
    val orderId: String,
    val offerId: String,
    val sku: String?,
    val name: String,
    val requiredQuantity: Int,
    val status: String,
    val transferQuantity: String,
    val comment: String
)
