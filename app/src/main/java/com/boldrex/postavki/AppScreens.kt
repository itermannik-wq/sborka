package com.boldrex.postavki

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.BitmapFactory
import android.widget.Toast
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
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.ContentTransform
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
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Print
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.LocalDate
import kotlinx.coroutines.delay
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
private val WarningColor = Color(0xFFF97316)

private val CompactScreenBreakpoint = 380.dp
private val NarrowScreenBreakpoint = 340.dp

private enum class AppMode { MENU, SUPPLY, PRE_ASSEMBLY }

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

private enum class PreAssemblyBulkAction(val title: String, val confirmTitle: String, val successButton: String) {
    MARK_AVAILABLE("Отметить все видимые как “Есть”", "Отметить как “Есть”?", "Отметить"),
    RESET_CHECK("Сбросить проверку", "Сбросить проверку?", "Сбросить"),
    CLEAR_COMMENTS("Очистить комментарии", "Очистить комментарии?", "Очистить")
}

@Composable
private fun AppPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
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
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
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
    val shape = RoundedCornerShape(24.dp)
    Card(
        modifier = modifier.shadow(
            elevation = 14.dp,
            shape = shape,
            ambientColor = Color(0x120B1226),
            spotColor = Color(0x180B1226)
        ),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorderColor.copy(alpha = 0.82f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
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

private fun screenLevel(screen: AppScreen): Int = when (screen) {
    AppScreen.SHIPMENTS -> 0
    AppScreen.CITIES -> 1
    AppScreen.BOXES -> 2
    AppScreen.BOX -> 3
    AppScreen.SCANNER -> 4
    AppScreen.SETTINGS -> 1
}

@Composable
private fun BottomDockSurface(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    ModernCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content
        )
    }
}

@Composable
private fun DockHandle() {
    Box(
        modifier = Modifier
            .width(42.dp)
            .height(5.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(CardBorderColor.copy(alpha = 0.95f))
    )
}


@Composable
private fun FloatingBottomMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(animationSpec = tween(170)) +
                slideInVertically(animationSpec = spring(dampingRatio = 0.72f, stiffness = 520f)) { it / 5 } +
                scaleIn(initialScale = 0.92f, animationSpec = spring(dampingRatio = 0.74f, stiffness = 560f)),
            exit = fadeOut(animationSpec = tween(130)) +
                slideOutVertically(animationSpec = tween(160)) { it / 6 } +
                scaleOut(targetScale = 0.94f, animationSpec = tween(130))
        ) {
            Column(
                modifier = Modifier.widthIn(max = 330.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                content = content
            )
        }

        val fabScale by androidx.compose.animation.core.animateFloatAsState(
            targetValue = if (expanded) 1.04f else 1f,
            animationSpec = spring(dampingRatio = 0.62f, stiffness = 520f),
            label = "floating_menu_fab_scale"
        )
        val iconRotation by androidx.compose.animation.core.animateFloatAsState(
            targetValue = if (expanded) 90f else 0f,
            animationSpec = spring(dampingRatio = 0.58f, stiffness = 600f),
            label = "floating_menu_icon_rotation"
        )

        Button(
            onClick = { onExpandedChange(!expanded) },
            modifier = Modifier
                .size(58.dp)
                .graphicsLayer {
                    scaleX = fabScale
                    scaleY = fabScale
                }
                .shadow(
                    elevation = 18.dp,
                    shape = CircleShape,
                    ambientColor = AccentColor.copy(alpha = 0.20f),
                    spotColor = AccentColor.copy(alpha = 0.32f)
                ),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentColor, contentColor = Color.White)
        ) {
            Icon(
                imageVector = if (expanded) Icons.Outlined.Close else Icons.Outlined.Add,
                contentDescription = if (expanded) "Свернуть меню" else "Открыть меню",
                modifier = Modifier
                    .size(if (expanded) 28.dp else 30.dp)
                    .rotate(iconRotation)
            )
        }
    }
}

@Composable
private fun FloatingMenuAction(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ModernCard(
        modifier = modifier
            .widthIn(min = 236.dp, max = 318.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .padding(start = 16.dp, top = 7.dp, end = 7.dp, bottom = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                color = MainTextColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, CardBorderColor.copy(alpha = 0.58f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = AccentColor, modifier = Modifier.size(23.dp))
            }
        }
    }
}

@Composable
private fun FloatingMenuPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    ModernCard(
        modifier = modifier
            .widthIn(min = 278.dp, max = 330.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content
        )
    }
}

@Composable
private fun FloatingSearchInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.heightIn(min = 54.dp),
        singleLine = true,
        placeholder = {
            Text(placeholder, color = SoftTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        leadingIcon = {
            Icon(Icons.Outlined.Search, contentDescription = null, tint = MutedTextColor)
        },
        shape = RoundedCornerShape(18.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = MainTextColor,
            unfocusedTextColor = MainTextColor,
            focusedContainerColor = InputContainerColor,
            unfocusedContainerColor = InputContainerColor,
            disabledContainerColor = InputContainerColor,
            focusedIndicatorColor = AccentColor,
            unfocusedIndicatorColor = CardBorderColor,
            cursorColor = AccentColor
        )
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
    var mode by rememberSaveable { mutableStateOf(AppMode.MENU) }
    val context = LocalContext.current
    val preAssemblyVm = remember(context.applicationContext) {
        PreAssemblyViewModel(
            archiveRepository = PreAssemblyArchiveRepository(
                LocalDatabase.get(context.applicationContext).dao()
            )
        )
    }
    val state by vm.state.collectAsState()
    val preAssemblyState by preAssemblyVm.state.collectAsState()
    val showStartupLoader = mode == AppMode.SUPPLY && state.isBusy && state.shipments.isEmpty() && state.screen == AppScreen.SHIPMENTS
    Box(Modifier.fillMaxSize().background(AppBackgroundGradient)) {
        if (mode == AppMode.MENU) {
            MainMenuScreen(
                onBack = { context.findActivity()?.finish() },
                onSupply = { mode = AppMode.SUPPLY },
                onPreAssembly = { mode = AppMode.PRE_ASSEMBLY }
            )
            return@Box
        }
        if (mode == AppMode.PRE_ASSEMBLY) {
            PreAssemblyScreen(state = preAssemblyState, vm = preAssemblyVm, onBack = { mode = AppMode.MENU })
            return@Box
        }
        if (showStartupLoader) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AccentColor, strokeWidth = 3.dp, modifier = Modifier.size(32.dp))
            }
            return@Box
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Header(
                state = state,
                vm = vm,
                onBackToModeSelect = {
                    mode = AppMode.MENU
                }
            )
            AnimatedContent(
                targetState = state.screen,
                modifier = Modifier.weight(1f),
                transitionSpec = {
                    val forward = screenLevel(targetState) >= screenLevel(initialState)
                    ContentTransform(
                        targetContentEnter = fadeIn(animationSpec = tween(220)) +
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> if (forward) fullWidth / 4 else -fullWidth / 4 },
                                animationSpec = tween(300)
                            ) +
                            scaleIn(initialScale = 0.985f, animationSpec = tween(300)),
                        initialContentExit = fadeOut(animationSpec = tween(180)) +
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> if (forward) -fullWidth / 6 else fullWidth / 6 },
                                animationSpec = tween(240)
                            ),
                        sizeTransform = SizeTransform(clip = false)
                    )
                },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
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
        LaunchedEffect(state.message) {
            if (state.message != null) {
                delay(3_000)
                vm.clearMessage()
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
private fun Header(
    state: AppUiState,
    vm: AppViewModel,
    onBackToModeSelect: () -> Unit
) {
    val isRoot = state.screen == AppScreen.SHIPMENTS
    val title = when (state.screen) {
        AppScreen.SHIPMENTS -> "Сборка поставок"
        AppScreen.CITIES -> state.selectedShipmentTitle.ifBlank { "Поставка" }
        AppScreen.BOXES -> state.selectedCityName.ifBlank { "Город" }
        AppScreen.BOX -> state.selectedBoxNumber.ifBlank { "Коробка" }
        AppScreen.SCANNER -> "Сканер товара"
        AppScreen.SETTINGS -> "Настройки"
    }
    val subtitle = when (state.screen) {
        AppScreen.SHIPMENTS -> "Быстрая сборка Ozon / Wildberries"
        AppScreen.CITIES -> "Города и направления"
        AppScreen.BOXES -> state.selectedShipmentTitle.ifBlank { "Коробки направления" }
        AppScreen.BOX -> listOf(state.selectedCityName, "товары в коробке").filter { it.isNotBlank() }.joinToString(" • ")
        AppScreen.SCANNER -> "Наведите камеру на штрихкод"
        AppScreen.SETTINGS -> "Импорт, отчеты и правила"
    }

    Row(
        Modifier
            .fillMaxWidth()
            .heightIn(min = if (isRoot) 66.dp else 58.dp)
            .padding(top = 2.dp, bottom = 10.dp),
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
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, CardBorderColor.copy(alpha = 0.65f), RoundedCornerShape(16.dp))
            ) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Назад", tint = MainTextColor)
            }
            Spacer(Modifier.width(10.dp))
        }

        Column(Modifier.weight(1f)) {
            Text(
                title,
                fontSize = if (isRoot) 28.sp else 20.sp,
                lineHeight = if (isRoot) 32.sp else 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MainTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(subtitle, fontSize = 14.sp, color = MutedTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (isRoot) {
                    Text("•", fontSize = 14.sp, color = MutedTextColor, maxLines = 1)
                    MarketplaceLogo("Ozon", modifier = Modifier.height(14.dp).width(marketplaceLogoWidth("Ozon", 14)))
                    MarketplaceLogo("Wildberries", modifier = Modifier.height(14.dp).width(marketplaceLogoWidth("Wildberries", 14)))
                }
            }
        }

        if (isRoot) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedIconButton(
                    onClick = onBackToModeSelect,
                    modifier = Modifier.size(34.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CardBorderColor.copy(alpha = 0.7f))
                ) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Назад в выбор режима",
                        tint = MainTextColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
                IconButton(
                    onClick = vm::goSettings,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, CardBorderColor.copy(alpha = 0.65f), CircleShape)
                ) {
                    Icon(Icons.Outlined.Settings, contentDescription = "Настройки", tint = AccentColor)
                }
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
    var menuExpanded by rememberSaveable { mutableStateOf(false) }
    var searchExpanded by rememberSaveable { mutableStateOf(false) }
    var newShipmentExpanded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(state.openNewShipmentForm) {
        if (state.openNewShipmentForm) {
            newShipmentExpanded = true
            menuExpanded = true
            searchExpanded = false
            vm.consumeNewShipmentShortcut()
        }
    }

    val filtered = state.shipments.filter {
        query.isBlank() || it.title.contains(query, true) || it.marketplace.contains(query, true) || it.date.contains(query, true)
    }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            ShipmentsDashboard(state)
            Spacer(Modifier.height(12.dp))

            LazyColumn(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 92.dp)
            ) {
                if (filtered.isEmpty()) {
                    item {
                        EmptyStateCard(
                            if (state.shipments.isEmpty()) "Поставок пока нет" else "Ничего не найдено",
                            if (state.shipments.isEmpty()) "Создайте первую поставку для выбранного маркетплейса" else "Попробуйте изменить текст поиска"
                        )
                    }
                }
                items(filtered, key = { it.id }) { item ->
                    ShipmentCard(item = item, vm = vm)
                }
            }
        }

        FloatingBottomMenu(
            expanded = menuExpanded,
            onExpandedChange = { expanded ->
                menuExpanded = expanded
                if (!expanded) {
                    searchExpanded = false
                    newShipmentExpanded = false
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 16.dp)
        ) {
            AnimatedVisibility(
                visible = searchExpanded,
                enter = fadeIn(animationSpec = tween(180)) + expandVertically(animationSpec = spring(dampingRatio = 0.76f, stiffness = 520f)),
                exit = fadeOut(animationSpec = tween(120)) + shrinkVertically(animationSpec = tween(160))
            ) {
                FloatingMenuPanel {
                    Text("Поиск поставки", color = MainTextColor, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                    FloatingSearchInput(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = "Название / дата / маркетплейс",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            AnimatedVisibility(
                visible = newShipmentExpanded,
                enter = fadeIn(animationSpec = tween(180)) + expandVertically(animationSpec = spring(dampingRatio = 0.76f, stiffness = 520f)),
                exit = fadeOut(animationSpec = tween(120)) + shrinkVertically(animationSpec = tween(160))
            ) {
                FloatingMenuPanel {
                    Text("Новая поставка", color = MainTextColor, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                    ModernTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = "Название",
                        placeholder = "Например: Поставка 07.05"
                    )
                    ModernTextField(
                        value = date,
                        onValueChange = { date = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = "Дата",
                        placeholder = "2026-05-07",
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
                    AppPrimaryButton("Создать", Modifier.fillMaxWidth(), Icons.Outlined.Add) {
                        vm.createShipment(title, date, marketplace)
                        title = ""
                        newShipmentExpanded = false
                        menuExpanded = false
                    }
                }
            }

            FloatingMenuAction("Поиск поставок", Icons.Outlined.Search) {
                searchExpanded = !searchExpanded
                newShipmentExpanded = false
                menuExpanded = true
            }
            FloatingMenuAction("Новая поставка", Icons.Outlined.Add) {
                newShipmentExpanded = !newShipmentExpanded
                searchExpanded = false
                menuExpanded = true
            }
            FloatingMenuAction("Импорт / отчёты", Icons.Outlined.FileDownload) {
                searchExpanded = false
                newShipmentExpanded = false
                menuExpanded = false
                vm.goSettings()
            }
        }
    }
}

@Composable
private fun ShipmentsDashboard(state: AppUiState) {
    val activeCount = state.shipments.count { !it.isArchived }
    val boxCount = state.shipments.sumOf { it.boxCount }
    val itemCount = state.shipments.sumOf { it.itemCount }
    val positionCount = state.shipments.sumOf { it.positionCount }

    ModernCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Обзор сборки", color = MainTextColor, fontWeight = FontWeight.ExtraBold, fontSize = 21.sp, maxLines = 1)
                    Text("Активные поставки, коробки и товары", color = MutedTextColor, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                StatusBadge("$activeCount активн.", tone = if (activeCount > 0) BadgeTone.Green else BadgeTone.Gray)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DashboardMetric(value = state.shipments.size.toString(), label = "Поставки", icon = Icons.Outlined.Business, modifier = Modifier.weight(1f))
                DashboardMetric(value = boxCount.toString(), label = "Коробки", icon = Icons.Outlined.Inventory2, modifier = Modifier.weight(1f))
                DashboardMetric(value = itemCount.toString(), label = "Единицы", icon = Icons.Outlined.CheckCircle, modifier = Modifier.weight(1f))
            }
            Text("Позиций в работе: $positionCount", color = MutedTextColor, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun DashboardMetric(
    value: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF7F9FF))
            .border(1.dp, CardBorderColor.copy(alpha = 0.75f), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = AccentColor, modifier = Modifier.size(20.dp))
        Column(Modifier.weight(1f)) {
            Text(value, color = MainTextColor, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, maxLines = 1)
            Text(label, color = MutedTextColor, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun MarketplaceButton(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
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
        enabled = enabled,
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
    var cityQuery by remember { mutableStateOf("") }
    var menuExpanded by rememberSaveable { mutableStateOf(false) }
    var searchExpanded by rememberSaveable { mutableStateOf(false) }
    var addExpanded by rememberSaveable { mutableStateOf(false) }
    var exportExpanded by rememberSaveable { mutableStateOf(false) }
    val shipment = state.shipments.firstOrNull { it.id == state.selectedShipmentId }
    val filteredCities = state.shipmentCities.filter {
        cityQuery.isBlank() || it.cityName.contains(cityQuery, ignoreCase = true)
    }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            shipment?.let {
                ShipmentSummaryCard(it)
                Spacer(Modifier.height(12.dp))
            }

            LazyColumn(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 92.dp)
            ) {
                when {
                    state.shipmentCities.isEmpty() -> {
                        item { EmptyStateCard("Города пока не добавлены", "Добавьте город или направление для сборки коробок") }
                    }
                    filteredCities.isEmpty() -> {
                        item { EmptyStateCard("Ничего не найдено", "Попробуйте изменить город или направление в поиске") }
                    }
                }
                items(filteredCities, key = { it.id }) { item ->
                    CityCard(item = item, onOpen = { vm.openCity(item.id) })
                }
            }
        }

        FloatingBottomMenu(
            expanded = menuExpanded,
            onExpandedChange = { expanded ->
                menuExpanded = expanded
                if (!expanded) {
                    searchExpanded = false
                    addExpanded = false
                    exportExpanded = false
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 16.dp)
        ) {
            AnimatedVisibility(
                visible = searchExpanded,
                enter = fadeIn(animationSpec = tween(180)) + expandVertically(animationSpec = spring(dampingRatio = 0.76f, stiffness = 520f)),
                exit = fadeOut(animationSpec = tween(120)) + shrinkVertically(animationSpec = tween(160))
            ) {
                FloatingMenuPanel {
                    Text("Поиск направления", color = MainTextColor, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                    FloatingSearchInput(
                        value = cityQuery,
                        onValueChange = { cityQuery = it },
                        placeholder = "Город / направление",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            AnimatedVisibility(
                visible = addExpanded,
                enter = fadeIn(animationSpec = tween(180)) + expandVertically(animationSpec = spring(dampingRatio = 0.76f, stiffness = 520f)),
                exit = fadeOut(animationSpec = tween(120)) + shrinkVertically(animationSpec = tween(160))
            ) {
                FloatingMenuPanel {
                    Text("Новое направление", color = MainTextColor, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                    ModernTextField(
                        city,
                        { city = it },
                        Modifier.fillMaxWidth(),
                        label = "Город / направление",
                        placeholder = "Москва, СПБ, Казань",
                        leadingIcon = Icons.Outlined.Search
                    )
                    AppPrimaryButton("Добавить", Modifier.fillMaxWidth(), Icons.Outlined.Add) {
                        vm.addCity(city)
                        city = ""
                        addExpanded = false
                        menuExpanded = false
                    }
                }
            }

            AnimatedVisibility(
                visible = exportExpanded,
                enter = fadeIn(animationSpec = tween(180)) + expandVertically(animationSpec = spring(dampingRatio = 0.76f, stiffness = 520f)),
                exit = fadeOut(animationSpec = tween(120)) + shrinkVertically(animationSpec = tween(160))
            ) {
                FloatingMenuPanel {
                    Text("Экспорт", color = MainTextColor, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppPrimaryButton("Excel", Modifier.weight(1f), Icons.Outlined.Description) {
                            vm.generateExcel()
                            exportExpanded = false
                            menuExpanded = false
                        }
                        AppSecondaryButton("CSV", Modifier.weight(1f), Icons.Outlined.FileDownload) {
                            vm.exportCsv()
                            exportExpanded = false
                            menuExpanded = false
                        }
                    }
                }
            }

            FloatingMenuAction("Поиск города", Icons.Outlined.Search) {
                searchExpanded = !searchExpanded
                addExpanded = false
                exportExpanded = false
                menuExpanded = true
            }
            FloatingMenuAction("Добавить направление", Icons.Outlined.Add) {
                addExpanded = !addExpanded
                searchExpanded = false
                exportExpanded = false
                menuExpanded = true
            }
            FloatingMenuAction("Экспорт / Импорт", Icons.Outlined.FileDownload) {
                exportExpanded = !exportExpanded
                searchExpanded = false
                addExpanded = false
                menuExpanded = true
            }
        }
    }
}

@Composable
private fun ShipmentSummaryCard(item: ShipmentCardData) {
    ModernCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AppIconBubble(Icons.Outlined.Inventory2, modifier = Modifier.size(48.dp))
                Column(Modifier.weight(1f)) {
                    Text(item.title, fontWeight = FontWeight.ExtraBold, fontSize = 21.sp, color = MainTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    MarketplaceMetaRow(
                        date = item.date,
                        marketplace = item.marketplace,
                        cityCount = item.cityCount,
                        fontSize = 13
                    )
                }
                MarketplaceBadge(item.marketplace, heightDp = 15)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DashboardMetric(item.boxCount.toString(), "Коробки", Icons.Outlined.Inventory2, Modifier.weight(1f))
                DashboardMetric(item.itemCount.toString(), "Единицы", Icons.Outlined.CheckCircle, Modifier.weight(1f))
                DashboardMetric(item.positionCount.toString(), "Позиций", Icons.Outlined.Description, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun CityCard(item: ShipmentCityCard, onOpen: () -> Unit) {
    ModernCard(Modifier.fillMaxWidth()) {
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val compact = maxWidth < CompactScreenBreakpoint
            Column(Modifier.padding(if (compact) 14.dp else 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    AppIconBubble(Icons.Outlined.Business, modifier = Modifier.size(if (compact) 46.dp else 50.dp))
                    Column(Modifier.weight(1f)) {
                        Text(item.cityName, fontWeight = FontWeight.ExtraBold, fontSize = if (compact) 20.sp else 22.sp, color = MainTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("${item.boxCount} коробок • ${item.itemCount} единиц", color = MutedTextColor, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    StatusBadge(if (item.boxCount == 0) "Пусто" else "В работе", tone = if (item.boxCount == 0) BadgeTone.Gray else BadgeTone.Blue)
                }
                AppPrimaryButton("Открыть коробки", Modifier.fillMaxWidth(), Icons.Outlined.Inventory2, onClick = onOpen)
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
            details = listOfNotNull(shipment?.marketplace, shipment?.let { "${state.boxes.size} коробок" }).joinToString(" • ")
        )
        Spacer(Modifier.height(12.dp))

        ModernCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Новая коробка", color = MainTextColor, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    ModernTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        modifier = Modifier.weight(1f),
                        label = "Комментарий",
                        placeholder = "Например: хрупкий товар",
                        singleLine = true
                    )
                    AppIconActionButton(Icons.Outlined.Add, "Создать коробку", primary = true) {
                        vm.createBox(comment)
                        comment = ""
                    }
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

private fun boxStatus(box: BoxCardData): Pair<String, BadgeTone> {
    val comment = box.comment.orEmpty()
    return when {
        comment.contains("провер", ignoreCase = true) -> "Проверена" to BadgeTone.Green
        comment.contains("собран", ignoreCase = true) || comment.contains("готов", ignoreCase = true) -> "Собрана" to BadgeTone.Green
        box.positionCount.toLong() == 0L && box.itemCount.toLong() == 0L -> "Пустая" to BadgeTone.Gray
        else -> "В работе" to BadgeTone.Blue
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
    val status = boxStatus(box)
    ModernCard(Modifier.fillMaxWidth()) {
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val compact = maxWidth < CompactScreenBreakpoint
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppIconBubble(Icons.Outlined.Inventory2, modifier = Modifier.size(if (compact) 48.dp else 52.dp))
                    Column(Modifier.weight(1f)) {
                        Text(box.boxNumber, fontWeight = FontWeight.ExtraBold, fontSize = if (compact) 21.sp else 24.sp, color = MainTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("${box.positionCount} позиций • ${box.itemCount} единиц", color = MutedTextColor, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    StatusBadge(status.first, tone = status.second)
                }

                if (!box.comment.isNullOrBlank()) {
                    Text(box.comment, color = MutedTextColor, fontSize = 14.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }

                if (renameId == box.id) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        ModernTextField(newNumber, onNewNumberChange, Modifier.weight(1f), label = "Новый номер")
                        AppPrimaryButton("OK", Modifier.width(64.dp), onClick = onSaveRename)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    AppPrimaryButton("Открыть", Modifier.weight(1f), Icons.Outlined.Inventory2, onClick = onOpen)
                    AppIconActionButton(Icons.Outlined.Description, "Переименовать", onClick = onStartRename)
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
    var quickAddExpanded by rememberSaveable { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<BoxItemData?>(null) }
    val shipment = state.shipments.firstOrNull { it.id == state.selectedShipmentId }
    val currentMarketplace = shipment?.marketplace ?: "Ozon"
    val totalUnits = state.boxItems.sumOf { it.quantity }

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .blur(if (selectedItem != null) 3.dp else 0.dp)
        ) {
            ContextStrip(
                title = state.selectedBoxNumber.ifBlank { "Коробка" },
                details = listOf(currentMarketplace, state.selectedCityName, "${state.boxItems.size} поз. • $totalUnits ед.")
                    .filter { it.isNotBlank() }
                    .joinToString(" • ")
            )
            Spacer(Modifier.height(12.dp))

            if (state.pendingBarcode != null) {
                ModernCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Неизвестный код", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = MainTextColor)
                        Text(state.pendingBarcode.orEmpty(), color = MutedTextColor, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        Text("Создайте товар — после сохранения он сразу добавится в коробку.", color = MutedTextColor, fontSize = 13.sp)
                        ModernTextField(article, { article = it }, Modifier.fillMaxWidth(), label = "Артикул")
                        ModernTextField(name, { name = it }, Modifier.fillMaxWidth(), label = "Название товара")
                        ModernTextField(barcode, { barcode = it }, Modifier.fillMaxWidth(), label = "Штрихкод")
                        ModernTextField(qty, { qty = it }, Modifier.fillMaxWidth(), label = "Количество", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                        AppPrimaryButton("Создать и добавить", Modifier.fillMaxWidth(), Icons.Outlined.Add) {
                            vm.createProductAndAdd(article, name, barcode, qty.toIntOrNull() ?: 1, fromScan = true)
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            ModernCard(Modifier.fillMaxWidth()) {
                BoxWithConstraints(Modifier.fillMaxWidth()) {
                    val compact = maxWidth < CompactScreenBreakpoint
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { quickAddExpanded = !quickAddExpanded }
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text("Быстрое добавление", fontWeight = FontWeight.ExtraBold, fontSize = if (compact) 19.sp else 21.sp, color = MainTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("Поиск товара, ручное создание и добавление", color = MutedTextColor, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                            val collapseRotation by androidx.compose.animation.core.animateFloatAsState(
                                targetValue = if (quickAddExpanded) 180f else 0f,
                                animationSpec = spring(dampingRatio = 0.58f, stiffness = 520f),
                                label = "quick_add_collapse_rotation"
                            )
                            Icon(
                                imageVector = Icons.Outlined.ExpandMore,
                                contentDescription = if (quickAddExpanded) "Свернуть" else "Развернуть",
                                tint = AccentColor,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(24.dp)
                                    .rotate(collapseRotation)
                            )
                        }

                        AnimatedVisibility(
                            visible = quickAddExpanded,
                            enter = fadeIn(animationSpec = tween(260)) + slideInVertically(animationSpec = spring(dampingRatio = 0.68f, stiffness = 460f)) { -it / 5 } + expandVertically(animationSpec = spring(dampingRatio = 0.7f, stiffness = 500f)),
                            exit = fadeOut(animationSpec = tween(170)) + slideOutVertically(animationSpec = tween(210)) { -it / 6 } + shrinkVertically(animationSpec = tween(220))
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                if (compact) {
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        ModernTextField(query, { query = it }, Modifier.fillMaxWidth(), label = "Артикул / название / код", placeholder = "333", leadingIcon = Icons.Outlined.Search)
                                        ModernTextField(qty, { qty = it }, Modifier.fillMaxWidth(), label = "Кол-во", placeholder = "1", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                                    }
                                } else {
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                        ModernTextField(query, { query = it }, Modifier.weight(1f), label = "Артикул / название / код", placeholder = "333", leadingIcon = Icons.Outlined.Search)
                                        ModernTextField(qty, { qty = it }, Modifier.widthIn(min = 82.dp, max = 96.dp), label = "Кол-во", placeholder = "1", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
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
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppSectionTitle()
                StatusBadge("$totalUnits ед.", tone = if (totalUnits > 0) BadgeTone.Blue else BadgeTone.Gray)
            }

            LazyColumn(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 108.dp)
            ) {
                if (state.boxItems.isEmpty()) {
                    item { EmptyStateCard("В коробке пока нет товаров", "Отсканируйте товар или добавьте его вручную") }
                }
                items(state.boxItems, key = { it.id }) { item ->
                    BoxItemCard(
                        item = item,
                        marketplace = currentMarketplace,
                        onChangeQuantity = vm::changeItemQuantity,
                        onOpenDetails = { selectedItem = it }
                    )
                }
            }
        }

        BoxBottomActionBar(
            qty = qty,
            onQtyChange = { qty = it },
            onScan = vm::openScanner,
            onExcel = { vm.generateExcel() },
            onToggleQuickAdd = { quickAddExpanded = !quickAddExpanded },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        ProductDetailsBottomSheet(
            item = selectedItem,
            marketplace = currentMarketplace,
            onDismiss = { selectedItem = null }
        )
    }
}

@Composable
private fun BoxBottomActionBar(
    qty: String,
    onQtyChange: (String) -> Unit,
    onScan: () -> Unit,
    onExcel: () -> Unit,
    onToggleQuickAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomDockSurface(modifier = modifier.padding(bottom = 2.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            DockHandle()
        }
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(InputContainerColor)
                    .border(1.dp, CardBorderColor.copy(alpha = 0.92f), RoundedCornerShape(18.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuantityIconButton(Icons.Outlined.Remove) {
                    val next = ((qty.toIntOrNull() ?: 1) - 1).coerceAtLeast(1)
                    onQtyChange(next.toString())
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Кол-во", color = MutedTextColor, fontSize = 11.sp, maxLines = 1)
                    Text(
                        (qty.toIntOrNull() ?: 1).toString(),
                        color = MainTextColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        maxLines = 1
                    )
                }
                QuantityIconButton(Icons.Outlined.Add, accent = true) {
                    val next = (qty.toIntOrNull() ?: 1) + 1
                    onQtyChange(next.toString())
                }
            }

            AppIconActionButton(Icons.Outlined.Description, "Excel", onClick = onExcel)
            AppIconActionButton(Icons.Outlined.Add, "Добавить", onClick = onToggleQuickAdd)
            Button(
                onClick = onScan,
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentColor, contentColor = Color.White)
            ) {
                Icon(
                    Icons.Outlined.QrCodeScanner,
                    contentDescription = "Сканировать",
                    modifier = Modifier.size(26.dp)
                )
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
private fun BoxItemCard(
    item: BoxItemData,
    marketplace: String,
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

    val title = item.name.ifBlank { item.article }
    val code = item.barcode.orEmpty().ifBlank { item.article }

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
                Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AppIconBubble(Icons.Outlined.Inventory2, modifier = Modifier.size(if (compact) 46.dp else 50.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                title,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = if (compact) 18.sp else 20.sp,
                                lineHeight = if (compact) 21.sp else 23.sp,
                                color = MainTextColor,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                "Артикул: ${item.article} • Код: $code",
                                color = MutedTextColor,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        MarketplaceBadge(marketplace, heightDp = 13)
                    }

                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            QuantityIconButton(icon = Icons.Outlined.Remove, onClick = { onChangeQuantity(item.id, item.quantity - 1) })
                            AnimatedQuantity(item.quantity, Modifier.widthIn(min = 28.dp))
                            QuantityIconButton(icon = Icons.Outlined.Add, accent = true, onClick = { onChangeQuantity(item.id, item.quantity + 1) })
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            StatusBadge("Кол-во: ${item.quantity}", tone = BadgeTone.Gray)
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
    marketplace: String,
    onDismiss: () -> Unit
) {
    val visibleItem = item ?: return
    val closeInteraction = remember { MutableInteractionSource() }
    val sheetInteraction = remember { MutableInteractionSource() }
    val title = visibleItem.name.ifBlank { visibleItem.article }
    val code = visibleItem.barcode.orEmpty().ifBlank { visibleItem.article }

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
                        .shadow(
                            elevation = 24.dp,
                            shape = RoundedCornerShape(30.dp),
                            ambientColor = Color(0x220B1226),
                            spotColor = Color(0x300B1226)
                        )
                        .clickable(
                            interactionSource = sheetInteraction,
                            indication = null,
                            onClick = {}
                        ),
                    shape = RoundedCornerShape(30.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.9f))
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
                                        "Позиция в текущей коробке",
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
                            value = title,
                            label = "Название товара",
                            icon = Icons.Outlined.Description
                        )
                        ProductDetailInfoBlock(
                            value = visibleItem.article,
                            label = "Артикул",
                            icon = Icons.Outlined.Inventory2
                        )
                        ProductDetailInfoBlock(
                            value = code,
                            label = "Штрихкод / код",
                            icon = Icons.Outlined.QrCodeScanner
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MarketplaceBadge(marketplace, heightDp = 15)
                            StatusBadge("Кол-во: ${visibleItem.quantity}", tone = BadgeTone.Blue)
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
                fontSize = if (label.contains("Название")) 18.sp else 21.sp,
                lineHeight = if (label.contains("Название")) 22.sp else 24.sp,
                maxLines = if (label.contains("Название")) 4 else 2,
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
                ContentTransform(
                    targetContentEnter = slideInVertically { it / 2 } + fadeIn(),
                    initialContentExit = slideOutVertically { -it / 2 } + fadeOut(),
                    sizeTransform = SizeTransform(clip = false)
                )
            } else {
                ContentTransform(
                    targetContentEnter = slideInVertically { -it / 2 } + fadeIn(),
                    initialContentExit = slideOutVertically { it / 2 } + fadeOut(),
                    sizeTransform = SizeTransform(clip = false)
                )
            }
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
        if (uri != null) vm.previewProductImport(uri)
    }
    LazyColumn(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            SettingsGroup(
                title = "Импорт товаров",
                subtitle = "Безопасная загрузка справочника для сканирования",
                icon = Icons.Outlined.FileDownload
            ) {
                Text(
                    "Сначала приложение покажет предпросмотр: найденные колонки, строки с ошибками, дубли штрихкодов и товары, которые будут обновлены.",
                    color = MutedTextColor,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
                AppPrimaryButton("Выбрать файл", Modifier.fillMaxWidth(), Icons.Outlined.FileDownload) {
                    picker.launch(arrayOf("text/*", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/xml", "text/xml", "text/csv"))
                }
                Text(
                    "Поддерживаемые колонки: article/артикул, name/название, barcode/штрихкод. Строки без названия товара не импортируются.",
                    color = SoftTextColor,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
        state.importPreview?.let { preview ->
            item { ImportPreviewCard(preview = preview, vm = vm) }
        }
        state.importResult?.let { result ->
            item { ImportResultCard(result = result, onClose = vm::clearImportResult) }
        }
        item {
            SettingsGroup(
                title = "Отчеты",
                subtitle = "Excel и CSV для контроля поставки",
                icon = Icons.Outlined.Description
            ) {
                SettingsInfoRow("Формат", "Excel для итогового отчета, CSV для обмена")
                SettingsInfoRow("Папка", "Файлы сохраняются в Documents")
                state.lastFile?.let {
                    StatusBadge("Последний файл: ${it.name}", tone = BadgeTone.Gray)
                } ?: Text("Последний файл пока не сформирован", color = MutedTextColor, fontSize = 14.sp)
            }
        }
        item {
            SettingsGroup(
                title = "Правила сборки",
                subtitle = "То, что помогает не ошибаться на складе",
                icon = Icons.Outlined.Settings
            ) {
                SettingsInfoRow("Номер коробки", "Рекомендуемый вид: CITY-001")
                SettingsInfoRow("Удаление", "Опасные действия выделены красным")
                SettingsInfoRow("Статусы", "Пустая, В работе, Собрана, Проверена")
            }
        }
        item {
            SettingsGroup(
                title = "Маркетплейсы",
                subtitle = "Логотипы вместо текстовых подписей",
                icon = Icons.Outlined.Business
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    MarketplaceBadge("Ozon")
                    MarketplaceBadge("Wildberries")
                }
                Text("В карточках товаров используется маркетплейс текущей поставки, а не жестко заданный Ozon.", color = MutedTextColor, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun ImportPreviewCard(preview: ProductImportPreview, vm: AppViewModel) {
    SettingsGroup(
        title = "Предпросмотр импорта",
        subtitle = preview.fileName,
        icon = Icons.Outlined.Search
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ImportMetric("Строк", preview.rowsTotal.toString(), Modifier.weight(1f))
                ImportMetric("К импорту", preview.rowsForImport.toString(), Modifier.weight(1f), positive = true)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ImportMetric("Обновится", preview.updateRows.toString(), Modifier.weight(1f), positive = true)
                ImportMetric("Добавится", preview.addRows.toString(), Modifier.weight(1f), positive = true)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ImportMetric("Ошибки", preview.errorRows.toString(), Modifier.weight(1f), danger = preview.errorRows > 0)
                ImportMetric("Дубли", preview.duplicateBarcodeRows.toString(), Modifier.weight(1f), danger = preview.duplicateBarcodeRows > 0)
            }

            Text("Найденные колонки", fontWeight = FontWeight.ExtraBold, color = MainTextColor, fontSize = 15.sp)
            preview.columns.forEach { column ->
                SettingsInfoRow(column.role, if (column.found) column.source else "Не найдена")
            }

            val problemRows = preview.rows.filter { it.errors.isNotEmpty() || it.duplicateInFile }.take(8)
            if (problemRows.isNotEmpty()) {
                Text("Строки, которые будут пропущены", fontWeight = FontWeight.ExtraBold, color = MainTextColor, fontSize = 15.sp)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    problemRows.forEach { row ->
                        val reason = when {
                            row.errors.isNotEmpty() -> row.errors.joinToString(", ")
                            row.duplicateInFile -> "Дубль штрихкода в файле"
                            else -> "Проверить строку"
                        }
                        ImportIssueRow(row.rowNumber, reason)
                    }
                }
                val rest = preview.rows.count { it.errors.isNotEmpty() || it.duplicateInFile } - problemRows.size
                if (rest > 0) Text("Ещё проблемных строк: $rest", color = MutedTextColor, fontSize = 13.sp)
            }

            val pageSize = 12
            var pageIndex by rememberSaveable(preview.fileName, preview.rows.size) { mutableStateOf(0) }
            val totalPages = ((preview.rows.size + pageSize - 1) / pageSize).coerceAtLeast(1)
            if (pageIndex >= totalPages) pageIndex = totalPages - 1
            val fromIndex = pageIndex * pageSize
            val toIndex = minOf(fromIndex + pageSize, preview.rows.size)

            Text("Первые строки файла", fontWeight = FontWeight.ExtraBold, color = MainTextColor, fontSize = 15.sp)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                preview.rows.subList(fromIndex, toIndex).forEach { row -> ImportRowPreviewItem(row) }
            }
            if (preview.rows.size > pageSize) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Показано ${fromIndex + 1}–$toIndex из ${preview.rows.size}", color = MutedTextColor, fontSize = 13.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        AppSecondaryButton("Назад", icon = Icons.Outlined.Remove, enabled = pageIndex > 0) {
                            pageIndex -= 1
                        }
                        AppSecondaryButton("Вперёд", icon = Icons.Outlined.Add, enabled = pageIndex < totalPages - 1) {
                            pageIndex += 1
                        }
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AppSecondaryButton("Отменить", Modifier.weight(1f), Icons.Outlined.Close) { vm.cancelProductImport() }
                AppPrimaryButton(
                    text = "Импортировать",
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.CheckCircle,
                    enabled = preview.rowsForImport > 0
                ) { vm.confirmProductImport() }
            }
        }
    }
}

@Composable
private fun ImportResultCard(result: ProductImportResult, onClose: () -> Unit) {
    SettingsGroup(
        title = "Результат импорта",
        subtitle = result.fileName,
        icon = Icons.Outlined.CheckCircle
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ImportMetric("Обновлено", result.updated.toString(), Modifier.weight(1f), positive = true)
            ImportMetric("Добавлено", result.added.toString(), Modifier.weight(1f), positive = true)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ImportMetric("Пропущено", result.skipped.toString(), Modifier.weight(1f), danger = result.skipped > 0)
            ImportMetric("Ошибки", result.errors.toString(), Modifier.weight(1f), danger = result.errors > 0)
        }
        if (result.duplicateBarcodes > 0) {
            StatusBadge("Дубли штрихкодов в файле: ${result.duplicateBarcodes}", tone = BadgeTone.Purple)
        }
        AppSecondaryButton("Скрыть результат", Modifier.fillMaxWidth(), Icons.Outlined.Close, onClick = onClose)
    }
}

@Composable
private fun ImportMetric(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    positive: Boolean = false,
    danger: Boolean = false
) {
    val tint = when {
        danger -> DangerColor
        positive -> SuccessColor
        else -> AccentColor
    }
    Column(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF7F9FF))
            .border(1.dp, CardBorderColor.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(title, color = MutedTextColor, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(value, color = tint, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, maxLines = 1)
    }
}

@Composable
private fun ImportIssueRow(rowNumber: Int, reason: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFFFF7ED))
            .border(1.dp, Color(0xFFFED7AA), RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 9.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatusBadge("$rowNumber", tone = BadgeTone.Purple)
        Text(reason, color = MainTextColor, fontSize = 13.sp, lineHeight = 16.sp, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ImportRowPreviewItem(row: ImportRowPreview) {
    val statusText = when {
        row.errors.isNotEmpty() -> "Ошибка"
        row.duplicateInFile -> "Дубль"
        row.willUpdate -> "Обновится"
        else -> "Добавится"
    }
    val statusTone = when {
        row.errors.isNotEmpty() || row.duplicateInFile -> BadgeTone.Purple
        row.willUpdate -> BadgeTone.Blue
        else -> BadgeTone.Green
    }
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF7F9FF))
            .border(1.dp, CardBorderColor.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Строка ${row.rowNumber}", color = MutedTextColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            StatusBadge(statusText, tone = statusTone)
        }
        Text(row.name.ifBlank { "Без названия" }, color = MainTextColor, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text("Артикул: ${row.article.ifBlank { "—" }}", color = MutedTextColor, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text("Штрихкод: ${row.barcode ?: "—"}", color = MutedTextColor, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun SettingsGroup(
    title: String,
    subtitle: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    ModernCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AppIconBubble(icon, modifier = Modifier.size(50.dp))
                Column(Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = MainTextColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(subtitle, color = MutedTextColor, fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            }
            HorizontalDivider(color = CardBorderColor.copy(alpha = 0.7f))
            content()
        }
    }
}

@Composable
private fun SettingsInfoRow(title: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF7F9FF))
            .border(1.dp, CardBorderColor.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 11.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = MainTextColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.weight(0.9f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(value, color = MutedTextColor, fontSize = 13.sp, textAlign = TextAlign.End, modifier = Modifier.weight(1.5f), maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}


private data class ModeBadgeData(
    val text: String,
    val icon: ImageVector,
    val tone: Color
)

@Composable
private fun MainMenuScreen(onBack: () -> Unit, onSupply: () -> Unit, onPreAssembly: () -> Unit) {
    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFBFDFF), Color(0xFFF2F7FF), Color(0xFFEAF2FF))
                )
            )
    ) {
        val compact = maxWidth < CompactScreenBreakpoint
        val horizontalPadding = if (compact) 16.dp else 24.dp

        ModeMenuBackground(Modifier.matchParentSize())
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = horizontalPadding,
                end = horizontalPadding,
                top = if (compact) 14.dp else 22.dp,
                bottom = 28.dp
            ),
            verticalArrangement = Arrangement.spacedBy(if (compact) 14.dp else 18.dp)
        ) {
            item {
                ModeMenuHero(onBack = onBack, compact = compact)
            }
            item {
                ModeChoiceCard(
                    title = "Поставка на склады",
                    subtitle = "Основной режим работы с поставками, городами и коробами",
                    icon = Icons.Outlined.Inventory2,
                    accent = AccentColor,
                    onClick = onSupply
                )
            }
            item {
                ModeChoiceCard(
                    title = "Предварительная сборка",
                    subtitle = "Ручная проверка заказов Ozon: есть товар, нет товара, сколько переместить",
                    icon = Icons.Outlined.CheckCircle,
                    accent = SuccessColor,
                    badges = listOf(
                        ModeBadgeData("Отдельный экран", Icons.Outlined.Description, AccentColor),
                        ModeBadgeData("Список бухгалтеру", Icons.Outlined.CheckCircle, SuccessColor)
                    ),
                    onClick = onPreAssembly
                )
            }
        }
    }
}

@Composable
private fun ModeMenuBackground(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val stroke = 2.dp.toPx()
        drawCircle(
            color = Color.White.copy(alpha = 0.78f),
            radius = size.width * 0.42f,
            center = Offset(size.width * 0.98f, -size.height * 0.03f),
            style = Stroke(width = stroke)
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.58f),
            radius = size.width * 0.30f,
            center = Offset(size.width * 0.96f, 0f),
            style = Stroke(width = stroke)
        )
        drawCircle(
            color = Color(0xFFE3ECFF).copy(alpha = 0.54f),
            radius = size.width * 0.44f,
            center = Offset(size.width * 0.88f, size.height * 1.03f),
            style = Stroke(width = stroke)
        )
    }
}

@Composable
private fun ModeMenuHero(onBack: () -> Unit, compact: Boolean) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(if (compact) 306.dp else 346.dp)
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(if (compact) 54.dp else 62.dp)
                .shadow(
                    elevation = 14.dp,
                    shape = CircleShape,
                    ambientColor = Color(0x160B1226),
                    spotColor = Color(0x220B1226)
                )
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.96f))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Назад",
                tint = MainTextColor,
                modifier = Modifier.size(if (compact) 27.dp else 31.dp)
            )
        }

        ModeHeroIllustration(
            Modifier
                .align(Alignment.TopEnd)
                .size(if (compact) 178.dp else 232.dp)
                .alpha(if (compact) 0.88f else 1f)
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(if (compact) 0.94f else 0.74f),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            Text(
                "Выбор режима",
                fontSize = if (compact) 35.sp else 42.sp,
                lineHeight = if (compact) 39.sp else 46.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MainTextColor
            )
            Text(
                "Выберите, что нужно сделать сейчас.\nОсновная поставка и предварительная сборка открываются отдельно.",
                color = MutedTextColor,
                fontSize = if (compact) 17.sp else 19.sp,
                lineHeight = if (compact) 25.sp else 28.sp
            )
        }
    }
}

@Composable
private fun ModeHeroIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        fun cube(center: Offset, side: Float, top: Color, left: Color, right: Color, edge: Color) {
            val topPoint = Offset(center.x, center.y - side * 0.58f)
            val rightPoint = Offset(center.x + side * 0.72f, center.y - side * 0.17f)
            val bottomPoint = Offset(center.x, center.y + side * 0.24f)
            val leftPoint = Offset(center.x - side * 0.72f, center.y - side * 0.17f)
            val rightBottom = Offset(rightPoint.x, rightPoint.y + side * 0.78f)
            val bottomDrop = Offset(center.x, bottomPoint.y + side * 0.78f)
            val leftBottom = Offset(leftPoint.x, leftPoint.y + side * 0.78f)

            val topPath = Path().apply {
                moveTo(topPoint.x, topPoint.y)
                lineTo(rightPoint.x, rightPoint.y)
                lineTo(bottomPoint.x, bottomPoint.y)
                lineTo(leftPoint.x, leftPoint.y)
                close()
            }
            val rightPath = Path().apply {
                moveTo(rightPoint.x, rightPoint.y)
                lineTo(rightBottom.x, rightBottom.y)
                lineTo(bottomDrop.x, bottomDrop.y)
                lineTo(bottomPoint.x, bottomPoint.y)
                close()
            }
            val leftPath = Path().apply {
                moveTo(leftPoint.x, leftPoint.y)
                lineTo(bottomPoint.x, bottomPoint.y)
                lineTo(bottomDrop.x, bottomDrop.y)
                lineTo(leftBottom.x, leftBottom.y)
                close()
            }

            drawPath(leftPath, left)
            drawPath(rightPath, right)
            drawPath(topPath, top)
            drawPath(topPath, edge, style = Stroke(width = 1.dp.toPx()))
            drawPath(leftPath, edge.copy(alpha = 0.58f), style = Stroke(width = 1.dp.toPx()))
            drawPath(rightPath, edge.copy(alpha = 0.58f), style = Stroke(width = 1.dp.toPx()))
        }

        drawOval(
            color = Color(0xFFD9E8FF).copy(alpha = 0.55f),
            topLeft = Offset(size.width * 0.10f, size.height * 0.72f),
            size = Size(size.width * 0.76f, size.height * 0.18f)
        )
        cube(
            center = Offset(size.width * 0.53f, size.height * 0.48f),
            side = size.width * 0.29f,
            top = Color.White.copy(alpha = 0.95f),
            left = Color(0xFFE8F0FF).copy(alpha = 0.86f),
            right = Color(0xFFF8FBFF).copy(alpha = 0.95f),
            edge = Color(0xFFC8D9F8).copy(alpha = 0.50f)
        )
        cube(
            center = Offset(size.width * 0.56f, size.height * 0.18f),
            side = size.width * 0.21f,
            top = Color(0xFFA8C9FF),
            left = Color(0xFF4EA7FF),
            right = Color(0xFF2F6BFF),
            edge = Color(0xFF7FAEFF)
        )
        cube(
            center = Offset(size.width * 0.88f, size.height * 0.62f),
            side = size.width * 0.15f,
            top = Color(0xFFBEE1FF).copy(alpha = 0.95f),
            left = Color(0xFF77C7FF).copy(alpha = 0.88f),
            right = Color(0xFF57A1FF).copy(alpha = 0.88f),
            edge = Color(0xFFB6D5FF).copy(alpha = 0.76f)
        )
    }
}

@Composable
private fun ModeChoiceCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accent: Color,
    badges: List<ModeBadgeData> = emptyList(),
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(28.dp)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = shape,
                ambientColor = Color(0x120B1226),
                spotColor = Color(0x180B1226)
            )
            .clip(shape)
            .background(Color.White.copy(alpha = 0.96f))
            .border(1.dp, Color(0xFFE4EBF7), shape)
            .clickable(onClick = onClick)
    ) {
        val compact = maxWidth < CompactScreenBreakpoint
        Canvas(Modifier.matchParentSize()) {
            drawRect(
                color = accent,
                topLeft = Offset.Zero,
                size = Size(7.dp.toPx(), size.height)
            )
        }
        Column(
            Modifier.padding(
                start = if (compact) 18.dp else 24.dp,
                top = if (compact) 18.dp else 24.dp,
                end = if (compact) 16.dp else 22.dp,
                bottom = if (badges.isEmpty()) {
                    if (compact) 18.dp else 24.dp
                } else {
                    if (compact) 20.dp else 24.dp
                }
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(if (compact) 14.dp else 20.dp)
            ) {
                ModeChoiceIcon(icon = icon, accent = accent, size = if (compact) 70.dp else 86.dp)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Text(
                        title,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = if (compact) 24.sp else 28.sp,
                        lineHeight = if (compact) 28.sp else 32.sp,
                        color = MainTextColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        subtitle,
                        color = MutedTextColor,
                        fontSize = if (compact) 16.sp else 18.sp,
                        lineHeight = if (compact) 23.sp else 26.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Box(
                    modifier = Modifier
                        .size(if (compact) 48.dp else 58.dp)
                        .clip(CircleShape)
                        .background(accent.copy(alpha = 0.10f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.ExpandMore,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier
                            .size(if (compact) 28.dp else 32.dp)
                            .rotate(-90f)
                    )
                }
            }
            if (badges.isNotEmpty()) {
                Spacer(Modifier.height(if (compact) 18.dp else 22.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    badges.forEach { badge ->
                        ModeBadge(badge = badge, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun ModeChoiceIcon(icon: ImageVector, accent: Color, size: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    listOf(accent.copy(alpha = 0.15f), accent.copy(alpha = 0.05f), Color.Transparent)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(size * 0.44f))
    }
}

@Composable
private fun ModeBadge(badge: ModeBadgeData, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(badge.tone.copy(alpha = 0.10f))
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(badge.icon, contentDescription = null, tint = badge.tone, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(7.dp))
        Text(
            badge.text,
            color = badge.tone,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PreAssemblyScreen(state: PreAssemblyUiState, vm: PreAssemblyViewModel, onBack: () -> Unit) {
    var showPreview by remember { mutableStateOf(false) }
    var showControls by rememberSaveable { mutableStateOf(false) }
    var showBulkActions by rememberSaveable { mutableStateOf(false) }
    var showFinishDialog by rememberSaveable { mutableStateOf(false) }
    var showArchiveDialog by rememberSaveable { mutableStateOf(false) }
    var selectedArchiveId by rememberSaveable { mutableStateOf<Long?>(null) }
    var pendingBulkAction by remember { mutableStateOf<PreAssemblyBulkAction?>(null) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var statusFilter by rememberSaveable { mutableStateOf("ALL") }
    var sortOrderKey by rememberSaveable { mutableStateOf(PreAssemblySortOrder.ATTENTION.name) }
    var isSummaryExpanded by rememberSaveable { mutableStateOf(true) }
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current
    var printBridgeHost by rememberSaveable { mutableStateOf(PreAssemblyPrinter.savedBridgeHost(context)) }
    var printPrinterName by rememberSaveable { mutableStateOf(PreAssemblyPrinter.savedPrinterName(context)) }
    val sortOrder = PreAssemblySortOrder.values().firstOrNull { it.name == sortOrderKey } ?: PreAssemblySortOrder.ATTENTION
    val checkedCount = state.items.count { it.status != PreAssemblyStatus.NOT_CHECKED }
    val availableCount = state.items.count { it.status == PreAssemblyStatus.AVAILABLE }
    val toTransferCount = state.items.count { it.status == PreAssemblyStatus.NOT_AVAILABLE || it.status == PreAssemblyStatus.NEED_TRANSFER }
    val notCheckedCount = state.items.size - checkedCount
    val commentsCount = state.items.count { it.comment.isNotBlank() }
    val hasActiveFilters = searchQuery.isNotBlank() || statusFilter != "ALL"
    val filteredItems = state.items.filter { item ->
        val byStatus = statusFilter == "ALL" || item.status.name == statusFilter
        val query = searchQuery.trim()
        val bySearch = query.isBlank() || item.offerId.contains(query, ignoreCase = true) ||
            item.name.contains(query, ignoreCase = true) ||
            item.orderId.contains(query, ignoreCase = true) ||
            (item.sku?.contains(query, ignoreCase = true) == true)
        byStatus && bySearch
    }
    val sortedItems = sortPreAssemblyItems(filteredItems, sortOrder)
    val visibleIds = filteredItems.map { it.id }

    LaunchedEffect(state.message) {
        if (state.message != null) {
            delay(2600)
            vm.clearMessage()
        }
    }

    fun shareReport(text: String) {
        context.startActivity(
            android.content.Intent.createChooser(
                android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(android.content.Intent.EXTRA_TEXT, text)
                },
                "Отправить через"
            )
        )
    }

    fun printReport(text: String) {
        PreAssemblyPrinter.printTransferList(context, text)
            .onFailure { error ->
                Toast.makeText(
                    context,
                    "Не удалось открыть печать: ${error.message ?: "ошибка печати"}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    fun printTestPage() {
        PreAssemblyPrinter.printTestPage(context)
            .onFailure { error ->
                Toast.makeText(
                    context,
                    "Не удалось открыть тестовую печать: ${error.message ?: "ошибка печати"}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    fun sharePrintLogs() {
        PreAssemblyPrintLog.append(context, "Пользователь запросил выгрузку логов печати")
        PreAssemblyPrintLog.share(context)
            .onFailure { error ->
                PreAssemblyPrintLog.append(context, "Не удалось выгрузить логи печати", error)
                Toast.makeText(
                    context,
                    "Не удалось выгрузить логи печати: ${error.message ?: "ошибка экспорта"}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PreAssemblyHeader(onBack = onBack, onRefresh = vm::loadOrders, isLoading = state.isLoading)

            when {
                state.isLoading -> PreAssemblyLoadingCard()
                state.error != null -> PreAssemblyErrorCard(message = state.error, onRetry = vm::loadOrders)
                state.items.isEmpty() -> PreAssemblyEmptyCard(onLoad = vm::loadOrders)
                else -> {
                    PreAssemblySummaryPanel(
                        total = state.items.size,
                        checked = checkedCount,
                        available = availableCount,
                        toTransfer = toTransferCount,
                        notChecked = notCheckedCount,
                        isExpanded = isSummaryExpanded,
                        onToggleExpanded = { isSummaryExpanded = !isSummaryExpanded }
                    )
                    if (state.isCompleted) {
                        PreAssemblyCompletedBanner(
                            completedAt = state.completedAt,
                            hasProblems = toTransferCount > 0,
                            hasUnchecked = notCheckedCount > 0,
                            onReturnToWork = vm::returnToWork
                        )
                    }
                    PreAssemblyVisibleInfoRow(
                        visibleCount = filteredItems.size,
                        totalCount = state.items.size,
                        sortTitle = sortOrder.title,
                        isCompleted = state.isCompleted
                    )
                    if (filteredItems.isEmpty()) {
                        PreAssemblyNoResultsCard(onReset = { searchQuery = ""; statusFilter = "ALL" })
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 282.dp)
                        ) {
                            items(sortedItems, key = { it.id }) { item ->
                                PreAssemblyItemCard(item = item, vm = vm, readOnly = state.isCompleted)
                            }
                        }
                    }
                }
            }

            state.message?.let { message ->
                PreAssemblyMessageCard(message = message)
            }
        }

        PreAssemblyActionPanel(
            hasItems = state.items.isNotEmpty(),
            hasVisibleItems = filteredItems.isNotEmpty(),
            hasReport = state.reportText.isNotBlank(),
            hasActiveFilters = hasActiveFilters,
            hasArchive = state.archive.isNotEmpty(),
            isCompleted = state.isCompleted,
            onOpenControls = { showControls = true },
            onOpenBulkActions = { showBulkActions = true },
            onBuildReport = { showPreview = vm.buildReport() },
            onFinishAssembly = { showFinishDialog = true },
            onShareReport = {
                if (vm.buildReport()) {
                    showPreview = true
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 18.dp)
        )
    }

    if (showControls) {
        PreAssemblyControlsDialog(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            statusFilter = statusFilter,
            onStatusFilterChange = { statusFilter = it },
            sortOrder = sortOrder,
            onSortOrderChange = { sortOrderKey = it.name },
            onReload = vm::loadOrders,
            isLoading = state.isLoading,
            onDismiss = { showControls = false }
        )
    }

    if (showBulkActions) {
        PreAssemblyBulkActionsDialog(
            visibleCount = filteredItems.size,
            totalCount = state.items.size,
            isCompleted = state.isCompleted,
            archive = state.archive,
            printBridgeHost = printBridgeHost,
            onPrintBridgeHostChange = { host ->
                printBridgeHost = host
                PreAssemblyPrinter.saveBridgeHost(context, host)
            },
            printPrinterName = printPrinterName,
            onPrintPrinterNameChange = { printerName ->
                printPrinterName = printerName
                PreAssemblyPrinter.savePrinterName(context, printerName)
            },
            onOpenArchive = {
                showBulkActions = false
                showArchiveDialog = true
            },
            onPrintTest = {
                showBulkActions = false
                printTestPage()
            },
            onSharePrintLogs = {
                showBulkActions = false
                sharePrintLogs()
            },
            onAction = { action ->
                pendingBulkAction = action
                showBulkActions = false
            },
            onDismiss = { showBulkActions = false }
        )
    }

    pendingBulkAction?.let { action ->
        PreAssemblyBulkConfirmDialog(
            action = action,
            visibleCount = filteredItems.size,
            totalCount = state.items.size,
            onDismiss = { pendingBulkAction = null },
            onConfirm = {
                when (action) {
                    PreAssemblyBulkAction.MARK_AVAILABLE -> vm.markVisibleAsAvailable(visibleIds)
                    PreAssemblyBulkAction.RESET_CHECK -> vm.resetVisibleCheck(visibleIds)
                    PreAssemblyBulkAction.CLEAR_COMMENTS -> vm.clearVisibleComments(visibleIds)
                }
                pendingBulkAction = null
            }
        )
    }

    if (showFinishDialog) {
        PreAssemblyFinishDialog(
            total = state.items.size,
            checked = checkedCount,
            available = availableCount,
            toTransfer = toTransferCount,
            notChecked = notCheckedCount,
            comments = commentsCount,
            isCompleted = state.isCompleted,
            completedAt = state.completedAt,
            onDismiss = { showFinishDialog = false },
            onFinish = {
                vm.finishPreAssembly()
                showFinishDialog = false
            },
            onReturnToWork = {
                vm.returnToWork()
                showFinishDialog = false
            }
        )
    }

    if (showPreview && state.reportText.isNotBlank()) {
        PreAssemblyReportDialog(
            reportText = state.reportText,
            onDismiss = { showPreview = false },
            onCopy = { clipboard.setText(AnnotatedString(state.reportText)) },
            onPrint = { printReport(state.reportText) },
            onShare = {
                printReport(state.reportText)
                shareReport(state.reportText)
            }
        )
    }

    if (showArchiveDialog) {
        PreAssemblyArchiveDialog(
            archive = state.archive,
            onOpenEntry = {
                selectedArchiveId = it
                showArchiveDialog = false
            },
            onDismiss = { showArchiveDialog = false }
        )
    }

    state.archive.firstOrNull { it.id == selectedArchiveId }?.let { entry ->
        PreAssemblyArchiveDetailsDialog(
            entry = entry,
            onSave = vm::updateArchiveEntry,
            onShare = { shareReport(PreAssemblyArchiveMessageText(it)) },
            onDismiss = { selectedArchiveId = null }
        )
    }
}

@Composable
private fun PreAssemblyHeader(onBack: () -> Unit, onRefresh: () -> Unit, isLoading: Boolean) {
    ModernCard(Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AppIconActionButton(Icons.AutoMirrored.Outlined.ArrowBack, "Назад", onClick = onBack)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    "Предварительная сборка Ozon",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MainTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "Ручная проверка наличия и списка на перемещение",
                    color = MutedTextColor,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            AppIconActionButton(
                icon = Icons.Outlined.FileDownload,
                contentDescription = "Загрузить заказы",
                primary = true,
                onClick = onRefresh,
                modifier = Modifier.alpha(if (isLoading) 0.55f else 1f)
            )
        }
    }
}

@Composable
private fun PreAssemblyLoadingCard() {
    ModernCard(Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            CircularProgressIndicator(color = AccentColor, strokeWidth = 3.dp, modifier = Modifier.size(30.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Загружаю заказы Ozon", color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text("После загрузки позиции объединятся по артикулу", color = MutedTextColor, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun PreAssemblyEmptyCard(onLoad: () -> Unit) {
    ModernCard(Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppIconBubble(Icons.Outlined.Inventory2, tint = AccentColor, background = SoftBlueColor, modifier = Modifier.size(58.dp))
            Text("Заказы ещё не загружены", color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 20.sp, textAlign = TextAlign.Center)
            Text(
                "Нажмите кнопку ниже, чтобы получить список заказов Ozon для ручной проверки остатков.",
                color = MutedTextColor,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            AppPrimaryButton("Загрузить заказы Ozon", icon = Icons.Outlined.FileDownload, onClick = onLoad, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun PreAssemblyErrorCard(message: String, onRetry: () -> Unit) {
    ModernCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFE8E8)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Close, contentDescription = null, tint = DangerColor)
                }
                Column(Modifier.weight(1f)) {
                    Text("Не удалось загрузить данные", color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text(message, color = MutedTextColor, fontSize = 13.sp)
                }
            }
            AppPrimaryButton("Повторить загрузку", icon = Icons.Outlined.FileDownload, onClick = onRetry, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun PreAssemblySummaryPanel(
    total: Int,
    checked: Int,
    available: Int,
    toTransfer: Int,
    notChecked: Int,
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit
) {
    val progress = if (total == 0) 0f else checked.toFloat() / total.toFloat()
    val progressPercent = (progress * 100).toInt()
    val progressColor = if (notChecked == 0 && total > 0) SuccessColor else AccentColor
    val arrowRotation by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = spring(dampingRatio = 0.72f, stiffness = 520f),
        label = "pre_assembly_summary_arrow"
    )
    val cardScale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0.985f,
        animationSpec = spring(dampingRatio = 0.78f, stiffness = 420f),
        label = "pre_assembly_summary_scale"
    )

    ModernCard(
        Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .clickable(onClick = onToggleExpanded)
                    .padding(2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AppIconBubble(Icons.Outlined.CheckCircle, tint = SuccessColor, background = Color(0xFFE9F8EF), modifier = Modifier.size(42.dp))
                Column(Modifier.weight(1f)) {
                    Text("Сводка проверки", fontWeight = FontWeight.Bold, color = MainTextColor, fontSize = 17.sp)
                    Text("Проверено: $checked из $total позиций — $progressPercent%", color = MutedTextColor, fontSize = 13.sp)
                }
                StatusBadge("$progressPercent%", tone = if (notChecked == 0 && total > 0) BadgeTone.Green else BadgeTone.Blue)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, CardBorderColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.ExpandMore,
                        contentDescription = if (isExpanded) "Свернуть сводку" else "Развернуть сводку",
                        tint = MainTextColor,
                        modifier = Modifier
                            .size(22.dp)
                            .rotate(arrowRotation)
                    )
                }
            }
            PreAssemblyProgressBar(progress = progress, color = progressColor)
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(
                    animationSpec = spring(dampingRatio = 0.78f, stiffness = 390f),
                    expandFrom = Alignment.Top
                ) + fadeIn(animationSpec = tween(180)) + scaleIn(
                    initialScale = 0.96f,
                    animationSpec = spring(dampingRatio = 0.82f, stiffness = 420f)
                ),
                exit = shrinkVertically(
                    animationSpec = tween(190),
                    shrinkTowards = Alignment.Top
                ) + fadeOut(animationSpec = tween(120)) + scaleOut(
                    targetScale = 0.96f,
                    animationSpec = tween(160)
                )
            ) {
                BoxWithConstraints(Modifier.fillMaxWidth()) {
                    if (maxWidth < 420.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            PreAssemblyMetricRow("Есть на остатке", available.toString(), Color(0xFFE9F8EF), SuccessColor)
                            PreAssemblyMetricRow("К перемещению", toTransfer.toString(), Color(0xFFFFE8E8), DangerColor)
                            PreAssemblyMetricRow("Не проверено", notChecked.toString(), Color(0xFFF2F4F7), MutedTextColor)
                        }
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            PreAssemblyMetricRow("Есть", available.toString(), Color(0xFFE9F8EF), SuccessColor, Modifier.weight(1f))
                            PreAssemblyMetricRow("Переместить", toTransfer.toString(), Color(0xFFFFE8E8), DangerColor, Modifier.weight(1f))
                            PreAssemblyMetricRow("Не проверено", notChecked.toString(), Color(0xFFF2F4F7), MutedTextColor, Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PreAssemblyProgressBar(progress: Float, color: Color) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(9.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(Color(0xFFE8EDF5))
    ) {
        Box(
            Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(9.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(color)
        )
    }
}

@Composable
private fun PreAssemblyMetricRow(title: String, value: String, background: Color, valueColor: Color, modifier: Modifier = Modifier) {
    Row(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(background)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = MutedTextColor, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
        Text(value, color = valueColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}

@Composable
private fun PreAssemblyControlPanel(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    statusFilter: String,
    onStatusFilterChange: (String) -> Unit,
    sortOrder: PreAssemblySortOrder,
    onSortOrderChange: (PreAssemblySortOrder) -> Unit,
    onReload: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        FloatingSearchInput(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = "Поиск: артикул, SKU, заказ, название",
            modifier = Modifier.fillMaxWidth()
        )
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            if (maxWidth < 430.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PreAssemblyFilterDropdown(statusFilter, onStatusFilterChange, Modifier.fillMaxWidth())
                    PreAssemblySortDropdown(sortOrder, onSortOrderChange, Modifier.fillMaxWidth())
                    AppSecondaryButton("Обновить заказы", icon = Icons.Outlined.FileDownload, enabled = !isLoading, onClick = onReload, modifier = Modifier.fillMaxWidth())
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        PreAssemblyFilterDropdown(statusFilter, onStatusFilterChange, Modifier.weight(1f))
                        PreAssemblySortDropdown(sortOrder, onSortOrderChange, Modifier.weight(1f))
                    }
                    AppSecondaryButton("Обновить", icon = Icons.Outlined.FileDownload, enabled = !isLoading, onClick = onReload, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
private fun PreAssemblyFilterDropdown(selectedKey: String, onSelected: (String) -> Unit, modifier: Modifier = Modifier) {
    val options = listOf("ALL" to "Все позиции") + PreAssemblyStatus.values().map { it.name to it.title }
    val selectedTitle = options.firstOrNull { it.first == selectedKey }?.second ?: "Все позиции"
    var expanded by remember { mutableStateOf(false) }
    Box(modifier) {
        Row(
            Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(InputContainerColor)
                .border(1.dp, CardBorderColor, RoundedCornerShape(15.dp))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Outlined.MoreVert, contentDescription = null, tint = AccentColor, modifier = Modifier.size(18.dp))
            Column(Modifier.weight(1f)) {
                Text("Фильтр статуса", color = MutedTextColor, fontSize = 11.sp, maxLines = 1)
                Text(selectedTitle, color = MainTextColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.Outlined.ExpandMore, contentDescription = null, tint = MutedTextColor, modifier = Modifier.rotate(if (expanded) 180f else 0f))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color.White)) {
            options.forEach { (key, title) ->
                DropdownMenuItem(
                    text = { Text(title, color = MainTextColor, fontWeight = if (key == selectedKey) FontWeight.Bold else FontWeight.Normal) },
                    onClick = {
                        onSelected(key)
                        expanded = false
                    },
                    leadingIcon = {
                        if (key == "ALL") {
                            Icon(Icons.Outlined.Inventory2, contentDescription = null, tint = AccentColor)
                        } else {
                            val status = PreAssemblyStatus.valueOf(key)
                            PreAssemblyStatusDot(status)
                        }
                    }
                )
            }
        }
    }
}


@Composable
private fun PreAssemblySortDropdown(selected: PreAssemblySortOrder, onSelected: (PreAssemblySortOrder) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier) {
        Row(
            Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(InputContainerColor)
                .border(1.dp, CardBorderColor, RoundedCornerShape(15.dp))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Outlined.MoreVert, contentDescription = null, tint = AccentColor, modifier = Modifier.size(18.dp))
            Column(Modifier.weight(1f)) {
                Text("Сортировка", color = MutedTextColor, fontSize = 11.sp, maxLines = 1)
                Text(selected.title, color = MainTextColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.Outlined.ExpandMore, contentDescription = null, tint = MutedTextColor, modifier = Modifier.rotate(if (expanded) 180f else 0f))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color.White)) {
            PreAssemblySortOrder.values().forEach { order ->
                DropdownMenuItem(
                    text = { Text(order.title, color = MainTextColor, fontWeight = if (order == selected) FontWeight.Bold else FontWeight.Normal) },
                    onClick = {
                        onSelected(order)
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = if (order == selected) AccentColor else MutedTextColor)
                    }
                )
            }
        }
    }
}

@Composable
private fun PreAssemblyItemCard(item: PreAssemblyItem, vm: PreAssemblyViewModel, readOnly: Boolean = false) {
    val hasComment = item.comment.isNotBlank()
    val showTransferInput = item.status == PreAssemblyStatus.NOT_AVAILABLE || item.status == PreAssemblyStatus.NEED_TRANSFER
    var showEditor by rememberSaveable(item.id) { mutableStateOf(false) }
    var showCommentEditor by rememberSaveable(item.id) { mutableStateOf(false) }

    ModernCard(
        Modifier
            .fillMaxWidth()
            .border(1.dp, PreAssemblyStatusColor(item.status).copy(alpha = 0.22f), RoundedCornerShape(24.dp))
    ) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .heightIn(min = 72.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(PreAssemblyStatusColor(item.status).copy(alpha = 0.82f))
                )
                PreAssemblyProductPhoto(
                    imageUrl = item.imageUrl,
                    status = item.status,
                    modifier = Modifier.clickable(enabled = !readOnly) { showEditor = true }
                )
                Column(
                    Modifier
                        .weight(1f)
                        .clickable(enabled = !readOnly) { showEditor = true }
                        .padding(start = 2.dp, top = 1.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        item.name,
                        color = MainTextColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        lineHeight = 16.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PreAssemblyMetaChip("Арт. ${item.offerId}", modifier = Modifier.widthIn(max = 132.dp))
                        PreAssemblyQuantityChip(item.requiredQuantity)
                        if (!item.sku.isNullOrBlank()) {
                            PreAssemblyMetaChip("SKU", background = Color(0xFFF4F6FB), contentColor = MutedTextColor)
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = showTransferInput || hasComment,
                enter = fadeIn(animationSpec = tween(180)) + expandVertically(animationSpec = tween(210)),
                exit = fadeOut(animationSpec = tween(130)) + shrinkVertically(animationSpec = tween(170))
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF8FAFF))
                        .border(1.dp, CardBorderColor.copy(alpha = 0.65f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalArrangement = Arrangement.spacedBy(if (showTransferInput && hasComment) 6.dp else 0.dp)
                ) {
                    if (showTransferInput) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "К перемещению",
                                modifier = Modifier.weight(1f),
                                color = MutedTextColor,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            PreAssemblyCompactQuantityField(
                                value = item.transferQuantity,
                                onValueChange = { vm.updateTransferQuantity(item.id, it) },
                                enabled = !readOnly,
                                modifier = Modifier.widthIn(min = 118.dp, max = 136.dp)
                            )
                        }
                    }
                    if (hasComment) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Outlined.Description, contentDescription = null, tint = AccentColor, modifier = Modifier.size(16.dp))
                            Text(
                                item.comment,
                                color = MutedTextColor,
                                fontSize = 12.sp,
                                lineHeight = 15.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PreAssemblyQuickStatusButton(
                    icon = Icons.Outlined.CheckCircle,
                    contentDescription = "Есть",
                    color = SuccessColor,
                    selected = item.status == PreAssemblyStatus.AVAILABLE,
                    enabled = !readOnly,
                    onClick = { vm.updateStatus(item.id, PreAssemblyStatus.AVAILABLE) }
                )
                PreAssemblyQuickStatusButton(
                    icon = Icons.Outlined.Close,
                    contentDescription = "Нет",
                    color = DangerColor,
                    selected = item.status == PreAssemblyStatus.NOT_AVAILABLE,
                    enabled = !readOnly,
                    onClick = { vm.updateStatus(item.id, PreAssemblyStatus.NOT_AVAILABLE) }
                )
                PreAssemblyQuickStatusButton(
                    icon = Icons.Outlined.Archive,
                    contentDescription = "Нужно переместить",
                    color = WarningColor,
                    selected = item.status == PreAssemblyStatus.NEED_TRANSFER,
                    enabled = !readOnly,
                    onClick = { vm.updateStatus(item.id, PreAssemblyStatus.NEED_TRANSFER) }
                )
                PreAssemblyQuickCommentButton(
                    hasComment = hasComment,
                    enabled = !readOnly,
                    onClick = { showCommentEditor = true }
                )
                Spacer(Modifier.weight(1f))
                PreAssemblyQuickCardButton(enabled = !readOnly, onClick = { showEditor = true })
            }
        }
    }

    if (showCommentEditor) {
        PreAssemblyCommentDialog(
            item = item,
            vm = vm,
            onDismiss = { showCommentEditor = false }
        )
    }

    if (showEditor) {
        PreAssemblyItemEditorDialog(
            item = item,
            vm = vm,
            onDismiss = { showEditor = false }
        )
    }
}

private val preAssemblyProductImageCache = java.util.concurrent.ConcurrentHashMap<String, ImageBitmap>()

@Composable
private fun PreAssemblyProductPhoto(
    imageUrl: String?,
    status: PreAssemblyStatus,
    modifier: Modifier = Modifier
) {
    val imageBitmap by produceState<ImageBitmap?>(initialValue = null, imageUrl) {
        value = imageUrl?.let { url ->
            preAssemblyProductImageCache[url] ?: loadPreAssemblyProductImage(url)?.also { bitmap ->
                preAssemblyProductImageCache[url] = bitmap
            }
        }
    }
    val shape = RoundedCornerShape(15.dp)

    Box(
        modifier
            .size(width = 72.dp, height = 96.dp)
            .clip(shape)
            .background(PreAssemblyStatusBackground(status))
            .border(1.dp, PreAssemblyStatusColor(status).copy(alpha = 0.22f), shape),
        contentAlignment = Alignment.Center
    ) {
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                Icons.Outlined.Inventory2,
                contentDescription = null,
                tint = PreAssemblyStatusColor(status),
                modifier = Modifier.size(23.dp)
            )
        }
    }
}

private suspend fun loadPreAssemblyProductImage(url: String): ImageBitmap? = withContext(Dispatchers.IO) {
    runCatching {
        val imageUrl = url.trim().let { if (it.startsWith("//")) "https:$it" else it }
        val connection = URL(imageUrl).openConnection().apply {
            connectTimeout = 5_000
            readTimeout = 8_000
            setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 Chrome/124 Mobile Safari/537.36")
            setRequestProperty("Accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8")
        }
        val bytes = connection.getInputStream().use { it.readBytes() }
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = preAssemblyImageSampleSize(bounds, 300, 400)
        }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, decodeOptions)?.asImageBitmap()
    }.getOrNull()
}

private fun preAssemblyImageSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    var sampleSize = 1
    val height = options.outHeight
    val width = options.outWidth
    if (height > reqHeight || width > reqWidth) {
        var halfHeight = height / 2
        var halfWidth = width / 2
        while (halfHeight / sampleSize >= reqHeight && halfWidth / sampleSize >= reqWidth) {
            sampleSize *= 2
        }
    }
    return sampleSize
}

@Composable
private fun PreAssemblyQuickStatusButton(
    icon: ImageVector,
    contentDescription: String,
    color: Color,
    selected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .clip(CircleShape)
            .background(if (selected) color else color.copy(alpha = 0.10f))
            .border(1.dp, color.copy(alpha = if (selected) 0.95f else 0.28f), CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = if (selected) Color.White else color,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun PreAssemblyQuickCommentButton(hasComment: Boolean, enabled: Boolean = true, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .clip(CircleShape)
            .background(if (hasComment) AccentColor.copy(alpha = 0.12f) else Color.White)
            .border(1.dp, if (hasComment) AccentColor.copy(alpha = 0.38f) else CardBorderColor, CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Outlined.Description,
            contentDescription = "Комментарий",
            tint = if (hasComment) AccentColor else MutedTextColor,
            modifier = Modifier.size(19.dp)
        )
        if (hasComment) {
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(SuccessColor)
                    .border(1.dp, Color.White, CircleShape)
            )
        }
    }
}

@Composable
private fun PreAssemblyQuickCardButton(enabled: Boolean = true, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .clip(CircleShape)
            .background(Color.White)
            .border(1.dp, CardBorderColor, CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Outlined.Inventory2,
            contentDescription = "Открыть полную карточку",
            tint = MainTextColor,
            modifier = Modifier.size(19.dp)
        )
    }
}

@Composable
private fun PreAssemblyCommentDialog(
    item: PreAssemblyItem,
    vm: PreAssemblyViewModel,
    onDismiss: () -> Unit
) {
    var draft by rememberSaveable(item.id, item.comment) { mutableStateOf(item.comment) }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        ModernCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppIconBubble(Icons.Outlined.Description, tint = AccentColor, background = SoftBlueColor, modifier = Modifier.size(42.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Комментарий", color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(item.name, color = MutedTextColor, fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                    AppIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
                }

                ModernTextField(
                    value = draft,
                    onValueChange = { draft = it },
                    label = "Примечание к товару",
                    placeholder = "Например: нет на полке / коробка повреждена",
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppSecondaryButton(
                        "Очистить",
                        icon = Icons.Outlined.Delete,
                        danger = true,
                        onClick = {
                            draft = ""
                            vm.updateComment(item.id, "")
                        },
                        modifier = Modifier.weight(1f)
                    )
                    AppPrimaryButton(
                        "Сохранить",
                        icon = Icons.Outlined.CheckCircle,
                        onClick = {
                            vm.updateComment(item.id, draft.trim())
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun PreAssemblyStatusDropdown(selected: PreAssemblyStatus, onSelected: (PreAssemblyStatus) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(1.dp, PreAssemblyStatusColor(selected).copy(alpha = 0.45f), RoundedCornerShape(16.dp))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PreAssemblyStatusDot(selected)
            Column(Modifier.weight(1f)) {
                Text("Статус проверки", color = MutedTextColor, fontSize = 11.sp, maxLines = 1)
                Text(selected.title, color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.Outlined.ExpandMore, contentDescription = null, tint = PreAssemblyStatusColor(selected), modifier = Modifier.rotate(if (expanded) 180f else 0f))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color.White)) {
            PreAssemblyStatus.values().forEach { status ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(status.title, color = MainTextColor, fontWeight = if (status == selected) FontWeight.Bold else FontWeight.SemiBold)
                            Text(PreAssemblyStatusHint(status), color = MutedTextColor, fontSize = 12.sp)
                        }
                    },
                    leadingIcon = { PreAssemblyStatusDot(status) },
                    onClick = {
                        onSelected(status)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun PreAssemblyStatusPill(status: PreAssemblyStatus) {
    Row(
        Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(PreAssemblyStatusBackground(status))
            .padding(horizontal = 9.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        PreAssemblyStatusDot(status, size = 7.dp)
        Text(status.title, color = PreAssemblyStatusColor(status), fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
    }
}

@Composable
private fun PreAssemblyStatusDot(status: PreAssemblyStatus, size: androidx.compose.ui.unit.Dp = 10.dp) {
    Box(
        Modifier
            .size(size)
            .clip(CircleShape)
            .background(PreAssemblyStatusColor(status))
    )
}

@Composable
private fun PreAssemblyInfoLine(title: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
        Text(title, color = MutedTextColor, fontSize = 13.sp, modifier = Modifier.weight(0.8f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(value, color = MainTextColor, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, textAlign = TextAlign.End, modifier = Modifier.weight(1.2f), maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun PreAssemblyNoResultsCard(onReset: () -> Unit) {
    ModernCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            AppIconBubble(Icons.Outlined.Search, tint = MutedTextColor, background = Color(0xFFF2F4F7))
            Text("Ничего не найдено", color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Очистите поиск или смените фильтр статуса.", color = MutedTextColor, textAlign = TextAlign.Center)
            AppSecondaryButton("Сбросить фильтры", icon = Icons.Outlined.Close, onClick = onReset, modifier = Modifier.fillMaxWidth())
        }
    }
}



@Composable
private fun PreAssemblyVisibleInfoRow(visibleCount: Int, totalCount: Int, sortTitle: String, isCompleted: Boolean) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isCompleted) Color(0xFFE9F8EF) else Color(0xFFF7F9FF))
            .border(1.dp, CardBorderColor.copy(alpha = 0.72f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(Icons.Outlined.Inventory2, contentDescription = null, tint = if (isCompleted) SuccessColor else AccentColor, modifier = Modifier.size(18.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text("Видимых позиций: $visibleCount из $totalCount", color = MainTextColor, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text("Сортировка: $sortTitle", color = MutedTextColor, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        if (isCompleted) {
            StatusBadge("Завершена", tone = BadgeTone.Green)
        }
    }
}

@Composable
private fun PreAssemblyCompletedBanner(completedAt: String?, hasProblems: Boolean, hasUnchecked: Boolean, onReturnToWork: () -> Unit) {
    val title = when {
        hasUnchecked -> "Завершена с непроверенными позициями"
        hasProblems -> "Завершена с проблемами"
        else -> "Завершена без проблем"
    }
    ModernCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AppIconBubble(Icons.Outlined.CheckCircle, tint = SuccessColor, background = Color(0xFFE9F8EF), modifier = Modifier.size(42.dp))
                Column(Modifier.weight(1f)) {
                    Text(title, color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Дата завершения: ${completedAt ?: "—"}", color = MutedTextColor, fontSize = 13.sp)
                }
            }
            Text("Позиции заблокированы от случайных изменений. Для исправлений верните сборку в работу.", color = MutedTextColor, fontSize = 13.sp, lineHeight = 17.sp)
            AppSecondaryButton("Вернуть в работу", icon = Icons.Outlined.Archive, onClick = onReturnToWork, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun PreAssemblyArchiveDialog(
    archive: List<PreAssemblyArchiveEntry>,
    onOpenEntry: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        ModernCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppIconBubble(Icons.Outlined.Archive, tint = AccentColor, background = SoftBlueColor, modifier = Modifier.size(42.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Архив сборок", color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                        Text("Завершённые предварительные сборки сохраняются здесь автоматически.", color = MutedTextColor, fontSize = 13.sp)
                    }
                    AppIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
                }
                if (archive.isEmpty()) {
                    PreAssemblyMessageCard("Архив пуст. Завершите предварительную сборку, чтобы она появилась здесь.")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 520.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(archive.sortedByDescending { it.completedAt }, key = { it.id }) { entry ->
                            PreAssemblyArchiveRow(entry = entry, onOpen = { onOpenEntry(entry.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PreAssemblyArchiveRow(entry: PreAssemblyArchiveEntry, onOpen: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, CardBorderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onOpen)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(entry.title, color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(entry.resultTitle, color = MutedTextColor, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            StatusBadge("${entry.total} поз.", tone = if (entry.toTransfer == 0 && entry.notChecked == 0) BadgeTone.Green else BadgeTone.Blue)
        }
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            if (maxWidth < 300.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        PreAssemblyArchiveStat("Пров.", entry.checked.toString(), AccentColor, Modifier.weight(1f))
                        PreAssemblyArchiveStat("Есть", entry.available.toString(), SuccessColor, Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        PreAssemblyArchiveStat("Перем.", entry.toTransfer.toString(), DangerColor, Modifier.weight(1f))
                        PreAssemblyArchiveStat("Не пров.", entry.notChecked.toString(), MutedTextColor, Modifier.weight(1f))
                    }
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    PreAssemblyArchiveStat("Пров.", entry.checked.toString(), AccentColor, Modifier.weight(1f))
                    PreAssemblyArchiveStat("Есть", entry.available.toString(), SuccessColor, Modifier.weight(1f))
                    PreAssemblyArchiveStat("Перем.", entry.toTransfer.toString(), DangerColor, Modifier.weight(1f))
                    PreAssemblyArchiveStat("Не пров.", entry.notChecked.toString(), MutedTextColor, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun PreAssemblyArchiveStat(title: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Column(
        modifier
            .height(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF7F9FF))
            .padding(horizontal = 6.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, color = valueColor, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, lineHeight = 15.sp, maxLines = 1)
        Text(title, color = MutedTextColor, fontSize = 9.sp, lineHeight = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun PreAssemblyArchiveDetailsDialog(
    entry: PreAssemblyArchiveEntry,
    onSave: (PreAssemblyArchiveEntry) -> Unit,
    onShare: (PreAssemblyArchiveEntry) -> Unit,
    onDismiss: () -> Unit
) {
    var isEditing by rememberSaveable(entry.id) { mutableStateOf(false) }
    var draftItems by remember(entry.id) { mutableStateOf(entry.items) }
    val displayEntry = PreAssemblyArchiveEntryWithItems(entry, draftItems)

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        ModernCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppIconBubble(Icons.Outlined.Description, tint = AccentColor, background = SoftBlueColor, modifier = Modifier.size(42.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(displayEntry.title, color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 17.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(displayEntry.completedAtText, color = MutedTextColor, fontSize = 13.sp)
                    }
                    AppIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
                }

                PreAssemblyArchiveSummaryCard(displayEntry)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 410.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    items(displayEntry.items, key = { "${it.offerId}_${it.orderId}" }) { item ->
                        PreAssemblyArchiveItemRow(
                            item = item,
                            isEditing = isEditing,
                            onItemChange = { updated ->
                                draftItems = draftItems.map { old ->
                                    if (old.offerId == updated.offerId && old.orderId == updated.orderId) updated else old
                                }
                            }
                        )
                    }
                }

                PreAssemblyArchiveDetailsActions(
                    isEditing = isEditing,
                    onEdit = { isEditing = true },
                    onCancel = {
                        draftItems = entry.items
                        isEditing = false
                    },
                    onSave = {
                        onSave(displayEntry)
                        isEditing = false
                    },
                    onShare = { onShare(displayEntry) }
                )
            }
        }
    }
}

@Composable
private fun PreAssemblyArchiveSummaryCard(entry: PreAssemblyArchiveEntry) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFF7F9FF))
            .border(1.dp, CardBorderColor.copy(alpha = 0.72f), RoundedCornerShape(18.dp))
            .padding(horizontal = 11.dp, vertical = 9.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        PreAssemblyArchiveSummaryLine("Статус", entry.resultTitle, emphasized = true)
        PreAssemblyArchiveSummaryLine("Всего позиций", entry.total.toString())
        PreAssemblyArchiveSummaryLine("Проверено", entry.checked.toString())
        PreAssemblyArchiveSummaryLine("Есть", entry.available.toString())
        PreAssemblyArchiveSummaryLine("Нет / переместить", entry.toTransfer.toString())
        PreAssemblyArchiveSummaryLine("Комментариев", entry.comments.toString())
    }
}

@Composable
private fun PreAssemblyArchiveSummaryLine(title: String, value: String, emphasized: Boolean = false) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
        Text(title, color = MutedTextColor, fontSize = 12.sp, modifier = Modifier.weight(0.85f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(
            value,
            color = MainTextColor,
            fontWeight = if (emphasized) FontWeight.Bold else FontWeight.SemiBold,
            fontSize = if (emphasized) 12.sp else 13.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1.15f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PreAssemblyArchiveItemRow(
    item: PreAssemblyItem,
    isEditing: Boolean,
    onItemChange: (PreAssemblyItem) -> Unit
) {
    val needsTransfer = item.status == PreAssemblyStatus.NOT_AVAILABLE || item.status == PreAssemblyStatus.NEED_TRANSFER
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.dp, PreAssemblyStatusColor(item.status).copy(alpha = 0.35f), RoundedCornerShape(18.dp))
            .padding(9.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(item.name, color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text("Арт. ${item.offerId} · ${item.requiredQuantity} шт.", color = MutedTextColor, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            PreAssemblyArchiveStatusBadge(item.status)
        }

        if (isEditing) {
            PreAssemblyArchiveStatusSelector(
                selected = item.status,
                onSelected = { status -> onItemChange(PreAssemblyArchiveItemWithStatus(item, status)) }
            )
            AnimatedVisibility(
                visible = needsTransfer,
                enter = fadeIn(animationSpec = tween(180)) + expandVertically(animationSpec = tween(210)),
                exit = fadeOut(animationSpec = tween(130)) + shrinkVertically(animationSpec = tween(170))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("К перемещению", color = MutedTextColor, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    PreAssemblyArchiveQuantityField(
                        value = item.transferQuantity,
                        onValueChange = { onItemChange(item.copy(transferQuantity = it)) },
                        modifier = Modifier.width(86.dp)
                    )
                }
            }
            PreAssemblyArchiveCommentField(
                value = item.comment,
                onValueChange = { onItemChange(item.copy(comment = it)) },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            if (needsTransfer) {
                PreAssemblyInfoLine("К перемещению", "${item.transferQuantity.ifBlank { "0" }} шт.")
            }
            if (item.comment.isNotBlank()) {
                Text(item.comment, color = MutedTextColor, fontSize = 12.sp, lineHeight = 16.sp)
            }
        }
    }
}

@Composable
private fun PreAssemblyArchiveStatusSelector(selected: PreAssemblyStatus, onSelected: (PreAssemblyStatus) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(42.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(Color(0xFFFAFBFF))
                .border(1.dp, PreAssemblyStatusColor(selected).copy(alpha = 0.5f), RoundedCornerShape(13.dp))
                .clickable { expanded = true }
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PreAssemblyStatusDot(selected, size = 8.dp)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(0.dp)) {
                Text("Статус", color = MutedTextColor, fontSize = 10.sp, lineHeight = 11.sp, maxLines = 1)
                Text(selected.title, color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 13.sp, lineHeight = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.Outlined.ExpandMore, contentDescription = null, tint = PreAssemblyStatusColor(selected), modifier = Modifier.size(18.dp).rotate(if (expanded) 180f else 0f))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color.White)) {
            PreAssemblyStatus.values().forEach { status ->
                DropdownMenuItem(
                    text = {
                        Text(status.title, color = MainTextColor, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    },
                    leadingIcon = { PreAssemblyStatusDot(status, size = 8.dp) },
                    onClick = {
                        onSelected(status)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun PreAssemblyArchiveQuantityField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = { raw -> onValueChange(raw.filter { it.isDigit() }.take(5)) },
        modifier = modifier
            .height(34.dp)
            .clip(RoundedCornerShape(11.dp))
            .background(Color.White)
            .border(1.2.dp, AccentColor, RoundedCornerShape(11.dp))
            .padding(horizontal = 8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = androidx.compose.ui.text.TextStyle(
            color = MainTextColor,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 13.sp,
            textAlign = TextAlign.End
        ),
        decorationBox = { innerTextField ->
            Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                    if (value.isBlank()) {
                        Text("0", color = SoftTextColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    innerTextField()
                }
                Spacer(Modifier.width(4.dp))
                Text("шт.", color = MainTextColor, fontWeight = FontWeight.SemiBold, fontSize = 10.sp, maxLines = 1)
            }
        }
    )
}

@Composable
private fun PreAssemblyArchiveCommentField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .height(58.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(Color(0xFFF7F9FF))
            .border(1.dp, CardBorderColor, RoundedCornerShape(13.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        textStyle = androidx.compose.ui.text.TextStyle(
            color = MainTextColor,
            fontSize = 13.sp,
            lineHeight = 16.sp
        ),
        decorationBox = { innerTextField ->
            Box(Modifier.fillMaxSize()) {
                if (value.isBlank()) {
                    Text("Комментарий", color = MutedTextColor, fontSize = 12.sp, lineHeight = 15.sp)
                }
                innerTextField()
            }
        }
    )
}

@Composable
private fun PreAssemblyArchiveStatusBadge(status: PreAssemblyStatus) {
    val title = when (status) {
        PreAssemblyStatus.NOT_CHECKED -> "Не проверено"
        PreAssemblyStatus.AVAILABLE -> "Есть"
        PreAssemblyStatus.NOT_AVAILABLE -> "Нет"
        PreAssemblyStatus.NEED_TRANSFER -> "Переместить"
    }
    Row(
        Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(PreAssemblyStatusBackground(status))
            .padding(horizontal = 9.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        PreAssemblyStatusDot(status, size = 7.dp)
        Text(title, color = PreAssemblyStatusColor(status), fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
    }
}

@Composable
private fun PreAssemblyArchiveDetailsActions(
    isEditing: Boolean,
    onEdit: () -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    onShare: () -> Unit
) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(7.dp)) {
        if (isEditing) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                PreAssemblyArchiveActionButton("Отмена", icon = Icons.Outlined.Close, primary = false, onClick = onCancel, modifier = Modifier.weight(1f))
                PreAssemblyArchiveActionButton("Сохранить", icon = Icons.Outlined.CheckCircle, primary = true, onClick = onSave, modifier = Modifier.weight(1f))
            }
            PreAssemblyArchiveActionButton(
                "Отправить текстом",
                icon = Icons.AutoMirrored.Outlined.Send,
                primary = true,
                onClick = onShare,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                PreAssemblyArchiveActionButton("Редактировать", icon = Icons.Outlined.Edit, primary = false, onClick = onEdit, modifier = Modifier.weight(1f))
                PreAssemblyArchiveActionButton("Отправить", icon = Icons.AutoMirrored.Outlined.Send, primary = true, onClick = onShare, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun PreAssemblyArchiveActionButton(
    text: String,
    icon: ImageVector,
    primary: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    if (primary) {
        Button(
            onClick = onClick,
            modifier = modifier.height(36.dp),
            shape = shape,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentColor, contentColor = Color.White)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(15.dp))
            Spacer(Modifier.width(4.dp))
            Text(text, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(36.dp),
            shape = shape,
            border = BorderStroke(1.dp, CardBorderColor),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = MainTextColor)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(15.dp))
            Spacer(Modifier.width(4.dp))
            Text(text, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

private fun PreAssemblyArchiveEntryWithItems(
    entry: PreAssemblyArchiveEntry,
    items: List<PreAssemblyItem>
): PreAssemblyArchiveEntry {
    val checked = items.count { it.status != PreAssemblyStatus.NOT_CHECKED }
    val available = items.count { it.status == PreAssemblyStatus.AVAILABLE }
    val toTransfer = items.count { it.status == PreAssemblyStatus.NOT_AVAILABLE || it.status == PreAssemblyStatus.NEED_TRANSFER }
    val notChecked = items.size - checked
    val comments = items.count { it.comment.isNotBlank() }
    val resultTitle = when {
        notChecked > 0 -> "Предварительная сборка завершена с непроверенными позициями"
        toTransfer > 0 -> "Предварительная сборка завершена с проблемами"
        else -> "Предварительная сборка завершена без проблем"
    }
    return entry.copy(
        resultTitle = resultTitle,
        total = items.size,
        checked = checked,
        available = available,
        toTransfer = toTransfer,
        notChecked = notChecked,
        comments = comments,
        items = items
    )
}

private fun PreAssemblyArchiveItemWithStatus(item: PreAssemblyItem, status: PreAssemblyStatus): PreAssemblyItem {
    val transferQuantity = when (status) {
        PreAssemblyStatus.NOT_CHECKED -> ""
        PreAssemblyStatus.AVAILABLE -> "0"
        PreAssemblyStatus.NOT_AVAILABLE -> item.requiredQuantity.toString()
        PreAssemblyStatus.NEED_TRANSFER -> item.transferQuantity.takeIf { it.isNotBlank() && it != "0" }.orEmpty()
    }
    return item.copy(status = status, transferQuantity = transferQuantity)
}

private fun PreAssemblyArchiveMessageText(entry: PreAssemblyArchiveEntry): String {
    val transferItems = entry.items.filter {
        it.status == PreAssemblyStatus.NOT_AVAILABLE || it.status == PreAssemblyStatus.NEED_TRANSFER
    }
    val summary = """
Итог проверки:
Всего позиций: ${entry.total}
Проверено: ${entry.checked}
Есть: ${entry.available}
Нет / переместить: ${entry.toTransfer}
Не проверено: ${entry.notChecked}
    """.trimIndent()

    if (transferItems.isEmpty()) {
        return """
Добрый день.

По предварительной сборке заказов Ozon от ${entry.completedAtText} перемещение на склад не требуется.

$summary
        """.trimIndent()
    }

    val rows = transferItems.mapIndexed { index, item ->
        val reason = when (item.status) {
            PreAssemblyStatus.NOT_AVAILABLE -> "Нет в наличии"
            PreAssemblyStatus.NEED_TRANSFER -> "Недостаточное количество"
            else -> ""
        }
        """${index + 1}. Артикул: ${item.offerId}
Товар: ${item.name}
Причина перемещения: $reason
Количество к перемещению: ${item.transferQuantity.ifBlank { "0" }} шт.${if (item.comment.isBlank()) "" else "\nКомментарий: ${item.comment}"}
""".trimIndent()
    }

    return """
Добрый день.

По предварительной сборке заказов Ozon от ${entry.completedAtText} нужно сделать перемещение на склад:

${rows.joinToString("\n\n")}

Итого позиций к перемещению: ${rows.size}.

$summary
    """.trimIndent()
}

@Composable
private fun PreAssemblyBulkActionsDialog(
    visibleCount: Int,
    totalCount: Int,
    isCompleted: Boolean,
    archive: List<PreAssemblyArchiveEntry>,
    printBridgeHost: String,
    onPrintBridgeHostChange: (String) -> Unit,
    printPrinterName: String,
    onPrintPrinterNameChange: (String) -> Unit,
    onOpenArchive: () -> Unit,
    onPrintTest: () -> Unit,
    onSharePrintLogs: () -> Unit,
    onAction: (PreAssemblyBulkAction) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        ModernCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Дополнительно", color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                        Text("Архив и действия с видимыми позициями.", color = MutedTextColor, fontSize = 13.sp)
                    }
                    AppIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
                }
                if (archive.isNotEmpty()) {
                    PreAssemblyArchiveActionRow(archive = archive, onClick = onOpenArchive)
                    HorizontalDivider(color = CardBorderColor.copy(alpha = 0.72f))
                }
                ModernTextField(
                    value = printBridgeHost,
                    onValueChange = onPrintBridgeHostChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = "IP моста печати",
                    placeholder = "192.168.10.104",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    leadingIcon = Icons.Outlined.Settings
                )
                ModernTextField(
                    value = printPrinterName,
                    onValueChange = onPrintPrinterNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = "Имя принтера Windows",
                    placeholder = "HP LaserJet MFP M129-M134",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    leadingIcon = Icons.Outlined.Print
                )
                PreAssemblyUtilityActionRow(
                    title = "Тестовая печать",
                    subtitle = "Проверить ${printBridgeHost}:8787 -> $printPrinterName",
                    icon = Icons.Outlined.Print,
                    onClick = onPrintTest
                )
                PreAssemblyUtilityActionRow(
                    title = "Выгрузить логи печати",
                    subtitle = "Файл со всеми проверками моста и ошибками принтера",
                    icon = Icons.Outlined.Description,
                    onClick = onSharePrintLogs
                )
                HorizontalDivider(color = CardBorderColor.copy(alpha = 0.72f))
                if (isCompleted) {
                    PreAssemblyMessageCard("Сборка завершена. Верните её в работу, чтобы менять позиции.")
                } else if (totalCount == 0) {
                    PreAssemblyMessageCard("Активной предварительной сборки сейчас нет.")
                }
                PreAssemblyBulkAction.values().forEach { action ->
                    PreAssemblyBulkActionRow(
                        action = action,
                        enabled = visibleCount > 0 && !isCompleted,
                        onClick = { onAction(action) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PreAssemblyUtilityActionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.48f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, CardBorderColor, RoundedCornerShape(16.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(icon, contentDescription = null, tint = AccentColor, modifier = Modifier.size(21.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, color = MainTextColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(subtitle, color = MutedTextColor, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Icon(Icons.Outlined.ExpandMore, contentDescription = null, tint = MutedTextColor, modifier = Modifier.rotate(-90f))
    }
}

@Composable
private fun PreAssemblyArchiveActionRow(archive: List<PreAssemblyArchiveEntry>, onClick: () -> Unit) {
    val latest = archive.maxByOrNull { it.completedAt }
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, CardBorderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(Icons.Outlined.Archive, contentDescription = null, tint = AccentColor, modifier = Modifier.size(21.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("Архив предварительных сборок", color = MainTextColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                latest?.let { "Последняя: ${it.completedAtText}, позиций: ${it.total}" } ?: "Открыть архив",
                color = MutedTextColor,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        StatusBadge(archive.size.toString(), tone = BadgeTone.Blue)
    }
}

@Composable
private fun PreAssemblyBulkActionRow(action: PreAssemblyBulkAction, enabled: Boolean, onClick: () -> Unit) {
    val icon = when (action) {
        PreAssemblyBulkAction.MARK_AVAILABLE -> Icons.Outlined.CheckCircle
        PreAssemblyBulkAction.RESET_CHECK -> Icons.Outlined.Archive
        PreAssemblyBulkAction.CLEAR_COMMENTS -> Icons.Outlined.Delete
    }
    Row(
        Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.48f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, CardBorderColor, RoundedCornerShape(16.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(icon, contentDescription = null, tint = AccentColor, modifier = Modifier.size(21.dp))
        Text(action.title, color = MainTextColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Icon(Icons.Outlined.ExpandMore, contentDescription = null, tint = MutedTextColor, modifier = Modifier.rotate(-90f))
    }
}

@Composable
private fun PreAssemblyBulkConfirmDialog(
    action: PreAssemblyBulkAction,
    visibleCount: Int,
    totalCount: Int,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        ModernCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(action.confirmTitle, color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                Text(
                    when (action) {
                        PreAssemblyBulkAction.MARK_AVAILABLE -> "Отметить $visibleCount позиций как “Есть”? Будут изменены только товары, которые сейчас отображаются после поиска и фильтра."
                        PreAssemblyBulkAction.RESET_CHECK -> "Сбросить проверку у $visibleCount позиций? Комментарии сохранятся, а статус станет “Не проверено”."
                        PreAssemblyBulkAction.CLEAR_COMMENTS -> "Очистить комментарии у $visibleCount позиций? Статусы и количество к перемещению не изменятся."
                    },
                    color = MutedTextColor,
                    fontSize = 14.sp,
                    lineHeight = 19.sp
                )
                Text("Видимых позиций: $visibleCount из $totalCount", color = AccentColor, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppSecondaryButton("Отмена", icon = Icons.Outlined.Close, onClick = onDismiss, modifier = Modifier.weight(1f))
                    AppPrimaryButton(action.successButton, icon = Icons.Outlined.CheckCircle, onClick = onConfirm, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun PreAssemblyFinishDialog(
    total: Int,
    checked: Int,
    available: Int,
    toTransfer: Int,
    notChecked: Int,
    comments: Int,
    isCompleted: Boolean,
    completedAt: String?,
    onDismiss: () -> Unit,
    onFinish: () -> Unit,
    onReturnToWork: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        ModernCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppIconBubble(Icons.Outlined.CheckCircle, tint = if (isCompleted) SuccessColor else AccentColor, background = if (isCompleted) Color(0xFFE9F8EF) else SoftBlueColor, modifier = Modifier.size(42.dp))
                    Column(Modifier.weight(1f)) {
                        Text(if (isCompleted) "Предварительная сборка завершена" else "Завершить предварительную сборку?", color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                        Text(if (isCompleted) "Завершено: ${completedAt ?: "—"}" else "Проверьте итог перед фиксацией результата.", color = MutedTextColor, fontSize = 13.sp)
                    }
                    AppIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFF7F9FF))
                        .border(1.dp, CardBorderColor.copy(alpha = 0.72f), RoundedCornerShape(18.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PreAssemblyInfoLine("Всего позиций", total.toString())
                    PreAssemblyInfoLine("Проверено", checked.toString())
                    PreAssemblyInfoLine("Не проверено", notChecked.toString())
                    PreAssemblyInfoLine("Есть", available.toString())
                    PreAssemblyInfoLine("Нет / переместить", toTransfer.toString())
                    PreAssemblyInfoLine("Комментариев", comments.toString())
                }

                if (notChecked > 0 && !isCompleted) {
                    PreAssemblyMessageCard("Осталось $notChecked непроверенных позиций. Можно вернуться к списку или завершить всё равно.")
                } else if (toTransfer > 0 && !isCompleted) {
                    PreAssemblyMessageCard("Есть $toTransfer проблемных позиций. После завершения сборка будет отмечена как завершённая с проблемами.")
                }

                if (isCompleted) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppSecondaryButton("Закрыть", icon = Icons.Outlined.Close, onClick = onDismiss, modifier = Modifier.weight(1f))
                        AppPrimaryButton("Вернуть в работу", icon = Icons.Outlined.Archive, onClick = onReturnToWork, modifier = Modifier.weight(1f))
                    }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppSecondaryButton("Вернуться", icon = Icons.Outlined.Close, onClick = onDismiss, modifier = Modifier.weight(1f))
                        AppPrimaryButton(if (notChecked > 0) "Завершить всё равно" else "Завершить", icon = Icons.Outlined.CheckCircle, onClick = onFinish, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun PreAssemblyControlsDialog(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    statusFilter: String,
    onStatusFilterChange: (String) -> Unit,
    sortOrder: PreAssemblySortOrder,
    onSortOrderChange: (PreAssemblySortOrder) -> Unit,
    onReload: () -> Unit,
    isLoading: Boolean,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        ModernCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Поиск, фильтр и сортировка", color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                        Text("Сортировка “Что проверять дальше” поднимает наверх непроверенные и проблемные товары.", color = MutedTextColor, fontSize = 13.sp)
                    }
                    AppIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
                }
                PreAssemblyControlPanel(
                    searchQuery = searchQuery,
                    onSearchQueryChange = onSearchQueryChange,
                    statusFilter = statusFilter,
                    onStatusFilterChange = onStatusFilterChange,
                    sortOrder = sortOrder,
                    onSortOrderChange = onSortOrderChange,
                    onReload = onReload,
                    isLoading = isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun PreAssemblyStatusPickerDialog(
    selected: PreAssemblyStatus,
    onSelected: (PreAssemblyStatus) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        ModernCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text("Статус позиции", color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Выберите результат проверки для товара.", color = MutedTextColor, fontSize = 13.sp)
                    }
                    AppIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
                }
                PreAssemblyStatus.values().forEach { status ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (status == selected) PreAssemblyStatusBackground(status) else Color.White)
                            .border(1.dp, if (status == selected) PreAssemblyStatusColor(status).copy(alpha = 0.42f) else CardBorderColor, RoundedCornerShape(16.dp))
                            .clickable {
                                onSelected(status)
                            }
                            .padding(horizontal = 12.dp, vertical = 11.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        PreAssemblyStatusDot(status)
                        Column(Modifier.weight(1f)) {
                            Text(status.title, color = MainTextColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text(PreAssemblyStatusHint(status), color = MutedTextColor, fontSize = 12.sp)
                        }
                        if (status == selected) {
                            Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = PreAssemblyStatusColor(status))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PreAssemblyItemEditorDialog(
    item: PreAssemblyItem,
    vm: PreAssemblyViewModel,
    onDismiss: () -> Unit
) {
    val needsTransfer = item.status == PreAssemblyStatus.NOT_AVAILABLE || item.status == PreAssemblyStatus.NEED_TRANSFER
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        ModernCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("Карточка заказа", color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                        Text(item.name, color = MainTextColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 18.sp, maxLines = 3, overflow = TextOverflow.Ellipsis)
                    }
                    AppIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    PreAssemblyMetaChip("Арт. ${item.offerId}")
                    PreAssemblyQuantityChip(item.requiredQuantity)
                    PreAssemblyStatusPill(item.status)
                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFF7F9FF))
                        .border(1.dp, CardBorderColor.copy(alpha = 0.72f), RoundedCornerShape(18.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PreAssemblyInfoLine("SKU Ozon", item.sku ?: "—")
                    PreAssemblyInfoLine("Заказы", item.orderId)
                    PreAssemblyInfoLine("Нужно по заказам", "${item.requiredQuantity} шт.")
                }

                PreAssemblyStatusDropdown(selected = item.status, onSelected = { vm.updateStatus(item.id, it) })

                AnimatedVisibility(
                    visible = needsTransfer,
                    enter = fadeIn(animationSpec = tween(180)) + expandVertically(animationSpec = tween(210)),
                    exit = fadeOut(animationSpec = tween(130)) + shrinkVertically(animationSpec = tween(170))
                ) {
                    ModernTextField(
                        item.transferQuantity,
                        { vm.updateTransferQuantity(item.id, it) },
                        label = "Сколько переместить",
                        placeholder = "0",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                ModernTextField(
                    item.comment,
                    { vm.updateComment(item.id, it) },
                    label = "Комментарий",
                    placeholder = "Например: нет на полке / нужно со склада",
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppSecondaryButton("Закрыть", icon = Icons.Outlined.Close, onClick = onDismiss, modifier = Modifier.weight(1f))
                    AppPrimaryButton("Готово", icon = Icons.Outlined.CheckCircle, onClick = onDismiss, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun PreAssemblyQuantityChip(quantity: Int, modifier: Modifier = Modifier) {
    Row(
        modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color(0xFFF4F6FB))
            .padding(horizontal = 9.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            quantity.toString(),
            color = MainTextColor,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 17.sp,
            lineHeight = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            "шт.",
            color = MainTextColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            lineHeight = 12.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun PreAssemblyMetaChip(
    text: String,
    background: Color = SoftBlueColor,
    contentColor: Color = AccentColor,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .clip(RoundedCornerShape(999.dp))
            .background(background)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text,
            color = contentColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PreAssemblyCompactQuantityField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    BasicTextField(
        value = value,
        onValueChange = { raw -> onValueChange(raw.filter { it.isDigit() }.take(5)) },
        enabled = enabled,
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.4.dp, AccentColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = androidx.compose.ui.text.TextStyle(
            color = MainTextColor,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp,
            textAlign = TextAlign.End
        ),
        decorationBox = { innerTextField ->
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    if (value.isBlank()) {
                        Text(
                            "0",
                            color = SoftTextColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            textAlign = TextAlign.End
                        )
                    }
                    innerTextField()
                }
                Spacer(Modifier.width(5.dp))
                Text(
                    "шт.",
                    color = MainTextColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }
        }
    )
}

@Composable
private fun PreAssemblyCompactInfoLine(title: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = MutedTextColor, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(value, color = MainTextColor, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, textAlign = TextAlign.End, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun PreAssemblyCompactCardButton(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    primary: Boolean = false,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)
    if (primary) {
        Button(
            onClick = onClick,
            modifier = modifier.height(40.dp),
            shape = shape,
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentColor, contentColor = Color.White)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(text, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, maxLines = 1)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(40.dp),
            shape = shape,
            border = BorderStroke(1.dp, CardBorderColor),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = MainTextColor)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(text, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, maxLines = 1)
        }
    }
}

@Composable
private fun PreAssemblyActionPanel(
    hasItems: Boolean,
    hasVisibleItems: Boolean,
    hasReport: Boolean,
    hasActiveFilters: Boolean,
    hasArchive: Boolean,
    isCompleted: Boolean,
    onOpenControls: () -> Unit,
    onOpenBulkActions: () -> Unit,
    onBuildReport: () -> Unit,
    onFinishAssembly: () -> Unit,
    onShareReport: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        PreAssemblyMinimalActionButton(
            icon = Icons.Outlined.Search,
            primary = hasActiveFilters,
            onClick = onOpenControls
        )
        PreAssemblyMinimalActionButton(
            icon = Icons.Outlined.MoreVert,
            enabled = hasArchive || (hasItems && hasVisibleItems && !isCompleted),
            onClick = onOpenBulkActions
        )
        PreAssemblyMinimalActionButton(
            icon = Icons.Outlined.Description,
            primary = true,
            enabled = hasItems,
            onClick = onBuildReport
        )
        PreAssemblyMinimalActionButton(
            icon = Icons.Outlined.CheckCircle,
            primary = isCompleted,
            enabled = hasItems || hasReport,
            onClick = onFinishAssembly
        )
    }
}

@Composable
private fun PreAssemblyMinimalActionButton(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    primary: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val shape = CircleShape
    val buttonModifier = modifier
        .size(56.dp)
        .shadow(10.dp, shape, clip = false)

    if (primary) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = buttonModifier,
            shape = shape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentColor,
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFE8ECF3),
                disabledContentColor = SoftTextColor
            )
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(22.dp))
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            modifier = buttonModifier,
            shape = shape,
            border = BorderStroke(1.dp, Color(0xFFE1E7F0)),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = MainTextColor,
                disabledContainerColor = Color(0xFFF5F7FB),
                disabledContentColor = SoftTextColor
            )
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(22.dp))
        }
    }
}

@Composable
private fun PreAssemblyMessageCard(message: String) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFFFF3E8))
            .border(1.dp, Color(0xFFFFD7B5), RoundedCornerShape(18.dp))
            .padding(horizontal = 14.dp, vertical = 11.dp)
    ) {
        Text(message, color = Color(0xFFB45309), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }
}

@Composable
private fun PreAssemblyReportDialog(
    reportText: String,
    onDismiss: () -> Unit,
    onCopy: () -> Unit,
    onPrint: () -> Unit,
    onShare: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        ModernCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppIconBubble(Icons.Outlined.Description, modifier = Modifier.size(42.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Список на перемещение", color = MainTextColor, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                        Text("Проверьте текст перед отправкой бухгалтеру", color = MutedTextColor, fontSize = 13.sp)
                    }
                    AppIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFF7F9FF))
                        .border(1.dp, CardBorderColor.copy(alpha = 0.75f), RoundedCornerShape(18.dp))
                        .padding(12.dp)
                ) {
                    item {
                        Text(reportText, color = MainTextColor, fontSize = 14.sp, lineHeight = 20.sp)
                    }
                }
                Text(
                    "Итого позиций: ${reportText.lines().count { it.trim().matches(Regex("\\d+\\..*")) }}",
                    color = MutedTextColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                BoxWithConstraints(Modifier.fillMaxWidth()) {
                    if (maxWidth < 430.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            AppSecondaryButton("Скопировать", icon = Icons.Outlined.Description, onClick = onCopy, modifier = Modifier.fillMaxWidth())
                            AppSecondaryButton("Напечатать", icon = Icons.Outlined.Print, onClick = onPrint, modifier = Modifier.fillMaxWidth())
                            AppPrimaryButton("Печать + отправка", icon = Icons.AutoMirrored.Outlined.Send, onClick = onShare, modifier = Modifier.fillMaxWidth())
                        }
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AppSecondaryButton("Скопировать", icon = Icons.Outlined.Description, onClick = onCopy, modifier = Modifier.weight(0.95f))
                            AppSecondaryButton("Напечатать", icon = Icons.Outlined.Print, onClick = onPrint, modifier = Modifier.weight(0.95f))
                            AppPrimaryButton("Печать + отправка", icon = Icons.AutoMirrored.Outlined.Send, onClick = onShare, modifier = Modifier.weight(1.2f))
                        }
                    }
                }
            }
        }
    }
}


private fun sortPreAssemblyItems(items: List<PreAssemblyItem>, sortOrder: PreAssemblySortOrder): List<PreAssemblyItem> {
    return when (sortOrder) {
        PreAssemblySortOrder.ATTENTION -> items.sortedWith(
            compareBy<PreAssemblyItem> { PreAssemblyAttentionPriority(it.status) }
                .thenBy { it.name.lowercase() }
                .thenBy { it.offerId.lowercase() }
        )
        PreAssemblySortOrder.NOT_CHECKED_FIRST -> items.sortedWith(
            compareBy<PreAssemblyItem> { if (it.status == PreAssemblyStatus.NOT_CHECKED) 0 else 1 }
                .thenBy { PreAssemblyAttentionPriority(it.status) }
                .thenBy { it.name.lowercase() }
        )
        PreAssemblySortOrder.NOT_AVAILABLE_FIRST -> items.sortedWith(
            compareBy<PreAssemblyItem> { if (it.status == PreAssemblyStatus.NOT_AVAILABLE) 0 else 1 }
                .thenBy { PreAssemblyAttentionPriority(it.status) }
                .thenBy { it.name.lowercase() }
        )
        PreAssemblySortOrder.NEED_TRANSFER_FIRST -> items.sortedWith(
            compareBy<PreAssemblyItem> { if (it.status == PreAssemblyStatus.NEED_TRANSFER) 0 else 1 }
                .thenBy { PreAssemblyAttentionPriority(it.status) }
                .thenBy { it.name.lowercase() }
        )
        PreAssemblySortOrder.ARTICLE_ASC -> items.sortedWith(
            compareBy<PreAssemblyItem> { it.offerId.lowercase() }
                .thenBy { it.name.lowercase() }
        )
        PreAssemblySortOrder.NAME_ASC -> items.sortedWith(
            compareBy<PreAssemblyItem> { it.name.lowercase() }
                .thenBy { it.offerId.lowercase() }
        )
        PreAssemblySortOrder.QUANTITY_DESC -> items.sortedWith(
            compareByDescending<PreAssemblyItem> { it.requiredQuantity }
                .thenBy { it.name.lowercase() }
        )
    }
}

private fun PreAssemblyAttentionPriority(status: PreAssemblyStatus): Int = when (status) {
    PreAssemblyStatus.NOT_CHECKED -> 0
    PreAssemblyStatus.NEED_TRANSFER -> 1
    PreAssemblyStatus.NOT_AVAILABLE -> 2
    PreAssemblyStatus.AVAILABLE -> 3
}

private fun PreAssemblyStatusColor(status: PreAssemblyStatus): Color = when (status) {
    PreAssemblyStatus.NOT_CHECKED -> MutedTextColor
    PreAssemblyStatus.AVAILABLE -> SuccessColor
    PreAssemblyStatus.NOT_AVAILABLE -> DangerColor
    PreAssemblyStatus.NEED_TRANSFER -> WarningColor
}

private fun PreAssemblyStatusBackground(status: PreAssemblyStatus): Color = when (status) {
    PreAssemblyStatus.NOT_CHECKED -> Color(0xFFF2F4F7)
    PreAssemblyStatus.AVAILABLE -> Color(0xFFE9F8EF)
    PreAssemblyStatus.NOT_AVAILABLE -> Color(0xFFFFE8E8)
    PreAssemblyStatus.NEED_TRANSFER -> Color(0xFFFFF4E5)
}

private fun PreAssemblyStatusHint(status: PreAssemblyStatus): String = when (status) {
    PreAssemblyStatus.NOT_CHECKED -> "позиция ещё не проверялась"
    PreAssemblyStatus.AVAILABLE -> "товар есть, перемещение не нужно"
    PreAssemblyStatus.NOT_AVAILABLE -> "товара нет, нужно указать количество"
    PreAssemblyStatus.NEED_TRANSFER -> "товар есть частично, нужно переместить"
}
