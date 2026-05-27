package com.boldrex.postavki

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface OzonOrderRepository {
    suspend fun loadOrders(): List<OzonOrderItem>
}

class StubOzonOrderRepository : OzonOrderRepository {
    override suspend fun loadOrders(): List<OzonOrderItem> {
        delay(500)
        return listOf(
            OzonOrderItem("1001", "A-100", "SKU-100", "Насадка сменная", 3),
            OzonOrderItem("1002", "A-100", "SKU-100", "Насадка сменная", 2),
            OzonOrderItem("1003", "B-250", "SKU-250", "Кабель питания", 2),
            OzonOrderItem("1004", "C-300", null, "Блок питания", 1)
        )
    }
}

data class PreAssemblyUiState(
    val isLoading: Boolean = false,
    val isLoaded: Boolean = false,
    val items: List<PreAssemblyItem> = emptyList(),
    val error: String? = null,
    val message: String? = null,
    val reportText: String = "",
    val isCompleted: Boolean = false,
    val completedAt: String? = null
)

class PreAssemblyViewModel(
    private val repository: OzonOrderRepository = StubOzonOrderRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(PreAssemblyUiState())
    val state: StateFlow<PreAssemblyUiState> = _state.asStateFlow()

    fun loadOrders() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, message = null) }
            runCatching { repository.loadOrders() }
                .onSuccess { orders ->
                    val merged = orders.groupBy { it.offerId }
                        .map { (offerId, grouped) ->
                            val first = grouped.first()
                            PreAssemblyItem(
                                id = offerId,
                                orderId = grouped.joinToString(",") { it.orderId },
                                offerId = offerId,
                                sku = first.sku,
                                name = first.name,
                                requiredQuantity = grouped.sumOf { it.quantity }
                            )
                        }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isLoaded = true,
                            items = merged,
                            reportText = "",
                            isCompleted = false,
                            completedAt = null
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false, error = "Не удалось загрузить заказы. Попробуйте снова.") }
                }
        }
    }

    fun updateStatus(id: String, status: PreAssemblyStatus) {
        if (!canEdit()) return
        updateItem(id) { item ->
            when (status) {
                PreAssemblyStatus.NOT_CHECKED -> item.copy(
                    status = status,
                    transferQuantity = ""
                )
                PreAssemblyStatus.AVAILABLE -> item.copy(
                    status = status,
                    transferQuantity = "0"
                )
                PreAssemblyStatus.NOT_AVAILABLE -> item.copy(
                    status = status,
                    transferQuantity = item.requiredQuantity.toString()
                )
                PreAssemblyStatus.NEED_TRANSFER -> item.copy(
                    status = status,
                    transferQuantity = ""
                )
            }
        }
    }

    fun updateTransferQuantity(id: String, value: String) {
        if (!canEdit()) return
        updateItem(id) { it.copy(transferQuantity = value.filter(Char::isDigit).take(5)) }
    }

    fun updateComment(id: String, value: String) {
        if (!canEdit()) return
        updateItem(id) { it.copy(comment = value) }
    }

    fun markVisibleAsAvailable(ids: List<String>) = bulkUpdate(
        ids = ids,
        emptyMessage = "Нет видимых позиций для изменения",
        successMessage = "${ids.size} позиций отмечены как “Есть”"
    ) { item ->
        item.copy(status = PreAssemblyStatus.AVAILABLE, transferQuantity = "0")
    }

    fun resetVisibleCheck(ids: List<String>) = bulkUpdate(
        ids = ids,
        emptyMessage = "Нет видимых позиций для сброса",
        successMessage = "Проверка сброшена у ${ids.size} позиций"
    ) { item ->
        item.copy(status = PreAssemblyStatus.NOT_CHECKED, transferQuantity = "")
    }

    fun clearVisibleComments(ids: List<String>) = bulkUpdate(
        ids = ids,
        emptyMessage = "Нет видимых позиций для очистки",
        successMessage = "Комментарии очищены у ${ids.size} позиций"
    ) { item ->
        item.copy(comment = "")
    }

    fun finishPreAssembly() {
        val items = _state.value.items
        if (items.isEmpty()) {
            _state.update { it.copy(message = "Нет позиций для завершения") }
            return
        }
        val finishedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        val hasProblems = items.any { it.status == PreAssemblyStatus.NOT_AVAILABLE || it.status == PreAssemblyStatus.NEED_TRANSFER }
        val hasUnchecked = items.any { it.status == PreAssemblyStatus.NOT_CHECKED }
        val result = when {
            hasUnchecked -> "Предварительная сборка завершена с непроверенными позициями"
            hasProblems -> "Предварительная сборка завершена с проблемами"
            else -> "Предварительная сборка завершена без проблем"
        }
        _state.update { it.copy(isCompleted = true, completedAt = finishedAt, message = result) }
    }

    fun returnToWork() {
        _state.update {
            it.copy(
                isCompleted = false,
                completedAt = null,
                message = "Предварительная сборка возвращена в работу"
            )
        }
    }

    fun buildReport(): Boolean {
        val items = _state.value.items
        val toTransfer = items.filter { it.status == PreAssemblyStatus.NOT_AVAILABLE || it.status == PreAssemblyStatus.NEED_TRANSFER }
        if (toTransfer.isEmpty()) {
            _state.update { it.copy(message = "Нет товаров для перемещения") }
            return false
        }
        if (toTransfer.any { it.transferQuantity.isBlank() || it.transferQuantity == "0" }) {
            _state.update { it.copy(message = "Укажите количество к перемещению") }
            return false
        }
        if (items.any { it.status == PreAssemblyStatus.NOT_CHECKED }) {
            _state.update { it.copy(message = "В списке есть непроверенные позиции") }
        }

        val rows = toTransfer.mapIndexed { index, item ->
            val reason = when (item.status) {
                PreAssemblyStatus.NOT_AVAILABLE -> "Нет в наличии"
                PreAssemblyStatus.NEED_TRANSFER -> "Недостаточное количество"
                else -> ""
            }
            """${index + 1}. Артикул: ${item.offerId}
Товар: ${item.name}
Причина перемещения: $reason
Количество к перемещению: ${item.transferQuantity} шт.${if (item.comment.isBlank()) "" else "\nКомментарий: ${item.comment}"}
""".trimIndent()
        }
        val text = """
Добрый день.

По предварительной сборке заказов Ozon нужно сделать перемещение на склад:

${rows.joinToString("\n\n")}

Итого позиций: ${rows.size}.
        """.trimIndent()
        _state.update { it.copy(reportText = text) }
        return true
    }

    fun clearMessage() = _state.update { it.copy(message = null) }

    private fun canEdit(): Boolean {
        if (_state.value.isCompleted) {
            _state.update { it.copy(message = "Сборка завершена. Нажмите “Вернуть в работу”, чтобы изменить позиции.") }
            return false
        }
        return true
    }

    private fun bulkUpdate(
        ids: List<String>,
        emptyMessage: String,
        successMessage: String,
        update: (PreAssemblyItem) -> PreAssemblyItem
    ) {
        if (!canEdit()) return
        if (ids.isEmpty()) {
            _state.update { it.copy(message = emptyMessage) }
            return
        }
        val idSet = ids.toSet()
        _state.update { state ->
            state.copy(
                items = state.items.map { item -> if (item.id in idSet) update(item) else item },
                message = successMessage,
                reportText = ""
            )
        }
    }

    private fun updateItem(id: String, update: (PreAssemblyItem) -> PreAssemblyItem) {
        _state.update { state -> state.copy(items = state.items.map { if (it.id == id) update(it) else it }, reportText = "") }
    }
}
