package com.boldrex.postavki

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.ExpandMore
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.LocalDate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val AppBackgroundGradient = Brush.verticalGradient(
    listOf(Color(0xFFF8FAFF), Color(0xFFF1F6FF), Color(0xFFEAF1FF))
)

private val AccentColor = Color(0xFF246BFE)
private val MainTextColor = Color(0xFF0B1226)
private val MutedTextColor = Color(0xFF667085)
private val SoftTextColor = Color(0xFF7A869A)
private val CardBorderColor = Color(0xFFD8E0EE)
private val InputContainerColor = Color(0xFFF7F9FF)
private val SoftBlueColor = Color(0xFFEEF4FF)
private val SuccessColor = Color(0xFF16A34A)
private val DangerColor = Color(0xFFEF4444)

private val CompactScreenBreakpoint = 380.dp
private val NarrowScreenBreakpoint = 340.dp

@Composable
private fun AppPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(minHeight = 44.dp)
            .heightIn(min = 44.dp),
        shape = RoundedCornerShape(14.dp),
        contentPadding = PaddingValues(horizontal = 9.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AccentColor, contentColor = Color.White)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(5.dp))
        }
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            lineHeight = 15.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
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
        modifier = modifier
            .defaultMinSize(minHeight = 44.dp)
            .heightIn(min = 44.dp),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, CardBorderColor),
        contentPadding = PaddingValues(horizontal = 9.dp, vertical = 0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = if (danger) DangerColor else MainTextColor
        )
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(5.dp))
        }
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            lineHeight = 15.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun AppIconActionButton(
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    danger: Boolean = false,
    primary: Boolean = false,
    onClick: () -> Unit
) {
    if (primary) {
        Button(
            onClick = onClick,
            modifier = modifier.size(44.dp),
            shape = RoundedCornerShape(14.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentColor, contentColor = Color.White)
        ) {
            Icon(icon, contentDescription = contentDescription, modifier = Modifier.size(20.dp))
        }
    } else {
        OutlinedIconButton(
            onClick = onClick,
            modifier = modifier.size(44.dp),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, CardBorderColor)
        ) {
            Icon(
                icon,
                contentDescription = contentDescription,
                tint = if (danger) DangerColor else MainTextColor,
                modifier = Modifier.size(20.dp)
            )
        }
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


private fun isWildberriesMarketplace(name: String): Boolean =
    name.contains("wildberries", ignoreCase = true) || name.equals("wb", ignoreCase = true)

private fun marketplaceLogoRes(name: String): Int =
    if (isWildberriesMarketplace(name)) R.drawable.wildberries_logo else R.drawable.ozon_logo

private fun marketplaceLogoWidth(name: String, heightDp: Int): androidx.compose.ui.unit.Dp =
    if (isWildberriesMarketplace(name)) (heightDp * 4.4f).dp else (heightDp * 3.2f).dp

@Composable
private fun MarketplaceLogo(
    marketplace: String,
    modifier: Modifier = Modifier,
    alpha: Float = 1f
) {
    Image(
        painter = painterResource(id = marketplaceLogoRes(marketplace)),
        contentDescription = marketplace,
        modifier = modifier.alpha(alpha),
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun MarketplaceBadge(
    marketplace: String,
    modifier: Modifier = Modifier,
    heightDp: Int = 16
) {
    Box(
        modifier = modifier
            .background(
                color = if (isWildberriesMarketplace(marketplace)) Color(0xFFFCE7F3) else SoftBlueColor,
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        MarketplaceLogo(
            marketplace = marketplace,
            modifier = Modifier
                .height(heightDp.dp)
                .width(marketplaceLogoWidth(marketplace, heightDp))
        )
    }
}

@Composable
private fun MarketplaceMetaRow(
    date: String,
    marketplace: String,
    cityCount: Int,
    modifier: Modifier = Modifier,
    fontSize: Int = 14
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Icon(Icons.Outlined.CalendarMonth, null, tint = MutedTextColor, modifier = Modifier.size(18.dp))
        Text(date, color = MutedTextColor, fontSize = fontSize.sp, maxLines = 1)
        Text("•", color = MutedTextColor)
        MarketplaceLogo(
            marketplace = marketplace,
            modifier = Modifier
                .height(15.dp)
                .width(marketplaceLogoWidth(marketplace, 15))
        )
        Text("•", color = MutedTextColor)
        Text("$cityCount город", color = MutedTextColor, fontSize = fontSize.sp, maxLines = 1)
    }
}

@Composable
private fun AppSectionTitle(modifier: Modifier = Modifier) {
    Text(
        text = "Товары в коробке",
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
    modifier: Modifier = Modifier
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
                placeholder = { Text("Поиск по названию / городу / маркетплейсу", color = SoftTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis) },
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
        AnimatedVisibility(
            visible = state.message != null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp, vertical = 86.dp),
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 3 })
        ) {
            state.message?.let { AppMessage(text = it, onClose = vm::clearMessage) }
        }
        AnimatedVisibility(
            visible = state.isBusy,
            modifier = Modifier.align(Alignment.Center),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White.copy(alpha = 0.92f))
                    .border(1.dp, CardBorderColor, RoundedCornerShape(18.dp))
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AccentColor, strokeWidth = 3.dp, modifier = Modifier.size(28.dp))
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
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Назад", tint = MainTextColor)
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
            if (state.screen == AppScreen.SHIPMENTS) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Text("Поставки", fontSize = 16.sp, color = MutedTextColor, maxLines = 1)
                    Text("•", fontSize = 16.sp, color = MutedTextColor, maxLines = 1)
                    MarketplaceLogo("Ozon", modifier = Modifier.height(15.dp).width(marketplaceLogoWidth("Ozon", 15)))
                    Text("/", fontSize = 16.sp, color = MutedTextColor, maxLines = 1)
                    MarketplaceLogo("Wildberries", modifier = Modifier.height(15.dp).width(marketplaceLogoWidth("Wildberries", 15)))
                }
            } else {
                val sub = when (state.screen) {
                    AppScreen.CITIES -> state.selectedShipmentTitle.ifBlank { "Поставка" }
                    AppScreen.BOXES -> state.selectedCityName.ifBlank { state.selectedShipmentTitle }
                    AppScreen.BOX -> listOf(state.selectedCityName, state.selectedBoxNumber).filter { it.isNotBlank() }.joinToString(" • ")
                    AppScreen.SCANNER -> "Сканер товара"
                    AppScreen.SETTINGS -> "Настройки и импорт"
                    else -> ""
                }
                Text(sub, fontSize = 16.sp, color = MutedTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
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
    var newShipmentExpanded by remember { mutableStateOf(true) }

    Column(Modifier.fillMaxSize()) {
        ModernCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                val collapseRotation by androidx.compose.animation.core.animateFloatAsState(
                    targetValue = if (newShipmentExpanded) 180f else 0f,
                    animationSpec = spring(dampingRatio = 0.58f, stiffness = 520f),
                    label = "new_shipment_collapse_rotation"
                )
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppIconBubble(Icons.Outlined.Inventory2, modifier = Modifier.size(42.dp))
                    Text("Новая поставка", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = MainTextColor, modifier = Modifier.weight(1f))
                    IconButton(onClick = { newShipmentExpanded = !newShipmentExpanded }) {
                        Icon(
                            imageVector = Icons.Outlined.ExpandMore,
                            contentDescription = if (newShipmentExpanded) "Свернуть" else "Развернуть",
                            tint = AccentColor,
                            modifier = Modifier.rotate(collapseRotation)
                        )
                    }
                }
                AnimatedVisibility(
                    visible = newShipmentExpanded,
                    enter = fadeIn(animationSpec = tween(260)) + slideInVertically(animationSpec = spring(dampingRatio = 0.68f, stiffness = 460f)) { -it / 5 } + expandVertically(animationSpec = spring(dampingRatio = 0.7f, stiffness = 500f)),
                    exit = fadeOut(animationSpec = tween(170)) + slideOutVertically(animationSpec = tween(210)) { -it / 6 } + shrinkVertically(animationSpec = tween(220))
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
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
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    AppPrimaryButton("Создать", Modifier.weight(1f), Icons.Outlined.Add) {
                        vm.createShipment(title, date, marketplace)
                        title = ""
                    }
                    AppIconActionButton(Icons.Outlined.Settings, "Настройки", onClick = vm::goSettings)
                }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        SearchField(
            query = query,
            onQueryChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
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
                item { EmptyStateCard("Поставок пока нет", "Создайте первую поставку для выбранного маркетплейса") }
            }
            items(filtered, key = { it.id }) { item ->
                ShipmentCard(item = item, vm = vm)
            }
        }
    }
}

@Composable
private fun MarketplaceButton(title: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val animatedBorder by androidx.compose.animation.animateColorAsState(
        targetValue = if (selected) AccentColor else CardBorderColor,
        animationSpec = tween(260),
        label = "marketplace_border"
    )
    val animatedContainer by androidx.compose.animation.animateColorAsState(
        targetValue = if (selected) Color(0xFFF2F7FF) else Color.White,
        animationSpec = tween(260),
        label = "marketplace_container"
    )
    val animatedDotBorder by androidx.compose.animation.animateColorAsState(
        targetValue = if (selected) AccentColor else Color(0xFFAAB4C8),
        animationSpec = tween(260),
        label = "marketplace_dot_border"
    )
    val indicatorScale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (selected) 1f else 0.65f,
        animationSpec = spring(dampingRatio = 0.52f, stiffness = 650f),
        label = "marketplace_indicator_scale"
    )
    val logoScale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (selected) 1.04f else 0.96f,
        animationSpec = tween(250),
        label = "marketplace_logo_scale"
    )

    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(minHeight = 44.dp)
            .heightIn(min = 44.dp),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.3.dp, animatedBorder),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = animatedContainer,
            contentColor = if (selected) AccentColor else MutedTextColor
        )
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .border(1.8.dp, animatedDotBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .size(8.dp)
                    .graphicsLayer {
                        scaleX = indicatorScale
                        scaleY = indicatorScale
                        alpha = if (selected) 1f else 0f
                    }
                    .clip(CircleShape)
                    .background(AccentColor)
            )
        }
        Spacer(Modifier.width(8.dp))
        Box(Modifier.graphicsLayer { scaleX = logoScale; scaleY = logoScale }) {
            MarketplaceLogo(
                marketplace = title,
                modifier = Modifier
                    .height(18.dp)
                    .width(marketplaceLogoWidth(title, 18)),
                alpha = if (selected) 1f else 0.82f
            )
        }
    }
}

@Composable
private fun ShipmentCard(item: ShipmentCardData, vm: AppViewModel) {
    ModernCard(Modifier.fillMaxWidth()) {
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val compact = maxWidth < CompactScreenBreakpoint
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                if (compact) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AppIconBubble(Icons.Outlined.Business, modifier = Modifier.size(50.dp))
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(item.title, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = MainTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            MarketplaceMetaRow(
                                date = item.date,
                                marketplace = item.marketplace,
                                cityCount = item.cityCount,
                                fontSize = 14
                            )
                        }
                    }
                    StatusBadge(if (item.isArchived) "В архиве" else "Активна", tone = if (item.isArchived) BadgeTone.Gray else BadgeTone.Green)
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AppIconBubble(Icons.Outlined.Business, modifier = Modifier.size(54.dp))
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(item.title, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = MainTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            MarketplaceMetaRow(
                                date = item.date,
                                marketplace = item.marketplace,
                                cityCount = item.cityCount,
                                fontSize = 14
                            )
                        }
                        StatusBadge(if (item.isArchived) "В архиве" else "Активна", tone = if (item.isArchived) BadgeTone.Gray else BadgeTone.Green)
                    }
                }

                HorizontalDivider(color = CardBorderColor.copy(alpha = 0.7f))
                StatsRow(
                    firstValue = item.boxCount.toString(),
                    secondValue = item.itemCount.toString(),
                    thirdValue = item.positionCount.toString()
                )
                HorizontalDivider(color = CardBorderColor.copy(alpha = 0.7f))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    AppPrimaryButton("Открыть", Modifier.weight(1f), Icons.Outlined.FileDownload) { vm.openShipment(item.id) }
                    AppIconActionButton(Icons.Outlined.Description, "Excel") { vm.generateExcel(item.id) }
                    AppIconActionButton(Icons.Outlined.Archive, if (item.isArchived) "Вернуть" else "Архив") {
                        vm.archiveShipment(item.id, !item.isArchived)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsRow(
    firstValue: String,
    secondValue: String,
    thirdValue: String
) {
    val firstLabel = "Коробки"
    val secondLabel = "Единицы"
    val thirdLabel = "Позиций"
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val compact = maxWidth < NarrowScreenBreakpoint
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            if (compact) {
                CompactStatItem(firstValue, firstLabel, Modifier.weight(1f))
                StatDivider()
                CompactStatItem(secondValue, secondLabel, Modifier.weight(1f))
                StatDivider()
                CompactStatItem(thirdValue, thirdLabel, Modifier.weight(1f))
            } else {
                StatItem(Icons.Outlined.Inventory2, firstValue, firstLabel, Modifier.weight(1f))
                StatDivider()
                StatItem(Icons.Outlined.Inventory2, secondValue, secondLabel, Modifier.weight(1f))
                StatDivider()
                StatItem(Icons.Outlined.Description, thirdValue, thirdLabel, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun CompactStatItem(value: String, label: String, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 19.sp, color = MainTextColor, maxLines = 1)
        Text(label, color = MutedTextColor, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
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

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            ModernTextField(city, { city = it }, Modifier.weight(1f), label = "Город / направление", placeholder = "Город / направление", leadingIcon = Icons.Outlined.Search)
            AppIconActionButton(Icons.Outlined.Add, "Добавить", primary = true) { vm.addCity(city); city = "" }
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AppPrimaryButton("Excel", Modifier.weight(1f), Icons.Outlined.Description) { vm.generateExcel() }
            AppSecondaryButton("CSV", Modifier.widthIn(min = 76.dp, max = 92.dp), Icons.Outlined.FileDownload) { vm.exportCsv() }
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
                    MarketplaceMetaRow(
                        date = item.date,
                        marketplace = item.marketplace,
                        cityCount = item.cityCount,
                        fontSize = 14
                    )
                }
                MarketplaceBadge(item.marketplace)
            }
            HorizontalDivider(color = CardBorderColor.copy(alpha = 0.7f))
            StatsRow(item.boxCount.toString(), item.itemCount.toString(), item.positionCount.toString())
        }
    }
}

@Composable
private fun CityCard(item: ShipmentCityCard, onOpen: () -> Unit) {
    ModernCard(Modifier.fillMaxWidth()) {
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val compact = maxWidth < CompactScreenBreakpoint
            if (compact) {
                Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    AppIconBubble(Icons.Outlined.Business, modifier = Modifier.size(46.dp))
                    Column(Modifier.weight(1f)) {
                        Text(item.cityName, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = MainTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("${item.boxCount} коробок • ${item.itemCount} единиц", color = MutedTextColor, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    AppPrimaryButton("Коробки", Modifier.widthIn(min = 96.dp, max = 112.dp), Icons.Outlined.Inventory2, onClick = onOpen)
                }
            } else {
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
                    AppPrimaryButton("Коробки", Modifier.widthIn(min = 104.dp, max = 120.dp), Icons.Outlined.Inventory2, onClick = onOpen)
                    AppIconActionButton(Icons.Outlined.MoreVert, "Меню", onClick = {})
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
    val shipment = state.shipments.firstOrNull { it.id == state.selectedShipmentId }

    Column(Modifier.fillMaxSize()) {
        ContextStrip(
            title = state.selectedCityName.ifBlank { "Город" },
            details = listOfNotNull(shipment?.marketplace, shipment?.let { "${state.selectedCityName.uppercase()}-${it.date}" }).joinToString(" • ")
        )
        Spacer(Modifier.height(12.dp))

        ModernCard(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                ModernTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    modifier = Modifier.weight(1f),
                    label = "Комментарий",
                    placeholder = "Комментарий к коробке",
                    singleLine = true
                )
                AppIconActionButton(Icons.Outlined.Add, "Создать коробку", primary = true) {
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
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 19.sp, color = MainTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (details.isNotBlank()) {
                    Text(details, color = MutedTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 14.sp)
                }
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
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val compact = maxWidth < CompactScreenBreakpoint
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AppIconBubble(Icons.Outlined.Inventory2, modifier = Modifier.size(50.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(box.boxNumber, fontWeight = FontWeight.ExtraBold, fontSize = if (compact) 22.sp else 25.sp, color = MainTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        if (compact) {
                            Text("Позиций: ${box.positionCount} • Единиц: ${box.itemCount}", color = MutedTextColor, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        } else {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                StatusBadge("Позиций: ${box.positionCount}", tone = BadgeTone.Gray)
                                StatusBadge("Единиц: ${box.itemCount}", tone = BadgeTone.Gray)
                            }
                        }
                        if (!box.comment.isNullOrBlank()) Text(box.comment, color = MutedTextColor, fontSize = 14.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                    IconButton(onClick = {}) { Icon(Icons.Outlined.MoreVert, contentDescription = null, tint = MutedTextColor) }
                }
                if (renameId == box.id) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        ModernTextField(newNumber, onNewNumberChange, Modifier.weight(1f), label = "Новый номер")
                        AppPrimaryButton("OK", Modifier.width(64.dp), onClick = onSaveRename)
                    }
                }
                HorizontalDivider(color = CardBorderColor.copy(alpha = 0.7f))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    AppPrimaryButton("Открыть", Modifier.weight(1f), Icons.Outlined.FileDownload, onClick = onOpen)
                    AppIconActionButton(Icons.Outlined.Description, "Номер", onClick = onStartRename)
                    AppIconActionButton(Icons.Outlined.Delete, "Удалить", danger = true, onClick = onDelete)
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
    var manualCreate by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<BoxItemData?>(null) }

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .blur(if (selectedItem != null) 3.dp else 0.dp)
        ) {
        Row(
            Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CardBorderColor, RoundedCornerShape(16.dp))
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = vm::openScanner,
                modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentColor, contentColor = Color.White)
            ) {
                Icon(Icons.Outlined.QrCodeScanner, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(5.dp))
                Text("Сканировать", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, lineHeight = 15.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
            }
            TextButton(
                onClick = { vm.generateExcel() },
                modifier = Modifier.widthIn(min = 88.dp).heightIn(min = 48.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Icon(Icons.Outlined.Description, contentDescription = null, tint = MutedTextColor, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(5.dp))
                Text("Excel", color = MutedTextColor, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, maxLines = 1)
            }
        }
        Spacer(Modifier.height(12.dp))

        if (state.pendingBarcode != null) {
            ModernCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Неизвестный код: ${state.pendingBarcode}", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = MainTextColor, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text("Создайте товар — после сохранения он сразу добавится в коробку.", color = MutedTextColor)
                    ModernTextField(article, { article = it }, Modifier.fillMaxWidth(), label = "Артикул")
                    ModernTextField(name, { name = it }, Modifier.fillMaxWidth(), label = "Название товара")
                    ModernTextField(barcode, { barcode = it }, Modifier.fillMaxWidth(), label = "Штрихкод")
                    ModernTextField(qty, { qty = it }, Modifier.fillMaxWidth(), label = "Количество", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    AppPrimaryButton("Создать", Modifier.fillMaxWidth(), Icons.Outlined.Add) {
                        vm.createProductAndAdd(article, name, barcode, qty.toIntOrNull() ?: 1, fromScan = true)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        ModernCard(Modifier.fillMaxWidth()) {
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val compact = maxWidth < CompactScreenBreakpoint
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Быстрое добавление товаров", fontWeight = FontWeight.ExtraBold, fontSize = if (compact) 20.sp else 22.sp, color = MainTextColor, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    if (compact) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            ModernTextField(query, { query = it }, Modifier.fillMaxWidth(), label = "Артикул / название / код", placeholder = "333", leadingIcon = Icons.Outlined.Search)
                            ModernTextField(qty, { qty = it }, Modifier.fillMaxWidth(), label = "Кол", placeholder = "1", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                        }
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            ModernTextField(query, { query = it }, Modifier.weight(1f), label = "Артикул / название / код", placeholder = "333", leadingIcon = Icons.Outlined.Search)
                            ModernTextField(qty, { qty = it }, Modifier.widthIn(min = 82.dp, max = 96.dp), label = "Кол", placeholder = "1", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        AppPrimaryButton("Найти", Modifier.weight(1f), Icons.Outlined.Search) { vm.searchProducts(query) }
                        AppSecondaryButton("+ товар", Modifier.weight(1f), Icons.Outlined.Add) {
                            barcode = query
                            manualCreate = true
                        }
                    }
                    AnimatedVisibility(visible = manualCreate && state.pendingBarcode == null, enter = fadeIn(), exit = fadeOut()) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            ModernTextField(article, { article = it }, Modifier.fillMaxWidth(), label = "Артикул нового товара")
                            ModernTextField(name, { name = it }, Modifier.fillMaxWidth(), label = "Название нового товара")
                            ModernTextField(barcode, { barcode = it }, Modifier.fillMaxWidth(), label = "Штрихкод / код")
                            AppPrimaryButton("Создать", Modifier.fillMaxWidth(), Icons.Outlined.Add) {
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
        }

        AppSectionTitle(Modifier.padding(top = 16.dp, bottom = 8.dp))
        LazyColumn(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            if (state.boxItems.isEmpty()) {
                item { EmptyStateCard("В коробке пока нет товаров", "Отсканируйте товар или добавьте его вручную") }
            }
            items(state.boxItems, key = { it.id }) { item ->
                BoxItemCard(
                    item = item,
                    onChangeQuantity = vm::changeItemQuantity,
                    onOpenDetails = { selectedItem = it }
                )
            }
        }
        }

        ProductDetailsBottomSheet(
            item = selectedItem,
            onDismiss = { selectedItem = null }
        )
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
private fun BoxItemCard(
    item: BoxItemData,
    onChangeQuantity: (Long, Int) -> Unit,
    onOpenDetails: (BoxItemData) -> Unit
) {
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
            .clickable(enabled = !removing) { onOpenDetails(item) }
            .graphicsLayer {
                alpha = 1f - (progress.value * 0.9f)
                scaleX = 1f - (progress.value * 0.1f)
                scaleY = 1f - (progress.value * 0.1f)
            }
    ) {
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val compact = maxWidth < CompactScreenBreakpoint
            Box(Modifier.fillMaxWidth()) {
                if (compact) {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            AppIconBubble(Icons.Outlined.Inventory2, modifier = Modifier.size(50.dp))
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(item.article, fontWeight = FontWeight.ExtraBold, fontSize = 21.sp, color = MainTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("Код: ${item.barcode.orEmpty().ifBlank { item.article }}", color = MutedTextColor, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("Название: ${item.name}", color = MutedTextColor, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                MarketplaceBadge("Ozon", modifier = Modifier.padding(top = 4.dp), heightDp = 14)
                            }
                        }
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                QuantityIconButton(icon = Icons.Outlined.Remove, onClick = { onChangeQuantity(item.id, item.quantity - 1) })
                                AnimatedQuantity(item.quantity, Modifier.widthIn(min = 28.dp))
                                QuantityIconButton(icon = Icons.Outlined.Add, accent = true, onClick = { onChangeQuantity(item.id, item.quantity + 1) })
                            }
                            Button(
                                onClick = { removing = true },
                                enabled = !removing,
                                modifier = Modifier.size(42.dp),
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = DangerColor, contentColor = Color.White)
                            ) {
                                Icon(Icons.Outlined.Delete, contentDescription = "Удалить", modifier = Modifier.size(22.dp))
                            }
                        }
                    }
                } else {
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
                                Text("Код: ${item.barcode.orEmpty().ifBlank { item.article }}", color = MutedTextColor, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("Название: ${item.name}", color = MutedTextColor, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                MarketplaceBadge("Ozon", modifier = Modifier.padding(top = 4.dp), heightDp = 14)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            QuantityIconButton(icon = Icons.Outlined.Remove, onClick = { onChangeQuantity(item.id, item.quantity - 1) })
                            AnimatedQuantity(item.quantity, Modifier.widthIn(min = 26.dp))
                            QuantityIconButton(icon = Icons.Outlined.Add, accent = true, onClick = { onChangeQuantity(item.id, item.quantity + 1) })
                            Button(
                                onClick = { removing = true },
                                enabled = !removing,
                                modifier = Modifier.size(42.dp),
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = DangerColor, contentColor = Color.White)
                            ) {
                                Icon(Icons.Outlined.Delete, contentDescription = "Удалить", modifier = Modifier.size(22.dp))
                            }
                        }
                    }
                }
                if (removing) ParticleDissolveOverlay(progress = progress.value)
            }
        }
    }
}


@Composable
private fun ProductDetailsBottomSheet(
    item: BoxItemData?,
    onDismiss: () -> Unit
) {
    val visibleItem = item ?: return
    val closeInteraction = remember { MutableInteractionSource() }
    val sheetInteraction = remember { MutableInteractionSource() }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x990B1226))
                .clickable(
                    interactionSource = closeInteraction,
                    indication = null,
                    onClick = onDismiss
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = true,
                modifier = Modifier.align(Alignment.BottomCenter),
                enter = fadeIn(animationSpec = tween(durationMillis = 170)) +
                    slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = spring()
                    ) +
                    scaleIn(
                        initialScale = 0.96f,
                        animationSpec = tween(durationMillis = 220)
                    ),
                exit = fadeOut(animationSpec = tween(durationMillis = 140)) +
                    slideOutVertically(
                        targetOffsetY = { it / 3 },
                        animationSpec = tween(durationMillis = 180)
                    ) +
                    scaleOut(
                        targetScale = 0.98f,
                        animationSpec = tween(durationMillis = 140)
                    )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                        .clickable(
                            interactionSource = sheetInteraction,
                            indication = null,
                            onClick = {}
                        ),
                    shape = RoundedCornerShape(30.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.85f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.White, Color(0xFFF8FBFF))
                                )
                            )
                            .padding(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(58.dp)
                                .height(5.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(Color(0xFFC3CAD7))
                                .align(Alignment.CenterHorizontally)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape)
                                        .background(SoftBlueColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Outlined.Inventory2,
                                        contentDescription = null,
                                        tint = AccentColor,
                                        modifier = Modifier.size(27.dp)
                                    )
                                }
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        "Карточка товара",
                                        color = MainTextColor,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        "Проверка позиции в коробке",
                                        color = MutedTextColor,
                                        fontSize = 13.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            OutlinedIconButton(
                                onClick = onDismiss,
                                modifier = Modifier.size(46.dp),
                                shape = CircleShape,
                                border = BorderStroke(1.dp, CardBorderColor)
                            ) {
                                Icon(
                                    Icons.Outlined.Close,
                                    contentDescription = "Закрыть",
                                    tint = MainTextColor,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        HorizontalDivider(color = CardBorderColor.copy(alpha = 0.75f))

                        ProductDetailInfoBlock(
                            value = visibleItem.article,
                            label = "Артикул",
                            icon = Icons.Outlined.Inventory2
                        )
                        ProductDetailInfoBlock(
                            value = visibleItem.name,
                            label = "Полное наименование",
                            icon = Icons.Outlined.Description
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MarketplaceBadge("Ozon", heightDp = 15)
                            Text(
                                "Кол-во: ${visibleItem.quantity}",
                                color = MutedTextColor,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                        }

                        AppPrimaryButton(
                            text = "Закрыть",
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 54.dp),
                            onClick = onDismiss
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun ProductDetailInfoBlock(
    value: String,
    label: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFFF6F9FF), Color(0xFFEEF4FF).copy(alpha = 0.72f))
                )
            )
            .border(1.dp, Color(0xFFE5ECF8), RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFFE6F0FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = AccentColor,
                modifier = Modifier.size(23.dp)
            )
        }
        Column(Modifier.weight(1f)) {
            Text(
                value.ifBlank { "—" },
                color = MainTextColor,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 21.sp,
                lineHeight = 24.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(3.dp))
            Text(
                label,
                color = MutedTextColor,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun AnimatedQuantity(value: Int, modifier: Modifier = Modifier) {
    AnimatedContent(
        targetState = value,
        modifier = modifier,
        transitionSpec = {
            if (targetState > initialState) {
                slideInVertically { it / 2 } + fadeIn() togetherWith slideOutVertically { -it / 2 } + fadeOut()
            } else {
                slideInVertically { -it / 2 } + fadeIn() togetherWith slideOutVertically { it / 2 } + fadeOut()
            }.using(SizeTransform(clip = false))
        },
        label = "quantity_change"
    ) { target ->
        Text(
            target.toString(),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = MainTextColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun QuantityIconButton(icon: ImageVector, accent: Boolean = false, onClick: () -> Unit) {
    OutlinedIconButton(
        onClick = onClick,
        modifier = Modifier.size(40.dp),
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
                    x = startX + dx - (particleW * shrink / 2f),
                    y = startY + dy - (particleH * shrink / 2f)
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
