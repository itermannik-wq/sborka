package com.boldrex.postavki

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun AppRoot(vm: AppViewModel) {
    val state by vm.state.collectAsState()
    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Header(state, vm)
        state.message?.let {
            Card(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                Row(Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(it, Modifier.weight(1f))
                    TextButton(onClick = vm::clearMessage) { Text("ОК") }
                }
            }
        }
        if (state.isBusy) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) { CircularProgressIndicator() }
        when (state.screen) {
            AppScreen.SHIPMENTS -> ShipmentsScreen(state, vm)
            AppScreen.CITIES -> CitiesScreen(state, vm)
            AppScreen.BOXES -> BoxesScreen(state, vm)
            AppScreen.BOX -> BoxScreen(state, vm)
            AppScreen.SCANNER -> BarcodeScannerScreen(onCodeScanned = vm::handleScan, onClose = { vm.openBox(state.selectedBoxId ?: 0) })
            AppScreen.SETTINGS -> SettingsScreen(state, vm)
        }
    }
}

@Composable
private fun Header(state: AppUiState, vm: AppViewModel) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text("Сборка поставок", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            val sub = when (state.screen) {
                AppScreen.SHIPMENTS -> "Поставки Ozon / Wildberries"
                AppScreen.CITIES -> state.selectedShipmentTitle
                AppScreen.BOXES -> "${state.selectedShipmentTitle} • ${state.selectedCityName}"
                AppScreen.BOX -> "${state.selectedCityName} • ${state.selectedBoxNumber}"
                AppScreen.SCANNER -> "Сканер"
                AppScreen.SETTINGS -> "Настройки и импорт"
            }
            if (sub.isNotBlank()) Text(sub, style = MaterialTheme.typography.bodySmall)
        }
        if (state.screen != AppScreen.SHIPMENTS) TextButton(onClick = {
            when (state.screen) {
                AppScreen.CITIES, AppScreen.SETTINGS -> vm.goShipments()
                AppScreen.BOXES -> state.selectedShipmentId?.let(vm::openShipment)
                AppScreen.BOX -> state.selectedCityId?.let(vm::openCity)
                AppScreen.SCANNER -> state.selectedBoxId?.let(vm::openBox)
                else -> vm.goShipments()
            }
        }) { Text("Назад") }
    }
    HorizontalDivider(Modifier.padding(vertical = 8.dp))
}

@Composable
private fun ShipmentsScreen(state: AppUiState, vm: AppViewModel) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var marketplace by remember { mutableStateOf("Ozon") }
    var query by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Новая поставка", fontWeight = FontWeight.Bold)
                OutlinedTextField(title, { title = it }, Modifier.fillMaxWidth(), label = { Text("Название") }, singleLine = true)
                OutlinedTextField(date, { date = it }, Modifier.fillMaxWidth(), label = { Text("Дата: 2026-05-05") }, singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (marketplace == "Ozon") Button(onClick = { marketplace = "Ozon" }) { Text("Ozon") } else OutlinedButton(onClick = { marketplace = "Ozon" }) { Text("Ozon") }
                    if (marketplace == "Wildberries") Button(onClick = { marketplace = "Wildberries" }) { Text("Wildberries") } else OutlinedButton(onClick = { marketplace = "Wildberries" }) { Text("Wildberries") }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { vm.createShipment(title, date, marketplace); title = "" }) { Text("Создать") }
                    OutlinedButton(onClick = vm::goSettings) { Text("Настройки") }
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(query, { query = it }, Modifier.fillMaxWidth(), label = { Text("Поиск по названию / городу / маркетплейсу") }, singleLine = true)
        Spacer(Modifier.height(8.dp))
        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val filtered = state.shipments.filter {
                query.isBlank() || it.title.contains(query, true) || it.marketplace.contains(query, true)
            }
            items(filtered, key = { it.id }) { item ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(item.title, fontWeight = FontWeight.Bold)
                        Text("${item.date} • ${item.marketplace} • городов: ${item.cityCount} • коробок: ${item.boxCount} • единиц: ${item.itemCount}")
                        if (item.isArchived) Text("Архив", color = MaterialTheme.colorScheme.secondary)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { vm.openShipment(item.id) }) { Text("Открыть") }
                            OutlinedButton(onClick = { vm.generateExcel(item.id) }) { Text("Excel") }
                            OutlinedButton(onClick = { vm.archiveShipment(item.id, !item.isArchived) }) { Text(if (item.isArchived) "Вернуть" else "Архив") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CitiesScreen(state: AppUiState, vm: AppViewModel) {
    var city by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(city, { city = it }, Modifier.weight(1f), label = { Text("Город / направление") }, singleLine = true)
            Button(onClick = { vm.addCity(city); city = "" }) { Text("Добавить") }
        }
        Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.generateExcel() }) { Text("Сформировать Excel") }
            OutlinedButton(onClick = { vm.exportCsv() }) { Text("CSV") }
        }
        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.shipmentCities, key = { it.id }) { item ->
                Card(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(item.cityName, fontWeight = FontWeight.Bold)
                            Text("Коробок: ${item.boxCount}, единиц: ${item.itemCount}")
                        }
                        Button(onClick = { vm.openCity(item.id) }) { Text("Коробки") }
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxesScreen(state: AppUiState, vm: AppViewModel) {
    var comment by remember { mutableStateOf("") }
    var renameId by remember { mutableStateOf<Long?>(null) }
    var newNumber by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(comment, { comment = it }, Modifier.weight(1f), label = { Text("Комментарий к коробке, необязательно") }, singleLine = true)
            Button(onClick = { vm.createBox(comment); comment = "" }) { Text("+ Коробка") }
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.boxes, key = { it.id }) { box ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(box.boxNumber, fontWeight = FontWeight.Bold)
                        Text("Позиций: ${box.positionCount}, единиц: ${box.itemCount}" + box.comment?.let { " • $it" }.orEmpty())
                        if (renameId == box.id) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(newNumber, { newNumber = it }, Modifier.weight(1f), label = { Text("Новый номер") }, singleLine = true)
                                Button(onClick = { vm.renameBox(box.id, newNumber); renameId = null }) { Text("OK") }
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { vm.openBox(box.id) }) { Text("Открыть") }
                            OutlinedButton(onClick = { renameId = box.id; newNumber = box.boxNumber }) { Text("Номер") }
                            OutlinedButton(onClick = { vm.deleteBox(box.id) }) { Text("Удалить") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScreen(state: AppUiState, vm: AppViewModel) {
    var query by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("1") }
    var article by remember(state.pendingBarcode) { mutableStateOf("") }
    var name by remember(state.pendingBarcode) { mutableStateOf("") }
    var barcode by remember(state.pendingBarcode) { mutableStateOf(state.pendingBarcode.orEmpty()) }

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = vm::openScanner, modifier = Modifier.weight(1f)) { Text("Сканировать") }
            OutlinedButton(onClick = { vm.generateExcel() }, modifier = Modifier.weight(1f)) { Text("Excel") }
        }
        Spacer(Modifier.height(8.dp))

        if (state.pendingBarcode != null) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Неизвестный код: ${state.pendingBarcode}", fontWeight = FontWeight.Bold)
                    OutlinedTextField(article, { article = it }, Modifier.fillMaxWidth(), label = { Text("Артикул") }, singleLine = true)
                    OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text("Название товара") }, singleLine = true)
                    OutlinedTextField(barcode, { barcode = it }, Modifier.fillMaxWidth(), label = { Text("Штрихкод") }, singleLine = true)
                    OutlinedTextField(qty, { qty = it }, Modifier.fillMaxWidth(), label = { Text("Количество") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    Button(onClick = { vm.createProductAndAdd(article, name, barcode, qty.toIntOrNull() ?: 1, fromScan = true) }) { Text("Создать товар и добавить") }
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Поиск и ручное добавление", fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(query, { query = it }, Modifier.weight(1f), label = { Text("Артикул / название / код") }, singleLine = true)
                    OutlinedTextField(qty, { qty = it }, Modifier.width(82.dp), label = { Text("Кол") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { vm.searchProducts(query) }) { Text("Найти") }
                    OutlinedButton(onClick = { barcode = query; article = ""; name = "" }) { Text("Новый товар") }
                }
                if (barcode.isNotBlank() && state.pendingBarcode == null) {
                    OutlinedTextField(article, { article = it }, Modifier.fillMaxWidth(), label = { Text("Артикул нового товара") }, singleLine = true)
                    OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text("Название нового товара") }, singleLine = true)
                    OutlinedTextField(barcode, { barcode = it }, Modifier.fillMaxWidth(), label = { Text("Штрихкод / код") }, singleLine = true)
                    Button(onClick = { vm.createProductAndAdd(article, name, barcode, qty.toIntOrNull() ?: 1, fromScan = false); article = ""; name = ""; barcode = "" }) { Text("Создать и добавить") }
                }
                state.productSearch.forEach { p ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(p.article, fontWeight = FontWeight.Bold)
                            Text(p.name)
                            Text(p.barcode.orEmpty(), style = MaterialTheme.typography.bodySmall)
                        }
                        Button(onClick = { vm.addProductToCurrentBox(p.id, qty.toIntOrNull() ?: 1) }) { Text("+") }
                    }
                }
            }
        }

        Text("Состав коробки", Modifier.padding(top = 12.dp, bottom = 4.dp), fontWeight = FontWeight.Bold)
        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.boxItems, key = { it.id }) { item ->
                Card(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(item.article, fontWeight = FontWeight.Bold)
                            Text(item.name)
                            Text(item.barcode.orEmpty(), style = MaterialTheme.typography.bodySmall)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedButton(onClick = { vm.changeItemQuantity(item.id, item.quantity - 1) }) { Text("−") }
                            Text(item.quantity.toString(), Modifier.padding(horizontal = 10.dp), fontWeight = FontWeight.Bold)
                            OutlinedButton(onClick = { vm.changeItemQuantity(item.id, item.quantity + 1) }) { Text("+") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsScreen(state: AppUiState, vm: AppViewModel) {
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) vm.importProducts(uri)
    }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Импорт справочника товаров", fontWeight = FontWeight.Bold)
                Text("Поддерживаемые колонки: article/артикул, name/название, barcode/штрихкод. CSV, простые XLSX и XML.")
                Button(onClick = { picker.launch(arrayOf("text/*", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/xml", "text/xml", "text/csv")) }) {
                    Text("Выбрать файл")
                }
            }
        }
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Файлы отчётов", fontWeight = FontWeight.Bold)
                Text("Excel и CSV сохраняются в папку приложения Documents и могут быть отправлены через стандартное меню Android.")
                state.lastFile?.let { Text("Последний файл: ${it.name}") }
            }
        }
    }
}
