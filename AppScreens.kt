package com.boldrex.postavki

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val AppBackgroundGradient = Brush.verticalGradient(
    listOf(Color(0xFFF8FAFF), Color(0xFFF1F6FF), Color(0xFFEAF1FF))
)

private val AccentColor = Color(0xFF246BFE)
private val AccentDarkColor = Color(0xFF1F63F2)
private val MainTextColor = Color(0xFF0B1226)
private val MutedTextColor = Color(0xFF667085)
private val SoftTextColor = Color(0xFF7A869A)
private val CardBorderColor = Color(0xFFD8E0EE)
private val InputContainerColor = Color(0xFFF7F9FF)
private val SoftBlueColor = Color(0xFFEEF4FF)
private val SuccessColor = Color(0xFF16A34A)
private val DangerColor = Color(0xFFEF4444)

@Composable
private fun AppPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AccentColor, contentColor = Color.White)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun AppSecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    danger: Boolean = false,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, CardBorderColor),
        contentPadding = PaddingValues(horizontal = 14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = if (danger) DangerColor else MainTextColor
        )
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(21.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun ModernCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorderColor.copy(alpha = 0.75f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        content = { content() }
    )
}

@Composable
private fun AppIconBubble(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = AccentColor,
    background: Color = SoftBlueColor
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(25.dp))
    }
}

@Composable
private fun StatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    tone: BadgeTone = BadgeTone.Blue
) {
    val background = when (tone) {
        BadgeTone.Blue -> SoftBlueColor
        BadgeTone.Green -> Color(0xFFE9F8EF)
        BadgeTone.Purple -> Color(0xFFFCE7F3)
        BadgeTone.Gray -> Color(0xFFF2F4F7)
    }
    val color = when (tone) {
        BadgeTone.Blue -> AccentColor
        BadgeTone.Green -> SuccessColor
        BadgeTone.Purple -> Color(0xFFC026D3)
        BadgeTone.Gray -> MutedTextColor
    }
    Box(
        modifier = modifier
            .background(background, RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(text, color = color, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
    }
}

private enum class BadgeTone { Blue, Green, Purple, Gray }

@Composable
private fun AppSectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp,
        color = MainTextColor
    )
}

@Composable
private fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String = label,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: ImageVector? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder, color = SoftTextColor) },
        leadingIcon = leadingIcon?.let { icon -> { Icon(icon, contentDescription = null, tint = MutedTextColor) } },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        modifier = modifier.heightIn(min = if (singleLine) 60.dp else 86.dp),
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = MainTextColor,
            unfocusedTextColor = MainTextColor,
            focusedLabelColor = AccentColor,
            unfocusedLabelColor = MutedTextColor,
            cursorColor = AccentColor,
            focusedContainerColor = InputContainerColor,
            unfocusedContainerColor = InputContainerColor,
            disabledContainerColor = InputContainerColor,
            focusedIndicatorColor = AccentColor,
            unfocusedIndicatorColor = CardBorderColor
        )
    )
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String
) {
    ModernCard(modifier) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Outlined.Search, contentDescription = null, tint = MutedTextColor, modifier = Modifier.size(24.dp))
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f).height(56.dp),
                singleLine = true,
                placeholder = { Text(placeholder, color = SoftTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = MainTextColor,
                    unfocusedTextColor = MainTextColor,
                    cursorColor = AccentColor
                ),
                shape = RoundedCornerShape(16.dp)
            )
            Icon(Icons.Outlined.MoreVert, contentDescription = null, tint = AccentColor)
        }
    }
}

@Composable
fun AppRoot(vm: AppViewModel) {
    val state by vm.state.collectAsState()
    Box(Modifier.fillMaxSize().background(AppBackgroundGradient)) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Header(state, vm)
            AnimatedVisibility(visible = state.message != null, enter = fadeIn(), exit = fadeOut()) {
                state.message?.let { AppMessage(text = it, onClose = vm::clearMessage) }
            }
            AnimatedVisibility(visible = state.isBusy, enter = fadeIn(), exit = fadeOut()) {
                Row(Modifier.fillMaxWidth().padding(bottom = 10.dp), horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator(color = AccentColor, strokeWidth = 3.dp)
                }
            }
            when (state.screen) {
                AppScreen.SHIPMENTS -> ShipmentsScreen(state, vm)
                AppScreen.CITIES -> CitiesScreen(state, vm)
                AppScreen.BOXES -> BoxesScreen(state, vm)
                AppScreen.BOX -> BoxScreen(state, vm)
                AppScreen.SCANNER -> BarcodeScannerScreen(
                    onCodeScanned = vm::handleScan,
                    onClose = { state.selectedBoxId?.let(vm::openBox) ?: vm.goShipments() }
                )
                AppScreen.SETTINGS -> SettingsScreen(state, vm)
            }
        }
    }
}

@Composable
private fun AppMessage(text: String, onClose: () -> Unit) {
    ModernCard(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = SuccessColor, modifier = Modifier.size(26.dp))
                Text(text, color = MainTextColor, fontWeight = FontWeight.Medium, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            TextButton(onClick = onClose) { Text("OK", color = AccentColor, fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
private fun Header(state: AppUiState, vm: AppViewModel) {
    val isRoot = state.screen == AppScreen.SHIPMENTS
    Row(
        Modifier.fillMaxWidth().padding(top = 2.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isRoot) {
            IconButton(
                onClick = {
                    when (state.screen) {
                        AppScreen.CITIES, AppScreen.SETTINGS -> vm.goShipments()
                        AppScreen.BOXES -> state.selectedShipmentId?.let(vm::openShipment) ?: vm.goShipments()
                        AppScreen.BOX -> state.selectedCityId?.let(vm::openCity) ?: vm.goShipments()
                        AppScreen.SCANNER -> state.selectedBoxId?.let(vm::openBox) ?: vm.goShipments()
                        else -> vm.goShipments()
                    }
                },
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
                    .border(1.dp, CardBorderColor.copy(alpha = 0.55f), RoundedCornerShape(18.dp))
            ) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "Назад", tint = MainTextColor)
            }
            Spacer(Modifier.width(12.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(
                "Сборка поставок",
                fontSize = 30.sp,
                lineHeight = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MainTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            val sub = when (state.screen) {
                AppScreen.SHIPMENTS -> "Поставки • Ozon / Wildberries"
                AppScreen.CITIES -> state.selectedShipmentTitle.ifBlank { "Поставка" }
                AppScreen.BOXES -> state.selectedCityName.ifBlank { state.selectedShipmentTitle }
                AppScreen.BOX -> listOf(state.selectedCityName, state.selectedBoxNumber).filter { it.isNotBlank() }.joinToString(" • ")
                AppScreen.SCANNER -> "Сканер товара"
                AppScreen.SETTINGS -> "Настройки и импорт"
            }
            Text(sub, fontSize = 16.sp, color = MutedTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        if (isRoot) {
            IconButton(
                onClick = vm::goSettings,
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, CardBorderColor.copy(alpha = 0.55f), CircleShape)
            ) {
                Icon(Icons.Outlined.Settings, contentDescription = "Настройки", tint = AccentColor)
            }
        }
    }
}

@Composable
private fun ShipmentsScreen(state: AppUiState, vm: AppViewModel) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var marketplace by remember { mutableStateOf("Ozon") }
    var query by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        ModernCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppIconBubble(Icons.Outlined.Inventory2, modifier = Modifier.size(42.dp))
                    Text("Новая поставка", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = MainTextColor)
                }
                ModernTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = "Название",
                    placeholder = "Введите название поставки"
                )
                ModernTextField(
                    value = date,
                    onValueChange = { date = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = "Дата",
                    placeholder = "2026-05-05",
                    leadingIcon = Icons.Outlined.CalendarMonth
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MarketplaceButton(
                        title = "Ozon",
                        selected = marketplace == "Ozon",
                        modifier = Modifier.weight(1f),
                        onClick = { marketplace = "Ozon" }
                    )
                    MarketplaceButton(
                        title = "Wildberries",
                        selected = marketplace == "Wildberries",
                        modifier = Modifier.weight(1f),
                        onClick = { marketplace = "Wildberries" }
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppPrimaryButton("Создать", Modifier.weight(1f), Icons.Outlined.Add) {
                        vm.createShipment(title, date, marketplace)
                        title = ""
                    }
                    AppSecondaryButton("Настройки", Modifier.weight(1f), Icons.Outlined.Settings, onClick = vm::goSettings)
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        SearchField(
            query = query,
            onQueryChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Поиск по названию / городу / маркетплейсу"
        )
        Spacer(Modifier.height(12.dp))

        val filtered = state.shipments.filter {
            query.isBlank() || it.title.contains(query, true) || it.marketplace.contains(query, true) || it.date.contains(query, true)
        }

        LazyColumn(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            if (filtered.isEmpty()) {
                item { EmptyStateCard("Поставок пока нет", "Создайте первую поставку для Ozon или Wildberries") }
            }
            items(filtered, key = { it.id }) { item ->
                ShipmentCard(item = item, vm = vm)
            }
        }
    }
}

@Composable
private fun MarketplaceButton(title: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.4.dp, if (selected) AccentColor else CardBorderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) Color(0xFFF6F9FF) else Color.White,
            contentColor = if (selected) AccentColor else MutedTextColor
        )
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .border(2.dp, if (selected) AccentColor else Color(0xFFAAB4C8), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (selected) Box(Modifier.size(10.dp).clip(CircleShape).background(AccentColor))
        }
        Spacer(Modifier.width(8.dp))
        Text(title, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun ShipmentCard(item: ShipmentCardData, vm: AppViewModel) {
    ModernCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppIconBubble(Icons.Outlined.Business, modifier = Modifier.size(54.dp))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(item.title, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = MainTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                        Icon(Icons.Outlined.CalendarMonth, null, tint = MutedTextColor, modifier = Modifier.size(18.dp))
                        Text(item.date, color = MutedTextColor, fontSize = 14.sp, maxLines = 1)
                        Text("•", color = MutedTextColor)
                        Text(item.marketplace, color = MutedTextColor, fontSize = 14.sp, maxLines = 1)
                        Text("•", color = MutedTextColor)
                        Text("${item.cityCount} город", color = MutedTextColor, fontSize = 14.sp, maxLines = 1)
                    }
                }
                StatusBadge(if (item.isArchived) "В архиве" else "Активна", tone = if (item.isArchived) BadgeTone.Gray else BadgeTone.Green)
            }

            HorizontalDivider(color = CardBorderColor.copy(alpha = 0.7f))
            StatsRow(
                firstValue = item.boxCount.toString(),
                firstLabel = "Коробки",
                secondValue = item.itemCount.toString(),
                secondLabel = "Единицы",
                thirdValue = item.positionCount.toString(),
                thirdLabel = "Позиций"
            )
            HorizontalDivider(color = CardBorderColor.copy(alpha = 0.7f))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AppPrimaryButton("Открыть", Modifier.weight(1f), Icons.Outlined.FileDownload) { vm.openShipment(item.id) }
                AppSecondaryButton("Excel", Modifier.weight(1f), Icons.Outlined.Description) { vm.generateExcel(item.id) }
                AppSecondaryButton(if (item.isArchived) "Вернуть" else "Архив", Modifier.weight(1f), Icons.Outlined.Archive) {
                    vm.archiveShipment(item.id, !item.isArchived)
                }
            }
        }
    }
}

@Composable
private fun StatsRow(
    firstValue: String,
    firstLabel: String,
    secondValue: String,
    secondLabel: String,
    thirdValue: String,
    thirdLabel: String
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        StatItem(Icons.Outlined.Inventory2, firstValue, firstLabel, Modifier.weight(1f))
        StatDivider()
        StatItem(Icons.Outlined.Inventory2, secondValue, secondLabel, Modifier.weight(1f))
        StatDivider()
        StatItem(Icons.Outlined.Description, thirdValue, thirdLabel, Modifier.weight(1f))
    }
}

@Composable
private fun StatDivider() {
    Box(Modifier.height(44.dp).width(1.dp).background(CardBorderColor.copy(alpha = 0.75f)))
}

@Composable
private fun StatItem(icon: ImageVector, value: String, label: String, modifier: Modifier = Modifier) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AppIconBubble(icon, modifier = Modifier.size(38.dp), tint = if (label == "Позиций") MutedTextColor else AccentColor, background = Color(0xFFF2F5FB))
        Column {
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = MainTextColor, maxLines = 1)
            Text(label, color = MutedTextColor, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun CitiesScreen(state: AppUiState, vm: AppViewModel) {
    var city by remember { mutableStateOf("") }
    val shipment = state.shipments.firstOrNull { it.id == state.selectedShipmentId }

    Column(Modifier.fillMaxSize()) {
        shipment?.let {
            ShipmentSummaryCard(it)
            Spacer(Modifier.height(12.dp))
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            ModernTextField(city, { city = it }, Modifier.weight(1f), label = "Город / направление", placeholder = "Город / направление", leadingIcon = Icons.Outlined.Search)
            AppPrimaryButton("Добавить", Modifier.width(136.dp), Icons.Outlined.Add) { vm.addCity(city); city = "" }
        }
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AppPrimaryButton("Сформировать Excel", Modifier.weight(1.35f), Icons.Outlined.Description) { vm.generateExcel() }
            AppSecondaryButton("CSV", Modifier.weight(0.65f), Icons.Outlined.FileDownload) { vm.exportCsv() }
        }
        Spacer(Modifier.height(12.dp))

        LazyColumn(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            if (state.shipmentCities.isEmpty()) {
                item { EmptyStateCard("Города пока не добавлены", "Добавьте город или направление для сборки коробок") }
            }
            items(state.shipmentCities, key = { it.id }) { item ->
                CityCard(item = item, onOpen = { vm.openCity(item.id) })
            }
        }
    }
}

@Composable
private fun ShipmentSummaryCard(item: ShipmentCardData) {
    ModernCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppIconBubble(Icons.Outlined.Inventory2, modifier = Modifier.size(54.dp))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(item.title, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = MainTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${item.date} • ${item.marketplace} • ${item.cityCount} город", color = MutedTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                StatusBadge(item.marketplace, tone = if (item.marketplace.equals("Wildberries", true)) BadgeTone.Purple else BadgeTone.Blue)
            }
            HorizontalDivider(color = CardBorderColor.copy(alpha = 0.7f))
            StatsRow(item.boxCount.toString(), "Коробки", item.itemCount.toString(), "Единицы", item.positionCount.toString(), "Позиций")
        }
    }
}

@Composable
private fun CityCard(item: ShipmentCityCard, onOpen: () -> Unit) {
    ModernCard(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                AppIconBubble(Icons.Outlined.Business, modifier = Modifier.size(52.dp))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(item.cityName, fontWeight = FontWeight.ExtraBold, fontSize = 21.sp, color = MainTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatusBadge("${item.boxCount} коробок", tone = BadgeTone.Gray)
                        StatusBadge("${item.itemCount} единиц", tone = BadgeTone.Gray)
                    }
                }
            }
            AppPrimaryButton("Коробки", Modifier.width(124.dp), Icons.Outlined.Inventory2, onClick = onOpen)
            IconButton(onClick = {}) { Icon(Icons.Outlined.MoreVert, contentDescription = null, tint = MutedTextColor) }
        }
    }
}

@Composable
private fun BoxesScreen(state: AppUiState, vm: AppViewModel) {
    var comment by remember { mutableStateOf("") }
    var renameId by remember { mutableStateOf<Long?>(null) }
    var newNumber by remember { mutableStateOf("") }
    val shipment = state.shipments.firstOrNull { it.id == state.selectedShipmentId }

    Column(Modifier.fillMaxSize()) {
        ContextStrip(
            title = state.selectedCityName.ifBlank { "Город" },
            details = listOfNotNull(shipment?.marketplace, shipment?.let { "${state.selectedCityName.uppercase()}-${it.date}" }).joinToString(" • ")
        )
        Spacer(Modifier.height(12.dp))

        ModernCard(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                ModernTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    modifier = Modifier.weight(1f),
                    label = "Комментарий",
                    placeholder = "Комментарий к коробке, необязательно",
                    singleLine = false
                )
                AppPrimaryButton("+ Коробка", Modifier.width(142.dp), Icons.Outlined.Add) {
                    vm.createBox(comment)
                    comment = ""
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        LazyColumn(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            if (state.boxes.isEmpty()) {
                item { EmptyStateCard("Коробок пока нет", "Создайте первую коробку для города ${state.selectedCityName}") }
            }
            items(state.boxes, key = { it.id }) { box ->
                BoxCard(
                    box = box,
                    renameId = renameId,
                    newNumber = newNumber,
                    onNewNumberChange = { newNumber = it },
                    onStartRename = { renameId = box.id; newNumber = box.boxNumber },
                    onSaveRename = { vm.renameBox(box.id, newNumber); renameId = null },
                    onOpen = { vm.openBox(box.id) },
                    onDelete = { vm.deleteBox(box.id) }
                )
            }
        }
    }
}

@Composable
private fun ContextStrip(title: String, details: String) {
    ModernCard(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AppIconBubble(Icons.Outlined.Business, modifier = Modifier.size(46.dp))
            Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 19.sp, color = MainTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (details.isNotBlank()) {
                Text("•", color = MutedTextColor)
                Text(details, color = MutedTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun BoxCard(
    box: BoxCardData,
    renameId: Long?,
    newNumber: String,
    onNewNumberChange: (String) -> Unit,
    onStartRename: () -> Unit,
    onSaveRename: () -> Unit,
    onOpen: () -> Unit,
    onDelete: () -> Unit
) {
    ModernCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppIconBubble(Icons.Outlined.Inventory2, modifier = Modifier.size(50.dp))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(box.boxNumber, fontWeight = FontWeight.ExtraBold, fontSize = 25.sp, color = MainTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        StatusBadge("Позиций: ${box.positionCount}", tone = BadgeTone.Gray)
                        StatusBadge("Единиц: ${box.itemCount}", tone = BadgeTone.Gray)
                    }
                    if (!box.comment.isNullOrBlank()) Text(box.comment, color = MutedTextColor, fontSize = 14.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
                IconButton(onClick = {}) { Icon(Icons.Outlined.MoreVert, contentDescription = null, tint = MutedTextColor) }
            }
            if (renameId == box.id) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    ModernTextField(newNumber, onNewNumberChange, Modifier.weight(1f), label = "Новый номер")
                    AppPrimaryButton("OK", Modifier.width(78.dp), onClick = onSaveRename)
                }
            }
            HorizontalDivider(color = CardBorderColor.copy(alpha = 0.7f))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AppPrimaryButton("Открыть", Modifier.weight(1f), Icons.Outlined.FileDownload, onClick = onOpen)
                AppSecondaryButton("Номер", Modifier.weight(1f), Icons.Outlined.Description, onClick = onStartRename)
                AppSecondaryButton("Удалить", Modifier.weight(1f), Icons.Outlined.Delete, danger = true, onClick = onDelete)
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
    var manualCreate by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(999.dp))
                .border(1.dp, CardBorderColor, RoundedCornerShape(999.dp))
                .background(Color.White)
        ) {
            Button(
                onClick = vm::openScanner,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentColor, contentColor = Color.White)
            ) {
                Icon(Icons.Outlined.QrCodeScanner, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Сканировать", fontWeight = FontWeight.SemiBold)
            }
            TextButton(
                onClick = { vm.generateExcel() },
                modifier = Modifier.weight(1f).height(56.dp)
            ) {
                Icon(Icons.Outlined.Description, contentDescription = null, tint = MutedTextColor)
                Spacer(Modifier.width(8.dp))
                Text("Excel", color = MutedTextColor, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(Modifier.height(12.dp))

        if (state.pendingBarcode != null) {
            ModernCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Неизвестный код: ${state.pendingBarcode}", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = MainTextColor)
                    Text("Создайте товар — после сохранения он сразу добавится в коробку.", color = MutedTextColor)
                    ModernTextField(article, { article = it }, Modifier.fillMaxWidth(), label = "Артикул")
                    ModernTextField(name, { name = it }, Modifier.fillMaxWidth(), label = "Название товара")
                    ModernTextField(barcode, { barcode = it }, Modifier.fillMaxWidth(), label = "Штрихкод")
                    ModernTextField(qty, { qty = it }, Modifier.fillMaxWidth(), label = "Количество", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    AppPrimaryButton("Создать товар и добавить", Modifier.fillMaxWidth(), Icons.Outlined.Add) {
                        vm.createProductAndAdd(article, name, barcode, qty.toIntOrNull() ?: 1, fromScan = true)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        ModernCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Быстрое добавление товаров", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = MainTextColor)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    ModernTextField(query, { query = it }, Modifier.weight(1f), label = "Артикул / название / код", placeholder = "333", leadingIcon = Icons.Outlined.Search)
                    ModernTextField(qty, { qty = it }, Modifier.width(92.dp), label = "Кол", placeholder = "1", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppPrimaryButton("Найти", Modifier.weight(1f), Icons.Outlined.Search) { vm.searchProducts(query) }
                    AppSecondaryButton("Новый товар", Modifier.weight(1f), Icons.Outlined.Add) {
                        barcode = query
                        manualCreate = true
                    }
                }
                AnimatedVisibility(visible = manualCreate && state.pendingBarcode == null, enter = fadeIn(), exit = fadeOut()) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        ModernTextField(article, { article = it }, Modifier.fillMaxWidth(), label = "Артикул нового товара")
                        ModernTextField(name, { name = it }, Modifier.fillMaxWidth(), label = "Название нового товара")
                        ModernTextField(barcode, { barcode = it }, Modifier.fillMaxWidth(), label = "Штрихкод / код")
                        AppPrimaryButton("Создать и добавить", Modifier.fillMaxWidth(), Icons.Outlined.Add) {
                            vm.createProductAndAdd(article, name, barcode, qty.toIntOrNull() ?: 1, fromScan = false)
                            article = ""
                            name = ""
                            barcode = ""
                            manualCreate = false
                        }
                    }
                }
                state.productSearch.forEach { p ->
                    ProductSearchRow(p = p, onAdd = { vm.addProductToCurrentBox(p.id, qty.toIntOrNull() ?: 1) })
                }
            }
        }

        AppSectionTitle("Товары в коробке", Modifier.padding(top = 16.dp, bottom = 8.dp))
        LazyColumn(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            if (state.boxItems.isEmpty()) {
                item { EmptyStateCard("В коробке пока нет товаров", "Отсканируйте товар или добавьте его вручную") }
            }
            items(state.boxItems, key = { it.id }) { item ->
                BoxItemCard(item = item, onChangeQuantity = vm::changeItemQuantity)
            }
        }
    }
}

@Composable
private fun ProductSearchRow(p: ProductSearchData, onAdd: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFF7F9FF))
            .border(1.dp, CardBorderColor, RoundedCornerShape(18.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(p.article, fontWeight = FontWeight.ExtraBold, color = MainTextColor)
            Text(p.name, color = MutedTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (!p.barcode.isNullOrBlank()) Text(p.barcode, color = SoftTextColor, fontSize = 13.sp)
        }
        AppPrimaryButton("+", Modifier.width(62.dp), onClick = onAdd)
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
            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    AppIconBubble(Icons.Outlined.Inventory2, modifier = Modifier.size(52.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(item.article, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = MainTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("Код: ${item.barcode.orEmpty().ifBlank { item.article }}", color = MutedTextColor, fontSize = 14.sp, maxLines = 1)
                        Text("Название: ${item.name}", color = MutedTextColor, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        StatusBadge("Ozon", tone = BadgeTone.Blue, modifier = Modifier.padding(top = 4.dp))
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuantityIconButton(icon = Icons.Outlined.Remove, onClick = { onChangeQuantity(item.id, item.quantity - 1) })
                    Text(item.quantity.toString(), fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = MainTextColor, textAlign = TextAlign.Center)
                    QuantityIconButton(icon = Icons.Outlined.Add, accent = true, onClick = { onChangeQuantity(item.id, item.quantity + 1) })
                    Button(
                        onClick = { removing = true },
                        enabled = !removing,
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DangerColor, contentColor = Color.White)
                    ) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Удалить", modifier = Modifier.size(22.dp))
                    }
                }
            }
            if (removing) ParticleDissolveOverlay(progress = progress.value)
        }
    }
}

@Composable
private fun QuantityIconButton(icon: ImageVector, accent: Boolean = false, onClick: () -> Unit) {
    OutlinedIconButton(
        onClick = onClick,
        modifier = Modifier.size(44.dp),
        shape = CircleShape,
        border = BorderStroke(1.2.dp, if (accent) AccentColor else Color(0xFF98A2B3))
    ) {
        Icon(icon, contentDescription = null, tint = if (accent) AccentColor else MainTextColor, modifier = Modifier.size(20.dp))
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
private fun EmptyStateCard(title: String, subtitle: String) {
    ModernCard(Modifier.fillMaxWidth()) {
        Column(
            Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppIconBubble(Icons.Outlined.Inventory2, modifier = Modifier.size(58.dp), background = Color(0xFFF2F5FB))
            Text(title, fontWeight = FontWeight.ExtraBold, color = MainTextColor, fontSize = 19.sp, textAlign = TextAlign.Center)
            Text(subtitle, color = MutedTextColor, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun SettingsScreen(state: AppUiState, vm: AppViewModel) {
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) vm.importProducts(uri)
    }
    LazyColumn(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            ModernCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AppIconBubble(Icons.Outlined.FileDownload)
                        Column(Modifier.weight(1f)) {
                            Text("Импорт справочника товаров", fontWeight = FontWeight.ExtraBold, fontSize = 21.sp, color = MainTextColor)
                            Text("CSV, простые XLSX и XML", color = MutedTextColor)
                        }
                    }
                    Text("Поддерживаемые колонки: article/артикул, name/название, barcode/штрихкод.", color = MutedTextColor)
                    AppPrimaryButton("Выбрать файл", Modifier.fillMaxWidth(), Icons.Outlined.FileDownload) {
                        picker.launch(arrayOf("text/*", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/xml", "text/xml", "text/csv"))
                    }
                }
            }
        }
        item {
            ModernCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AppIconBubble(Icons.Outlined.Description)
                        Column(Modifier.weight(1f)) {
                            Text("Файлы отчётов", fontWeight = FontWeight.ExtraBold, fontSize = 21.sp, color = MainTextColor)
                            Text("Excel и CSV сохраняются в Documents", color = MutedTextColor)
                        }
                    }
                    state.lastFile?.let {
                        StatusBadge("Последний файл: ${it.name}", tone = BadgeTone.Gray)
                    } ?: Text("Последний файл пока не сформирован", color = MutedTextColor)
                }
            }
        }
        item {
            ModernCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AppIconBubble(Icons.Outlined.Settings)
                        Column(Modifier.weight(1f)) {
                            Text("Шаблоны и правила", fontWeight = FontWeight.ExtraBold, fontSize = 21.sp, color = MainTextColor)
                            Text("Рекомендуемый шаблон коробки: CITY-001", color = MutedTextColor)
                        }
                    }
                    Text("Этот блок можно расширить: формат номера коробки, подтверждения удаления, поля Excel-отчета и маркетплейсы.", color = MutedTextColor)
                }
            }
        }
    }
}
