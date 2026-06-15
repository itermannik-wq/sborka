package com.boldrex.postavki

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDate

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: ShipmentRepository by lazy(LazyThreadSafetyMode.NONE) {
        ShipmentRepository(LocalDatabase.get(getApplication()).dao())
    }
    private val updateService: AppUpdateService by lazy(LazyThreadSafetyMode.NONE) {
        AppUpdateService(getApplication())
    }
    private val _state = MutableStateFlow(
        AppUiState(update = AppUpdateUiState(serverUrl = updateService.savedManifestUrl()))
    )
    val state: StateFlow<AppUiState> = _state.asStateFlow()
    private var initialLoadStarted = false

    fun ensureLoaded() {
        if (initialLoadStarted) return
        initialLoadStarted = true
        runBusy {
            repo.ensureBaseData()
            load()
        }
    }

    fun goShipments() = runBusy {
        _state.update { it.copy(screen = AppScreen.SHIPMENTS, selectedShipmentId = null, selectedCityId = null, selectedBoxId = null, pendingBarcode = null, openNewShipmentForm = false) }
        load()
    }

    fun goSettings() = _state.update { it.copy(screen = AppScreen.SETTINGS, message = null) }

    fun handleLauncherShortcut(action: String?, shortcutId: String?) {
        when {
            action == MainActivity.ACTION_NEW_SHIPMENT || shortcutId == MainActivity.SHORTCUT_ID_NEW_SHIPMENT -> _state.update {
                it.copy(
                    screen = AppScreen.SHIPMENTS,
                    selectedShipmentId = null,
                    selectedCityId = null,
                    selectedBoxId = null,
                    pendingBarcode = null,
                    openNewShipmentForm = true,
                    message = null
                )
            }
            action == MainActivity.ACTION_IMPORT_REPORTS || shortcutId == MainActivity.SHORTCUT_ID_IMPORT_REPORTS -> goSettings()
        }
    }

    @Deprecated("Use handleLauncherShortcut(action, shortcutId)")
    fun handleLauncherShortcut(action: String?) {
        when (action) {
            MainActivity.ACTION_NEW_SHIPMENT -> _state.update {
                it.copy(
                    screen = AppScreen.SHIPMENTS,
                    selectedShipmentId = null,
                    selectedCityId = null,
                    selectedBoxId = null,
                    pendingBarcode = null,
                    openNewShipmentForm = true,
                    message = null
                )
            }
            MainActivity.ACTION_IMPORT_REPORTS -> goSettings()
        }
    }

    fun consumeNewShipmentShortcut() = _state.update { it.copy(openNewShipmentForm = false) }

    fun openShipment(id: Long) = runBusy {
        _state.update { it.copy(screen = AppScreen.CITIES, selectedShipmentId = id, selectedCityId = null, selectedBoxId = null, pendingBarcode = null) }
        load()
    }

    fun openCity(id: Long) = runBusy {
        _state.update { it.copy(screen = AppScreen.BOXES, selectedCityId = id, selectedBoxId = null, pendingBarcode = null) }
        load()
    }

    fun openBox(id: Long) = runBusy {
        _state.update { it.copy(screen = AppScreen.BOX, selectedBoxId = id, pendingBarcode = null) }
        load()
    }

    fun openScanner() {
        if (_state.value.selectedBoxId == null) {
            message("Сначала откройте коробку")
            return
        }
        _state.update { it.copy(screen = AppScreen.SCANNER, message = null) }
    }

    fun createShipment(title: String, date: String, marketplace: String) = runBusy {
        val id = repo.createShipment(title, date.ifBlank { LocalDate.now().toString() }, marketplace)
        _state.update { it.copy(selectedShipmentId = id, screen = AppScreen.CITIES, message = "Поставка создана") }
        load()
    }

    fun archiveShipment(id: Long, archived: Boolean) = runBusy {
        repo.archiveShipment(id, archived)
        message(if (archived) "Поставка отправлена в архив" else "Поставка возвращена из архива")
        load()
    }

    fun addCity(name: String) = runBusy {
        val shipmentId = requireNotNull(_state.value.selectedShipmentId) { "Поставка не выбрана" }
        repo.addCityToShipment(shipmentId, name)
        message("Город добавлен")
        load()
    }

    fun createBox(comment: String = "") = runBusy {
        val shipmentId = requireNotNull(_state.value.selectedShipmentId) { "Поставка не выбрана" }
        val cityId = requireNotNull(_state.value.selectedCityId) { "Город не выбран" }
        val boxId = repo.createBox(shipmentId, cityId, comment)
        _state.update { it.copy(selectedBoxId = boxId, screen = AppScreen.BOX, message = "Коробка создана") }
        load()
    }

    fun renameBox(boxId: Long, newNumber: String) = runBusy {
        repo.renameBox(boxId, newNumber)
        message("Номер коробки изменён")
        load()
    }

    fun deleteBox(boxId: Long) = runBusy {
        repo.deleteBox(boxId)
        _state.update { it.copy(selectedBoxId = null) }
        message("Коробка удалена")
        load()
    }

    fun searchProducts(query: String) = runBusy {
        val results = repo.searchProducts(query)
        _state.update { it.copy(productSearch = results, message = if (results.isEmpty()) "Ничего не найдено" else null) }
    }

    fun addProductToCurrentBox(productId: Long, quantity: Int) = runBusy {
        val boxId = requireNotNull(_state.value.selectedBoxId) { "Коробка не выбрана" }
        repo.addProductToBox(boxId, productId, quantity)
        _state.update { it.copy(productSearch = emptyList(), message = "Товар добавлен") }
        loadCurrentBoxItems()
    }

    fun createProductAndAdd(article: String, name: String, barcode: String?, quantity: Int, fromScan: Boolean) = runBusy {
        val boxId = requireNotNull(_state.value.selectedBoxId) { "Коробка не выбрана" }
        val productId = repo.createOrUpdateProduct(article, name, barcode, fromScan)
        repo.addProductToBox(boxId, productId, quantity)
        _state.update { it.copy(pendingBarcode = null, productSearch = emptyList(), screen = AppScreen.BOX, message = "Новый товар добавлен") }
        loadCurrentBoxItems()
    }

    fun changeItemQuantity(itemId: Long, quantity: Int) = runBusy {
        repo.setItemQuantity(itemId, quantity)
        loadCurrentBoxItems()
    }

    fun handleScan(code: String) = runBusy {
        val clean = code.trim()
        if (clean.isBlank()) error("Пустой код")
        val boxId = requireNotNull(_state.value.selectedBoxId) { "Коробка не выбрана" }
        val added = repo.addScannedCodeToBox(boxId, clean)
        if (added) {
            _state.update { it.copy(screen = AppScreen.BOX, pendingBarcode = null, message = "Скан: товар добавлен +1") }
            loadCurrentBoxItems()
        } else {
            _state.update { it.copy(screen = AppScreen.BOX, pendingBarcode = clean, message = "Код не найден. Создайте товар") }
        }
    }

    fun generateExcel(shipmentId: Long? = null, share: Boolean = true) = runBusy {
        val id = shipmentId ?: requireNotNull(_state.value.selectedShipmentId) { "Поставка не выбрана" }
        val info = repo.shipmentInfo(id) ?: error("Поставка не найдена")
        val rows = repo.reportRows(id)
        val boxes = repo.reportBoxes(id)
        val file = ExcelService.generateXlsx(getApplication(), info, rows, boxes)
        repo.logReport(id, file.name, file.absolutePath)
        if (share) ExcelService.shareFile(getApplication(), file)
        _state.update { it.copy(lastFile = file, message = "Excel-отчёт сформирован: ${file.name}") }
    }

    fun exportCsv(shipmentId: Long? = null, share: Boolean = true) = runBusy {
        val id = shipmentId ?: requireNotNull(_state.value.selectedShipmentId) { "Поставка не выбрана" }
        val info = repo.shipmentInfo(id) ?: error("Поставка не найдена")
        val file = ExcelService.generateCsv(getApplication(), info, repo.reportRows(id))
        if (share) ExcelService.shareFile(getApplication(), file)
        _state.update { it.copy(lastFile = file, message = "CSV сформирован: ${file.name}") }
    }

    fun previewProductImport(uri: Uri) = runBusy {
        val preview = ExcelService.previewProductImport(getApplication(), uri, repo)
        _state.update {
            it.copy(
                importPreview = preview,
                importResult = null,
                message = "Предпросмотр готов: к импорту ${preview.rowsForImport} из ${preview.rowsTotal}"
            )
        }
    }

    fun confirmProductImport() = runBusy {
        val preview = requireNotNull(_state.value.importPreview) { "Сначала выберите файл для предпросмотра" }
        val result = ExcelService.commitProductImport(preview, repo)
        _state.update {
            it.copy(
                importPreview = null,
                importResult = result,
                message = "Импорт завершён: обновлено ${result.updated}, добавлено ${result.added}, пропущено ${result.skipped}"
            )
        }
    }

    fun cancelProductImport() = _state.update { it.copy(importPreview = null, message = "Импорт отменён") }

    fun clearImportResult() = _state.update { it.copy(importResult = null) }

    fun importProducts(uri: Uri) = previewProductImport(uri)

    fun clearMessage() = _state.update { it.copy(message = null) }

    fun changeUpdateServerUrl(url: String) {
        _state.update {
            it.copy(update = it.update.copy(serverUrl = url, message = null, error = null))
        }
    }

    fun checkForUpdate() {
        val manifestUrl = updateService.saveManifestUrl(_state.value.update.serverUrl)
        viewModelScope.launch {
            _state.update {
                it.copy(
                    update = it.update.copy(
                        serverUrl = manifestUrl,
                        status = AppUpdateStatus.CHECKING,
                        isChecking = true,
                        isDownloading = false,
                        downloadProgress = null,
                        downloadedBytes = 0L,
                        totalBytes = null,
                        downloadedApkPath = null,
                        message = null,
                        error = null
                    )
                )
            }
            try {
                val info = withContext(Dispatchers.IO) {
                    updateService.checkForUpdate(manifestUrl)
                }
                _state.update {
                    it.copy(
                        update = it.update.copy(
                            info = info,
                            status = if (info.isUpdateAvailable) AppUpdateStatus.AVAILABLE else AppUpdateStatus.UP_TO_DATE,
                            isChecking = false,
                            message = if (info.isUpdateAvailable) {
                                "Доступна версия ${info.versionName} (${info.versionCode})"
                            } else {
                                "Установлена актуальная версия"
                            },
                            error = null
                        )
                    )
                }
            } catch (t: Throwable) {
                _state.update {
                    it.copy(
                        update = it.update.copy(
                            status = AppUpdateStatus.ERROR,
                            isChecking = false,
                            message = null,
                            error = t.message ?: "Не удалось проверить обновление"
                        )
                    )
                }
            }
        }
    }

    fun downloadUpdate() {
        val info = _state.value.update.info
        if (info == null || !info.isUpdateAvailable) {
            _state.update {
                it.copy(update = it.update.copy(message = null, error = "Сначала найдите доступное обновление"))
            }
            return
        }
        viewModelScope.launch {
            _state.update {
                it.copy(
                    update = it.update.copy(
                        status = AppUpdateStatus.DOWNLOADING,
                        isDownloading = true,
                        downloadProgress = null,
                        downloadedBytes = 0L,
                        totalBytes = info.apkSizeBytes,
                        downloadedApkPath = null,
                        message = null,
                        error = null
                    )
                )
            }
            try {
                val apk = withContext(Dispatchers.IO) {
                    updateService.downloadApk(info) { downloadedBytes, totalBytes ->
                        _state.update {
                            val progress = totalBytes
                                ?.takeIf { total -> total > 0L }
                                ?.let { total -> (downloadedBytes.toFloat() / total.toFloat()).coerceIn(0f, 1f) }
                            it.copy(
                                update = it.update.copy(
                                    downloadProgress = progress,
                                    downloadedBytes = downloadedBytes,
                                    totalBytes = totalBytes
                                )
                            )
                        }
                    }
                }
                val needsPermission = !updateService.canRequestPackageInstalls()
                _state.update {
                    it.copy(
                        update = it.update.copy(
                            status = if (needsPermission) AppUpdateStatus.INSTALL_PERMISSION_REQUIRED else AppUpdateStatus.DOWNLOADED,
                            isDownloading = false,
                            downloadProgress = 1f,
                            downloadedApkPath = apk.absolutePath,
                            message = if (needsPermission) {
                                "APK скачан. Разрешите установку из этого приложения"
                            } else {
                                "APK скачан, можно устанавливать"
                            },
                            error = null
                        )
                    )
                }
            } catch (t: Throwable) {
                _state.update {
                    it.copy(
                        update = it.update.copy(
                            status = AppUpdateStatus.ERROR,
                            isDownloading = false,
                            message = null,
                            error = t.message ?: "Не удалось скачать обновление"
                        )
                    )
                }
            }
        }
    }

    fun installDownloadedUpdate() {
        val apkPath = _state.value.update.downloadedApkPath
        if (apkPath.isNullOrBlank()) {
            _state.update {
                it.copy(update = it.update.copy(message = null, error = "Сначала скачайте APK обновления"))
            }
            return
        }
        runCatching {
            updateService.installApk(File(apkPath))
        }.onSuccess {
            _state.update {
                it.copy(
                    update = it.update.copy(
                        status = AppUpdateStatus.INSTALLING,
                        message = "Открыт установщик Android",
                        error = null
                    )
                )
            }
        }.onFailure { t ->
            _state.update {
                it.copy(
                    update = it.update.copy(
                        status = AppUpdateStatus.INSTALL_PERMISSION_REQUIRED,
                        message = null,
                        error = t.message ?: "Не удалось открыть установщик"
                    )
                )
            }
        }
    }

    fun openUpdateInstallPermission() {
        runCatching {
            updateService.openInstallPermissionSettings()
        }.onSuccess {
            _state.update {
                it.copy(
                    update = it.update.copy(
                        status = AppUpdateStatus.INSTALL_PERMISSION_REQUIRED,
                        message = "После разрешения вернитесь и нажмите «Установить»",
                        error = null
                    )
                )
            }
        }.onFailure { t ->
            _state.update {
                it.copy(update = it.update.copy(message = null, error = t.message ?: "Не удалось открыть настройки Android"))
            }
        }
    }

    private suspend fun load() {
        val current = _state.value
        val shipments = repo.listShipments()
        val cities = current.selectedShipmentId?.let { repo.listShipmentCities(it) }.orEmpty()
        val boxes = current.selectedCityId?.let { repo.listBoxes(it) }.orEmpty()
        val items = current.selectedBoxId?.let { repo.listBoxItems(it) }.orEmpty()
        val selectedShipmentTitle = shipments.firstOrNull { it.id == current.selectedShipmentId }?.title.orEmpty()
        val selectedCityName = cities.firstOrNull { it.id == current.selectedCityId }?.cityName.orEmpty()
        val selectedBoxNumber = boxes.firstOrNull { it.id == current.selectedBoxId }?.boxNumber.orEmpty()
        _state.update {
            it.copy(
                shipments = shipments,
                shipmentCities = cities,
                boxes = boxes,
                boxItems = items,
                selectedShipmentTitle = selectedShipmentTitle,
                selectedCityName = selectedCityName,
                selectedBoxNumber = selectedBoxNumber
            )
        }
    }

    private suspend fun loadCurrentBoxItems() {
        val boxId = _state.value.selectedBoxId ?: return
        val items = repo.listBoxItems(boxId)
        _state.update { it.copy(boxItems = items) }
    }

    private fun message(text: String) = _state.update { it.copy(message = text) }

    private fun runBusy(block: suspend () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isBusy = true, message = null) }
            try {
                withContext(Dispatchers.IO) {
                    block()
                }
            } catch (t: Throwable) {
                _state.update { it.copy(message = t.message ?: "Ошибка") }
            } finally {
                _state.update { it.copy(isBusy = false) }
            }
        }
    }

    companion object {
        fun factory(application: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = AppViewModel(application) as T
        }
    }
}
