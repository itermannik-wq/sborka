package com.boldrex.postavki

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import java.time.LocalDate


private val AppBackgroundGradient = Brush.verticalGradient(
    listOf(Color(0xFFF4F7FF), Color(0xFFEAF1FF), Color(0xFFE6EEFF))
)

private val CardBorderColor = Color(0xFFD4DFFF)
private val InputContainerColor = Color(0xFFF7F9FF)
private val AccentColor = Color(0xFF305DFF)
private val MutedTextColor = Color(0xFF6F7B95)
private val SuccessColor = Color(0xFF16A34A)
private val DangerColor = Color(0xFFEF4444)
private val MainTextColor = Color(0xFF0B1226)

@Composable
private fun AppPrimaryButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AccentColor, contentColor = Color.White)
    ) { Text(text, fontWeight = FontWeight.SemiBold) }
}

@Composable
private fun AppSecondaryButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MainTextColor)
    ) { Text(text, fontWeight = FontWeight.Medium) }
}


@Composable
private fun MarketplaceIconButton(
    selected: Boolean,
    logoRes: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = modifier.height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentColor, contentColor = Color.White)
        ) {
            Image(painter = painterResource(logoRes), contentDescription = contentDescription, modifier = Modifier.height(18.dp))
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MainTextColor)
        ) {
            Image(painter = painterResource(logoRes), contentDescription = contentDescription, modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
private fun StatusBadge(text: String, isSuccess: Boolean = false) {
    Box(
        Modifier
            .background(
                color = if (isSuccess) Color(0xFFE9F8EF) else Color(0xFFEEF4FF),
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(text, color = if (isSuccess) SuccessColor else AccentColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun ModernCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        content = { content() }
    )
}

@Composable
private fun AppSectionTitle(text: String) {
    Text(text = text, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF0B1226))
}

@Composable
private fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(label, color = MutedTextColor) },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        modifier = modifier.heightIn(min = 56.dp),
        shape = RoundedCornerShape(14.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color(0xFF0B1533),
            unfocusedTextColor = Color(0xFF0B1533),
            focusedLabelColor = AccentColor,
            unfocusedLabelColor = MutedTextColor,
            focusedContainerColor = InputContainerColor,
            unfocusedContainerColor = InputContainerColor,
            focusedIndicatorColor = AccentColor,
            unfocusedIndicatorColor = Color(0xFFC7D3FA)
        )
    )
}

@Composable
fun AppRoot(vm: AppViewModel) {
    val state by vm.state.collectAsState()
    Box(Modifier.fillMaxSize().background(AppBackgroundGradient)) {
        Column(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp)) {
            Header(state, vm)
            AnimatedVisibility(visible = state.message != null, enter = fadeIn(), exit = fadeOut()) {
                state.message?.let {
                    ModernCard(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                        Row(Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = SuccessColor)
                                Text(it)
                            }
                            TextButton(onClick = vm::clearMessage) { Text("ОК", color = SuccessColor) }
                        }
                    }
                }
            }
            AnimatedVisibility(visible = state.isBusy, enter = fadeIn(), exit = fadeOut()) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) { CircularProgressIndicator() }
            }
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
}

@Composable
private fun Header(state: AppUiState, vm: AppViewModel) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text("Сборка поставок", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
            val sub = when (state.screen) {
                AppScreen.SHIPMENTS -> "Поставки • Ozon / Wildberries"
                AppScreen.CITIES -> state.selectedShipmentTitle
                AppScreen.BOXES -> "${state.selectedShipmentTitle} • ${state.selectedCityName}"
                AppScreen.BOX -> "${state.selectedCityName} • ${state.selectedBoxNumber}"
                AppScreen.SCANNER -> "Сканер"
                AppScreen.SETTINGS -> "Настройки и импорт"
            }
            if (sub.isNotBlank()) Text(sub, style = MaterialTheme.typography.bodyLarge, color = MutedTextColor, maxLines = 2)
        }
        if (state.screen == AppScreen.SHIPMENTS) {
            OutlinedIconButton(onClick = vm::goSettings) {
                Icon(Icons.Outlined.Settings, contentDescription = "Настройки", tint = AccentColor)
            }
        } else OutlinedButton(onClick = {
            when (state.screen) {
                AppScreen.CITIES, AppScreen.SETTINGS -> vm.goShipments()
                AppScreen.BOXES -> state.selectedShipmentId?.let(vm::openShipment)
                AppScreen.BOX -> state.selectedCityId?.let(vm::openCity)
                AppScreen.SCANNER -> state.selectedBoxId?.let(vm::openBox)
                else -> vm.goShipments()
            }
        }, shape = RoundedCornerShape(16.dp), modifier = Modifier.height(48.dp)) { Text("Назад") }
    }
    HorizontalDivider(Modifier.padding(vertical = 10.dp), color = Color(0xFFC9D6FF))
}

@Composable
private fun ShipmentsScreen(state: AppUiState, vm: AppViewModel) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var marketplace by remember { mutableStateOf("Ozon") }
    var query by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        ModernCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Outlined.Inventory2, contentDescription = null, tint = AccentColor)
                    Text("Новая поставка", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
                }
                ModernTextField(title, { title = it }, Modifier.fillMaxWidth(), label = "Введите название поставки")
                ModernTextField(date, { date = it }, Modifier.fillMaxWidth(), label = "05.05.2026")
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Outlined.CalendarMonth, null, tint = MutedTextColor)
                    Text("Формат: ДД.ММ.ГГГГ", color = MutedTextColor)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MarketplaceIconButton(
                        selected = marketplace == "Ozon",
                        logoRes = R.drawable.ozon_logo,
                        contentDescription = "Ozon",
                        modifier = Modifier.weight(1f)
                    ) { marketplace = "Ozon" }
                    MarketplaceIconButton(
                        selected = marketplace == "Wildberries",
                        logoRes = R.drawable.wildberries_logo,
                        contentDescription = "Wildberries",
                        modifier = Modifier.weight(1f)
                    ) { marketplace = "Wildberries" }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppPrimaryButton("Создать", Modifier.weight(1f)) { vm.createShipment(title, date, marketplace); title = "" }
                    AppSecondaryButton("Настройки", Modifier.weight(1f), vm::goSettings)
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        ModernTextField(query, { query = it }, Modifier.fillMaxWidth(), label = "Поиск по названию / городу / маркетплейсу")
        Spacer(Modifier.height(8.dp))
        val filtered = state.shipments.filter {
            query.isBlank() || it.title.contains(query, true) || it.marketplace.contains(query, true)
        }
        if (filtered.isEmpty()) {
            ModernCard(Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Поставок пока нет", fontWeight = FontWeight.Bold)
                    Text("Создайте первую поставку для Ozon или Wildberries", color = MutedTextColor, textAlign = TextAlign.Center)
                }
            }
        }
        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered, key = { it.id }) { item ->
                ModernCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(item.title, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.headlineSmall, color = MainTextColor)
                        Text("${item.date} • ${item.marketplace} • городов: ${item.cityCount} • коробок: ${item.boxCount} • единиц: ${item.itemCount}", color = MutedTextColor)
                        StatusBadge(if (item.isArchived) "В архиве" else "Активна", isSuccess = !item.isArchived)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AppPrimaryButton("Открыть", Modifier.weight(1f)) { vm.openShipment(item.id) }
                            AppSecondaryButton("Excel", Modifier.weight(1f)) { vm.generateExcel(item.id) }
                            AppSecondaryButton(if (item.isArchived) "Вернуть" else "Архив", Modifier.weight(1f)) { vm.archiveShipment(item.id, !item.isArchived) }
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
        AppSectionTitle("Города и направления")
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            ModernTextField(city, { city = it }, Modifier.weight(1f), label = "Город / направление")
            AppPrimaryButton("Добавить") { vm.addCity(city); city = "" }
        }
        Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AppPrimaryButton("Сформировать Excel", Modifier.weight(1f)) { vm.generateExcel() }
            AppSecondaryButton("CSV", Modifier.weight(1f)) { vm.exportCsv() }
        }
        if (state.shipmentCities.isEmpty()) {
            ModernCard(Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Города пока не добавлены", fontWeight = FontWeight.Bold)
                    Text("Добавьте город или направление для сборки коробок", color = MutedTextColor, textAlign = TextAlign.Center)
                }
            }
        }
        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.shipmentCities, key = { it.id }) { item ->
                ModernCard(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(item.cityName, fontWeight = FontWeight.Bold)
                            Text("Коробок: ${item.boxCount}, единиц: ${item.itemCount}")
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            AppPrimaryButton("Коробки", Modifier.width(122.dp)) { vm.openCity(item.id) }
                            OutlinedIconButton(onClick = {}, modifier = Modifier.border(1.dp, CardBorderColor, RoundedCornerShape(16.dp))) {
                                Icon(Icons.Outlined.MoreVert, contentDescription = null, tint = MutedTextColor)
                            }
                        }
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
            ModernTextField(comment, { comment = it }, Modifier.weight(1f), label = "Комментарий к коробке, необязательно")
            Button(onClick = { vm.createBox(comment); comment = "" }, modifier = Modifier.height(56.dp), shape = RoundedCornerShape(18.dp)) { Text("+ Коробка") }
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.boxes, key = { it.id }) { box ->
                ModernCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(box.boxNumber, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                        Text("Позиций: ${box.positionCount}, единиц: ${box.itemCount}" + box.comment?.let { " • $it" }.orEmpty())
                        if (renameId == box.id) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                ModernTextField(newNumber, { newNumber = it }, Modifier.weight(1f), label = "Новый номер")
                                Button(onClick = { vm.renameBox(box.id, newNumber); renameId = null }) { Text("OK") }
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { vm.openBox(box.id) }, modifier = Modifier.height(52.dp), shape = RoundedCornerShape(18.dp)) { Text("Открыть") }
                            OutlinedButton(onClick = { renameId = box.id; newNumber = box.boxNumber }, modifier = Modifier.height(52.dp), shape = RoundedCornerShape(18.dp)) { Text("Номер") }
                            OutlinedButton(onClick = { vm.deleteBox(box.id) }, modifier = Modifier.height(52.dp), shape = RoundedCornerShape(18.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerColor)) { Text("Удалить") }
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
        ModernCard(Modifier.fillMaxWidth()) {
            Text(
                "${state.selectedCityName} • ${state.selectedBoxNumber}",
                Modifier.padding(14.dp),
                color = Color(0xFF0B1226),
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = vm::openScanner, modifier = Modifier.weight(1f).height(56.dp), shape = RoundedCornerShape(18.dp)) { Text("Сканировать") }
            OutlinedButton(onClick = { vm.generateExcel() }, modifier = Modifier.weight(1f).height(56.dp), shape = RoundedCornerShape(18.dp)) { Text("Excel") }
        }
        Spacer(Modifier.height(8.dp))

        if (state.pendingBarcode != null) {
            ModernCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Неизвестный код: ${state.pendingBarcode}", fontWeight = FontWeight.Bold)
                    ModernTextField(article, { article = it }, Modifier.fillMaxWidth(), label = "Артикул")
                    ModernTextField(name, { name = it }, Modifier.fillMaxWidth(), label = "Название товара")
                    ModernTextField(barcode, { barcode = it }, Modifier.fillMaxWidth(), label = "Штрихкод")
                    ModernTextField(qty, { qty = it }, Modifier.fillMaxWidth(), label = "Количество", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    Button(onClick = { vm.createProductAndAdd(article, name, barcode, qty.toIntOrNull() ?: 1, fromScan = true) }) { Text("Создать товар и добавить") }
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        ModernCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Быстрое добавление товаров", fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    ModernTextField(query, { query = it }, Modifier.weight(1f), label = "Артикул / название / код")
                    ModernTextField(qty, { qty = it }, Modifier.width(82.dp), label = "Кол", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { vm.searchProducts(query) }) {
                        Icon(Icons.Outlined.Search, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Найти")
                    }
                    OutlinedButton(onClick = { barcode = query; article = ""; name = "" }) { Text("Новый товар") }
                }
                if (barcode.isNotBlank() && state.pendingBarcode == null) {
                    ModernTextField(article, { article = it }, Modifier.fillMaxWidth(), label = "Артикул нового товара")
                    ModernTextField(name, { name = it }, Modifier.fillMaxWidth(), label = "Название нового товара")
                    ModernTextField(barcode, { barcode = it }, Modifier.fillMaxWidth(), label = "Штрихкод / код")
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

        Text("Товары в коробке", Modifier.padding(top = 14.dp, bottom = 6.dp), fontWeight = FontWeight.Bold)
        if (state.boxItems.isEmpty()) {
            ModernCard(Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("В коробке пока нет товаров", fontWeight = FontWeight.Bold)
                    Text("Отсканируйте товар или добавьте его вручную", color = MutedTextColor, textAlign = TextAlign.Center)
                }
            }
        }
        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.boxItems, key = { it.id }) { item ->
                BoxItemCard(item = item, onChangeQuantity = vm::changeItemQuantity)
            }
        }
    }
}

@Composable
private fun BoxItemCard(item: BoxItemData, onChangeQuantity: (Long, Int) -> Unit) {
    var removing by remember(item.id) { mutableStateOf(false) }
    val progress = remember(item.id) { Animatable(0f) }

    LaunchedEffect(removing) {
        if (removing) {
            progress.snapTo(0f)
            progress.animateTo(1f, animationSpec = tween(durationMillis = 650, easing = LinearEasing))
            onChangeQuantity(item.id, 0)
        }
    }

    ModernCard(
        Modifier
            .fillMaxWidth()
            .graphicsLayer {
                alpha = 1f - (progress.value * 0.9f)
                scaleX = 1f - (progress.value * 0.1f)
                scaleY = 1f - (progress.value * 0.1f)
            }
    ) {
        Box(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(item.article, fontWeight = FontWeight.Bold)
                    Text(item.name)
                    Text(item.barcode.orEmpty(), style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(onClick = { onChangeQuantity(item.id, item.quantity - 1) }, modifier = Modifier.width(44.dp)) { Text("−", textAlign = TextAlign.Center) }
                    Text(item.quantity.toString(), Modifier.padding(horizontal = 4.dp), fontWeight = FontWeight.Bold)
                    OutlinedButton(onClick = { onChangeQuantity(item.id, item.quantity + 1) }, modifier = Modifier.width(44.dp)) { Text("+", textAlign = TextAlign.Center) }
                    Button(
                        onClick = { removing = true },
                        enabled = !removing,
                        modifier = Modifier.size(width = 44.dp, height = 40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD72D45), contentColor = Color.White)
                    ) {
                        Text("🗑", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            if (removing) {
                ParticleDissolveOverlay(progress = progress.value)
            }
        }
    }
}

@Composable
private fun ParticleDissolveOverlay(progress: Float) {
    val particlesX = 17
    val particlesY = 8
    Canvas(Modifier.fillMaxSize().alpha((1f - progress).coerceIn(0f, 1f))) {
        val particleW = size.width / particlesX
        val particleH = size.height / particlesY
        repeat(particlesX * particlesY) { index ->
            val col = index % particlesX
            val row = index / particlesX
            val seed = index * 73
            val angle = ((seed % 120) - 60f) * (PI / 180f).toFloat()
            val speed = 45f + (seed % 70)
            val swirl = ((seed % 9) - 4f) * 1.6f
            val startX = col * particleW + particleW * 0.5f
            val startY = row * particleH + particleH * 0.5f
            val dx = cos(angle) * speed * progress + swirl * progress * 18f
            val dy = -sin(angle) * speed * progress - 90f * progress
            val shrink = (1f - progress * 0.75f).coerceAtLeast(0.1f)

            drawRect(
                color = Color(0xFFE7EDFF).copy(alpha = (0.95f - progress).coerceAtLeast(0f)),
                topLeft = Offset(
                    x = startX + dx.toFloat() - (particleW * shrink / 2f),
                    y = startY + dy.toFloat() - (particleH * shrink / 2f)
                ),
                size = androidx.compose.ui.geometry.Size(particleW * shrink, particleH * shrink),
                style = Fill
            )
        }
    }
}

@Composable
private fun SettingsScreen(state: AppUiState, vm: AppViewModel) {
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) vm.importProducts(uri)
    }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        ModernCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Импорт справочника товаров", fontWeight = FontWeight.Bold)
                Text("Поддерживаемые колонки: article/артикул, name/название, barcode/штрихкод. CSV, простые XLSX и XML.")
                Button(onClick = { picker.launch(arrayOf("text/*", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/xml", "text/xml", "text/csv")) }) {
                    Text("Выбрать файл")
                }
            }
        }
        ModernCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Файлы отчётов", fontWeight = FontWeight.Bold)
                Text("Excel и CSV сохраняются в папку приложения Documents и могут быть отправлены через стандартное меню Android.")
                state.lastFile?.let { Text("Последний файл: ${it.name}") }
            }
        }
    }
}
