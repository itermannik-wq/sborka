package com.boldrex.postavki

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.BitmapFactory
import android.util.LruCache
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
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
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WarningAmber
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
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
private val FbsNeonGreen = Color(0xFF78F04B)
private val FbsDarkBackground = Color(0xFF050605)
private val FbsDarkPanel = Color(0xFF101110)
private val FbsDarkPanelAlt = Color(0xFF191A18)
private val FbsDarkBorder = Color(0xFF353735)
private val FbsDarkText = Color(0xFFF7F7F2)
private val FbsDarkMutedText = Color(0xFF9B9B9B)

private val CompactScreenBreakpoint = 380.dp
private val NarrowScreenBreakpoint = 340.dp

private enum class AppMode { MENU, SUPPLY, PRE_ASSEMBLY, FBS_ASSEMBLY }

private enum class FbsAssemblyPage { HOME, LIST, WORK, FINISH }
private enum class FbsAssemblyOverlay { ANALYTICS, HISTORY, SETTINGS }

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
private fun FbsAssemblyDarkCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.shadow(
            elevation = 18.dp,
            shape = shape,
            ambientColor = Color.Black.copy(alpha = 0.50f),
            spotColor = FbsNeonGreen.copy(alpha = 0.10f)
        ),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = FbsDarkPanel.copy(alpha = 0.98f)),
        border = BorderStroke(1.dp, FbsDarkBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        content = { content() }
    )
}

@Composable
private fun PreAssemblyDarkCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    content: @Composable () -> Unit
) {
    FbsAssemblyDarkCard(modifier = modifier, shape = shape, content = content)
}

@Composable
private fun PreAssemblyDarkPrimaryButton(
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
            .heightIn(min = 44.dp)
            .alpha(if (enabled) 1f else 0.48f),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = FbsNeonGreen,
            contentColor = Color.Black,
            disabledContainerColor = FbsNeonGreen.copy(alpha = 0.46f),
            disabledContentColor = Color.Black.copy(alpha = 0.68f)
        )
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
        }
        Text(
            text,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PreAssemblyDarkSecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    danger: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val contentColor = if (danger) DangerColor else FbsDarkText
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .defaultMinSize(minHeight = 44.dp)
            .heightIn(min = 44.dp)
            .alpha(if (enabled) 1f else 0.48f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, if (danger) DangerColor.copy(alpha = 0.62f) else FbsDarkBorder),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = FbsDarkPanelAlt,
            contentColor = contentColor,
            disabledContainerColor = FbsDarkPanelAlt,
            disabledContentColor = FbsDarkMutedText
        )
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
        }
        Text(
            text,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PreAssemblyDarkIconActionButton(
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    danger: Boolean = false,
    primary: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    val tint = when {
        danger -> DangerColor
        primary -> Color.Black
        else -> FbsNeonGreen
    }
    if (primary) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier
                .size(44.dp)
                .alpha(if (enabled) 1f else 0.48f),
            shape = shape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FbsNeonGreen, contentColor = Color.Black)
        ) {
            Icon(icon, contentDescription = contentDescription, modifier = Modifier.size(20.dp))
        }
    } else {
        OutlinedIconButton(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier
                .size(44.dp)
                .alpha(if (enabled) 1f else 0.48f),
            shape = shape,
            border = BorderStroke(1.dp, if (danger) DangerColor.copy(alpha = 0.62f) else FbsDarkBorder)
        ) {
            Icon(icon, contentDescription = contentDescription, tint = tint, modifier = Modifier.size(20.dp))
        }
    }
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
private fun FbsAssemblyTextField(
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
        placeholder = { Text(placeholder, color = FbsDarkMutedText.copy(alpha = 0.76f)) },
        leadingIcon = leadingIcon?.let { icon ->
            { Icon(icon, contentDescription = null, tint = FbsNeonGreen) }
        },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        modifier = modifier.heightIn(min = if (singleLine) 58.dp else 86.dp),
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = FbsDarkText,
            unfocusedTextColor = FbsDarkText,
            focusedLabelColor = FbsNeonGreen,
            unfocusedLabelColor = FbsDarkMutedText,
            cursorColor = FbsNeonGreen,
            focusedContainerColor = FbsDarkPanelAlt,
            unfocusedContainerColor = FbsDarkPanelAlt,
            disabledContainerColor = FbsDarkPanelAlt,
            focusedIndicatorColor = FbsNeonGreen,
            unfocusedIndicatorColor = FbsDarkBorder
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
        lazy(LazyThreadSafetyMode.NONE) {
            PreAssemblyViewModel(
                archiveRepository = PreAssemblyArchiveRepository(
                    LocalDatabase.get(context.applicationContext).dao()
                )
            )
        }
    }
    val fbsAssemblyVm = remember(context.applicationContext) {
        lazy(LazyThreadSafetyMode.NONE) {
            FbsAssemblyViewModel(
                historyRepository = FbsAssemblyHistoryRepository(
                    LocalDatabase.get(context.applicationContext).dao()
                ),
                labelPrinter = OzonLabelPrinter(context.applicationContext),
                notificationService = FbsOrderNotificationService(context.applicationContext)
            )
        }
    }
    val rootModifier = if (mode == AppMode.FBS_ASSEMBLY || mode == AppMode.PRE_ASSEMBLY) {
        Modifier.fillMaxSize().background(FbsDarkBackground)
    } else {
        Modifier.fillMaxSize().background(AppBackgroundGradient)
    }
    var printerMessage by remember { mutableStateOf<PrinterUiMessage?>(null) }
    LaunchedEffect(Unit) {
        PrinterUiNotifier.messages.collect { message ->
            printerMessage = message
        }
    }
    LaunchedEffect(printerMessage?.id) {
        val message = printerMessage ?: return@LaunchedEffect
        delay(if (message.isError) 4_600L else 3_200L)
        if (printerMessage?.id == message.id) printerMessage = null
    }
    val safeTopPadding = rememberFullscreenSafeTopPadding()
    Box(
        rootModifier
            .padding(top = safeTopPadding)
            .navigationBarsPadding()
    ) {
        PrinterNotificationHost(
            message = printerMessage,
            onClose = { printerMessage = null },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 14.dp, vertical = 8.dp)
                .zIndex(10f)
        )
        if (mode == AppMode.MENU) {
            MainMenuScreen(
                onBack = { context.findActivity()?.finish() },
                onSupply = { mode = AppMode.SUPPLY },
                onPreAssembly = { mode = AppMode.PRE_ASSEMBLY },
                onFbsAssembly = { mode = AppMode.FBS_ASSEMBLY },
                onUpdates = {
                    mode = AppMode.SUPPLY
                    vm.goSettings()
                }
            )
            return@Box
        }
        if (mode == AppMode.FBS_ASSEMBLY) {
            val screenVm = fbsAssemblyVm.value
            val screenState by screenVm.state.collectAsStateWithLifecycle()
            FbsAssemblyScreen(state = screenState, vm = screenVm, onBack = { mode = AppMode.MENU })
            return@Box
        }
        if (mode == AppMode.PRE_ASSEMBLY) {
            val screenVm = preAssemblyVm.value
            val screenState by screenVm.state.collectAsStateWithLifecycle()
            PreAssemblyScreen(state = screenState, vm = screenVm, onBack = { mode = AppMode.MENU })
            return@Box
        }
        val state by vm.state.collectAsStateWithLifecycle()
        LaunchedEffect(vm) {
            vm.ensureLoaded()
        }
        val showStartupLoader = state.isBusy && state.shipments.isEmpty() && state.screen == AppScreen.SHIPMENTS
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
private fun rememberFullscreenSafeTopPadding(): Dp {
    val context = LocalContext.current
    val view = LocalView.current
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val fallbackPadding = remember(context) {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        val resourcePadding = if (resourceId > 0) {
            context.resources.getDimension(resourceId) / context.resources.displayMetrics.density
        } else {
            0f
        }
        maxOf(resourcePadding, 32f).dp
    }
    var measuredTopInsetPx by remember(view, configuration.orientation) { mutableStateOf(0) }

    LaunchedEffect(view, configuration.orientation) {
        ViewCompat.requestApplyInsets(view)
        repeat(4) {
            val insets = ViewCompat.getRootWindowInsets(view)
            val statusTop = insets
                ?.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.statusBars())
                ?.top
                ?: 0
            val cutoutTop = insets
                ?.getInsets(WindowInsetsCompat.Type.displayCutout())
                ?.top
                ?: 0
            measuredTopInsetPx = maxOf(measuredTopInsetPx, statusTop, cutoutTop)
            if (measuredTopInsetPx > 0) return@LaunchedEffect
            delay(50L)
        }
    }

    val measuredPadding = with(density) { measuredTopInsetPx.toDp() }
    val basePadding = if (measuredPadding > fallbackPadding) measuredPadding else fallbackPadding
    return maxOf(basePadding - 6.dp, 24.dp)
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
private fun PrinterNotificationHost(
    message: PrinterUiMessage?,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = message != null,
        modifier = modifier,
        enter = fadeIn(animationSpec = tween(160)) +
            slideInVertically(animationSpec = spring(dampingRatio = 0.78f, stiffness = 520f)) { -it / 3 } +
            scaleIn(initialScale = 0.96f, animationSpec = spring(dampingRatio = 0.78f, stiffness = 520f)),
        exit = fadeOut(animationSpec = tween(130)) +
            slideOutVertically(animationSpec = tween(160)) { -it / 4 } +
            scaleOut(targetScale = 0.97f, animationSpec = tween(130))
    ) {
        message?.let { item ->
            val accent = if (item.isError) DangerColor else FbsNeonGreen
            Row(
                Modifier
                    .widthIn(max = 360.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(FbsDarkPanelAlt.copy(alpha = 0.98f))
                    .border(1.dp, accent.copy(alpha = 0.52f), RoundedCornerShape(22.dp))
                    .shadow(
                        elevation = 18.dp,
                        shape = RoundedCornerShape(22.dp),
                        ambientColor = Color.Black.copy(alpha = 0.36f),
                        spotColor = accent.copy(alpha = 0.18f)
                    )
                    .padding(start = 12.dp, top = 10.dp, end = 8.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(accent.copy(alpha = 0.14f))
                        .border(1.dp, accent.copy(alpha = 0.32f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (item.isError) Icons.Outlined.WarningAmber else Icons.Outlined.Print,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        item.title,
                        color = FbsDarkText,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        lineHeight = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    item.text?.let { text ->
                        Text(
                            text,
                            color = FbsDarkMutedText,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            lineHeight = 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                IconButton(onClick = onClose, modifier = Modifier.size(30.dp)) {
                    Icon(Icons.Outlined.Close, contentDescription = "Закрыть", tint = accent, modifier = Modifier.size(18.dp))
                }
            }
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

    val filtered = remember(state.shipments, query) {
        state.shipments.filter {
            query.isBlank() || it.title.contains(query, true) || it.marketplace.contains(query, true) || it.date.contains(query, true)
        }
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
    val metrics = remember(state.shipments) {
        longArrayOf(
            state.shipments.count { !it.isArchived }.toLong(),
            state.shipments.sumOf { it.boxCount.toLong() },
            state.shipments.sumOf { it.itemCount },
            state.shipments.sumOf { it.positionCount.toLong() }
        )
    }
    val activeCount = metrics[0]
    val boxCount = metrics[1]
    val itemCount = metrics[2]
    val positionCount = metrics[3]

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
    val filteredCities = remember(state.shipmentCities, cityQuery) {
        state.shipmentCities.filter {
            cityQuery.isBlank() || it.cityName.contains(cityQuery, ignoreCase = true)
        }
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
    val totalUnits = remember(state.boxItems) { state.boxItems.sumOf { it.quantity } }

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
            AppUpdateSettingsCard(update = state.update, vm = vm)
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
private fun AppUpdateSettingsCard(update: AppUpdateUiState, vm: AppViewModel) {
    val info = update.info
    val actionEnabled = !update.isChecking && !update.isDownloading
    val canDownload = info?.isUpdateAvailable == true && update.downloadedApkPath == null
    val canInstall = update.downloadedApkPath != null
    SettingsGroup(
        title = "Обновление приложения",
        subtitle = "Локальный сервер обновлений на этом компьютере",
        icon = Icons.Outlined.FileDownload
    ) {
        SettingsInfoRow("Текущая версия", "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
        ModernTextField(
            value = update.serverUrl,
            onValueChange = vm::changeUpdateServerUrl,
            label = "URL манифеста",
            placeholder = "192.168.10.104:8088",
            leadingIcon = Icons.Outlined.Settings,
            modifier = Modifier.fillMaxWidth()
        )
        if (info != null) {
            SettingsInfoRow("Версия на сервере", "${info.versionName} (${info.versionCode})")
            SettingsInfoRow("Размер APK", formatUpdateBytes(info.apkSizeBytes))
            StatusBadge(
                text = if (info.isUpdateAvailable) "Доступно обновление" else "Версия актуальна",
                tone = if (info.isUpdateAvailable) BadgeTone.Green else BadgeTone.Gray
            )
            info.notes?.takeIf { it.isNotBlank() }?.let { notes ->
                Text(notes, color = MutedTextColor, fontSize = 13.sp, lineHeight = 17.sp)
            }
        }
        if (update.isChecking || update.isDownloading) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF7F9FF))
                    .border(1.dp, CardBorderColor.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 11.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(color = AccentColor, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                Text(
                    text = if (update.isDownloading) {
                        "Скачивание ${formatUpdateProgress(update)}"
                    } else {
                        "Проверка сервера"
                    },
                    color = MainTextColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        update.message?.let { message ->
            StatusBadge(message, tone = if (update.status == AppUpdateStatus.ERROR) BadgeTone.Purple else BadgeTone.Blue)
        }
        update.error?.let { error ->
            Text(error, color = DangerColor, fontSize = 13.sp, lineHeight = 17.sp)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AppSecondaryButton(
                text = "Проверить",
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.Search,
                enabled = actionEnabled,
                onClick = vm::checkForUpdate
            )
            if (canInstall) {
                AppPrimaryButton(
                    text = "Установить",
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.CheckCircle,
                    enabled = actionEnabled,
                    onClick = vm::installDownloadedUpdate
                )
            } else {
                AppPrimaryButton(
                    text = "Скачать",
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.FileDownload,
                    enabled = actionEnabled && canDownload,
                    onClick = vm::downloadUpdate
                )
            }
        }
        if (update.status == AppUpdateStatus.INSTALL_PERMISSION_REQUIRED) {
            AppSecondaryButton(
                text = "Разрешить установку",
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Outlined.Settings,
                onClick = vm::openUpdateInstallPermission
            )
        }
    }
}

private fun formatUpdateProgress(update: AppUpdateUiState): String {
    val percent = update.downloadProgress?.let { "${(it * 100).toInt()}%" }
    val bytes = when {
        update.totalBytes != null -> "${formatUpdateBytes(update.downloadedBytes)} / ${formatUpdateBytes(update.totalBytes)}"
        update.downloadedBytes > 0L -> formatUpdateBytes(update.downloadedBytes)
        else -> null
    }
    return listOfNotNull(percent, bytes).joinToString(" · ").ifBlank { "" }
}

private fun formatUpdateBytes(bytes: Long?): String {
    val value = bytes ?: return "неизвестно"
    if (value <= 0L) return "неизвестно"
    val mb = 1024L * 1024L
    val kb = 1024L
    return if (value >= mb) {
        val whole = value / mb
        val tenth = ((value % mb) * 10L) / mb
        "$whole.$tenth МБ"
    } else {
        "${(value / kb).coerceAtLeast(1L)} КБ"
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

private data class ModeCompactData(
    val title: String,
    val subtitle: String,
    val tag: String,
    val icon: ImageVector,
    val accent: Color
)

@Composable
private fun MainMenuScreen(
    onBack: () -> Unit,
    onSupply: () -> Unit,
    onPreAssembly: () -> Unit,
    onFbsAssembly: () -> Unit,
    onUpdates: () -> Unit
) {
    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF7F8F3), Color(0xFFEFF6F7), Color(0xFFF8F2E8))
                )
            )
    ) {
        val narrow = maxWidth < 410.dp
        val dense = maxHeight < 740.dp
        val horizontalPadding = if (narrow) 14.dp else 20.dp
        val modeCards = listOf(
            ModeCompactData(
                title = "Поставки",
                subtitle = "Склады, города, короба и товары",
                tag = "Основной склад",
                icon = Icons.Outlined.Inventory2,
                accent = AccentColor
            ),
            ModeCompactData(
                title = "Предсборка Ozon",
                subtitle = "Проверка остатков и список на перемещение",
                tag = "Перемещение",
                icon = Icons.Outlined.CheckCircle,
                accent = SuccessColor
            )
        )

        ModeMenuBackground(Modifier.matchParentSize())
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = horizontalPadding,
                end = horizontalPadding,
                top = if (dense || narrow) 8.dp else 12.dp,
                bottom = if (dense) 16.dp else 22.dp
            ),
            verticalArrangement = Arrangement.spacedBy(if (dense || narrow) 10.dp else 12.dp)
        ) {
            item {
                ModeMenuHero(onBack = onBack, narrow = narrow, dense = dense)
            }
            item {
                ModeFeaturedModeCard(
                    title = "Сборка FBS",
                    subtitle = "Последовательно собираем заказы Ozon, печатаем этикетки и держим темп до 16:00.",
                    icon = Icons.Outlined.QrCodeScanner,
                    accent = FbsNeonGreen,
                    dense = dense,
                    onClick = onFbsAssembly
                )
            }
            item {
                ModeCompactGrid(
                    items = modeCards,
                    dense = dense,
                    onSupply = onSupply,
                    onPreAssembly = onPreAssembly
                )
            }
            item {
                ModeMaintenanceStrip(dense = dense, onClick = onUpdates)
            }
        }
    }
}

@Composable
private fun ModeMenuBackground(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val lane = 1.dp.toPx()
        drawRect(
            color = Color.White.copy(alpha = 0.50f),
            topLeft = Offset(0f, 0f),
            size = Size(size.width, size.height * 0.28f)
        )
        drawRect(
            color = Color(0xFFE8F2EF).copy(alpha = 0.42f),
            topLeft = Offset(0f, size.height * 0.48f),
            size = Size(size.width, size.height * 0.24f)
        )
        repeat(6) { index ->
            val y = size.height * (0.16f + index * 0.12f)
            drawLine(
                color = Color.White.copy(alpha = 0.34f),
                start = Offset(size.width * -0.06f, y),
                end = Offset(size.width * 1.06f, y - size.height * 0.045f),
                strokeWidth = lane,
                cap = StrokeCap.Round
            )
        }
        drawLine(
            color = SuccessColor.copy(alpha = 0.12f),
            start = Offset(size.width * 0.10f, size.height * 0.08f),
            end = Offset(size.width * 0.90f, size.height * 0.02f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = WarningColor.copy(alpha = 0.10f),
            start = Offset(size.width * 0.10f, size.height * 0.93f),
            end = Offset(size.width * 0.90f, size.height * 0.84f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun ModeMenuHero(onBack: () -> Unit, narrow: Boolean, dense: Boolean) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(
                when {
                    dense -> 96.dp
                    narrow -> 104.dp
                    else -> 112.dp
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart),
            verticalArrangement = Arrangement.spacedBy(if (dense || narrow) 8.dp else 10.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(if (dense || narrow) 40.dp else 44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.96f))
                        .border(1.dp, Color(0xFFE0E7DF), RoundedCornerShape(14.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Назад",
                        tint = MainTextColor,
                        modifier = Modifier.size(if (dense || narrow) 21.dp else 23.dp)
                    )
                }
                ModeMetricPill("Рабочая панель", Icons.Outlined.Business, AccentColor)
            }
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    "Выберите режим",
                    fontSize = if (dense || narrow) 27.sp else 30.sp,
                    lineHeight = if (dense || narrow) 29.sp else 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MainTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "Откройте нужный рабочий сценарий",
                    color = MutedTextColor,
                    fontSize = if (dense || narrow) 12.sp else 13.sp,
                    lineHeight = if (dense || narrow) 15.sp else 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ModeMetricPill(text: String, icon: ImageVector, color: Color, dark: Boolean = false) {
    val shape = RoundedCornerShape(999.dp)
    Row(
        modifier = Modifier
            .heightIn(min = 28.dp)
            .clip(shape)
            .background(if (dark) color.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.88f))
            .border(1.dp, if (dark) color.copy(alpha = 0.28f) else Color(0xFFE0E7DF), shape)
            .padding(horizontal = 9.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(5.dp))
        Text(
            text,
            color = if (dark) FbsDarkText else MainTextColor,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            lineHeight = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ModeMenuIconTile(
    icon: ImageVector,
    accent: Color,
    modifier: Modifier = Modifier,
    dark: Boolean = false,
    size: Dp = 48.dp
) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(if (dark) accent.copy(alpha = 0.13f) else accent.copy(alpha = 0.10f))
            .border(1.dp, if (dark) accent.copy(alpha = 0.36f) else accent.copy(alpha = 0.16f), shape),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(size * 0.47f))
    }
}

@Composable
private fun ModeFeaturedModeCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accent: Color,
    dense: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = if (dense) 148.dp else 164.dp)
            .shadow(
                elevation = 10.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.14f),
                spotColor = accent.copy(alpha = 0.10f)
            )
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF171A15), Color(0xFF0F120F))
                )
            )
            .border(1.dp, accent.copy(alpha = 0.30f), shape)
            .clickable(onClick = onClick)
    ) {
        val compact = maxWidth < 390.dp
        Canvas(Modifier.matchParentSize()) {
            drawRect(color = accent.copy(alpha = 0.92f), size = Size(5.dp.toPx(), size.height))
            drawCircle(
                color = accent.copy(alpha = 0.10f),
                radius = size.width * 0.28f,
                center = Offset(size.width * 0.94f, size.height * 0.08f)
            )
            repeat(4) { index ->
                val x = size.width * (0.58f + index * 0.12f)
                drawLine(
                    color = Color.White.copy(alpha = 0.06f),
                    start = Offset(x, 0f),
                    end = Offset(x - size.width * 0.14f, size.height),
                    strokeWidth = 1.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = if (dense || compact) 15.dp else 18.dp, top = if (dense || compact) 14.dp else 16.dp, end = if (dense || compact) 14.dp else 16.dp, bottom = if (dense || compact) 14.dp else 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(if (compact) 10.dp else 14.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(if (dense || compact) 9.dp else 11.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    ModeMenuIconTile(icon = icon, accent = accent, dark = true, size = if (compact) 46.dp else 50.dp)
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(
                            "Рекомендуемый поток",
                            color = accent,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp,
                            lineHeight = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            title,
                            color = FbsDarkText,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = if (compact) 24.sp else 27.sp,
                            lineHeight = if (compact) 26.sp else 29.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Text(
                    subtitle,
                    color = FbsDarkMutedText,
                    fontSize = if (compact) 12.sp else 13.sp,
                    lineHeight = if (compact) 16.sp else 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (compact) {
                    Row(horizontalArrangement = Arrangement.spacedBy(7.dp), verticalAlignment = Alignment.CenterVertically) {
                        ModeMetricPill("Заказы по одному", Icons.Outlined.Inventory2, AccentColor, dark = true)
                        ModeMetricPill("Таймер 16:00", Icons.Outlined.CalendarMonth, WarningColor, dark = true)
                    }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        ModeMetricPill("Заказы по одному", Icons.Outlined.Inventory2, AccentColor, dark = true)
                        ModeMetricPill("Печать", Icons.Outlined.Print, Color(0xFF0EA5E9), dark = true)
                        ModeMetricPill("16:00", Icons.Outlined.CalendarMonth, WarningColor, dark = true)
                    }
                }
            }
            if (!compact) {
                ModeFbsRoutePreview(accent = accent)
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .size(34.dp)
                .clip(CircleShape)
                .background(accent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.ArrowForward,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ModeFbsRoutePreview(accent: Color) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = Modifier
            .width(92.dp)
            .height(116.dp)
            .clip(shape)
            .background(Color.White.copy(alpha = 0.06f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), shape)
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ModeRouteDot(icon = Icons.Outlined.Inventory2, color = AccentColor)
        Box(
            Modifier
                .width(2.dp)
                .weight(1f)
                .background(Color.White.copy(alpha = 0.14f))
        )
        ModeRouteDot(icon = Icons.Outlined.QrCodeScanner, color = accent, active = true)
        Box(
            Modifier
                .width(2.dp)
                .weight(1f)
                .background(Color.White.copy(alpha = 0.14f))
        )
        ModeRouteDot(icon = Icons.Outlined.Print, color = Color(0xFF0EA5E9))
    }
}

@Composable
private fun ModeRouteDot(icon: ImageVector, color: Color, active: Boolean = false) {
    Box(
        modifier = Modifier
            .size(if (active) 34.dp else 30.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = if (active) 0.96f else 0.18f))
            .border(1.dp, color.copy(alpha = if (active) 0.96f else 0.36f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = if (active) Color.Black else color, modifier = Modifier.size(if (active) 18.dp else 16.dp))
    }
}

@Composable
private fun ModeCompactGrid(
    items: List<ModeCompactData>,
    dense: Boolean,
    onSupply: () -> Unit,
    onPreAssembly: () -> Unit
) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val narrow = maxWidth < 500.dp
        if (narrow) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ModeCompactModeCard(item = items[0], dense = dense, onClick = onSupply, modifier = Modifier.fillMaxWidth())
                ModeCompactModeCard(item = items[1], dense = dense, onClick = onPreAssembly, modifier = Modifier.fillMaxWidth())
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ModeCompactModeCard(item = items[0], dense = dense, onClick = onSupply, modifier = Modifier.weight(1f))
                ModeCompactModeCard(item = items[1], dense = dense, onClick = onPreAssembly, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ModeCompactModeCard(
    item: ModeCompactData,
    dense: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier = modifier
            .heightIn(min = if (dense) 104.dp else 112.dp)
            .clip(shape)
            .background(Color.White.copy(alpha = 0.96f))
            .border(1.dp, Color(0xFFE0E7DF), shape)
            .clickable(onClick = onClick)
    ) {
        Canvas(Modifier.matchParentSize()) {
            drawRect(color = item.accent, size = Size(4.dp.toPx(), size.height))
            drawCircle(
                color = item.accent.copy(alpha = 0.055f),
                radius = size.width * 0.28f,
                center = Offset(size.width * 0.98f, size.height * 0.08f)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 14.dp, top = if (dense) 12.dp else 14.dp, end = 13.dp, bottom = if (dense) 12.dp else 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ModeMenuIconTile(icon = item.icon, accent = item.accent, size = 46.dp)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        item.title,
                        color = MainTextColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        lineHeight = 19.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    ModeInlineTag(item.tag, color = item.accent)
                }
                Text(
                    item.subtitle,
                    color = MutedTextColor,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                Icons.AutoMirrored.Outlined.ArrowForward,
                contentDescription = null,
                tint = item.accent,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun ModeInlineTag(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(color.copy(alpha = 0.10f))
            .border(1.dp, color.copy(alpha = 0.18f), RoundedCornerShape(999.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            lineHeight = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ModeMaintenanceStrip(dense: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = if (dense) 58.dp else 64.dp)
            .clip(shape)
            .background(Color(0xFFFEFCF7).copy(alpha = 0.96f))
            .border(1.dp, Color(0xFFE8E0CF), shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 13.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        ModeMenuIconTile(icon = Icons.Outlined.FileDownload, accent = Color(0xFF0EA5E9), size = 42.dp)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                "Обновление приложения",
                color = MainTextColor,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "Локальный сервер APK",
                color = MutedTextColor,
                fontSize = 11.sp,
                lineHeight = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null, tint = Color(0xFF0EA5E9), modifier = Modifier.size(22.dp))
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
            topLeft = Offset(size.width * -0.18f, size.height * 0.72f),
            size = Size(size.width, size.height * 0.18f)
        )
        cube(
            center = Offset(size.width * 0.34f, size.height * 0.50f),
            side = size.width * 0.38f,
            top = Color.White.copy(alpha = 0.95f),
            left = Color(0xFFE8F0FF).copy(alpha = 0.86f),
            right = Color(0xFFF8FBFF).copy(alpha = 0.95f),
            edge = Color(0xFFC8D9F8).copy(alpha = 0.50f)
        )
        cube(
            center = Offset(size.width * 0.38f, size.height * 0.17f),
            side = size.width * 0.29f,
            top = Color(0xFFA8C9FF),
            left = Color(0xFF4EA7FF),
            right = Color(0xFF2F6BFF),
            edge = Color(0xFF7FAEFF)
        )
        cube(
            center = Offset(size.width * 0.83f, size.height * 0.64f),
            side = size.width * 0.21f,
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
    dense: Boolean = false,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(19.dp)
    val minimumHeight = when {
        badges.isNotEmpty() -> 0.dp
        dense -> 96.dp
        else -> 90.dp
    }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minimumHeight)
            .shadow(
                elevation = 14.dp,
                shape = shape,
                ambientColor = Color(0x100B1226),
                spotColor = Color(0x160B1226)
            )
            .clip(shape)
            .background(Color.White.copy(alpha = 0.97f))
            .border(1.dp, Color(0xFFE4EBF7), shape)
            .clickable(onClick = onClick)
    ) {
        val narrow = maxWidth < NarrowScreenBreakpoint
        val condensed = dense || narrow
        val iconSize = if (condensed) 56.dp else 60.dp
        val contentGap = if (condensed) 30.dp else 26.dp
        val badgeGap = if (condensed) 12.dp else 8.dp
        Canvas(Modifier.matchParentSize()) {
            drawRect(
                color = accent,
                topLeft = Offset.Zero,
                size = Size(5.dp.toPx(), size.height)
            )
            drawCircle(
                color = accent.copy(alpha = 0.09f),
                radius = if (condensed) 145.dp.toPx() else 155.dp.toPx(),
                center = Offset(
                    x = (-10).dp.toPx(),
                    y = (-35).dp.toPx()
                )
            )
        }
        Column(
            Modifier.padding(
                start = if (condensed) 21.dp else 20.dp,
                top = if (condensed) 10.dp else 12.dp,
                end = if (condensed) 21.dp else 20.dp,
                bottom = if (condensed) 10.dp else 12.dp
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(contentGap)
            ) {
                ModeChoiceIcon(icon = icon, accent = accent, size = iconSize)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        title,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = if (condensed) 16.sp else 18.sp,
                        lineHeight = if (condensed) 19.sp else 21.sp,
                        color = MainTextColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        subtitle,
                        color = MutedTextColor,
                        fontSize = if (condensed) 11.sp else 13.sp,
                        lineHeight = if (condensed) 16.sp else 18.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Box(
                    modifier = Modifier
                        .size(if (condensed) 40.dp else 44.dp)
                        .clip(CircleShape)
                        .background(accent.copy(alpha = 0.10f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.ExpandMore,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier
                            .size(if (condensed) 24.dp else 27.dp)
                            .rotate(-90f)
                    )
                }
            }
            if (badges.isNotEmpty()) {
                Spacer(Modifier.height(if (condensed) 7.dp else 9.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(if (condensed) 6.dp else 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(iconSize + badgeGap))
                    badges.forEach { badge ->
                        ModeBadge(badge = badge, compact = condensed, modifier = Modifier.weight(1f))
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
            .shadow(
                elevation = 9.dp,
                shape = CircleShape,
                ambientColor = Color(0x140B1226),
                spotColor = accent.copy(alpha = 0.14f)
            )
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    listOf(Color.White, Color.White.copy(alpha = 0.96f), accent.copy(alpha = 0.05f))
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.82f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(size * 0.46f))
    }
}

@Composable
private fun ModeBadge(badge: ModeBadgeData, compact: Boolean, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(badge.tone.copy(alpha = 0.10f))
            .heightIn(min = if (compact) 26.dp else 28.dp)
            .padding(horizontal = if (compact) 8.dp else 9.dp, vertical = if (compact) 5.dp else 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            badge.icon,
            contentDescription = null,
            tint = badge.tone,
            modifier = Modifier.size(if (compact) 15.dp else 16.dp)
        )
        Spacer(Modifier.width(if (compact) 5.dp else 6.dp))
        Text(
            badge.text,
            color = badge.tone,
            fontWeight = FontWeight.Bold,
            fontSize = if (compact) 11.sp else 12.sp,
            lineHeight = if (compact) 13.sp else 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FbsAssemblyScreen(state: FbsAssemblyUiState, vm: FbsAssemblyViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    var pageName by rememberSaveable { mutableStateOf(FbsAssemblyPage.HOME.name) }
    var overlayName by rememberSaveable { mutableStateOf<String?>(null) }
    var renderedOverlayName by rememberSaveable { mutableStateOf<String?>(null) }
    var topChromeExpanded by rememberSaveable { mutableStateOf(true) }
    var showResetDataConfirm by rememberSaveable { mutableStateOf(false) }
    var labelServerHost by rememberSaveable { mutableStateOf(OzonLabelPrinter.savedServerHost(context)) }
    var usbDirectPrintEnabled by rememberSaveable { mutableStateOf(UsbDirectPrinter.isEnabled(context)) }
    var usbRawPdfEnabled by rememberSaveable { mutableStateOf(UsbDirectPrinter.isRawPdfEnabled(context)) }
    var usbPrintOffsetMm by rememberSaveable {
        mutableStateOf(UsbDirectPrinter.formatOffsetXMm(UsbDirectPrinter.offsetXMm(context)))
    }
    var usbStatusRefresh by rememberSaveable { mutableStateOf(0) }
    var notificationStatusRefresh by rememberSaveable { mutableStateOf(0) }
    val usbPrinterStatus = remember(usbStatusRefresh, usbDirectPrintEnabled, usbRawPdfEnabled) {
        UsbDirectPrinter.statusText(context)
    }
    val notificationStatus = remember(notificationStatusRefresh) {
        FbsOrderNotificationService(context).notificationStatusText()
    }
    val page = FbsAssemblyPage.values().firstOrNull { it.name == pageName } ?: FbsAssemblyPage.HOME
    val overlay = FbsAssemblyOverlay.values().firstOrNull { it.name == overlayName }
    val renderedOverlay = FbsAssemblyOverlay.values().firstOrNull { it.name == renderedOverlayName }
    val backgroundScale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (overlay == null) 1f else 0.985f,
        animationSpec = spring(dampingRatio = 0.78f, stiffness = 500f),
        label = "fbs_background_scale"
    )

    LaunchedEffect(Unit) {
        if (!state.isLoaded && !state.isLoading) vm.loadOrders()
    }
    LaunchedEffect(vm, state.mode, state.isFinished) {
        if (state.mode != FbsAssemblyMode.ASSEMBLY || state.isFinished) return@LaunchedEffect
        while (true) {
            delay(FBS_ASSEMBLY_AUTO_REFRESH_INTERVAL_MILLIS)
            vm.autoRefreshOrders()
        }
    }
    LaunchedEffect(state.isFinished) {
        if (state.isFinished) {
            overlayName = null
            pageName = FbsAssemblyPage.FINISH.name
        }
    }
    LaunchedEffect(overlayName) {
        if (overlayName != null) {
            renderedOverlayName = overlayName
        } else {
            delay(230)
            if (overlayName == null) renderedOverlayName = null
        }
    }
    LaunchedEffect(state.message) {
        if (state.message != null) {
            delay(2600)
            vm.clearMessage()
        }
    }

    fun openOverlay(value: FbsAssemblyOverlay) {
        renderedOverlayName = value.name
        overlayName = value.name
    }

    fun refreshOrders() {
        vm.loadOrders()
    }

    BackHandler(enabled = overlay != null) {
        overlayName = null
    }
    BackHandler(enabled = overlay == null) {
        if (page == FbsAssemblyPage.HOME) onBack() else pageName = FbsAssemblyPage.HOME.name
    }

    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .background(FbsDarkBackground)
    ) {
        val compactScreen = maxHeight < 740.dp || maxWidth < 430.dp
        val denseScreen = maxHeight < 620.dp || maxWidth < 370.dp
        val screenPadding = if (compactScreen) 10.dp else 18.dp
        val sectionSpacing = when {
            denseScreen -> 7.dp
            compactScreen -> 8.dp
            else -> 14.dp
        }
        val feedbackTopPadding = when {
            !topChromeExpanded -> 48.dp
            compactScreen -> 72.dp
            else -> 88.dp
        }
        Column(
            Modifier
                .fillMaxSize()
                .blur(if (overlay == null) 0.dp else 10.dp)
                .graphicsLayer {
                    scaleX = backgroundScale
                    scaleY = backgroundScale
                }
                .padding(horizontal = screenPadding, vertical = if (denseScreen) 8.dp else 12.dp),
            verticalArrangement = Arrangement.spacedBy(sectionSpacing)
        ) {
            FbsAssemblyHeader(
                onBack = {
                    if (page == FbsAssemblyPage.HOME) onBack() else pageName = FbsAssemblyPage.HOME.name
                },
                onRefresh = ::refreshOrders,
                isLoading = state.isLoading,
                compact = compactScreen,
                expanded = topChromeExpanded,
                onExpandedChange = { topChromeExpanded = it },
                subtitle = when (page) {
                    FbsAssemblyPage.HOME -> "Сборка: FBS-заказы Ozon"
                    FbsAssemblyPage.LIST -> "Весь список заказов и товаров"
                    FbsAssemblyPage.WORK -> "Текущий заказ и прогресс сборки"
                    FbsAssemblyPage.FINISH -> "Итоги завершённой сборки"
                }
            )

            when {
                state.isLoading && !state.isLoaded -> FbsAssemblyLoadingCard()
                state.error != null -> FbsAssemblyErrorCard(message = state.error, onRetry = vm::loadOrders)
                else -> when (page) {
                    FbsAssemblyPage.HOME -> FbsAssemblyHomeContent(
                        state = state,
                        onOpenList = { pageName = FbsAssemblyPage.LIST.name },
                        onOpenAnalytics = { openOverlay(FbsAssemblyOverlay.ANALYTICS) },
                        onOpenHistory = { openOverlay(FbsAssemblyOverlay.HISTORY) },
                        onOpenSettings = { openOverlay(FbsAssemblyOverlay.SETTINGS) },
                        onModeChange = vm::setMode,
                        onRefresh = ::refreshOrders,
                        onStart = {
                            vm.startAssembly()
                            if (fbsAssemblyPendingAssemblyOrders(state.orders).isNotEmpty()) {
                                pageName = FbsAssemblyPage.WORK.name
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    FbsAssemblyPage.LIST -> FbsAssemblyOrderListContent(
                        state = state,
                        onRefresh = ::refreshOrders,
                        modifier = Modifier.weight(1f)
                    )
                    FbsAssemblyPage.WORK -> FbsAssemblyWorkContent(
                        state = state,
                        onCollect = vm::collectCurrentOrder,
                        onPrintLabel = vm::printCurrentOrderLabel,
                        onSkip = vm::skipCurrentOrder,
                        onRestart = {
                            vm.resetProgress()
                            pageName = FbsAssemblyPage.HOME.name
                        },
                        modifier = Modifier.weight(1f)
                    )
                    FbsAssemblyPage.FINISH -> FbsAssemblyFinishContent(
                        state = state,
                        onRestart = {
                            vm.resetProgress()
                            pageName = FbsAssemblyPage.HOME.name
                        },
                        onReload = {
                            vm.loadOrders()
                            pageName = FbsAssemblyPage.HOME.name
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = state.message != null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 14.dp, vertical = feedbackTopPadding),
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 3 })
        ) {
            state.message?.let { FbsAssemblyMessage(text = it, onClose = vm::clearMessage) }
        }

        AnimatedVisibility(
            visible = overlay != null,
            modifier = Modifier.fillMaxSize(),
            enter = fadeIn(animationSpec = tween(170)) +
                slideInVertically(
                    initialOffsetY = { it / 14 },
                    animationSpec = spring(dampingRatio = 0.76f, stiffness = 460f)
                ) +
                scaleIn(
                    initialScale = 0.965f,
                    animationSpec = spring(dampingRatio = 0.76f, stiffness = 500f)
                ),
            exit = fadeOut(animationSpec = tween(210)) +
                slideOutVertically(targetOffsetY = { it / 18 }, animationSpec = tween(210)) +
                scaleOut(targetScale = 0.98f, animationSpec = tween(210))
        ) {
            renderedOverlay?.let { currentOverlay ->
                FbsAssemblyPopupSurface(
                    overlay = currentOverlay,
                    onDismiss = { overlayName = null }
                ) { contentModifier ->
                    when (currentOverlay) {
                        FbsAssemblyOverlay.ANALYTICS -> FbsAssemblyAnalyticsContent(
                            state = state,
                            onRefresh = ::refreshOrders,
                            modifier = contentModifier
                        )
                        FbsAssemblyOverlay.HISTORY -> FbsAssemblyHistoryContent(
                            state = state,
                            modifier = contentModifier
                        )
                        FbsAssemblyOverlay.SETTINGS -> FbsAssemblySettingsContent(
                            settings = state.settings,
                            notificationStatus = notificationStatus,
                            onSendTestNotification = {
                                FbsOrderNotificationService(context).notifyTest()
                                notificationStatusRefresh++
                            },
                            onOpenNotificationSettings = {
                                FbsOrderNotificationService.openNotificationSettings(context)
                                notificationStatusRefresh++
                            },
                            labelServerHost = labelServerHost,
                            onLabelServerHostChange = { host ->
                                labelServerHost = host
                                OzonLabelPrinter.saveServerHost(context, host)
                            },
                            onPrintLabelTest = { OzonLabelPrinter.printTestLabel(context) },
                            usbDirectPrintEnabled = usbDirectPrintEnabled,
                            usbRawPdfEnabled = usbRawPdfEnabled,
                            usbPrinterStatus = usbPrinterStatus,
                            usbPrintOffsetMm = usbPrintOffsetMm,
                            onUsbDirectPrintEnabledChange = { enabled ->
                                usbDirectPrintEnabled = enabled
                                UsbDirectPrinter.setEnabled(context, enabled)
                                usbStatusRefresh++
                            },
                            onUsbRawPdfEnabledChange = { enabled ->
                                usbRawPdfEnabled = enabled
                                UsbDirectPrinter.setRawPdfEnabled(context, enabled)
                                usbStatusRefresh++
                            },
                            onUsbPrintOffsetMmChange = { value ->
                                usbPrintOffsetMm = value
                                    .replace(',', '.')
                                    .filterIndexed { index, char ->
                                        char.isDigit() || char == '.' || (char == '-' && index == 0)
                                    }
                                    .take(6)
                            },
                            onSaveUsbPrintOffset = {
                                val saved = UsbDirectPrinter.saveOffsetXMm(
                                    context,
                                    usbPrintOffsetMm.replace(',', '.').toDoubleOrNull()
                                        ?: UsbDirectPrinter.offsetXMm(context)
                                )
                                usbPrintOffsetMm = UsbDirectPrinter.formatOffsetXMm(saved)
                                usbStatusRefresh++
                                PrinterUiNotifier.success(
                                    title = "Смещение сохранено",
                                    text = "${UsbDirectPrinter.formatOffsetXMm(saved)} мм"
                                )
                            },
                            onResetUsbPrintOffset = {
                                val saved = UsbDirectPrinter.resetOffsetXMm(context)
                                usbPrintOffsetMm = UsbDirectPrinter.formatOffsetXMm(saved)
                                usbStatusRefresh++
                                PrinterUiNotifier.success(
                                    title = "Смещение сброшено",
                                    text = "${UsbDirectPrinter.formatOffsetXMm(saved)} мм"
                                )
                            },
                            onPrintUsbTest = {
                                usbStatusRefresh++
                                UsbDirectPrinter.printTestPage(context)
                            },
                            onRefreshUsbStatus = { usbStatusRefresh++ },
                            onSave = vm::saveForecastSettings,
                            onResetData = { showResetDataConfirm = true },
                            modifier = contentModifier
                        )
                    }
                }
            }
        }
    }

    if (showResetDataConfirm) {
        FbsAssemblyResetDataConfirmDialog(
            onDismiss = { showResetDataConfirm = false },
            onConfirm = {
                showResetDataConfirm = false
                vm.resetAssemblyData()
                overlayName = null
                pageName = FbsAssemblyPage.HOME.name
            }
        )
    }
}

@Composable
private fun FbsAssemblyPopupSurface(
    overlay: FbsAssemblyOverlay,
    onDismiss: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    val closeInteraction = remember { MutableInteractionSource() }
    val surfaceInteraction = remember { MutableInteractionSource() }
    val (title, subtitle, icon) = when (overlay) {
        FbsAssemblyOverlay.ANALYTICS -> Triple(
            "Аналитика сборки",
            "Темп, прогноз и риск дедлайна",
            Icons.Outlined.Analytics
        )
        FbsAssemblyOverlay.HISTORY -> Triple(
            "История сборки",
            "Собранные и пропущенные заказы",
            Icons.Outlined.History
        )
        FbsAssemblyOverlay.SETTINGS -> Triple(
            "Настройки FBS",
            "Прогноз, печать и локальные данные",
            Icons.Outlined.Settings
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xCC000000), Color(0xF0000000))
                )
            )
            .clickable(
                interactionSource = closeInteraction,
                indication = null,
                onClick = onDismiss
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.90f)
                .padding(horizontal = 12.dp, vertical = 18.dp)
                .shadow(
                    elevation = 30.dp,
                    shape = RoundedCornerShape(30.dp),
                    ambientColor = Color.Black.copy(alpha = 0.70f),
                    spotColor = FbsNeonGreen.copy(alpha = 0.16f)
                )
                .clickable(
                    interactionSource = surfaceInteraction,
                    indication = null,
                    onClick = {}
            ),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = FbsDarkPanel.copy(alpha = 0.98f)),
            border = BorderStroke(1.dp, FbsDarkBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(FbsDarkPanel.copy(alpha = 0.98f), FbsDarkBackground.copy(alpha = 0.96f))
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(44.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(FbsDarkBorder)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AppIconBubble(
                        icon = icon,
                        tint = FbsNeonGreen,
                        background = FbsNeonGreen.copy(alpha = 0.12f),
                        modifier = Modifier.size(48.dp)
                    )
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            title,
                            color = FbsDarkText,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp,
                            lineHeight = 20.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            subtitle,
                            color = FbsDarkMutedText,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    OutlinedIconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, FbsDarkBorder)
                    ) {
                        Icon(Icons.Outlined.Close, contentDescription = "Закрыть", tint = FbsDarkText, modifier = Modifier.size(20.dp))
                    }
                }
                HorizontalDivider(color = FbsDarkBorder.copy(alpha = 0.82f))
                content(Modifier.weight(1f).fillMaxWidth())
            }
        }
    }
}

@Composable
private fun FbsAssemblyMessage(text: String, onClose: () -> Unit) {
    Row(
        Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(FbsDarkPanel.copy(alpha = 0.96f))
            .border(1.dp, FbsNeonGreen.copy(alpha = 0.42f), RoundedCornerShape(999.dp))
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text,
            color = FbsDarkText,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false)
        )
        IconButton(onClick = onClose, modifier = Modifier.size(26.dp)) {
            Icon(Icons.Outlined.Close, contentDescription = "Закрыть", tint = FbsNeonGreen, modifier = Modifier.size(17.dp))
        }
    }
}

@Composable
private fun FbsAssemblyHeader(
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    isLoading: Boolean,
    compact: Boolean = false,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    subtitle: String
) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(if (compact) 6.dp else 8.dp)
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(animationSpec = tween(160)) + expandVertically(expandFrom = Alignment.Top),
            exit = fadeOut(animationSpec = tween(120)) + shrinkVertically(shrinkTowards = Alignment.Top)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(if (compact) 10.dp else 14.dp)
            ) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(if (compact) 3.dp else 6.dp)) {
                    Text(
                        "Сборка FBS",
                        fontSize = if (compact) 22.sp else 24.sp,
                        lineHeight = if (compact) 26.sp else 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = FbsDarkText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        subtitle,
                        color = FbsDarkMutedText,
                        fontSize = if (compact) 12.sp else 13.sp,
                        lineHeight = if (compact) 16.sp else 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                OutlinedIconButton(
                    onClick = { onExpandedChange(false) },
                    modifier = Modifier.size(if (compact) 42.dp else 46.dp),
                    shape = RoundedCornerShape(if (compact) 15.dp else 17.dp),
                    border = BorderStroke(1.dp, FbsDarkBorder)
                ) {
                    Icon(
                        Icons.Outlined.ExpandMore,
                        contentDescription = "Свернуть верх",
                        tint = FbsNeonGreen,
                        modifier = Modifier
                            .size(if (compact) 23.dp else 25.dp)
                            .rotate(180f)
                    )
                }
                OutlinedIconButton(
                    onClick = onRefresh,
                    modifier = Modifier
                        .size(if (compact) 58.dp else 72.dp)
                        .alpha(if (isLoading) 0.55f else 1f),
                    shape = RoundedCornerShape(if (compact) 20.dp else 24.dp),
                    border = BorderStroke(1.dp, FbsDarkBorder)
                ) {
                    Icon(
                        Icons.Outlined.FileDownload,
                        contentDescription = "Обновить заказы",
                        tint = FbsNeonGreen,
                        modifier = Modifier.size(if (compact) 30.dp else 38.dp)
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = !expanded,
            enter = fadeIn(animationSpec = tween(160)) + expandVertically(expandFrom = Alignment.Top),
            exit = fadeOut(animationSpec = tween(120)) + shrinkVertically(shrinkTowards = Alignment.Top)
        ) {
            FbsAssemblyCollapsedHeader(
                onExpand = { onExpandedChange(true) },
                onRefresh = onRefresh,
                isLoading = isLoading,
                compact = compact
            )
        }
    }
}

@Composable
private fun FbsAssemblyCollapsedHeader(
    onExpand: () -> Unit,
    onRefresh: () -> Unit,
    isLoading: Boolean,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(if (compact) 18.dp else 22.dp)
    Row(
        modifier
            .fillMaxWidth()
            .height(if (compact) 44.dp else 50.dp)
            .clip(shape)
            .background(FbsDarkPanelAlt.copy(alpha = 0.96f))
            .border(1.dp, FbsNeonGreen.copy(alpha = 0.28f), shape)
            .clickable(onClick = onExpand)
            .padding(start = 14.dp, end = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Сборка FBS",
            modifier = Modifier.weight(1f),
            color = FbsDarkText,
            fontWeight = FontWeight.ExtraBold,
            fontSize = if (compact) 15.sp else 16.sp,
            lineHeight = if (compact) 17.sp else 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(
            onClick = onRefresh,
            modifier = Modifier
                .size(if (compact) 34.dp else 38.dp)
                .alpha(if (isLoading) 0.55f else 1f)
        ) {
            Icon(
                Icons.Outlined.FileDownload,
                contentDescription = "Обновить заказы",
                tint = FbsNeonGreen,
                modifier = Modifier.size(if (compact) 20.dp else 22.dp)
            )
        }
        IconButton(onClick = onExpand, modifier = Modifier.size(if (compact) 34.dp else 38.dp)) {
            Icon(
                Icons.Outlined.ExpandMore,
                contentDescription = "Развернуть верх",
                tint = FbsNeonGreen,
                modifier = Modifier.size(if (compact) 22.dp else 24.dp)
            )
        }
    }
}

@Composable
private fun FbsAssemblyHomeContent(
    state: FbsAssemblyUiState,
    onOpenList: () -> Unit,
    onOpenAnalytics: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenSettings: () -> Unit,
    onModeChange: (FbsAssemblyMode) -> Unit,
    onRefresh: () -> Unit,
    onStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pendingOrders = remember(state.orders) { fbsAssemblyPendingAssemblyOrders(state.orders) }
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val dense = maxHeight < 690.dp
        val compact = dense || maxHeight < 790.dp
        val spacing = when {
            dense -> 6.dp
            compact -> 8.dp
            else -> 10.dp
        }
        val actionHeight = when {
            dense -> 56.dp
            compact -> 62.dp
            else -> 76.dp
        }

        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            FbsAssemblyModeSwitch(
                selected = state.mode,
                onSelected = onModeChange,
                enabled = !state.isLoading,
                compact = compact
            )
            FbsAssemblySummaryCard(
                state = state.copy(orders = pendingOrders),
                compact = compact,
                dense = dense
            )
            state.planningError?.let { error ->
                FbsAssemblyErrorCard(message = error, onRetry = onRefresh, compact = compact)
            } ?: FbsAssemblyForecastCard(
                state = state,
                compact = compact,
                dense = false
            )
            Spacer(Modifier.weight(1f))
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(if (compact) 7.dp else 10.dp)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 12.dp)) {
                    FbsAssemblyLargeActionButton(
                        text = "Посмотреть весь список заказов",
                        icon = Icons.Outlined.Description,
                        enabled = state.planningOrders.isNotEmpty() && !state.isLoading,
                        onClick = onOpenList,
                        compact = compact,
                        modifier = Modifier.weight(1f).height(actionHeight)
                    )
                    FbsAssemblyOutlinedLargeActionButton(
                        text = "Начать сборку",
                        icon = Icons.Outlined.PlayArrow,
                        enabled = pendingOrders.isNotEmpty() && !state.isLoading,
                        onClick = onStart,
                        compact = compact,
                        modifier = Modifier.weight(1f).height(actionHeight)
                    )
                }
                FbsAssemblyQuickActions(
                    onOpenAnalytics = onOpenAnalytics,
                    onOpenHistory = onOpenHistory,
                    onOpenSettings = onOpenSettings,
                    onRefresh = onRefresh,
                    refreshEnabled = !state.isLoading,
                    compact = compact
                )
            }
        }
    }
}

@Composable
private fun FbsAssemblyQuickActions(
    onOpenAnalytics: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenSettings: () -> Unit,
    onRefresh: () -> Unit,
    refreshEnabled: Boolean,
    compact: Boolean = false
) {
    val shape = RoundedCornerShape(if (compact) 18.dp else 22.dp)
    Column(
        Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(
                        FbsDarkPanelAlt.copy(alpha = 0.98f),
                        FbsDarkPanel.copy(alpha = 0.97f)
                    )
                )
            )
            .border(1.dp, FbsNeonGreen.copy(alpha = 0.24f), shape)
            .padding(horizontal = if (compact) 6.dp else 8.dp, vertical = if (compact) 5.dp else 7.dp),
        verticalArrangement = Arrangement.spacedBy(if (compact) 5.dp else 7.dp)
    ) {
        HorizontalDivider(color = FbsNeonGreen.copy(alpha = 0.18f))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(if (compact) 5.dp else 7.dp)
        ) {
            FbsAssemblyQuickActionTile(
                text = "Аналитика",
                icon = Icons.Outlined.Analytics,
                onClick = onOpenAnalytics,
                compact = compact,
                active = true,
                modifier = Modifier.weight(1f)
            )
            FbsAssemblyQuickActionTile(
                text = "История",
                icon = Icons.Outlined.History,
                onClick = onOpenHistory,
                compact = compact,
                modifier = Modifier.weight(1f)
            )
            FbsAssemblyQuickActionTile(
                text = "Настройки",
                icon = Icons.Outlined.Settings,
                onClick = onOpenSettings,
                compact = compact,
                modifier = Modifier.weight(1f)
            )
            FbsAssemblyQuickActionTile(
                text = "Обновить",
                icon = Icons.Outlined.Refresh,
                enabled = refreshEnabled,
                onClick = onRefresh,
                compact = compact,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
@Composable
private fun FbsAssemblyQuickActionTile(
    text: String,
    icon: ImageVector,
    enabled: Boolean = true,
    compact: Boolean = false,
    active: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val itemColor = if (active) FbsNeonGreen else FbsDarkMutedText
    Column(
        modifier = modifier
            .height(if (compact) 58.dp else 74.dp)
            .alpha(if (enabled) 1f else 0.48f)
            .clickable(enabled = enabled, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = itemColor,
            modifier = Modifier.size(if (compact) 28.dp else 34.dp)
        )
        Spacer(Modifier.height(if (compact) 3.dp else 5.dp))
        Text(
            text,
            color = itemColor,
            fontWeight = FontWeight.ExtraBold,
            fontSize = if (compact) 11.sp else 13.sp,
            lineHeight = if (compact) 12.sp else 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        if (active) {
            Spacer(Modifier.height(if (compact) 3.dp else 5.dp))
            Box(
                Modifier
                    .width(if (compact) 44.dp else 58.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(FbsNeonGreen)
            )
        } else {
            Spacer(Modifier.height(if (compact) 6.dp else 8.dp))
        }
    }
}

@Composable
private fun FbsAssemblyModeSwitch(
    selected: FbsAssemblyMode,
    onSelected: (FbsAssemblyMode) -> Unit,
    enabled: Boolean,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 18.dp,
                shape = RoundedCornerShape(if (compact) 18.dp else 24.dp),
                ambientColor = FbsNeonGreen.copy(alpha = 0.10f),
                spotColor = FbsNeonGreen.copy(alpha = 0.14f)
            ),
        shape = RoundedCornerShape(if (compact) 18.dp else 24.dp),
        colors = CardDefaults.cardColors(containerColor = FbsDarkPanelAlt),
        border = BorderStroke(1.dp, FbsDarkBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(if (compact) 6.dp else 8.dp),
            horizontalArrangement = Arrangement.spacedBy(if (compact) 6.dp else 8.dp)
        ) {
            FbsAssemblyMode.values().forEach { mode ->
                val isSelected = mode == selected
                if (isSelected) {
                    Button(
                        onClick = { onSelected(mode) },
                        enabled = enabled,
                        modifier = Modifier.weight(1f).height(if (compact) 40.dp else 46.dp),
                        shape = RoundedCornerShape(if (compact) 14.dp else 18.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FbsNeonGreen, contentColor = Color.Black)
                    ) {
                        Text(
                            mode.title.uppercase(),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = if (compact) 13.sp else 14.sp,
                            lineHeight = if (compact) 15.sp else 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else {
                    OutlinedButton(
                        onClick = { onSelected(mode) },
                        enabled = enabled,
                        modifier = Modifier.weight(1f).height(if (compact) 40.dp else 46.dp),
                        shape = RoundedCornerShape(if (compact) 14.dp else 18.dp),
                        border = BorderStroke(1.dp, Color.Transparent),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent, contentColor = FbsDarkText)
                    ) {
                        Text(
                            mode.title.uppercase(),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = if (compact) 13.sp else 14.sp,
                            lineHeight = if (compact) 15.sp else 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FbsAssemblyLargeActionButton(
    text: String,
    icon: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(if (compact) 16.dp else 18.dp)
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .defaultMinSize(minHeight = if (compact) 54.dp else 68.dp)
            .alpha(if (enabled) 1f else 0.48f)
            .shadow(
                elevation = 12.dp,
                shape = shape,
                ambientColor = FbsNeonGreen.copy(alpha = 0.12f),
                spotColor = FbsNeonGreen.copy(alpha = 0.18f)
            )
            .background(Color.Transparent, shape),
        shape = shape,
        border = BorderStroke(1.4.dp, FbsNeonGreen),
        contentPadding = PaddingValues(horizontal = if (compact) 8.dp else 12.dp, vertical = if (compact) 4.dp else 6.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = FbsDarkText,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = FbsDarkText.copy(alpha = 0.62f)
        )
    ) {
        Icon(icon, contentDescription = null, tint = FbsNeonGreen, modifier = Modifier.size(if (compact) 24.dp else 30.dp))
        Spacer(Modifier.width(if (compact) 7.dp else 10.dp))
        Text(
            text,
            fontWeight = FontWeight.ExtraBold,
            fontSize = if (compact) 12.sp else 14.sp,
            lineHeight = if (compact) 15.sp else 18.sp,
            textAlign = TextAlign.Start,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FbsAssemblyOutlinedLargeActionButton(
    text: String,
    icon: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(if (compact) 16.dp else 18.dp)
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .defaultMinSize(minHeight = if (compact) 54.dp else 68.dp)
            .alpha(if (enabled) 1f else 0.48f)
            .shadow(
                elevation = 18.dp,
                shape = shape,
                ambientColor = FbsNeonGreen.copy(alpha = 0.24f),
                spotColor = FbsNeonGreen.copy(alpha = 0.34f)
            ),
        shape = shape,
        contentPadding = PaddingValues(horizontal = if (compact) 10.dp else 12.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = FbsNeonGreen,
            contentColor = Color.Black,
            disabledContainerColor = FbsNeonGreen.copy(alpha = 0.48f),
            disabledContentColor = Color.Black.copy(alpha = 0.72f)
        )
    ) {
        Box(
            Modifier
                .size(if (compact) 32.dp else 42.dp)
                .clip(CircleShape)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = FbsNeonGreen, modifier = Modifier.size(if (compact) 22.dp else 28.dp))
        }
        Spacer(Modifier.width(if (compact) 9.dp else 12.dp))
        Text(
            text,
            fontWeight = FontWeight.ExtraBold,
            fontSize = if (compact) 13.sp else 15.sp,
            lineHeight = if (compact) 16.sp else 18.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FbsAssemblyCollectButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(20.dp)
    Button(
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(minHeight = if (compact) 64.dp else 70.dp)
            .height(if (compact) 64.dp else 70.dp)
            .shadow(
                elevation = 14.dp,
                shape = shape,
                ambientColor = FbsNeonGreen.copy(alpha = 0.24f),
                spotColor = FbsNeonGreen.copy(alpha = 0.34f)
            ),
        shape = shape,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = FbsNeonGreen, contentColor = Color.Black)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(if (compact) 24.dp else 28.dp))
        Spacer(Modifier.width(if (compact) 9.dp else 12.dp))
        Text(
            text = text,
            fontWeight = FontWeight.ExtraBold,
            fontSize = if (compact) 15.sp else 15.sp,
            lineHeight = if (compact) 18.sp else 19.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FbsAssemblySecondaryActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean = true,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .defaultMinSize(minHeight = if (compact) 50.dp else 52.dp)
            .height(if (compact) 50.dp else 52.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .shadow(
                elevation = 8.dp,
                shape = shape,
                ambientColor = FbsNeonGreen.copy(alpha = 0.08f),
                spotColor = FbsNeonGreen.copy(alpha = 0.12f)
            ),
        shape = shape,
        border = BorderStroke(1.dp, FbsDarkBorder),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = FbsDarkPanel,
            contentColor = FbsDarkText
        )
    ) {
        Icon(icon, contentDescription = null, tint = FbsNeonGreen, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(7.dp))
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FbsAssemblyOrderListContent(
    state: FbsAssemblyUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 18.dp)
    ) {
        item {
            FbsAssemblySummaryCard(state.copy(orders = state.planningOrders), compact = false)
        }
        item {
            FbsAssemblySecondaryActionButton(
                text = "Обновить",
                icon = Icons.Outlined.FileDownload,
                onClick = onRefresh,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (state.planningError != null) {
            item { FbsAssemblyErrorCard(message = state.planningError, onRetry = onRefresh) }
        } else if (state.planningOrders.isEmpty()) {
            item { FbsAssemblyEmptyCard() }
        } else {
            items(state.planningOrders, key = { it.orderNumber }) { order ->
                FbsAssemblyOrderCard(
                    order = order,
                    large = false,
                    showMultiPositionWarning = fbsAssemblyShowMultiPositionWarning(order)
                )
            }
        }
    }
}

@Composable
private fun FbsAssemblyWorkContent(
    state: FbsAssemblyUiState,
    onCollect: () -> Unit,
    onPrintLabel: () -> Unit,
    onSkip: () -> Unit,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val now = rememberCurrentDateTime(refreshIntervalMillis = 1_000)
    val currentOrder = remember(state.orders, state.currentOrderNumber) { state.currentOrder }

    if (state.isFinished) {
        FbsAssemblyFinishContent(state = state, onRestart = onRestart, onReload = onRestart, modifier = modifier)
        return
    }

    BoxWithConstraints(modifier.fillMaxWidth()) {
        val dense = maxHeight < 600.dp
        val compact = maxHeight < 760.dp
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(if (dense) 6.dp else 9.dp)) {
            FbsAssemblyProgressPanel(state = state, now = now, compact = compact, prominent = true)
            if (currentOrder == null) {
                FbsAssemblyEmptyCard(
                    title = "Нет текущего заказа",
                    text = "Нажмите «Начать сборку» на стартовом экране или обновите список заказов."
                )
            } else {
                FbsAssemblyActiveOrderCard(
                    order = currentOrder,
                    compact = compact,
                    dense = dense,
                    showMultiPositionWarning = fbsAssemblyShowMultiPositionWarning(currentOrder),
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )
                Column(verticalArrangement = Arrangement.spacedBy(if (dense) 6.dp else 8.dp)) {
                    FbsAssemblyCollectButton(
                        text = "Заказ собран",
                        icon = Icons.Outlined.CheckCircle,
                        onClick = onCollect,
                        compact = compact,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FbsAssemblySecondaryActionButton(
                            text = "Печать",
                            icon = Icons.Outlined.Print,
                            onClick = onPrintLabel,
                            compact = compact,
                            modifier = Modifier.weight(1f)
                        )
                        FbsAssemblySecondaryActionButton(
                            text = "Пропустить",
                            icon = Icons.Outlined.Archive,
                            onClick = onSkip,
                            compact = compact,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FbsAssemblyFinishContent(
    state: FbsAssemblyUiState,
    onRestart: () -> Unit,
    onReload: () -> Unit,
    modifier: Modifier = Modifier
) {
    val finishedInTime = fbsAssemblyFinishedInTime(state.finishedAtMillis, state.settings.deadlineTime)
    val metrics = remember(state.orders) { calculateFbsAssemblyOrderMetrics(state.orders) }
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 18.dp)
    ) {
        item {
            FbsAssemblyDarkCard(Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp)) {
                Column(
                    Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(FbsNeonGreen.copy(alpha = 0.14f))
                            .border(2.dp, FbsNeonGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = FbsNeonGreen, modifier = Modifier.size(42.dp))
                    }
                    Text(
                        "Сборка завершена",
                        color = FbsDarkText,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 19.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(FbsNeonGreen.copy(alpha = 0.12f))
                            .border(1.dp, FbsNeonGreen.copy(alpha = 0.55f), RoundedCornerShape(999.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            if (finishedInTime) "Завершено вовремя" else "Завершено с опозданием",
                            color = FbsNeonGreen,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                    }
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(FbsDarkPanelAlt.copy(alpha = 0.74f))
                            .border(1.dp, FbsDarkBorder, RoundedCornerShape(18.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FbsAssemblyDarkInfoLine("Всего заказов", metrics.totalOrders.toString())
                        HorizontalDivider(color = FbsDarkBorder.copy(alpha = 0.78f))
                        FbsAssemblyDarkInfoLine("Собрано", metrics.collectedOrders.toString())
                        HorizontalDivider(color = FbsDarkBorder.copy(alpha = 0.78f))
                        FbsAssemblyDarkInfoLine("Канистр", metrics.canisterCount.toString())
                        HorizontalDivider(color = FbsDarkBorder.copy(alpha = 0.78f))
                        FbsAssemblyDarkInfoLine("Время начала", formatFbsAssemblyTime(state.startedAtMillis))
                        HorizontalDivider(color = FbsDarkBorder.copy(alpha = 0.78f))
                        FbsAssemblyDarkInfoLine("Время окончания", formatFbsAssemblyTime(state.finishedAtMillis))
                        HorizontalDivider(color = FbsDarkBorder.copy(alpha = 0.78f))
                        FbsAssemblyDarkInfoLine("Общее время сборки", formatFbsAssemblyDuration(state.startedAtMillis, state.finishedAtMillis))
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FbsAssemblyLargeActionButton("Начать заново", icon = Icons.Outlined.Edit, enabled = true, onClick = onRestart, compact = true, modifier = Modifier.weight(1f).height(58.dp))
                        FbsAssemblyOutlinedLargeActionButton("Обновить заказы", icon = Icons.Outlined.FileDownload, enabled = true, onClick = onReload, compact = true, modifier = Modifier.weight(1f).height(58.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun FbsAssemblyForecastCard(
    state: FbsAssemblyUiState,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    dense: Boolean = false,
    nowOverride: LocalDateTime? = null
) {
    val tickingNow = rememberCurrentDateTime(
        refreshIntervalMillis = 30_000,
        enabled = nowOverride == null
    )
    val now = nowOverride ?: tickingNow
    val forecastOrders = fbsAssemblyForecastSourceOrders(state)
    val metrics = remember(forecastOrders) { calculateFbsAssemblyOrderMetrics(forecastOrders) }
    val stats = remember(state.history, state.settings, now.toLocalDate()) {
        calculateFbsAssemblyHistoryStats(state.history, state.settings)
    }
    val averageSeconds = remember(state.history, state.startedAtMillis, stats) {
        fbsAssemblyEffectivePaceSeconds(state, stats)
    }
    val ordersToAssemble = if (state.isStarted) metrics.remainingOrders else metrics.totalOrders
    val forecast = calculateFbsAssemblyForecast(
        ordersToAssemble = ordersToAssemble,
        averageSecondsPerOrder = averageSeconds,
        settings = state.settings,
        now = now
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(if (compact) 24.dp else 28.dp),
                ambientColor = Color.Black.copy(alpha = 0.58f),
                spotColor = FbsNeonGreen.copy(alpha = 0.10f)
            ),
        shape = RoundedCornerShape(if (compact) 24.dp else 28.dp),
        colors = CardDefaults.cardColors(containerColor = FbsDarkPanel.copy(alpha = 0.98f)),
        border = BorderStroke(1.dp, FbsDarkBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            Modifier.padding(horizontal = if (compact) 12.dp else 18.dp, vertical = if (compact) 12.dp else 18.dp),
            verticalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 12.dp)) {
                Box(
                    Modifier
                        .size(if (compact) 34.dp else 44.dp)
                        .clip(CircleShape)
                        .background(FbsNeonGreen.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (forecast.risk == FbsAssemblyRisk.ENOUGH) Icons.Outlined.AccessTime else Icons.Outlined.WarningAmber,
                        contentDescription = null,
                        tint = FbsNeonGreen,
                        modifier = Modifier.size(if (compact) 22.dp else 28.dp)
                    )
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text(
                        if (state.isStarted) "Прогноз завершения" else "Прогноз сборки",
                        color = FbsDarkText,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = if (compact) 14.sp else 16.sp,
                        lineHeight = if (compact) 17.sp else 19.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!dense) {
                        Text(
                            if (stats.usesDefaultAverage) {
                                "Истории мало: ${stats.collectedCount}/${state.settings.minOrdersForAnalytics}, берём базовое время"
                            } else {
                                "Среднее время рассчитано по истории сборки"
                            },
                            color = FbsDarkMutedText,
                            fontSize = if (compact) 10.sp else 11.sp,
                            lineHeight = if (compact) 13.sp else 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Box(
                    Modifier
                        .clip(RoundedCornerShape(if (compact) 13.dp else 16.dp))
                        .background(FbsNeonGreen.copy(alpha = 0.10f))
                        .border(1.dp, FbsNeonGreen.copy(alpha = 0.80f), RoundedCornerShape(if (compact) 13.dp else 16.dp))
                        .padding(horizontal = if (compact) 9.dp else 12.dp, vertical = if (compact) 6.dp else 8.dp)
                ) {
                    Text(
                        state.settings.deadlineTime,
                        color = FbsNeonGreen,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = if (compact) 12.sp else 13.sp,
                        lineHeight = if (compact) 15.sp else 16.sp,
                        maxLines = 1
                    )
                }
            }

            FbsAssemblyMetricGrid(
                firstTitle = if (state.isStarted) "Осталось" else "К сборке",
                firstValue = forecast.ordersToAssemble.toString(),
                secondTitle = "На заказ",
                secondValue = formatFbsAssemblyShortDuration(averageSeconds),
                thirdTitle = "Запас",
                thirdValue = formatFbsAssemblyShortDuration(forecast.reserveSeconds),
                compact = compact || dense,
                dark = true
            )

            if (!dense) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(if (compact) 15.dp else 18.dp))
                        .background(FbsDarkPanelAlt.copy(alpha = 0.72f))
                        .border(1.dp, FbsDarkBorder, RoundedCornerShape(if (compact) 15.dp else 18.dp))
                        .padding(horizontal = if (compact) 10.dp else 14.dp, vertical = if (compact) 9.dp else 12.dp),
                    verticalArrangement = Arrangement.spacedBy(if (compact) 7.dp else 10.dp)
                ) {
                    FbsAssemblyDarkInfoLine("Примерно без запаса", formatFbsAssemblyShortDuration(forecast.forecastSeconds), compact)
                    HorizontalDivider(color = FbsDarkBorder.copy(alpha = 0.78f))
                    FbsAssemblyDarkInfoLine("С учётом запаса", formatFbsAssemblyShortDuration(forecast.totalSeconds), compact)
                    HorizontalDivider(color = FbsDarkBorder.copy(alpha = 0.78f))
                    if (state.isStarted) {
                        FbsAssemblyDarkInfoLine("Прогноз завершения", formatFbsAssemblyLocalTime(forecast.projectedFinish), compact)
                    } else {
                        FbsAssemblyDarkInfoLine("Начать не позже", formatFbsAssemblyLocalTime(forecast.recommendedStart), compact)
                    }
                }

                FbsAssemblyForecastNotice(forecast = forecast, isStarted = state.isStarted, compact = compact)
            }
        }
    }
}

@Composable
private fun FbsAssemblyForecastNotice(forecast: FbsAssemblyForecast, isStarted: Boolean, compact: Boolean = false) {
    val color = FbsNeonGreen
    val text = when (forecast.risk) {
        FbsAssemblyRisk.ENOUGH -> if (forecast.ordersToAssemble == 0) {
            "Сейчас нет заказов к сборке. Список обновляется автоматически."
        } else if (isStarted) {
            "Времени достаточно. Прогноз завершения: ${formatFbsAssemblyLocalTime(forecast.projectedFinish)}."
        } else {
            "Времени достаточно. Начать не позже ${formatFbsAssemblyLocalTime(forecast.recommendedStart)}."
        }
        FbsAssemblyRisk.START_SOON ->
            "Начните сейчас, чтобы успеть до ${formatFbsAssemblyLocalTime(forecast.deadline)}."
        FbsAssemblyRisk.LATE ->
            "Есть риск опоздать к ${formatFbsAssemblyLocalTime(forecast.deadline)}. Начните немедленно."
        FbsAssemblyRisk.NOT_ENOUGH_TIME ->
            "Риск дедлайна: осталось ${formatFbsAssemblyShortDuration(forecast.secondsUntilDeadline.coerceAtLeast(0L))}, " +
                "нужно ${formatFbsAssemblyShortDuration(forecast.totalSeconds)}, " +
                "не хватает ${formatFbsAssemblyShortDuration(forecast.missingSeconds)}."
    }
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(if (compact) 16.dp else 20.dp))
            .background(color.copy(alpha = 0.11f))
            .border(1.dp, color.copy(alpha = 0.55f), RoundedCornerShape(if (compact) 16.dp else 20.dp))
            .padding(horizontal = if (compact) 10.dp else 14.dp, vertical = if (compact) 9.dp else 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 12.dp)) {
            Box(
                Modifier
                    .size(if (compact) 28.dp else 36.dp)
                    .clip(CircleShape)
                    .border(2.dp, color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = color, modifier = Modifier.size(if (compact) 18.dp else 23.dp))
            }
            Text(
                text,
                color = color,
                fontWeight = FontWeight.ExtraBold,
                fontSize = if (compact) 11.sp else 12.sp,
                lineHeight = if (compact) 14.sp else 15.sp,
                maxLines = if (compact) 3 else 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun FbsAssemblyDarkInfoLine(title: String, value: String, compact: Boolean = false) {
    val displayValue = keepFbsAssemblyDurationOnOneLine(value)
    val durationValue = displayValue != value
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            color = FbsDarkMutedText,
            fontWeight = FontWeight.ExtraBold,
            fontSize = if (compact) 11.sp else 12.sp,
            lineHeight = if (compact) 14.sp else 15.sp,
            modifier = Modifier.weight(1f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            displayValue,
            color = FbsDarkText,
            fontWeight = FontWeight.ExtraBold,
            fontSize = if (compact) 11.sp else 12.sp,
            lineHeight = if (compact) 14.sp else 15.sp,
            maxLines = if (durationValue) 1 else 2,
            softWrap = !durationValue,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FbsAssemblyAnalyticsContent(
    state: FbsAssemblyUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val planningError = state.planningError
    if (planningError != null) {
        Column(modifier.fillMaxWidth()) {
            FbsAssemblyErrorCard(message = planningError, onRetry = onRefresh)
        }
        return
    }

    val now = rememberCurrentDateTime(refreshIntervalMillis = 30_000)
    val forecastOrders = fbsAssemblyForecastSourceOrders(state)
    val metrics = remember(forecastOrders) { calculateFbsAssemblyOrderMetrics(forecastOrders) }
    val stats = remember(state.history, state.settings, now.toLocalDate()) {
        calculateFbsAssemblyHistoryStats(state.history, state.settings)
    }
    val averageSeconds = remember(state.history, state.startedAtMillis, stats) {
        fbsAssemblyEffectivePaceSeconds(state, stats)
    }
    val forecast = calculateFbsAssemblyForecast(
        ordersToAssemble = if (state.isStarted) metrics.remainingOrders else metrics.totalOrders,
        averageSecondsPerOrder = averageSeconds,
        settings = state.settings,
        now = now
    )

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 18.dp)
    ) {
        item {
            FbsAssemblyDarkCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        AppIconBubble(Icons.Outlined.Analytics, tint = FbsNeonGreen, background = FbsNeonGreen.copy(alpha = 0.12f), modifier = Modifier.size(48.dp))
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Аналитика сборки", color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("Только заказы со статусом «Собран» влияют на среднее время", color = FbsDarkMutedText, fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        }
                    }
                    FbsAssemblyDarkInfoLine("Сегодня собрано", "${stats.collectedToday} заказов")
                    FbsAssemblyDarkInfoLine("Среднее время на заказ", formatFbsAssemblyShortDuration(stats.averageSecondsPerOrder))
                    FbsAssemblyDarkInfoLine("Общее время сегодня", formatFbsAssemblyShortDuration(stats.totalSecondsToday))
                    FbsAssemblyDarkInfoLine("Самый быстрый заказ", formatFbsAssemblyShortDuration(stats.fastestSeconds))
                    FbsAssemblyDarkInfoLine("Самый долгий заказ", formatFbsAssemblyShortDuration(stats.slowestSeconds))
                    FbsAssemblyDarkInfoLine("Проблемных/пропущенных", stats.problemOrders.toString())
                    FbsAssemblyDarkInfoLine("Осталось собрать", "${forecast.ordersToAssemble} заказов")
                    FbsAssemblyDarkInfoLine("Примерное время", formatFbsAssemblyShortDuration(forecast.totalSeconds))
                    FbsAssemblyDarkInfoLine("Начать не позже", formatFbsAssemblyLocalTime(forecast.recommendedStart))
                }
            }
        }
        item {
            FbsAssemblyForecastCard(state, nowOverride = now)
        }
    }
}

@Composable
private fun FbsAssemblyHistoryContent(state: FbsAssemblyUiState, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 18.dp)
    ) {
        if (state.history.isEmpty()) {
            item {
                FbsAssemblyEmptyCard(
                    title = "История пока пустая",
                    text = "Начните сборку и нажмите «Заказ собран» — приложение сохранит время каждого заказа локально."
                )
            }
        } else {
            items(state.history, key = { "${it.id}-${it.createdAt}-${it.orderNumber}" }) { entry ->
                FbsAssemblyHistoryCard(entry)
            }
        }
    }
}

@Composable
private fun FbsAssemblyHistoryCard(entry: FbsAssemblyHistoryEntry) {
    FbsAssemblyDarkCard(Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            FbsAssemblyProductPhoto(
                imageUrl = entry.productImageUrl,
                status = entry.status,
                modifier = Modifier.size(width = 58.dp, height = 74.dp)
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            "Заказ №${entry.orderNumber}",
                            color = FbsDarkText,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            lineHeight = 17.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            entry.productName,
                            color = FbsDarkText,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            lineHeight = 15.sp,
                            maxLines = 6,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    FbsAssemblyStatusBadge(entry.status)
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(FbsDarkPanelAlt.copy(alpha = 0.72f))
                        .border(1.dp, FbsDarkBorder, RoundedCornerShape(14.dp))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FbsAssemblyDarkInfoLine("Количество", "${entry.quantity} шт.", compact = true)
                    FbsAssemblyDarkInfoLine("Дата", formatFbsAssemblyDate(entry.createdAt), compact = true)
                    FbsAssemblyDarkInfoLine("Начало", formatFbsAssemblyTime(entry.startedAt), compact = true)
                    FbsAssemblyDarkInfoLine("Завершение", formatFbsAssemblyTime(entry.finishedAt), compact = true)
                    FbsAssemblyDarkInfoLine("Время сборки", formatFbsAssemblyShortDuration(entry.durationSeconds), compact = true)
                    entry.problemReason?.takeIf { it.isNotBlank() }?.let { reason ->
                        FbsAssemblyDarkInfoLine("Причина", reason, compact = true)
                    }
                }
            }
        }
    }
}

@Composable
private fun FbsAssemblySettingsContent(
    settings: AssemblyForecastSettings,
    notificationStatus: String,
    onSendTestNotification: () -> Unit,
    onOpenNotificationSettings: () -> Unit,
    labelServerHost: String,
    onLabelServerHostChange: (String) -> Unit,
    onPrintLabelTest: () -> Unit,
    usbDirectPrintEnabled: Boolean,
    usbRawPdfEnabled: Boolean,
    usbPrinterStatus: String,
    usbPrintOffsetMm: String,
    onUsbDirectPrintEnabledChange: (Boolean) -> Unit,
    onUsbRawPdfEnabledChange: (Boolean) -> Unit,
    onUsbPrintOffsetMmChange: (String) -> Unit,
    onSaveUsbPrintOffset: () -> Unit,
    onResetUsbPrintOffset: () -> Unit,
    onPrintUsbTest: () -> Unit,
    onRefreshUsbStatus: () -> Unit,
    onSave: (AssemblyForecastSettings) -> Unit,
    onResetData: () -> Unit,
    modifier: Modifier = Modifier
) {
    var deadline by rememberSaveable { mutableStateOf(settings.deadlineTime) }
    var reservePercent by rememberSaveable { mutableStateOf(settings.reservePercent.toString()) }
    var defaultMinutes by rememberSaveable { mutableStateOf((settings.defaultSecondsPerOrder / 60).coerceAtLeast(1).toString()) }
    var minOrders by rememberSaveable { mutableStateOf(settings.minOrdersForAnalytics.toString()) }

    LaunchedEffect(settings) {
        deadline = settings.deadlineTime
        reservePercent = settings.reservePercent.toString()
        defaultMinutes = (settings.defaultSecondsPerOrder / 60).coerceAtLeast(1).toString()
        minOrders = settings.minOrdersForAnalytics.toString()
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 18.dp)
    ) {
        item {
            FbsAssemblyDarkCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        AppIconBubble(Icons.Outlined.Settings, tint = FbsNeonGreen, background = FbsNeonGreen.copy(alpha = 0.12f), modifier = Modifier.size(48.dp))
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("Настройки", color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("Прогноз, печать и локальные данные FBS", color = FbsDarkMutedText, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                    FbsAssemblyTextField(
                        value = deadline,
                        onValueChange = { deadline = it.take(5) },
                        label = "Время окончания сборки",
                        placeholder = "16:00",
                        leadingIcon = Icons.Outlined.AccessTime,
                        modifier = Modifier.fillMaxWidth()
                    )
                    FbsAssemblyTextField(
                        value = reservePercent,
                        onValueChange = { reservePercent = it.filter(Char::isDigit).take(3) },
                        label = "Запас времени, %",
                        placeholder = "20",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    FbsAssemblyTextField(
                        value = defaultMinutes,
                        onValueChange = { defaultMinutes = it.filter(Char::isDigit).take(4) },
                        label = "Базовое время на заказ, мин",
                        placeholder = "3",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    FbsAssemblyTextField(
                        value = minOrders,
                        onValueChange = { minOrders = it.filter(Char::isDigit).take(4) },
                        label = "Минимум заказов для аналитики",
                        placeholder = "10",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    FbsAssemblyOutlinedLargeActionButton(
                        text = "Сохранить настройки",
                        icon = Icons.Outlined.CheckCircle,
                        enabled = true,
                        onClick = {
                            onSave(
                                AssemblyForecastSettings(
                                    deadlineTime = deadline,
                                    reservePercent = reservePercent.toIntOrNull() ?: 20,
                                    defaultSecondsPerOrder = (defaultMinutes.toIntOrNull() ?: 3) * 60,
                                    minOrdersForAnalytics = minOrders.toIntOrNull() ?: 10
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(54.dp)
                    )
                    HorizontalDivider(color = FbsDarkBorder.copy(alpha = 0.82f))
                    FbsAssemblyLargeActionButton(
                        text = "Сбросить данные",
                        icon = Icons.Outlined.Delete,
                        enabled = true,
                        onClick = onResetData,
                        compact = true,
                        modifier = Modifier.fillMaxWidth().height(54.dp)
                    )
                }
            }
        }
        item {
            FbsAssemblyDarkCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        AppIconBubble(Icons.Outlined.NotificationsActive, tint = FbsNeonGreen, background = FbsNeonGreen.copy(alpha = 0.12f), modifier = Modifier.size(48.dp))
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("Уведомления телефона", color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("Работают и когда экран сборки закрыт", color = FbsDarkMutedText, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                    FbsAssemblyDarkInfoLine("Новый заказ", "проверка примерно каждые 15 минут", compact = true)
                    FbsAssemblyDarkInfoLine("Проверка товара", "за 1,5 часа до ${settings.deadlineTime}", compact = true)
                    FbsAssemblyDarkInfoLine("Статус", notificationStatus, compact = true)
                    Text(
                        "Если статус не «Включены», откройте настройки Android и разрешите уведомления для канала новых заказов.",
                        color = FbsDarkMutedText,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FbsAssemblySecondaryActionButton(
                            text = "Тест",
                            icon = Icons.Outlined.NotificationsActive,
                            onClick = onSendTestNotification,
                            modifier = Modifier.weight(1f)
                        )
                        FbsAssemblySecondaryActionButton(
                            text = "Настройки",
                            icon = Icons.Outlined.Settings,
                            onClick = onOpenNotificationSettings,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
        item {
            FbsAssemblyDarkCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        AppIconBubble(Icons.Outlined.Print, tint = FbsNeonGreen, background = FbsNeonGreen.copy(alpha = 0.12f), modifier = Modifier.size(48.dp))
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("Печать этикеток", color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("Ozon Label Print Server", color = FbsDarkMutedText, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                    FbsAssemblyTextField(
                        value = labelServerHost,
                        onValueChange = onLabelServerHostChange,
                        label = "IP сервера этикеток",
                        placeholder = "192.168.10.107",
                        leadingIcon = Icons.Outlined.Print,
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider(color = FbsDarkBorder.copy(alpha = 0.82f))
                    FbsAssemblyDarkInfoLine("OTG-принтер", usbPrinterStatus, compact = true)
                    FbsAssemblySettingsSwitchRow(
                        title = "Авто OTG-принтер",
                        subtitle = "Если принтер подключён к телефону кабелем, печать сначала идёт на него",
                        checked = usbDirectPrintEnabled,
                        onCheckedChange = onUsbDirectPrintEnabledChange
                    )
                    FbsAssemblySettingsSwitchRow(
                        title = "PDF Ozon через OTG",
                        subtitle = "PDF-этикетки конвертируются в TSPL перед отправкой на USB-принтер",
                        checked = usbRawPdfEnabled,
                        enabled = usbDirectPrintEnabled,
                        onCheckedChange = onUsbRawPdfEnabledChange
                    )
                    FbsAssemblyTextField(
                        value = usbPrintOffsetMm,
                        onValueChange = onUsbPrintOffsetMmChange,
                        label = "Смещение печати, мм",
                        placeholder = "5.0",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        leadingIcon = Icons.Outlined.Settings,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FbsAssemblySecondaryActionButton(
                            text = "Сохранить смещение",
                            icon = Icons.Outlined.CheckCircle,
                            enabled = usbDirectPrintEnabled,
                            onClick = onSaveUsbPrintOffset,
                            modifier = Modifier.weight(1.25f)
                        )
                        FbsAssemblySecondaryActionButton(
                            text = "5.0 мм",
                            icon = Icons.Outlined.Close,
                            enabled = usbDirectPrintEnabled,
                            onClick = onResetUsbPrintOffset,
                            modifier = Modifier.weight(0.75f)
                        )
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FbsAssemblySecondaryActionButton(
                            text = "Обновить OTG",
                            icon = Icons.Outlined.Search,
                            onClick = onRefreshUsbStatus,
                            modifier = Modifier.weight(1f)
                        )
                        FbsAssemblySecondaryActionButton(
                            text = "Тест OTG",
                            icon = Icons.Outlined.Print,
                            enabled = usbDirectPrintEnabled,
                            onClick = onPrintUsbTest,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    FbsAssemblySecondaryActionButton(
                        text = "Тест печати",
                        icon = Icons.Outlined.Print,
                        onClick = onPrintLabelTest,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun FbsAssemblySettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.5f)
            .clip(RoundedCornerShape(16.dp))
            .background(FbsDarkPanelAlt.copy(alpha = 0.74f))
            .border(1.dp, FbsDarkBorder, RoundedCornerShape(16.dp))
            .clickable(enabled = enabled) { onCheckedChange(!checked) }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, color = FbsDarkText, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(subtitle, color = FbsDarkMutedText, fontSize = 11.sp, lineHeight = 14.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
        Switch(
            checked = checked,
            enabled = enabled,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun FbsAssemblySummaryCard(
    state: FbsAssemblyUiState,
    compact: Boolean = false,
    dense: Boolean = false
) {
    val metrics = remember(state.orders) { calculateFbsAssemblyOrderMetrics(state.orders) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(if (compact) 24.dp else 28.dp),
                ambientColor = Color.Black.copy(alpha = 0.55f),
                spotColor = FbsNeonGreen.copy(alpha = 0.08f)
            ),
        shape = RoundedCornerShape(if (compact) 24.dp else 28.dp),
        colors = CardDefaults.cardColors(containerColor = FbsDarkPanel.copy(alpha = 0.98f)),
        border = BorderStroke(1.dp, FbsDarkBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            Modifier.padding(horizontal = if (compact) 14.dp else 20.dp, vertical = if (compact) 14.dp else 20.dp),
            verticalArrangement = Arrangement.spacedBy(if (compact) 11.dp else 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(if (compact) 11.dp else 16.dp)) {
                Box(
                    Modifier
                        .size(if (compact) 38.dp else 48.dp)
                        .clip(CircleShape)
                        .background(FbsNeonGreen.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Inventory2, contentDescription = null, tint = FbsNeonGreen, modifier = Modifier.size(if (compact) 25.dp else 32.dp))
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(if (compact) 3.dp else 6.dp)) {
                    Text(
                        "Заказы к сборке",
                        color = FbsDarkText,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = if (compact) 17.sp else 19.sp,
                        lineHeight = if (compact) 20.sp else 23.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        if (state.orders.isEmpty()) "Нет заказов для сборки" else "${metrics.totalOrders} заказов · ${metrics.totalItems} шт. · ${metrics.canisterCount} канистр",
                        color = FbsDarkMutedText,
                        fontSize = if (compact) 12.sp else 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = if (compact) 1 else 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Box(
                    Modifier
                        .clip(RoundedCornerShape(if (compact) 13.dp else 16.dp))
                        .background(FbsDarkPanelAlt)
                        .border(1.dp, FbsDarkBorder, RoundedCornerShape(if (compact) 13.dp else 16.dp))
                        .padding(horizontal = if (compact) 14.dp else 18.dp, vertical = if (compact) 9.dp else 12.dp)
                ) {
                    Text(
                        metrics.totalOrders.toString(),
                        color = FbsDarkText,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = if (compact) 16.sp else 17.sp,
                        lineHeight = if (compact) 19.sp else 20.sp
                    )
                }
            }
            if (!dense) {
                HorizontalDivider(color = FbsDarkBorder.copy(alpha = 0.78f))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                    FbsAssemblySummaryMetricCell("Заказов", metrics.totalOrders.toString(), Modifier.weight(1f), compact)
                    FbsAssemblyVerticalDivider()
                    FbsAssemblySummaryMetricCell("Товаров", metrics.totalItems.toString(), Modifier.weight(1f), compact)
                    FbsAssemblyVerticalDivider()
                    FbsAssemblySummaryMetricCell("Канистр", metrics.canisterCount.toString(), Modifier.weight(1f), compact)
                }
            }
        }
    }
}

@Composable
private fun FbsAssemblySummaryMetricCell(title: String, value: String, modifier: Modifier = Modifier, compact: Boolean = false) {
    Column(
        modifier.padding(vertical = if (compact) 3.dp else 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 6.dp)
    ) {
        Text(
            title,
            color = FbsDarkMutedText,
            fontSize = if (compact) 11.sp else 12.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            value,
            color = FbsDarkText,
            fontSize = if (compact) 18.sp else 21.sp,
            lineHeight = if (compact) 21.sp else 24.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1
        )
    }
}

@Composable
private fun FbsAssemblyVerticalDivider() {
    Box(
        Modifier
            .width(1.dp)
            .height(52.dp)
            .background(FbsDarkBorder)
    )
}

@Composable
private fun FbsAssemblyAutoRefreshNotice(state: FbsAssemblyUiState, compact: Boolean = false) {
    val lastUpdate = state.lastAutoRefreshAtMillis ?: state.lastUpdatedAtMillis
    val detail = when {
        state.lastRefreshNewOrders > 0 ->
            "Добавлено новых заказов: ${state.lastRefreshNewOrders}. Они поставлены в конец очереди."
        lastUpdate != null ->
            "Последняя проверка: ${formatFbsAssemblyTime(lastUpdate)}. Новые заказы добавятся в конец очереди."
        else ->
            "Новые заказы будут добавляться в конец текущей очереди без сброса прогресса."
    }
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(if (compact) 14.dp else 18.dp))
            .background(FbsNeonGreen.copy(alpha = 0.10f))
            .border(1.dp, FbsNeonGreen.copy(alpha = 0.34f), RoundedCornerShape(if (compact) 14.dp else 18.dp))
            .padding(horizontal = 12.dp, vertical = if (compact) 7.dp else 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Icon(
            Icons.Outlined.Refresh,
            contentDescription = null,
            tint = FbsNeonGreen,
            modifier = Modifier.size(if (compact) 18.dp else 21.dp)
        )
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text(
                "Автообновление включено · каждые ${FBS_ASSEMBLY_AUTO_REFRESH_INTERVAL_MILLIS / 1_000} сек",
                color = FbsNeonGreen,
                fontWeight = FontWeight.Bold,
                fontSize = if (compact) 11.sp else 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!compact) {
                Text(
                    detail,
                    color = FbsDarkMutedText,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun FbsAssemblyProgressPanel(
    state: FbsAssemblyUiState,
    now: LocalDateTime,
    compact: Boolean = false,
    prominent: Boolean = false
) {
    val metrics = remember(state.orders) { calculateFbsAssemblyOrderMetrics(state.orders) }
    val progressPercent = (metrics.progress * 100f).toInt()
    val timer = fbsAssemblyTimer(now, state.settings.deadlineTime)
    FbsAssemblyDarkCard(Modifier.fillMaxWidth(), shape = RoundedCornerShape(if (compact) 18.dp else 22.dp)) {
        Column(
            Modifier.padding(horizontal = if (prominent) 14.dp else 12.dp, vertical = if (compact) 8.dp else 10.dp),
            verticalArrangement = Arrangement.spacedBy(if (compact) 6.dp else 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        "Собрано ${metrics.collectedOrders} из ${metrics.totalOrders}",
                        color = FbsDarkText,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = when {
                            prominent && compact -> 18.sp
                            prominent -> 20.sp
                            compact -> 15.sp
                            else -> 17.sp
                        },
                        lineHeight = when {
                            prominent && compact -> 21.sp
                            prominent -> 23.sp
                            compact -> 18.sp
                            else -> 20.sp
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        timer.text,
                        color = if (timer.isLate) DangerColor else FbsNeonGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (prominent) 13.sp else 12.sp,
                        lineHeight = if (prominent) 16.sp else 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Box(
                    Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(FbsNeonGreen.copy(alpha = 0.12f))
                        .border(1.dp, FbsNeonGreen.copy(alpha = 0.55f), RoundedCornerShape(999.dp))
                        .padding(horizontal = if (prominent) 12.dp else 10.dp, vertical = if (prominent) 7.dp else 5.dp)
                ) {
                    Text("$progressPercent%", color = FbsNeonGreen, fontWeight = FontWeight.ExtraBold, fontSize = if (prominent) 15.sp else 13.sp, maxLines = 1)
                }
            }
            PreAssemblyProgressBar(progress = metrics.progress, color = FbsNeonGreen)
            if (!compact) {
                FbsAssemblyMetricGrid(
                    firstTitle = "Собрано",
                    firstValue = metrics.collectedOrders.toString(),
                    secondTitle = "Осталось",
                    secondValue = metrics.remainingOrders.toString(),
                    thirdTitle = "Канистр",
                    thirdValue = metrics.canisterCount.toString(),
                    compact = true,
                    dark = true
                )
            }
        }
    }
}

@Composable
private fun FbsAssemblyMetricGrid(
    firstTitle: String,
    firstValue: String,
    secondTitle: String,
    secondValue: String,
    thirdTitle: String,
    thirdValue: String,
    compact: Boolean = false,
    dark: Boolean = false
) {
    val metrics = listOf(
        Triple(firstTitle, firstValue, AccentColor),
        Triple(secondTitle, secondValue, SuccessColor),
        Triple(thirdTitle, thirdValue, WarningColor)
    )
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        metrics.forEach { (title, value, color) ->
            FbsAssemblyMetricCell(title, value, color, compact, dark, Modifier.weight(1f))
        }
    }
}

@Composable
private fun FbsAssemblyMetricCell(
    title: String,
    value: String,
    color: Color,
    compact: Boolean,
    dark: Boolean,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(if (compact) 15.dp else 18.dp)
    val displayValue = keepFbsAssemblyDurationOnOneLine(value)
    Column(
        modifier
            .clip(shape)
            .background(if (dark) FbsDarkPanelAlt.copy(alpha = 0.72f) else color.copy(alpha = 0.09f))
            .border(1.dp, if (dark) FbsDarkBorder else color.copy(alpha = 0.18f), shape)
            .padding(horizontal = if (compact) 7.dp else 10.dp, vertical = if (compact) 8.dp else 11.dp),
        verticalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 6.dp)
    ) {
        Text(
            title,
            color = if (dark) FbsDarkMutedText else MutedTextColor,
            fontSize = if (compact) 11.sp else 13.sp,
            lineHeight = if (compact) 13.sp else 15.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            displayValue,
            color = if (dark) FbsNeonGreen else MainTextColor,
            fontWeight = FontWeight.ExtraBold,
            fontSize = if (dark) {
                if (compact) 12.sp else 14.sp
            } else {
                if (compact) 17.sp else 20.sp
            },
            lineHeight = if (dark) {
                if (compact) 14.sp else 16.sp
            } else {
                if (compact) 19.sp else 22.sp
            },
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FbsAssemblyActiveOrderCard(
    order: FbsAssemblyOrder,
    compact: Boolean,
    dense: Boolean,
    showMultiPositionWarning: Boolean,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(if (compact) 22.dp else 26.dp)
    FbsAssemblyDarkCard(
        modifier
            .fillMaxWidth()
            .border(1.2.dp, fbsAssemblyStatusColor(order.status).copy(alpha = 0.58f), shape),
        shape = shape
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = if (dense) 12.dp else 15.dp, vertical = if (dense) 11.dp else 14.dp),
            verticalArrangement = Arrangement.spacedBy(if (dense) 9.dp else 12.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FbsAssemblyFocusedOrderNumber(
                    orderNumber = order.orderNumber,
                    compact = compact,
                    modifier = Modifier.weight(1f)
                )
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FbsAssemblyWorkChip(
                        text = order.status.title,
                        color = fbsAssemblyStatusColor(order.status)
                    )
                    FbsAssemblyWorkChip(
                        text = "${order.totalQuantity} шт.",
                        color = FbsNeonGreen
                    )
                }
            }
            if (showMultiPositionWarning) {
                FbsAssemblyMultiPositionWarning(
                    positionCount = order.distinctPositionCount,
                    totalQuantity = order.totalQuantity,
                    compact = dense
                )
            }
            HorizontalDivider(color = FbsDarkBorder.copy(alpha = 0.82f))
            if (order.items.isEmpty()) {
                Box(
                    Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "В заказе нет товарных строк",
                        color = FbsDarkMutedText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else if (order.items.size == 1) {
                order.items.first().let { item ->
                    FbsAssemblyActiveItemCard(
                        item = item,
                        status = order.status,
                        compact = compact,
                        dense = dense,
                        modifier = Modifier.weight(1f).fillMaxWidth()
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(if (dense) 8.dp else 10.dp),
                    contentPadding = PaddingValues(bottom = 2.dp)
                ) {
                    items(order.items) { item ->
                        FbsAssemblyActiveItemCard(
                            item = item,
                            status = order.status,
                            compact = compact,
                            dense = dense,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = if (dense) 218.dp else 260.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FbsAssemblyMultiPositionWarning(
    positionCount: Int,
    totalQuantity: Int,
    compact: Boolean,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(if (compact) 15.dp else 18.dp)
    val positionText = "$positionCount ${fbsAssemblyPositionWord(positionCount)}"
    Row(
        modifier
            .fillMaxWidth()
            .clip(shape)
            .background(WarningColor.copy(alpha = 0.14f))
            .border(1.2.dp, WarningColor.copy(alpha = 0.54f), shape)
            .padding(horizontal = if (compact) 10.dp else 12.dp, vertical = if (compact) 8.dp else 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(if (compact) 9.dp else 11.dp)
    ) {
        Box(
            Modifier
                .size(if (compact) 30.dp else 36.dp)
                .clip(CircleShape)
                .background(WarningColor.copy(alpha = 0.18f))
                .border(1.dp, WarningColor.copy(alpha = 0.62f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Outlined.WarningAmber,
                contentDescription = null,
                tint = WarningColor,
                modifier = Modifier.size(if (compact) 19.dp else 23.dp)
            )
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(if (compact) 1.dp else 2.dp)) {
            Text(
                "Несколько разных позиций",
                color = WarningColor,
                fontWeight = FontWeight.ExtraBold,
                fontSize = if (compact) 12.sp else 13.sp,
                lineHeight = if (compact) 14.sp else 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "В заказе $positionText, всего $totalQuantity шт. Проверьте каждую позицию.",
                color = FbsDarkText,
                fontWeight = FontWeight.SemiBold,
                fontSize = if (compact) 11.sp else 12.sp,
                lineHeight = if (compact) 13.sp else 15.sp,
                maxLines = if (compact) 1 else 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun FbsAssemblyFocusedOrderNumber(
    orderNumber: String,
    compact: Boolean,
    modifier: Modifier = Modifier
) {
    val parts = fbsAssemblyOrderNumberParts(orderNumber)
    Column(modifier, verticalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 6.dp)) {
        Text(
            "Текущий заказ",
            color = FbsDarkMutedText,
            fontWeight = FontWeight.ExtraBold,
            fontSize = if (compact) 12.sp else 13.sp,
            lineHeight = if (compact) 14.sp else 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(
                parts.prefix,
                color = FbsDarkText,
                fontWeight = FontWeight.ExtraBold,
                fontSize = if (compact) 23.sp else 27.sp,
                lineHeight = if (compact) 26.sp else 30.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(if (compact) 12.dp else 14.dp))
                    .background(FbsNeonGreen)
                    .padding(horizontal = if (compact) 10.dp else 12.dp, vertical = if (compact) 5.dp else 6.dp)
            ) {
                Text(
                    parts.highlight,
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = if (compact) 24.sp else 28.sp,
                    lineHeight = if (compact) 27.sp else 31.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (parts.suffix.isNotBlank()) {
            Text(
                parts.suffix,
                color = FbsDarkMutedText,
                fontWeight = FontWeight.Bold,
                fontSize = if (compact) 13.sp else 14.sp,
                lineHeight = if (compact) 15.sp else 17.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun FbsAssemblyWorkChip(text: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier
            .clip(RoundedCornerShape(999.dp))
            .background(color.copy(alpha = 0.14f))
            .border(1.dp, color.copy(alpha = 0.42f), RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text,
            color = color,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            lineHeight = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FbsAssemblyActiveItemCard(
    item: FbsAssemblyItem,
    status: FbsAssemblyOrderStatus,
    compact: Boolean,
    dense: Boolean,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(if (compact) 19.dp else 22.dp)
    val hasExtraMeta = !item.article.isNullOrBlank() || item.apiQuantity != item.calculatedQuantity || item.isCanister
    val nameMaxLines = when {
        dense && hasExtraMeta -> 4
        dense -> 5
        compact && hasExtraMeta -> 5
        else -> 6
    }
    BoxWithConstraints(
        modifier
            .clip(shape)
            .background(FbsDarkPanelAlt.copy(alpha = 0.76f))
            .border(1.dp, FbsDarkBorder, shape)
            .padding(if (dense) 10.dp else 12.dp)
    ) {
        val narrow = maxWidth < 310.dp
        val photoWidth = when {
            dense || narrow -> 68.dp
            compact -> 78.dp
            else -> 92.dp
        }
        val photoHeight = when {
            dense || narrow -> 90.dp
            compact -> 106.dp
            else -> 124.dp
        }
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(if (dense) 8.dp else 11.dp)
        ) {
            Row(
                Modifier.weight(1f).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(if (dense) 10.dp else 12.dp),
                verticalAlignment = Alignment.Top
            ) {
                FbsAssemblyProductPhoto(
                    imageUrl = item.imageUrl,
                    status = status,
                    decorated = true,
                    modifier = Modifier.size(width = photoWidth, height = photoHeight)
                )
                Column(
                    Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(if (dense) 6.dp else 8.dp)
                ) {
                    Text(
                        item.name,
                        color = FbsDarkText,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = if (dense) 16.sp else 18.sp,
                        lineHeight = if (dense) 19.sp else 22.sp,
                        maxLines = nameMaxLines,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!item.article.isNullOrBlank()) {
                        Text(
                            "Арт. ${item.article}",
                            color = FbsDarkMutedText,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = if (dense) 12.sp else 13.sp,
                            lineHeight = if (dense) 15.sp else 16.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (item.apiQuantity != item.calculatedQuantity || item.isCanister) {
                        Row(horizontalArrangement = Arrangement.spacedBy(7.dp), verticalAlignment = Alignment.CenterVertically) {
                            if (item.apiQuantity != item.calculatedQuantity) {
                                FbsAssemblyWorkChip("API ${item.apiQuantity}", color = FbsDarkMutedText)
                            }
                            if (item.isCanister) {
                                FbsAssemblyWorkChip("канистра", color = WarningColor)
                            }
                        }
                    }
                }
            }
            FbsAssemblyQuantityBanner(
                quantity = item.calculatedQuantity,
                isCanister = item.isCanister,
                dense = dense,
                modifier = Modifier.fillMaxWidth().height(if (dense) 84.dp else 102.dp)
            )
        }
    }
}

@Composable
private fun FbsAssemblyQuantityBanner(
    quantity: Int,
    isCanister: Boolean,
    dense: Boolean,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(if (dense) 18.dp else 20.dp)
    Box(
        modifier
            .clip(shape)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        FbsNeonGreen.copy(alpha = 0.035f),
                        FbsNeonGreen.copy(alpha = 0.055f),
                        FbsNeonGreen.copy(alpha = 0.030f)
                    )
                )
            )
            .border(1.2.dp, FbsNeonGreen.copy(alpha = 0.28f), shape)
            .padding(horizontal = if (dense) 13.dp else 16.dp, vertical = if (dense) 10.dp else 12.dp)
    ) {
        Text(
            "Количество",
            color = FbsNeonGreen,
            fontWeight = FontWeight.ExtraBold,
            fontSize = if (dense) 12.sp else 13.sp,
            lineHeight = if (dense) 14.sp else 16.sp,
            modifier = Modifier.align(Alignment.TopStart)
        )
        Row(
            Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(
                quantity.toString(),
                color = FbsNeonGreen,
                fontWeight = FontWeight.ExtraBold,
                fontSize = if (dense) 38.sp else 46.sp,
                lineHeight = if (dense) 40.sp else 48.sp,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "шт.",
                color = FbsNeonGreen,
                fontWeight = FontWeight.ExtraBold,
                fontSize = if (dense) 18.sp else 21.sp,
                lineHeight = if (dense) 25.sp else 29.sp,
                maxLines = 1
            )
        }
        if (isCanister) {
            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(999.dp))
                    .background(WarningColor.copy(alpha = 0.16f))
                    .padding(horizontal = 9.dp, vertical = 5.dp)
            ) {
                Text("канистра", color = WarningColor, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, maxLines = 1)
            }
        }
    }
}

@Composable
private fun FbsAssemblyOrderCard(
    order: FbsAssemblyOrder,
    large: Boolean,
    modifier: Modifier = Modifier,
    showMultiPositionWarning: Boolean = false
) {
    FbsAssemblyDarkCard(
        modifier
            .fillMaxWidth()
            .heightIn(min = if (large) 430.dp else 0.dp)
            .border(1.dp, fbsAssemblyStatusColor(order.status).copy(alpha = 0.42f), RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(Modifier.padding(if (large) 18.dp else 14.dp), verticalArrangement = Arrangement.spacedBy(if (large) 14.dp else 10.dp)) {
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    FbsAssemblyOrderNumber(
                        orderNumber = order.orderNumber,
                        large = large,
                        status = order.status.takeIf { large }
                    )
                    if (!large) {
                        Row(horizontalArrangement = Arrangement.spacedBy(7.dp), verticalAlignment = Alignment.CenterVertically) {
                            FbsAssemblyStatusBadge(order.status)
                            PreAssemblyMetaChip("${order.totalQuantity} шт.", background = FbsNeonGreen.copy(alpha = 0.12f), contentColor = FbsNeonGreen)
                            if (showMultiPositionWarning) {
                                PreAssemblyMetaChip(
                                    "${order.distinctPositionCount} поз.",
                                    background = WarningColor.copy(alpha = 0.15f),
                                    contentColor = WarningColor
                                )
                            }
                            if (order.canisterCount > 0) {
                                PreAssemblyMetaChip("${order.canisterCount} канистр", background = WarningColor.copy(alpha = 0.15f), contentColor = WarningColor)
                            }
                        }
                    }
                }
            }
            if (showMultiPositionWarning) {
                FbsAssemblyMultiPositionWarning(
                    positionCount = order.distinctPositionCount,
                    totalQuantity = order.totalQuantity,
                    compact = !large
                )
            }
            if (large) {
                HorizontalDivider(color = FbsDarkBorder.copy(alpha = 0.82f))
            }
            Column(verticalArrangement = Arrangement.spacedBy(if (large) 12.dp else 7.dp)) {
                order.items.forEach { item ->
                    FbsAssemblyItemRow(item = item, status = order.status, large = large)
                }
            }
        }
    }
}

@Composable
private fun FbsAssemblyOrderNumber(
    orderNumber: String,
    large: Boolean,
    status: FbsAssemblyOrderStatus? = null
) {
    val parts = fbsAssemblyOrderNumberParts(orderNumber)
    Column(verticalArrangement = Arrangement.spacedBy(if (large) 6.dp else 3.dp)) {
        Text("Заказ", color = FbsDarkMutedText, fontWeight = FontWeight.ExtraBold, fontSize = if (large) 12.sp else 11.sp)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                parts.prefix,
                color = FbsDarkText,
                fontWeight = FontWeight.ExtraBold,
                fontSize = if (large) 19.sp else 16.sp,
                lineHeight = if (large) 22.sp else 19.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(if (large) 12.dp else 9.dp))
                    .background(FbsNeonGreen)
                    .padding(horizontal = if (large) 10.dp else 7.dp, vertical = if (large) 5.dp else 3.dp)
            ) {
                Text(
                    parts.highlight,
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = if (large) 20.sp else 17.sp,
                    lineHeight = if (large) 23.sp else 19.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (parts.suffix.isNotBlank() || status != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (parts.suffix.isNotBlank()) {
                    Text(
                        parts.suffix,
                        color = FbsDarkMutedText,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (large) 13.sp else 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                status?.let { FbsAssemblyStatusBadge(it) }
            }
        }
    }
}

@Composable
private fun FbsAssemblyItemRow(item: FbsAssemblyItem, status: FbsAssemblyOrderStatus, large: Boolean) {
    val shape = RoundedCornerShape(if (large) 22.dp else 18.dp)
    if (large) {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(FbsDarkPanelAlt.copy(alpha = 0.72f))
                .border(1.dp, FbsDarkBorder, shape)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val compact = maxWidth < 310.dp
                val photoWidth = if (compact) 98.dp else 122.dp
                val photoHeight = if (compact) 164.dp else 188.dp
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    FbsAssemblyProductPhoto(
                        imageUrl = item.imageUrl,
                        status = status,
                        decorated = true,
                        modifier = Modifier.size(width = photoWidth, height = photoHeight)
                    )
                    Column(
                        Modifier.weight(1f).heightIn(min = photoHeight),
                        verticalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        Text(
                            item.name,
                            color = FbsDarkText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            lineHeight = 16.sp,
                            maxLines = 8,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (!item.article.isNullOrBlank()) {
                            Text(
                                "Арт. ${item.article}",
                                color = FbsDarkMutedText,
                                fontSize = 11.sp,
                                lineHeight = 14.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        if (item.apiQuantity != item.calculatedQuantity || item.isCanister) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                if (item.apiQuantity != item.calculatedQuantity) {
                                    PreAssemblyMetaChip("API ${item.apiQuantity}", background = FbsDarkBorder, contentColor = FbsDarkMutedText)
                                }
                                if (item.isCanister) {
                                    PreAssemblyMetaChip("канистра", background = WarningColor.copy(alpha = 0.16f), contentColor = WarningColor)
                                }
                            }
                        }
                    }
                }
            }
            FbsAssemblyQuantityBadge(
                quantity = item.calculatedQuantity,
                large = true,
                modifier = Modifier.fillMaxWidth().height(92.dp)
            )
        }
    } else {
        Row(
            Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(FbsDarkPanelAlt.copy(alpha = 0.72f))
                .border(1.dp, FbsDarkBorder, shape)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            FbsAssemblyProductPhoto(
                imageUrl = item.imageUrl,
                status = status,
                modifier = Modifier.size(width = 58.dp, height = 74.dp)
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    item.name,
                    color = FbsDarkText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    FbsAssemblyQuantityBadge(item.calculatedQuantity, large = false)
                    if (item.apiQuantity != item.calculatedQuantity) {
                        PreAssemblyMetaChip("API ${item.apiQuantity}", background = FbsDarkBorder, contentColor = FbsDarkMutedText)
                    }
                    if (item.isCanister) {
                        PreAssemblyMetaChip("канистра", background = WarningColor.copy(alpha = 0.16f), contentColor = WarningColor)
                    }
                }
                if (!item.article.isNullOrBlank()) {
                    Text(
                        "Арт. ${item.article}",
                        color = FbsDarkMutedText,
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun FbsAssemblyQuantityBadge(quantity: Int, large: Boolean, modifier: Modifier = Modifier) {
    if (large) {
        Box(
            modifier
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(FbsNeonGreen.copy(alpha = 0.12f), FbsNeonGreen.copy(alpha = 0.18f), FbsNeonGreen.copy(alpha = 0.10f))
                    )
                )
                .border(1.dp, FbsNeonGreen.copy(alpha = 0.42f), RoundedCornerShape(18.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Text(
                "Количество",
                color = FbsNeonGreen,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.TopStart)
            )
            Text(
                quantity.toString(),
                color = FbsNeonGreen,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                lineHeight = 28.sp,
                modifier = Modifier.align(Alignment.Center)
            )
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(999.dp))
                    .background(FbsNeonGreen.copy(alpha = 0.13f))
                    .padding(horizontal = 9.dp, vertical = 5.dp)
            ) {
                Text("шт.", color = FbsNeonGreen, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
            }
        }
        return
    }

    Row(
        modifier
            .clip(RoundedCornerShape(999.dp))
            .background(FbsNeonGreen.copy(alpha = 0.14f))
            .padding(horizontal = 9.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            quantity.toString(),
            color = FbsNeonGreen,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            lineHeight = 16.sp,
            maxLines = 1
        )
        Text("шт.", color = FbsNeonGreen, fontWeight = FontWeight.Bold, fontSize = 10.sp, maxLines = 1)
    }
}

@Composable
private fun FbsAssemblyProductPhoto(
    imageUrl: String?,
    status: FbsAssemblyOrderStatus,
    decorated: Boolean = false,
    modifier: Modifier = Modifier
) {
    val imageBitmap by produceState<ImageBitmap?>(initialValue = null, imageUrl) {
        value = imageUrl?.let { url ->
            preAssemblyProductImageCache[url] ?: loadPreAssemblyProductImage(url)?.also { bitmap ->
                preAssemblyProductImageCache.put(url, bitmap)
            }
        }
    }
    val shape = RoundedCornerShape(15.dp)

    Box(
        modifier
            .clip(shape)
            .background(fbsAssemblyStatusColor(status).copy(alpha = 0.12f))
            .border(1.dp, fbsAssemblyStatusColor(status).copy(alpha = 0.38f), shape),
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
            if (decorated) {
                Canvas(Modifier.fillMaxSize()) {
                    val cornerInset = 17.dp.toPx()
                    val cornerLength = 9.dp.toPx()
                    val cornerStroke = 1.6.dp.toPx()
                    val cornerColor = FbsNeonGreen.copy(alpha = 0.26f)

                    drawLine(cornerColor, Offset(cornerInset, cornerInset), Offset(cornerInset + cornerLength, cornerInset), cornerStroke)
                    drawLine(cornerColor, Offset(cornerInset, cornerInset), Offset(cornerInset, cornerInset + cornerLength), cornerStroke)
                    drawLine(cornerColor, Offset(size.width - cornerInset, cornerInset), Offset(size.width - cornerInset - cornerLength, cornerInset), cornerStroke)
                    drawLine(cornerColor, Offset(size.width - cornerInset, cornerInset), Offset(size.width - cornerInset, cornerInset + cornerLength), cornerStroke)
                    drawLine(cornerColor, Offset(cornerInset, size.height - cornerInset), Offset(cornerInset + cornerLength, size.height - cornerInset), cornerStroke)
                    drawLine(cornerColor, Offset(cornerInset, size.height - cornerInset), Offset(cornerInset, size.height - cornerInset - cornerLength), cornerStroke)
                    drawLine(cornerColor, Offset(size.width - cornerInset, size.height - cornerInset), Offset(size.width - cornerInset - cornerLength, size.height - cornerInset), cornerStroke)
                    drawLine(cornerColor, Offset(size.width - cornerInset, size.height - cornerInset), Offset(size.width - cornerInset, size.height - cornerInset - cornerLength), cornerStroke)

                    val dotY = size.height - 18.dp.toPx()
                    val dotRadius = 3.5.dp.toPx()
                    val dotGap = 13.dp.toPx()
                    val centerX = size.width / 2f
                    drawCircle(FbsNeonGreen, dotRadius, Offset(centerX - dotGap, dotY))
                    drawCircle(FbsDarkBorder.copy(alpha = 0.68f), dotRadius, Offset(centerX, dotY))
                    drawCircle(FbsDarkBorder, dotRadius, Offset(centerX + dotGap, dotY))
                }
            }
            if (decorated) {
                FbsAssemblyPackagePlaceholderIcon(color = fbsAssemblyStatusColor(status))
            } else {
                Icon(
                    Icons.Outlined.Inventory2,
                    contentDescription = null,
                    tint = fbsAssemblyStatusColor(status),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun FbsAssemblyPackagePlaceholderIcon(color: Color) {
    Canvas(Modifier.size(48.dp)) {
        val stroke = 2.3.dp.toPx()
        val top = Offset(size.width * 0.50f, size.height * 0.10f)
        val leftTop = Offset(size.width * 0.13f, size.height * 0.31f)
        val center = Offset(size.width * 0.50f, size.height * 0.52f)
        val rightTop = Offset(size.width * 0.87f, size.height * 0.31f)
        val leftBottom = Offset(size.width * 0.13f, size.height * 0.73f)
        val bottom = Offset(size.width * 0.50f, size.height * 0.94f)
        val rightBottom = Offset(size.width * 0.87f, size.height * 0.73f)

        drawLine(color, top, leftTop, stroke)
        drawLine(color, leftTop, center, stroke)
        drawLine(color, center, rightTop, stroke)
        drawLine(color, rightTop, top, stroke)
        drawLine(color, leftTop, leftBottom, stroke)
        drawLine(color, leftBottom, bottom, stroke)
        drawLine(color, bottom, rightBottom, stroke)
        drawLine(color, rightBottom, rightTop, stroke)
        drawLine(color, center, bottom, stroke)
        drawLine(color, Offset(size.width * 0.31f, size.height * 0.20f), Offset(size.width * 0.68f, size.height * 0.41f), stroke)
        drawLine(color, Offset(size.width * 0.68f, size.height * 0.41f), Offset(size.width * 0.68f, size.height * 0.54f), stroke)
    }
}

@Composable
private fun FbsAssemblyStatusBadge(status: FbsAssemblyOrderStatus) {
    Box(
        Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(fbsAssemblyStatusColor(status).copy(alpha = 0.13f))
            .border(1.dp, fbsAssemblyStatusColor(status).copy(alpha = 0.42f), RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(status.title, color = fbsAssemblyStatusColor(status), fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
    }
}

@Composable
private fun FbsAssemblyLoadingCard() {
    FbsAssemblyDarkCard(Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            CircularProgressIndicator(color = FbsNeonGreen, strokeWidth = 3.dp, modifier = Modifier.size(30.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Загружаем заказы Ozon...",
                    color = FbsDarkText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "После загрузки можно открыть список или начать сборку",
                    color = FbsDarkMutedText,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun FbsAssemblyEmptyCard(
    title: String = "Нет заказов для сборки",
    text: String = "В актуальном списке Ozon нет FBS-заказов в статусе ожидания упаковки."
) {
    FbsAssemblyDarkCard(Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppIconBubble(Icons.Outlined.Inventory2, tint = FbsNeonGreen, background = FbsNeonGreen.copy(alpha = 0.12f), modifier = Modifier.size(58.dp))
            Text(
                title,
                color = FbsDarkText,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 19.sp,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text,
                color = FbsDarkMutedText,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun FbsAssemblyErrorCard(message: String, onRetry: () -> Unit, compact: Boolean = false) {
    FbsAssemblyDarkCard(Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(if (compact) 12.dp else 18.dp),
            verticalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 10.dp)) {
                Box(
                    modifier = Modifier
                        .size(if (compact) 34.dp else 42.dp)
                        .clip(CircleShape)
                        .background(DangerColor.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Close, contentDescription = null, tint = DangerColor, modifier = Modifier.size(if (compact) 18.dp else 24.dp))
                }
                Column(Modifier.weight(1f)) {
                    Text(
                        "Не удалось загрузить заказы",
                        color = FbsDarkText,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (compact) 14.sp else 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        message,
                        color = FbsDarkMutedText,
                        fontSize = if (compact) 12.sp else 13.sp,
                        maxLines = if (compact) 2 else 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            FbsAssemblyOutlinedLargeActionButton("Повторить загрузку", icon = Icons.Outlined.FileDownload, enabled = true, onClick = onRetry, compact = true, modifier = Modifier.fillMaxWidth().height(52.dp))
        }
    }
}

@Composable
private fun FbsAssemblyResetDataConfirmDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        FbsAssemblyDarkCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Сбросить данные FBS?",
                    color = FbsDarkText,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    lineHeight = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "Будут очищены локальная история, аналитика, настройки и текущий прогресс. Заказы Ozon не удаляются.",
                    color = FbsDarkMutedText,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FbsAssemblySecondaryActionButton("Отмена", icon = Icons.Outlined.Close, onClick = onDismiss, modifier = Modifier.weight(1f))
                    OutlinedButton(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, DangerColor.copy(alpha = 0.70f)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = FbsDarkPanelAlt,
                            contentColor = DangerColor
                        )
                    ) {
                        Icon(Icons.Outlined.Delete, contentDescription = null, tint = DangerColor, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(7.dp))
                        Text(
                            "Сбросить",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            lineHeight = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FbsAssemblyInfoLine(title: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = MutedTextColor, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(value, color = MainTextColor, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, textAlign = TextAlign.End, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

private data class FbsOrderNumberParts(val prefix: String, val highlight: String, val suffix: String)

private data class FbsAssemblyTimerInfo(val text: String, val isLate: Boolean)

private data class FbsAssemblyOrderMetrics(
    val totalOrders: Int,
    val collectedOrders: Int,
    val remainingOrders: Int,
    val totalItems: Int,
    val canisterCount: Int,
    val progress: Float
)

@Composable
private fun rememberCurrentDateTime(
    refreshIntervalMillis: Long,
    enabled: Boolean = true
): LocalDateTime {
    val lifecycleOwner = LocalLifecycleOwner.current
    val now by produceState(
        initialValue = LocalDateTime.now(),
        lifecycleOwner,
        refreshIntervalMillis,
        enabled
    ) {
        if (!enabled) return@produceState
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            while (true) {
                value = LocalDateTime.now()
                delay(refreshIntervalMillis)
            }
        }
    }
    return now
}

private fun calculateFbsAssemblyOrderMetrics(orders: List<FbsAssemblyOrder>): FbsAssemblyOrderMetrics {
    var collectedOrders = 0
    var totalItems = 0
    var canisterCount = 0
    orders.forEach { order ->
        if (order.status == FbsAssemblyOrderStatus.COLLECTED) collectedOrders++
        order.items.forEach { item ->
            totalItems += item.calculatedQuantity
            if (item.isCanister) canisterCount += item.apiQuantity.coerceAtLeast(1)
        }
    }
    val totalOrders = orders.size
    return FbsAssemblyOrderMetrics(
        totalOrders = totalOrders,
        collectedOrders = collectedOrders,
        remainingOrders = (totalOrders - collectedOrders).coerceAtLeast(0),
        totalItems = totalItems,
        canisterCount = canisterCount,
        progress = if (totalOrders == 0) 0f else collectedOrders.toFloat() / totalOrders.toFloat()
    )
}

private fun fbsAssemblyShowMultiPositionWarning(order: FbsAssemblyOrder): Boolean =
    order.hasMultipleDifferentPositions

private fun fbsAssemblyPositionWord(count: Int): String {
    val lastTwoDigits = count % 100
    val lastDigit = count % 10
    return when {
        lastTwoDigits in 11..14 -> "позиций"
        lastDigit == 1 -> "позиция"
        lastDigit in 2..4 -> "позиции"
        else -> "позиций"
    }
}

private fun fbsAssemblyOrderNumberParts(orderNumber: String): FbsOrderNumberParts {
    val firstBlock = orderNumber.substringBefore("-")
    val suffix = orderNumber.removePrefix(firstBlock)
    if (firstBlock.length < 4) {
        return FbsOrderNumberParts(prefix = "", highlight = firstBlock.ifBlank { orderNumber }, suffix = suffix)
    }
    return FbsOrderNumberParts(
        prefix = firstBlock.dropLast(4),
        highlight = firstBlock.takeLast(4),
        suffix = suffix
    )
}

private fun fbsAssemblyTimer(now: LocalDateTime, deadlineTime: String): FbsAssemblyTimerInfo {
    val parsedDeadlineTime = fbsAssemblyDeadlineTime(deadlineTime)
    val deadline = now.toLocalDate().atTime(parsedDeadlineTime)
    val deadlineText = "%02d:%02d".format(parsedDeadlineTime.hour, parsedDeadlineTime.minute)
    val isLate = now.isAfter(deadline)
    val duration = if (isLate) Duration.between(deadline, now) else Duration.between(now, deadline)
    return FbsAssemblyTimerInfo(
        text = if (isLate) "Просрочка: ${formatFbsAssemblyDuration(duration)}" else "До $deadlineText: ${formatFbsAssemblyDuration(duration)}",
        isLate = isLate
    )
}

private fun formatFbsAssemblyTime(millis: Long?): String =
    millis?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("HH:mm"))
    } ?: "—"

private fun formatFbsAssemblyDuration(startMillis: Long?, endMillis: Long?): String {
    if (startMillis == null || endMillis == null) return "—"
    return formatFbsAssemblyDuration(Duration.ofMillis((endMillis - startMillis).coerceAtLeast(0L)))
}

private fun formatFbsAssemblyDuration(duration: Duration): String {
    val seconds = duration.seconds.coerceAtLeast(0)
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return "%02d:%02d:%02d".format(hours, minutes, secs)
}

private fun formatFbsAssemblyShortDuration(seconds: Long?): String {
    val safeSeconds = seconds?.coerceAtLeast(0L) ?: return "—"
    val hours = safeSeconds / 3600
    val minutes = (safeSeconds % 3600) / 60
    val secs = safeSeconds % 60
    return when {
        hours > 0 -> "${hours} ч ${minutes} мин"
        minutes > 0 -> "${minutes} мин ${secs} сек"
        else -> "${secs} сек"
    }
}

private fun keepFbsAssemblyDurationOnOneLine(value: String): String =
    if (value.contains(" мин ") && value.endsWith(" сек")) {
        value.replace(" ", "\u00A0")
    } else {
        value
    }

private fun formatFbsAssemblyDate(millis: Long): String =
    Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

private fun formatFbsAssemblyLocalTime(dateTime: LocalDateTime): String =
    dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

private fun fbsAssemblyEffectivePaceSeconds(
    state: FbsAssemblyUiState,
    stats: FbsAssemblyHistoryStats
): Long {
    val startedAt = state.startedAtMillis ?: return stats.averageSecondsPerOrder
    val sessionEntries = state.history.filter { entry ->
        entry.status.countsInAverage && entry.createdAt >= startedAt && entry.durationSeconds > 0
    }
    if (sessionEntries.isEmpty()) return stats.averageSecondsPerOrder
    return (sessionEntries.sumOf { it.durationSeconds } / sessionEntries.size).coerceAtLeast(1L)
}

private fun fbsAssemblyForecastColor(risk: FbsAssemblyRisk): Color = when (risk) {
    FbsAssemblyRisk.ENOUGH -> SuccessColor
    FbsAssemblyRisk.START_SOON -> WarningColor
    FbsAssemblyRisk.LATE -> WarningColor
    FbsAssemblyRisk.NOT_ENOUGH_TIME -> DangerColor
}

private fun fbsAssemblyDeadlineTime(value: String): LocalTime {
    val match = Regex("""^\s*(\d{1,2}):(\d{2})\s*$""").matchEntire(value)
    val hour = match?.groupValues?.getOrNull(1)?.toIntOrNull()
    val minute = match?.groupValues?.getOrNull(2)?.toIntOrNull()
    return if (hour != null && minute != null && hour in 0..23 && minute in 0..59) {
        LocalTime.of(hour, minute)
    } else {
        LocalTime.of(16, 0)
    }
}

private fun fbsAssemblyFinishedInTime(finishedAtMillis: Long?, deadlineTime: String): Boolean {
    val finishedTime = finishedAtMillis?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalTime()
    } ?: return true
    return !finishedTime.isAfter(fbsAssemblyDeadlineTime(deadlineTime))
}

private fun fbsAssemblyStatusColor(status: FbsAssemblyOrderStatus): Color = when (status) {
    FbsAssemblyOrderStatus.WAITING -> FbsDarkMutedText
    FbsAssemblyOrderStatus.CURRENT -> FbsNeonGreen
    FbsAssemblyOrderStatus.COLLECTED -> FbsNeonGreen
    FbsAssemblyOrderStatus.SKIPPED -> WarningColor
    FbsAssemblyOrderStatus.PROBLEM,
    FbsAssemblyOrderStatus.NOT_FOUND,
    FbsAssemblyOrderStatus.SHORTAGE,
    FbsAssemblyOrderStatus.CANCELLED -> DangerColor
}

private fun fbsAssemblyStatusBackground(status: FbsAssemblyOrderStatus): Color = when (status) {
    FbsAssemblyOrderStatus.WAITING -> Color(0xFFF2F4F7)
    FbsAssemblyOrderStatus.CURRENT -> SoftBlueColor
    FbsAssemblyOrderStatus.COLLECTED -> Color(0xFFE9F8EF)
    FbsAssemblyOrderStatus.SKIPPED -> Color(0xFFFFF4E5)
    FbsAssemblyOrderStatus.PROBLEM,
    FbsAssemblyOrderStatus.NOT_FOUND,
    FbsAssemblyOrderStatus.SHORTAGE,
    FbsAssemblyOrderStatus.CANCELLED -> Color(0xFFFFE8E8)
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
    var topChromeExpanded by rememberSaveable { mutableStateOf(true) }
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current
    var printBridgeHost by rememberSaveable { mutableStateOf(PreAssemblyPrinter.savedBridgeHost(context)) }
    var printPrinterName by rememberSaveable { mutableStateOf(PreAssemblyPrinter.savedPrinterName(context)) }
    val sortOrder = PreAssemblySortOrder.values().firstOrNull { it.name == sortOrderKey } ?: PreAssemblySortOrder.ATTENTION
    val itemMetrics = remember(state.items) {
        var checked = 0
        var available = 0
        var toTransfer = 0
        var comments = 0
        state.items.forEach { item ->
            if (item.status != PreAssemblyStatus.NOT_CHECKED) checked++
            if (item.status == PreAssemblyStatus.AVAILABLE) available++
            if (item.status == PreAssemblyStatus.NOT_AVAILABLE || item.status == PreAssemblyStatus.NEED_TRANSFER) toTransfer++
            if (item.comment.isNotBlank()) comments++
        }
        intArrayOf(checked, available, toTransfer, comments)
    }
    val checkedCount = itemMetrics[0]
    val availableCount = itemMetrics[1]
    val toTransferCount = itemMetrics[2]
    val notCheckedCount = state.items.size - checkedCount
    val commentsCount = itemMetrics[3]
    val hasActiveFilters = searchQuery.isNotBlank() || statusFilter != "ALL"
    val normalizedSearchQuery = remember(searchQuery) { searchQuery.trim() }
    val filteredItems = remember(state.items, statusFilter, normalizedSearchQuery) {
        state.items.filter { item ->
            val byStatus = statusFilter == "ALL" || item.status.name == statusFilter
            val bySearch = normalizedSearchQuery.isBlank() ||
                item.offerId.contains(normalizedSearchQuery, ignoreCase = true) ||
                item.name.contains(normalizedSearchQuery, ignoreCase = true) ||
                item.orderId.contains(normalizedSearchQuery, ignoreCase = true) ||
                (item.sku?.contains(normalizedSearchQuery, ignoreCase = true) == true)
            byStatus && bySearch
        }
    }
    val sortedItems = remember(filteredItems, sortOrder) { sortPreAssemblyItems(filteredItems, sortOrder) }
    val visibleIds = remember(filteredItems) { filteredItems.map { it.id } }
    val actionDockVisible = state.items.isNotEmpty() || state.archive.isNotEmpty() || state.reportText.isNotBlank()

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
                PrinterUiNotifier.error(
                    title = "Не удалось открыть печать",
                    text = error.message ?: "Ошибка печати"
                )
            }
    }

    fun printTestPage() {
        PreAssemblyPrinter.printTestPage(context)
            .onFailure { error ->
                PrinterUiNotifier.error(
                    title = "Не удалось открыть тестовую печать",
                    text = error.message ?: "Ошибка печати"
                )
            }
    }

    fun sharePrintLogs() {
        PreAssemblyPrintLog.append(context, "Пользователь запросил выгрузку логов печати")
        PreAssemblyPrintLog.share(context)
            .onFailure { error ->
                PreAssemblyPrintLog.append(context, "Не удалось выгрузить логи печати", error)
                PrinterUiNotifier.error(
                    title = "Не удалось выгрузить логи печати",
                    text = error.message ?: "Ошибка экспорта"
                )
            }
    }

    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .background(FbsDarkBackground)
    ) {
        val compactScreen = maxHeight < 740.dp || maxWidth < 430.dp
        val denseScreen = maxHeight < 620.dp || maxWidth < 370.dp
        val screenPadding = if (compactScreen) 10.dp else 18.dp
        val sectionSpacing = when {
            denseScreen -> 7.dp
            compactScreen -> 8.dp
            else -> 14.dp
        }
        val feedbackTopPadding = when {
            !topChromeExpanded -> 48.dp
            compactScreen -> 72.dp
            else -> 88.dp
        }
        val listBottomPadding = when {
            !actionDockVisible -> 18.dp
            compactScreen -> 94.dp
            else -> 134.dp
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = screenPadding, vertical = if (denseScreen) 8.dp else 12.dp),
            verticalArrangement = Arrangement.spacedBy(sectionSpacing)
        ) {
            PreAssemblyHeader(
                onBack = onBack,
                onMore = { showBulkActions = true },
                onRefresh = vm::loadOrders,
                isLoading = state.isLoading,
                compact = compactScreen,
                expanded = topChromeExpanded,
                onExpandedChange = { topChromeExpanded = it },
                subtitle = when {
                    state.isCompleted -> "Проверка остатков завершена"
                    state.items.isEmpty() -> "Проверка остатков не загружена"
                    else -> "Проверка остатков открыта"
                }
            )

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
                        isCompleted = state.isCompleted,
                        hasActiveFilters = hasActiveFilters,
                        onOpenFilters = { showControls = true }
                    )
                    if (filteredItems.isEmpty()) {
                        PreAssemblyNoResultsCard(onReset = { searchQuery = ""; statusFilter = "ALL" })
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = listBottomPadding)
                        ) {
                            items(sortedItems, key = { it.id }) { item ->
                                PreAssemblyItemCard(item = item, vm = vm, readOnly = state.isCompleted)
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = state.message != null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 14.dp, vertical = feedbackTopPadding),
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 3 })
        ) {
            state.message?.let { FbsAssemblyMessage(text = it, onClose = vm::clearMessage) }
        }

        AnimatedVisibility(
            visible = actionDockVisible,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = screenPadding, vertical = if (compactScreen) 6.dp else 14.dp),
            enter = fadeIn(animationSpec = tween(160)) + slideInVertically(initialOffsetY = { it / 3 }),
            exit = fadeOut(animationSpec = tween(120)) + slideOutVertically(targetOffsetY = { it / 3 })
        ) {
            PreAssemblyActionPanel(
                hasItems = state.items.isNotEmpty(),
                hasVisibleItems = filteredItems.isNotEmpty(),
                hasReport = state.reportText.isNotBlank(),
                hasActiveFilters = hasActiveFilters,
                hasArchive = state.archive.isNotEmpty(),
                isCompleted = state.isCompleted,
                compact = compactScreen,
                onOpenControls = { showControls = true },
                onOpenBulkActions = { showBulkActions = true },
                onOpenArchive = { showArchiveDialog = true },
                onBuildReport = { showPreview = vm.buildReport() },
                onFinishAssembly = { showFinishDialog = true },
                modifier = Modifier.fillMaxWidth()
            )
        }
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
private fun PreAssemblyHeader(
    onBack: () -> Unit,
    onMore: () -> Unit,
    onRefresh: () -> Unit,
    isLoading: Boolean,
    compact: Boolean = false,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    subtitle: String
) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(if (compact) 6.dp else 8.dp)
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(animationSpec = tween(160)) + expandVertically(expandFrom = Alignment.Top),
            exit = fadeOut(animationSpec = tween(120)) + shrinkVertically(shrinkTowards = Alignment.Top)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(if (compact) 6.dp else 14.dp)
            ) {
                PreAssemblyDarkIconActionButton(
                    Icons.AutoMirrored.Outlined.ArrowBack,
                    "Назад",
                    modifier = Modifier.size(if (compact) 44.dp else 68.dp),
                    onClick = onBack
                )
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(if (compact) 3.dp else 6.dp)) {
                    Text(
                        if (compact) "Предсборка" else "Предсборка №1",
                        fontSize = if (compact) 20.sp else 31.sp,
                        lineHeight = if (compact) 23.sp else 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = FbsDarkText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        subtitle,
                        color = FbsDarkMutedText,
                        fontSize = if (compact) 9.sp else 13.sp,
                        lineHeight = if (compact) 11.sp else 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                PreAssemblyDarkIconActionButton(
                    Icons.Outlined.MoreVert,
                    "Дополнительно",
                    modifier = Modifier.size(if (compact) 44.dp else 68.dp),
                    onClick = onMore
                )
                OutlinedIconButton(
                    onClick = onRefresh,
                    modifier = Modifier
                        .size(if (compact) 44.dp else 68.dp)
                        .alpha(if (isLoading) 0.55f else 1f),
                    shape = RoundedCornerShape(if (compact) 14.dp else 24.dp),
                    border = BorderStroke(1.dp, FbsDarkBorder)
                ) {
                    Icon(
                        Icons.Outlined.FileDownload,
                        contentDescription = "Загрузить заказы",
                        tint = FbsNeonGreen,
                        modifier = Modifier.size(if (compact) 22.dp else 38.dp)
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = !expanded,
            enter = fadeIn(animationSpec = tween(160)) + expandVertically(expandFrom = Alignment.Top),
            exit = fadeOut(animationSpec = tween(120)) + shrinkVertically(shrinkTowards = Alignment.Top)
        ) {
            PreAssemblyCollapsedHeader(
                onBack = onBack,
                onMore = onMore,
                onExpand = { onExpandedChange(true) },
                onRefresh = onRefresh,
                isLoading = isLoading,
                compact = compact
            )
        }
    }
}

@Composable
private fun PreAssemblyCollapsedHeader(
    onBack: () -> Unit,
    onMore: () -> Unit,
    onExpand: () -> Unit,
    onRefresh: () -> Unit,
    isLoading: Boolean,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(if (compact) 18.dp else 22.dp)
    Row(
        modifier
            .fillMaxWidth()
            .height(if (compact) 44.dp else 50.dp)
            .clip(shape)
            .background(FbsDarkPanelAlt.copy(alpha = 0.96f))
            .border(1.dp, FbsNeonGreen.copy(alpha = 0.28f), shape)
            .clickable(onClick = onExpand)
            .padding(start = 4.dp, end = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        IconButton(onClick = onBack, modifier = Modifier.size(if (compact) 34.dp else 38.dp)) {
            Icon(
                Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Назад",
                tint = FbsNeonGreen,
                modifier = Modifier.size(if (compact) 20.dp else 22.dp)
            )
        }
        Text(
            "Предсборка Ozon",
            modifier = Modifier.weight(1f),
            color = FbsDarkText,
            fontWeight = FontWeight.ExtraBold,
            fontSize = if (compact) 15.sp else 16.sp,
            lineHeight = if (compact) 17.sp else 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(onClick = onMore, modifier = Modifier.size(if (compact) 34.dp else 38.dp)) {
            Icon(
                Icons.Outlined.MoreVert,
                contentDescription = "Дополнительно",
                tint = FbsNeonGreen,
                modifier = Modifier.size(if (compact) 20.dp else 22.dp)
            )
        }
        IconButton(
            onClick = onRefresh,
            modifier = Modifier
                .size(if (compact) 34.dp else 38.dp)
                .alpha(if (isLoading) 0.55f else 1f)
        ) {
            Icon(
                Icons.Outlined.FileDownload,
                contentDescription = "Загрузить заказы",
                tint = FbsNeonGreen,
                modifier = Modifier.size(if (compact) 20.dp else 22.dp)
            )
        }
        IconButton(onClick = onExpand, modifier = Modifier.size(if (compact) 34.dp else 38.dp)) {
            Icon(
                Icons.Outlined.ExpandMore,
                contentDescription = "Развернуть верх",
                tint = FbsNeonGreen,
                modifier = Modifier.size(if (compact) 22.dp else 24.dp)
            )
        }
    }
}

@Composable
private fun PreAssemblyLoadingCard() {
    PreAssemblyDarkCard(Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            CircularProgressIndicator(color = FbsNeonGreen, strokeWidth = 3.dp, modifier = Modifier.size(30.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Загружаю заказы Ozon", color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text("После загрузки позиции объединятся по артикулу", color = FbsDarkMutedText, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun PreAssemblyEmptyCard(onLoad: () -> Unit) {
    PreAssemblyDarkCard(Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppIconBubble(Icons.Outlined.Inventory2, tint = FbsNeonGreen, background = FbsNeonGreen.copy(alpha = 0.12f), modifier = Modifier.size(58.dp))
            Text("Заказы ещё не загружены", color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 20.sp, textAlign = TextAlign.Center)
            Text(
                "Нажмите кнопку ниже, чтобы получить список заказов Ozon для ручной проверки остатков.",
                color = FbsDarkMutedText,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            PreAssemblyDarkPrimaryButton("Загрузить заказы Ozon", icon = Icons.Outlined.FileDownload, onClick = onLoad, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun PreAssemblyErrorCard(message: String, onRetry: () -> Unit) {
    PreAssemblyDarkCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(DangerColor.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Close, contentDescription = null, tint = DangerColor)
                }
                Column(Modifier.weight(1f)) {
                    Text("Не удалось загрузить данные", color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text(message, color = FbsDarkMutedText, fontSize = 13.sp)
                }
            }
            PreAssemblyDarkPrimaryButton("Повторить загрузку", icon = Icons.Outlined.FileDownload, onClick = onRetry, modifier = Modifier.fillMaxWidth())
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
    val progressColor = if (notChecked == 0 && total > 0) SuccessColor else FbsNeonGreen
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        PreAssemblyDarkCard(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(Modifier.padding(horizontal = 12.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(11.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    PreAssemblyProgressRing(progress = progress, color = progressColor, percent = progressPercent)
                    PreAssemblySummaryMetric(
                        title = "Прогресс",
                        value = "$progressPercent%",
                        valueColor = progressColor,
                        modifier = Modifier.weight(1f)
                    )
                    PreAssemblyDashboardDivider()
                    PreAssemblySummaryMetric(
                        title = "Пров.",
                        value = checked.toString(),
                        suffix = "из $total",
                        valueColor = progressColor,
                        modifier = Modifier.weight(1f)
                    )
                    PreAssemblyDashboardDivider()
                    PreAssemblySummaryMetric(
                        title = "Всего",
                        value = total.toString(),
                        valueColor = FbsDarkText,
                        modifier = Modifier.weight(1f)
                    )
                }
                PreAssemblyProgressBar(progress = progress, color = progressColor)
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            PreAssemblyStatusMetricCard(
                title = "Есть",
                value = available.toString(),
                icon = Icons.Outlined.CheckCircle,
                color = FbsNeonGreen,
                modifier = Modifier.weight(1f)
            )
            PreAssemblyStatusMetricCard(
                title = "Перем.",
                value = toTransfer.toString(),
                icon = Icons.AutoMirrored.Outlined.ArrowForward,
                color = DangerColor,
                modifier = Modifier.weight(1f)
            )
            PreAssemblyStatusMetricCard(
                title = "Не пров.",
                value = notChecked.toString(),
                icon = Icons.Outlined.WarningAmber,
                color = FbsDarkMutedText,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PreAssemblyProgressRing(progress: Float, color: Color, percent: Int, modifier: Modifier = Modifier) {
    Box(
        modifier.size(52.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val strokeWidth = 6.dp.toPx()
            val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
            val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
            drawArc(
                color = FbsDarkBorder.copy(alpha = 0.78f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * progress.coerceIn(0f, 1f),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Text(
            "$percent%",
            color = FbsDarkText,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            lineHeight = 15.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun PreAssemblySummaryMetric(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    suffix: String? = null,
    valueColor: Color = FbsNeonGreen
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(
            title,
            color = FbsDarkMutedText,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                value,
                color = valueColor,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                lineHeight = 23.sp,
                maxLines = 1
            )
            if (suffix != null) {
                Text(
                    suffix,
                    color = FbsDarkMutedText,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun PreAssemblyDashboardDivider() {
    Box(
        Modifier
            .width(1.dp)
            .height(42.dp)
            .background(FbsDarkBorder.copy(alpha = 0.92f))
    )
}

@Composable
private fun PreAssemblyStatusMetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(15.dp)
    Row(
        modifier
            .heightIn(min = 70.dp)
            .clip(shape)
            .background(color.copy(alpha = if (color == FbsDarkMutedText) 0.09f else 0.12f))
            .border(1.dp, color.copy(alpha = if (color == FbsDarkMutedText) 0.28f else 0.42f), shape)
            .padding(horizontal = 7.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Box(
            Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(21.dp))
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(title, color = FbsDarkMutedText, fontSize = 10.sp, lineHeight = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(value, color = color, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, lineHeight = 21.sp, maxLines = 1)
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
            .background(FbsDarkBorder.copy(alpha = 0.72f))
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
        FbsAssemblyTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = "Поиск",
            placeholder = "Поиск: артикул, SKU, заказ, название",
            leadingIcon = Icons.Outlined.Search,
            modifier = Modifier.fillMaxWidth()
        )
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            if (maxWidth < 430.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PreAssemblyFilterDropdown(statusFilter, onStatusFilterChange, Modifier.fillMaxWidth())
                    PreAssemblySortDropdown(sortOrder, onSortOrderChange, Modifier.fillMaxWidth())
                    PreAssemblyDarkSecondaryButton("Обновить заказы", icon = Icons.Outlined.FileDownload, enabled = !isLoading, onClick = onReload, modifier = Modifier.fillMaxWidth())
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        PreAssemblyFilterDropdown(statusFilter, onStatusFilterChange, Modifier.weight(1f))
                        PreAssemblySortDropdown(sortOrder, onSortOrderChange, Modifier.weight(1f))
                    }
                    PreAssemblyDarkSecondaryButton("Обновить", icon = Icons.Outlined.FileDownload, enabled = !isLoading, onClick = onReload, modifier = Modifier.fillMaxWidth())
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
                .background(FbsDarkPanelAlt)
                .border(1.dp, FbsDarkBorder, RoundedCornerShape(15.dp))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Outlined.MoreVert, contentDescription = null, tint = FbsNeonGreen, modifier = Modifier.size(18.dp))
            Column(Modifier.weight(1f)) {
                Text("Фильтр статуса", color = FbsDarkMutedText, fontSize = 11.sp, maxLines = 1)
                Text(selectedTitle, color = FbsDarkText, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.Outlined.ExpandMore, contentDescription = null, tint = FbsNeonGreen, modifier = Modifier.rotate(if (expanded) 180f else 0f))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(FbsDarkPanelAlt)) {
            options.forEach { (key, title) ->
                DropdownMenuItem(
                    text = { Text(title, color = FbsDarkText, fontWeight = if (key == selectedKey) FontWeight.Bold else FontWeight.Normal) },
                    onClick = {
                        onSelected(key)
                        expanded = false
                    },
                    leadingIcon = {
                        if (key == "ALL") {
                            Icon(Icons.Outlined.Inventory2, contentDescription = null, tint = FbsNeonGreen)
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
                .background(FbsDarkPanelAlt)
                .border(1.dp, FbsDarkBorder, RoundedCornerShape(15.dp))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Outlined.MoreVert, contentDescription = null, tint = FbsNeonGreen, modifier = Modifier.size(18.dp))
            Column(Modifier.weight(1f)) {
                Text("Сортировка", color = FbsDarkMutedText, fontSize = 11.sp, maxLines = 1)
                Text(selected.title, color = FbsDarkText, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.Outlined.ExpandMore, contentDescription = null, tint = FbsNeonGreen, modifier = Modifier.rotate(if (expanded) 180f else 0f))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(FbsDarkPanelAlt)) {
            PreAssemblySortOrder.values().forEach { order ->
                DropdownMenuItem(
                    text = { Text(order.title, color = FbsDarkText, fontWeight = if (order == selected) FontWeight.Bold else FontWeight.Normal) },
                    onClick = {
                        onSelected(order)
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = if (order == selected) FbsNeonGreen else FbsDarkMutedText)
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
    val orderCount = remember(item.orderId) { item.orderId.split(',').count { it.isNotBlank() }.coerceAtLeast(1) }
    var showEditor by rememberSaveable(item.id) { mutableStateOf(false) }
    var showCommentEditor by rememberSaveable(item.id) { mutableStateOf(false) }

    PreAssemblyDarkCard(
        Modifier
            .fillMaxWidth()
            .border(1.dp, PreAssemblyStatusColor(item.status).copy(alpha = 0.42f), RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PreAssemblyStatusPill(item.status)
                        PreAssemblyQuantityChip(item.requiredQuantity)
                    }
                    Text(
                        item.name,
                        color = FbsDarkText,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        lineHeight = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PreAssemblyMetaChip(
                            "Арт. ${item.offerId}",
                            modifier = Modifier
                                .weight(1f)
                                .widthIn(max = 150.dp)
                        )
                        PreAssemblyMetaChip(
                            if (orderCount == 1) "1 заказ" else "$orderCount заказов",
                            background = FbsDarkBorder,
                            contentColor = FbsDarkMutedText
                        )
                        if (!item.sku.isNullOrBlank()) {
                            PreAssemblyMetaChip("SKU", background = FbsDarkBorder, contentColor = FbsDarkMutedText)
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
                        .clip(RoundedCornerShape(16.dp))
                        .background(FbsDarkPanelAlt.copy(alpha = 0.72f))
                        .border(1.dp, FbsDarkBorder, RoundedCornerShape(16.dp))
                        .padding(horizontal = 11.dp, vertical = 8.dp),
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
                                color = FbsDarkMutedText,
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
                            Icon(Icons.Outlined.Description, contentDescription = null, tint = FbsNeonGreen, modifier = Modifier.size(16.dp))
                            Text(
                                item.comment,
                                color = FbsDarkMutedText,
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
                Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
            ) {
                PreAssemblyItemActionBar(
                    status = item.status,
                    hasComment = hasComment,
                    enabled = !readOnly,
                    onAvailable = { vm.updateStatus(item.id, PreAssemblyStatus.AVAILABLE) },
                    onTransfer = { vm.updateStatus(item.id, PreAssemblyStatus.NEED_TRANSFER) },
                    onUnchecked = { vm.updateStatus(item.id, PreAssemblyStatus.NOT_CHECKED) },
                    onSkip = { vm.updateStatus(item.id, PreAssemblyStatus.NOT_CHECKED) },
                    onComment = { showCommentEditor = true }
                )
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

@Composable
private fun PreAssemblyItemActionBar(
    status: PreAssemblyStatus,
    hasComment: Boolean,
    enabled: Boolean,
    onAvailable: () -> Unit,
    onTransfer: () -> Unit,
    onUnchecked: () -> Unit,
    onSkip: () -> Unit,
    onComment: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
    ) {
        HorizontalDivider(color = FbsDarkBorder.copy(alpha = 0.80f))
        Row(
            Modifier
                .fillMaxWidth()
                .height(62.dp)
                .padding(top = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PreAssemblyCardAction(
                text = "Есть",
                icon = Icons.Outlined.CheckCircle,
                color = FbsNeonGreen,
                selected = status == PreAssemblyStatus.AVAILABLE,
                enabled = enabled,
                onClick = onAvailable,
                modifier = Modifier.weight(1f)
            )
            PreAssemblyCardActionDivider()
            PreAssemblyCardAction(
                text = "Перем.",
                icon = Icons.AutoMirrored.Outlined.ArrowForward,
                color = DangerColor,
                selected = status == PreAssemblyStatus.NOT_AVAILABLE || status == PreAssemblyStatus.NEED_TRANSFER,
                enabled = enabled,
                onClick = onTransfer,
                modifier = Modifier.weight(1f)
            )
            PreAssemblyCardActionDivider()
            PreAssemblyCardAction(
                text = "Не пров.",
                icon = Icons.Outlined.WarningAmber,
                color = FbsDarkMutedText,
                selected = status == PreAssemblyStatus.NOT_CHECKED,
                enabled = enabled,
                onClick = onUnchecked,
                modifier = Modifier.weight(1f)
            )
            PreAssemblyCardActionDivider()
            PreAssemblyCardAction(
                text = "Пропуск",
                icon = Icons.Outlined.Description,
                color = FbsDarkMutedText,
                enabled = enabled,
                onClick = onSkip,
                modifier = Modifier.weight(1f)
            )
            PreAssemblyCardActionDivider()
            PreAssemblyCardAction(
                text = "Заметка",
                icon = Icons.Outlined.Edit,
                color = FbsNeonGreen,
                selected = hasComment,
                enabled = enabled,
                onClick = onComment,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PreAssemblyCardAction(
    text: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val tint = if (selected) color else if (color == FbsNeonGreen) color else FbsDarkMutedText
    Column(
        modifier
            .fillMaxHeight()
            .alpha(if (enabled) 1f else 0.45f)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 3.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(if (selected) color.copy(alpha = 0.16f) else Color.Transparent)
                    .border(2.dp, tint.copy(alpha = if (selected) 0.96f else 0.78f), CircleShape)
            )
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text,
            color = if (selected) FbsDarkText else FbsDarkMutedText,
            fontSize = 9.sp,
            lineHeight = 10.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PreAssemblyCardActionDivider() {
    Box(
        Modifier
            .width(1.dp)
            .height(38.dp)
            .background(FbsDarkBorder.copy(alpha = 0.88f))
    )
}

private const val PRODUCT_IMAGE_CACHE_MAX_KB = 12 * 1024

private val preAssemblyProductImageCache = object : LruCache<String, ImageBitmap>(PRODUCT_IMAGE_CACHE_MAX_KB) {
    override fun sizeOf(key: String, value: ImageBitmap): Int =
        ((value.width.toLong() * value.height.toLong() * 4L) / 1024L)
            .coerceAtLeast(1L)
            .coerceAtMost(Int.MAX_VALUE.toLong())
            .toInt()
}

@Composable
private fun PreAssemblyProductPhoto(
    imageUrl: String?,
    status: PreAssemblyStatus,
    modifier: Modifier = Modifier
) {
    val imageBitmap by produceState<ImageBitmap?>(initialValue = null, imageUrl) {
        value = imageUrl?.let { url ->
            preAssemblyProductImageCache[url] ?: loadPreAssemblyProductImage(url)?.also { bitmap ->
                preAssemblyProductImageCache.put(url, bitmap)
            }
        }
    }
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier
            .size(width = 82.dp, height = 106.dp)
            .clip(shape)
            .background(FbsDarkPanelAlt)
            .border(1.2.dp, PreAssemblyStatusColor(status).copy(alpha = 0.50f), shape),
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
                modifier = Modifier.size(24.dp)
            )
        }
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp)
                .size(11.dp)
                .clip(CircleShape)
                .background(PreAssemblyStatusColor(status))
                .border(2.dp, FbsDarkPanelAlt, CircleShape)
        )
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
            .size(42.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .clip(CircleShape)
            .background(if (selected) color else FbsDarkPanelAlt)
            .border(1.2.dp, color.copy(alpha = if (selected) 0.95f else 0.42f), CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = if (selected) Color.White else color,
            modifier = Modifier.size(21.dp)
        )
    }
}

@Composable
private fun PreAssemblyQuickCommentButton(hasComment: Boolean, enabled: Boolean = true, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .clip(CircleShape)
            .background(if (hasComment) FbsNeonGreen.copy(alpha = 0.12f) else FbsDarkPanelAlt)
            .border(1.dp, if (hasComment) FbsNeonGreen.copy(alpha = 0.42f) else FbsDarkBorder, CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Outlined.Description,
            contentDescription = "Комментарий",
            tint = if (hasComment) FbsNeonGreen else FbsDarkMutedText,
            modifier = Modifier.size(20.dp)
        )
        if (hasComment) {
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(FbsNeonGreen)
                    .border(1.dp, FbsDarkPanel, CircleShape)
            )
        }
    }
}

@Composable
private fun PreAssemblyQuickCardButton(enabled: Boolean = true, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .clip(CircleShape)
            .background(FbsDarkPanelAlt)
            .border(1.dp, FbsDarkBorder, CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Outlined.Inventory2,
            contentDescription = "Открыть полную карточку",
            tint = FbsNeonGreen,
            modifier = Modifier.size(20.dp)
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
        PreAssemblyDarkCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppIconBubble(Icons.Outlined.Description, tint = FbsNeonGreen, background = FbsNeonGreen.copy(alpha = 0.12f), modifier = Modifier.size(42.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Комментарий", color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(item.name, color = FbsDarkMutedText, fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                    PreAssemblyDarkIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
                }

                FbsAssemblyTextField(
                    value = draft,
                    onValueChange = { draft = it },
                    label = "Примечание к товару",
                    placeholder = "Например: нет на полке / коробка повреждена",
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PreAssemblyDarkSecondaryButton(
                        "Очистить",
                        icon = Icons.Outlined.Delete,
                        danger = true,
                        onClick = {
                            draft = ""
                            vm.updateComment(item.id, "")
                        },
                        modifier = Modifier.weight(1f)
                    )
                    PreAssemblyDarkPrimaryButton(
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
                .background(FbsDarkPanelAlt)
                .border(1.dp, PreAssemblyStatusColor(selected).copy(alpha = 0.45f), RoundedCornerShape(16.dp))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PreAssemblyStatusDot(selected)
            Column(Modifier.weight(1f)) {
                Text("Статус проверки", color = FbsDarkMutedText, fontSize = 11.sp, maxLines = 1)
                Text(selected.title, color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.Outlined.ExpandMore, contentDescription = null, tint = PreAssemblyStatusColor(selected), modifier = Modifier.rotate(if (expanded) 180f else 0f))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(FbsDarkPanelAlt)) {
            PreAssemblyStatus.values().forEach { status ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(status.title, color = FbsDarkText, fontWeight = if (status == selected) FontWeight.Bold else FontWeight.SemiBold)
                            Text(PreAssemblyStatusHint(status), color = FbsDarkMutedText, fontSize = 12.sp)
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
            .padding(horizontal = 8.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        PreAssemblyStatusDot(status, size = 6.dp)
        Text(title, color = PreAssemblyStatusColor(status), fontWeight = FontWeight.Bold, fontSize = 10.sp, maxLines = 1)
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
        Text(title, color = FbsDarkMutedText, fontSize = 13.sp, modifier = Modifier.weight(0.8f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(value, color = FbsDarkText, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, textAlign = TextAlign.End, modifier = Modifier.weight(1.2f), maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun PreAssemblyNoResultsCard(onReset: () -> Unit) {
    PreAssemblyDarkCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            AppIconBubble(Icons.Outlined.Search, tint = FbsNeonGreen, background = FbsNeonGreen.copy(alpha = 0.12f))
            Text("Ничего не найдено", color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Очистите поиск или смените фильтр статуса.", color = FbsDarkMutedText, textAlign = TextAlign.Center)
            PreAssemblyDarkSecondaryButton("Сбросить фильтры", icon = Icons.Outlined.Close, onClick = onReset, modifier = Modifier.fillMaxWidth())
        }
    }
}



@Composable
private fun PreAssemblyVisibleInfoRow(
    visibleCount: Int,
    totalCount: Int,
    sortTitle: String,
    isCompleted: Boolean,
    hasActiveFilters: Boolean,
    onOpenFilters: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(if (isCompleted) SuccessColor.copy(alpha = 0.14f) else FbsDarkPanelAlt.copy(alpha = 0.96f))
            .border(1.dp, if (isCompleted) SuccessColor.copy(alpha = 0.38f) else FbsDarkBorder, RoundedCornerShape(18.dp))
            .padding(horizontal = 9.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(FbsNeonGreen.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Inventory2, contentDescription = null, tint = if (isCompleted) SuccessColor else FbsNeonGreen, modifier = Modifier.size(22.dp))
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                "Видимых: $visibleCount из $totalCount",
                color = FbsDarkText,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text("Сорт.: $sortTitle", color = FbsDarkMutedText, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        PreAssemblyFilterButton(
            active = hasActiveFilters,
            onClick = onOpenFilters
        )
    }
}

@Composable
private fun PreAssemblyFilterButton(active: Boolean, onClick: () -> Unit) {
    val color = if (active) Color.Black else FbsNeonGreen
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(15.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (active) FbsNeonGreen else Color.Transparent,
            contentColor = color
        ),
        border = BorderStroke(1.dp, FbsNeonGreen.copy(alpha = if (active) 0.88f else 0.54f)),
        modifier = Modifier.height(40.dp)
    ) {
        Icon(Icons.Outlined.Search, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(5.dp))
        Text(
            "Фильтры",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            lineHeight = 14.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun PreAssemblyCompletedBanner(completedAt: String?, hasProblems: Boolean, hasUnchecked: Boolean, onReturnToWork: () -> Unit) {
    val title = when {
        hasUnchecked -> "Завершена с непроверенными позициями"
        hasProblems -> "Завершена с проблемами"
        else -> "Завершена без проблем"
    }
    PreAssemblyDarkCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AppIconBubble(Icons.Outlined.CheckCircle, tint = SuccessColor, background = SuccessColor.copy(alpha = 0.14f), modifier = Modifier.size(42.dp))
                Column(Modifier.weight(1f)) {
                    Text(title, color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Дата завершения: ${completedAt ?: "—"}", color = FbsDarkMutedText, fontSize = 13.sp)
                }
            }
            Text("Позиции заблокированы от случайных изменений. Для исправлений верните сборку в работу.", color = FbsDarkMutedText, fontSize = 13.sp, lineHeight = 17.sp)
            PreAssemblyDarkSecondaryButton("Вернуть в работу", icon = Icons.Outlined.Archive, onClick = onReturnToWork, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun PreAssemblyArchiveDialog(
    archive: List<PreAssemblyArchiveEntry>,
    onOpenEntry: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val sortedArchive = remember(archive) { archive.sortedByDescending { it.completedAt } }
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        PreAssemblyDarkCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppIconBubble(Icons.Outlined.Archive, tint = FbsNeonGreen, background = FbsNeonGreen.copy(alpha = 0.12f), modifier = Modifier.size(42.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Архив сборок", color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                        Text("Завершённые предварительные сборки сохраняются здесь автоматически.", color = FbsDarkMutedText, fontSize = 13.sp)
                    }
                    PreAssemblyDarkIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
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
                        items(sortedArchive, key = { it.id }) { entry ->
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
            .background(FbsDarkPanelAlt)
            .border(1.dp, FbsDarkBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onOpen)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(entry.title, color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(entry.resultTitle, color = FbsDarkMutedText, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            PreAssemblyMetaChip(
                "${entry.total} поз.",
                background = if (entry.toTransfer == 0 && entry.notChecked == 0) SuccessColor.copy(alpha = 0.14f) else FbsNeonGreen.copy(alpha = 0.14f),
                contentColor = if (entry.toTransfer == 0 && entry.notChecked == 0) SuccessColor else FbsNeonGreen
            )
        }
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            if (maxWidth < 300.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        PreAssemblyArchiveStat("Пров.", entry.checked.toString(), FbsNeonGreen, Modifier.weight(1f))
                        PreAssemblyArchiveStat("Есть", entry.available.toString(), SuccessColor, Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        PreAssemblyArchiveStat("Перем.", entry.toTransfer.toString(), DangerColor, Modifier.weight(1f))
                        PreAssemblyArchiveStat("Не пров.", entry.notChecked.toString(), FbsDarkMutedText, Modifier.weight(1f))
                    }
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    PreAssemblyArchiveStat("Пров.", entry.checked.toString(), FbsNeonGreen, Modifier.weight(1f))
                    PreAssemblyArchiveStat("Есть", entry.available.toString(), SuccessColor, Modifier.weight(1f))
                    PreAssemblyArchiveStat("Перем.", entry.toTransfer.toString(), DangerColor, Modifier.weight(1f))
                    PreAssemblyArchiveStat("Не пров.", entry.notChecked.toString(), FbsDarkMutedText, Modifier.weight(1f))
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
            .background(FbsDarkPanel)
            .padding(horizontal = 6.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, color = valueColor, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, lineHeight = 15.sp, maxLines = 1)
        Text(title, color = FbsDarkMutedText, fontSize = 9.sp, lineHeight = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
        PreAssemblyDarkCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppIconBubble(Icons.Outlined.Description, tint = FbsNeonGreen, background = FbsNeonGreen.copy(alpha = 0.12f), modifier = Modifier.size(42.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(displayEntry.title, color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 17.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(displayEntry.completedAtText, color = FbsDarkMutedText, fontSize = 13.sp)
                    }
                    PreAssemblyDarkIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
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
            .background(FbsDarkPanelAlt.copy(alpha = 0.72f))
            .border(1.dp, FbsDarkBorder, RoundedCornerShape(18.dp))
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
        Text(title, color = FbsDarkMutedText, fontSize = 12.sp, modifier = Modifier.weight(0.85f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(
            value,
            color = FbsDarkText,
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
            .background(FbsDarkPanelAlt)
            .border(1.dp, PreAssemblyStatusColor(item.status).copy(alpha = 0.35f), RoundedCornerShape(18.dp))
            .padding(9.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(item.name, color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text("Арт. ${item.offerId} · ${item.requiredQuantity} шт.", color = FbsDarkMutedText, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
                    Text("К перемещению", color = FbsDarkMutedText, fontSize = 12.sp, modifier = Modifier.weight(1f))
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
                Text(item.comment, color = FbsDarkMutedText, fontSize = 12.sp, lineHeight = 16.sp)
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
                .background(FbsDarkPanel)
                .border(1.dp, PreAssemblyStatusColor(selected).copy(alpha = 0.5f), RoundedCornerShape(13.dp))
                .clickable { expanded = true }
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PreAssemblyStatusDot(selected, size = 8.dp)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(0.dp)) {
                Text("Статус", color = FbsDarkMutedText, fontSize = 10.sp, lineHeight = 11.sp, maxLines = 1)
                Text(selected.title, color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 13.sp, lineHeight = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.Outlined.ExpandMore, contentDescription = null, tint = PreAssemblyStatusColor(selected), modifier = Modifier.size(18.dp).rotate(if (expanded) 180f else 0f))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(FbsDarkPanelAlt)) {
            PreAssemblyStatus.values().forEach { status ->
                DropdownMenuItem(
                    text = {
                        Text(status.title, color = FbsDarkText, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
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
            .background(FbsDarkPanel)
            .border(1.2.dp, FbsNeonGreen, RoundedCornerShape(11.dp))
            .padding(horizontal = 8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = androidx.compose.ui.text.TextStyle(
            color = FbsDarkText,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 13.sp,
            textAlign = TextAlign.End
        ),
        decorationBox = { innerTextField ->
            Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                    if (value.isBlank()) {
                        Text("0", color = FbsDarkMutedText, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    innerTextField()
                }
                Spacer(Modifier.width(4.dp))
                Text("шт.", color = FbsDarkText, fontWeight = FontWeight.SemiBold, fontSize = 10.sp, maxLines = 1)
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
            .background(FbsDarkPanel)
            .border(1.dp, FbsDarkBorder, RoundedCornerShape(13.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        textStyle = androidx.compose.ui.text.TextStyle(
            color = FbsDarkText,
            fontSize = 13.sp,
            lineHeight = 16.sp
        ),
        decorationBox = { innerTextField ->
            Box(Modifier.fillMaxSize()) {
                if (value.isBlank()) {
                    Text("Комментарий", color = FbsDarkMutedText, fontSize = 12.sp, lineHeight = 15.sp)
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
            colors = ButtonDefaults.buttonColors(containerColor = FbsNeonGreen, contentColor = Color.Black)
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
            border = BorderStroke(1.dp, FbsDarkBorder),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = FbsDarkPanelAlt, contentColor = FbsDarkText)
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
Товар: ${PreAssemblyProductNames.nameFor(item)}
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
        PreAssemblyDarkCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Дополнительно", color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                        Text("Архив и действия с видимыми позициями.", color = FbsDarkMutedText, fontSize = 13.sp)
                    }
                    PreAssemblyDarkIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
                }
                if (archive.isNotEmpty()) {
                    PreAssemblyArchiveActionRow(archive = archive, onClick = onOpenArchive)
                    HorizontalDivider(color = FbsDarkBorder.copy(alpha = 0.82f))
                }
                FbsAssemblyTextField(
                    value = printBridgeHost,
                    onValueChange = onPrintBridgeHostChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = "IP моста печати",
                    placeholder = "192.168.10.104",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    leadingIcon = Icons.Outlined.Settings
                )
                FbsAssemblyTextField(
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
                HorizontalDivider(color = FbsDarkBorder.copy(alpha = 0.82f))
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
            .background(FbsDarkPanelAlt)
            .border(1.dp, FbsDarkBorder, RoundedCornerShape(16.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(icon, contentDescription = null, tint = FbsNeonGreen, modifier = Modifier.size(21.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, color = FbsDarkText, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(subtitle, color = FbsDarkMutedText, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Icon(Icons.Outlined.ExpandMore, contentDescription = null, tint = FbsDarkMutedText, modifier = Modifier.rotate(-90f))
    }
}

@Composable
private fun PreAssemblyArchiveActionRow(archive: List<PreAssemblyArchiveEntry>, onClick: () -> Unit) {
    val latest = archive.maxByOrNull { it.completedAt }
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(FbsDarkPanelAlt)
            .border(1.dp, FbsDarkBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(Icons.Outlined.Archive, contentDescription = null, tint = FbsNeonGreen, modifier = Modifier.size(21.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("Архив предварительных сборок", color = FbsDarkText, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                latest?.let { "Последняя: ${it.completedAtText}, позиций: ${it.total}" } ?: "Открыть архив",
                color = FbsDarkMutedText,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        PreAssemblyMetaChip(archive.size.toString(), background = FbsNeonGreen.copy(alpha = 0.14f), contentColor = FbsNeonGreen)
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
            .background(FbsDarkPanelAlt)
            .border(1.dp, FbsDarkBorder, RoundedCornerShape(16.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(icon, contentDescription = null, tint = FbsNeonGreen, modifier = Modifier.size(21.dp))
        Text(action.title, color = FbsDarkText, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Icon(Icons.Outlined.ExpandMore, contentDescription = null, tint = FbsDarkMutedText, modifier = Modifier.rotate(-90f))
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
        PreAssemblyDarkCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(action.confirmTitle, color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                Text(
                    when (action) {
                        PreAssemblyBulkAction.MARK_AVAILABLE -> "Отметить $visibleCount позиций как “Есть”? Будут изменены только товары, которые сейчас отображаются после поиска и фильтра."
                        PreAssemblyBulkAction.RESET_CHECK -> "Сбросить проверку у $visibleCount позиций? Комментарии сохранятся, а статус станет “Не проверено”."
                        PreAssemblyBulkAction.CLEAR_COMMENTS -> "Очистить комментарии у $visibleCount позиций? Статусы и количество к перемещению не изменятся."
                    },
                    color = FbsDarkMutedText,
                    fontSize = 14.sp,
                    lineHeight = 19.sp
                )
                Text("Видимых позиций: $visibleCount из $totalCount", color = FbsNeonGreen, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PreAssemblyDarkSecondaryButton("Отмена", icon = Icons.Outlined.Close, onClick = onDismiss, modifier = Modifier.weight(1f))
                    PreAssemblyDarkPrimaryButton(action.successButton, icon = Icons.Outlined.CheckCircle, onClick = onConfirm, modifier = Modifier.weight(1f))
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
        PreAssemblyDarkCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppIconBubble(
                        Icons.Outlined.CheckCircle,
                        tint = if (isCompleted) SuccessColor else FbsNeonGreen,
                        background = if (isCompleted) SuccessColor.copy(alpha = 0.14f) else FbsNeonGreen.copy(alpha = 0.12f),
                        modifier = Modifier.size(42.dp)
                    )
                    Column(Modifier.weight(1f)) {
                        Text(if (isCompleted) "Предварительная сборка завершена" else "Завершить предварительную сборку?", color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                        Text(if (isCompleted) "Завершено: ${completedAt ?: "—"}" else "Проверьте итог перед фиксацией результата.", color = FbsDarkMutedText, fontSize = 13.sp)
                    }
                    PreAssemblyDarkIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(FbsDarkPanelAlt.copy(alpha = 0.72f))
                        .border(1.dp, FbsDarkBorder, RoundedCornerShape(18.dp))
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
                        PreAssemblyDarkSecondaryButton("Закрыть", icon = Icons.Outlined.Close, onClick = onDismiss, modifier = Modifier.weight(1f))
                        PreAssemblyDarkPrimaryButton("Вернуть в работу", icon = Icons.Outlined.Archive, onClick = onReturnToWork, modifier = Modifier.weight(1f))
                    }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PreAssemblyDarkSecondaryButton("Вернуться", icon = Icons.Outlined.Close, onClick = onDismiss, modifier = Modifier.weight(1f))
                        PreAssemblyDarkPrimaryButton(if (notChecked > 0) "Завершить всё равно" else "Завершить", icon = Icons.Outlined.CheckCircle, onClick = onFinish, modifier = Modifier.weight(1f))
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
        PreAssemblyDarkCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Поиск, фильтр и сортировка", color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                        Text("Сортировка “Что проверять дальше” поднимает наверх непроверенные и проблемные товары.", color = FbsDarkMutedText, fontSize = 13.sp)
                    }
                    PreAssemblyDarkIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
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
        PreAssemblyDarkCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text("Статус позиции", color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Выберите результат проверки для товара.", color = FbsDarkMutedText, fontSize = 13.sp)
                    }
                    PreAssemblyDarkIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
                }
                PreAssemblyStatus.values().forEach { status ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (status == selected) PreAssemblyStatusBackground(status) else FbsDarkPanelAlt)
                            .border(1.dp, if (status == selected) PreAssemblyStatusColor(status).copy(alpha = 0.42f) else FbsDarkBorder, RoundedCornerShape(16.dp))
                            .clickable {
                                onSelected(status)
                            }
                            .padding(horizontal = 12.dp, vertical = 11.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        PreAssemblyStatusDot(status)
                        Column(Modifier.weight(1f)) {
                            Text(status.title, color = FbsDarkText, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text(PreAssemblyStatusHint(status), color = FbsDarkMutedText, fontSize = 12.sp)
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
        PreAssemblyDarkCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("Карточка заказа", color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                        Text(item.name, color = FbsDarkText, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 18.sp, maxLines = 3, overflow = TextOverflow.Ellipsis)
                    }
                    PreAssemblyDarkIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
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
                        .background(FbsDarkPanelAlt.copy(alpha = 0.72f))
                        .border(1.dp, FbsDarkBorder, RoundedCornerShape(18.dp))
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
                    FbsAssemblyTextField(
                        item.transferQuantity,
                        { vm.updateTransferQuantity(item.id, it) },
                        label = "Сколько переместить",
                        placeholder = "0",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                FbsAssemblyTextField(
                    item.comment,
                    { vm.updateComment(item.id, it) },
                    label = "Комментарий",
                    placeholder = "Например: нет на полке / нужно со склада",
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PreAssemblyDarkSecondaryButton("Закрыть", icon = Icons.Outlined.Close, onClick = onDismiss, modifier = Modifier.weight(1f))
                    PreAssemblyDarkPrimaryButton("Готово", icon = Icons.Outlined.CheckCircle, onClick = onDismiss, modifier = Modifier.weight(1f))
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
            .background(FbsNeonGreen.copy(alpha = 0.14f))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            quantity.toString(),
            color = FbsNeonGreen,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            lineHeight = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            "шт.",
            color = FbsNeonGreen,
            fontWeight = FontWeight.SemiBold,
            fontSize = 9.sp,
            lineHeight = 10.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun PreAssemblyMetaChip(
    text: String,
    background: Color = FbsNeonGreen.copy(alpha = 0.12f),
    contentColor: Color = FbsNeonGreen,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .clip(RoundedCornerShape(999.dp))
            .background(background)
            .padding(horizontal = 7.dp, vertical = 3.dp)
    ) {
        Text(
            text,
            color = contentColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
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
            .background(FbsDarkPanel)
            .border(1.4.dp, FbsNeonGreen, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = androidx.compose.ui.text.TextStyle(
            color = FbsDarkText,
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
                            color = FbsDarkMutedText,
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
                    color = FbsDarkText,
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
        Text(title, color = FbsDarkMutedText, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(value, color = FbsDarkText, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, textAlign = TextAlign.End, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
            colors = ButtonDefaults.buttonColors(containerColor = FbsNeonGreen, contentColor = Color.Black)
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
            border = BorderStroke(1.dp, FbsDarkBorder),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = FbsDarkPanelAlt, contentColor = FbsDarkText)
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
    compact: Boolean = false,
    onOpenControls: () -> Unit,
    onOpenBulkActions: () -> Unit,
    onOpenArchive: () -> Unit,
    onBuildReport: () -> Unit,
    onFinishAssembly: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
    Row(
        modifier
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(
                        FbsDarkPanelAlt.copy(alpha = 0.99f),
                        FbsDarkBackground.copy(alpha = 0.98f)
                    )
                )
            )
            .border(1.dp, FbsDarkBorder.copy(alpha = 0.95f), shape)
            .padding(horizontal = if (compact) 5.dp else 10.dp, vertical = if (compact) 6.dp else 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(if (compact) 2.dp else 4.dp)
    ) {
        PreAssemblyBottomNavItem(
            text = "Обзор",
            icon = Icons.Outlined.Description,
            enabled = hasItems,
            compact = compact,
            onClick = onOpenControls,
            modifier = Modifier.weight(1f)
        )
        PreAssemblyBottomNavItem(
            text = "Проверка",
            icon = Icons.Outlined.CheckCircle,
            active = true,
            compact = compact,
            onClick = onOpenControls,
            modifier = Modifier.weight(1f)
        )
        PreAssemblyBottomNavItem(
            text = "Перем.",
            icon = Icons.Outlined.Inventory2,
            enabled = hasItems,
            active = hasReport,
            compact = compact,
            onClick = onBuildReport,
            modifier = Modifier.weight(1f)
        )
        PreAssemblyBottomNavItem(
            text = "История",
            icon = Icons.Outlined.History,
            enabled = hasArchive,
            compact = compact,
            onClick = onOpenArchive,
            modifier = Modifier.weight(1f)
        )
        PreAssemblyBottomNavItem(
            text = "Настр.",
            icon = Icons.Outlined.Settings,
            enabled = hasArchive || (hasItems && hasVisibleItems && !isCompleted),
            active = hasActiveFilters || isCompleted,
            compact = compact,
            onClick = onOpenBulkActions,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PreAssemblyBottomNavItem(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    active: Boolean = false,
    enabled: Boolean = true,
    compact: Boolean = false,
    onClick: () -> Unit
) {
    val contentColor = when {
        active -> FbsNeonGreen
        enabled -> FbsDarkMutedText
        else -> FbsDarkMutedText.copy(alpha = 0.48f)
    }
    val shape = RoundedCornerShape(if (compact) 16.dp else 24.dp)
    Column(
        modifier
            .height(if (compact) 60.dp else 88.dp)
            .alpha(if (enabled) 1f else 0.48f)
            .clip(shape)
            .background(if (active) FbsDarkPanelAlt.copy(alpha = 0.92f) else Color.Transparent)
            .border(
                1.dp,
                if (active) FbsDarkBorder.copy(alpha = 0.95f) else Color.Transparent,
                shape
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 2.dp, vertical = if (compact) 4.dp else 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(if (compact) 21.dp else 29.dp))
            if (active) {
                Box(
                    Modifier
                        .offset(x = 5.dp, y = (-4).dp)
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(FbsNeonGreen)
                )
            }
        }
        Spacer(Modifier.height(if (compact) 3.dp else 5.dp))
        Text(
            text,
            color = contentColor,
            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
            fontSize = if (compact) 9.sp else 12.sp,
            lineHeight = if (compact) 11.sp else 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
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
                containerColor = FbsNeonGreen,
                contentColor = Color.Black,
                disabledContainerColor = FbsDarkBorder,
                disabledContentColor = FbsDarkMutedText
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
            border = BorderStroke(1.dp, FbsDarkBorder),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = FbsDarkPanelAlt,
                contentColor = FbsNeonGreen,
                disabledContainerColor = FbsDarkPanel,
                disabledContentColor = FbsDarkMutedText
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
            .background(WarningColor.copy(alpha = 0.16f))
            .border(1.dp, WarningColor.copy(alpha = 0.42f), RoundedCornerShape(18.dp))
            .padding(horizontal = 14.dp, vertical = 11.dp)
    ) {
        Text(message, color = WarningColor, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
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
        PreAssemblyDarkCard(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppIconBubble(Icons.Outlined.Description, tint = FbsNeonGreen, background = FbsNeonGreen.copy(alpha = 0.12f), modifier = Modifier.size(42.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Список на перемещение", color = FbsDarkText, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                        Text("Проверьте текст перед отправкой бухгалтеру", color = FbsDarkMutedText, fontSize = 13.sp)
                    }
                    PreAssemblyDarkIconActionButton(Icons.Outlined.Close, "Закрыть", onClick = onDismiss)
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(FbsDarkPanelAlt.copy(alpha = 0.72f))
                        .border(1.dp, FbsDarkBorder, RoundedCornerShape(18.dp))
                        .padding(12.dp)
                ) {
                    item {
                        Text(reportText, color = FbsDarkText, fontSize = 14.sp, lineHeight = 20.sp)
                    }
                }
                Text(
                    "Итого позиций: ${reportText.lines().count { it.trim().matches(Regex("\\d+\\..*")) }}",
                    color = FbsDarkMutedText,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                BoxWithConstraints(Modifier.fillMaxWidth()) {
                    if (maxWidth < 430.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            PreAssemblyDarkSecondaryButton("Скопировать", icon = Icons.Outlined.Description, onClick = onCopy, modifier = Modifier.fillMaxWidth())
                            PreAssemblyDarkSecondaryButton("Напечатать", icon = Icons.Outlined.Print, onClick = onPrint, modifier = Modifier.fillMaxWidth())
                            PreAssemblyDarkPrimaryButton("Печать + отправка", icon = Icons.AutoMirrored.Outlined.Send, onClick = onShare, modifier = Modifier.fillMaxWidth())
                        }
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            PreAssemblyDarkSecondaryButton("Скопировать", icon = Icons.Outlined.Description, onClick = onCopy, modifier = Modifier.weight(0.95f))
                            PreAssemblyDarkSecondaryButton("Напечатать", icon = Icons.Outlined.Print, onClick = onPrint, modifier = Modifier.weight(0.95f))
                            PreAssemblyDarkPrimaryButton("Печать + отправка", icon = Icons.AutoMirrored.Outlined.Send, onClick = onShare, modifier = Modifier.weight(1.2f))
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
    PreAssemblyStatus.NOT_CHECKED -> FbsDarkMutedText
    PreAssemblyStatus.AVAILABLE -> SuccessColor
    PreAssemblyStatus.NOT_AVAILABLE -> DangerColor
    PreAssemblyStatus.NEED_TRANSFER -> WarningColor
}

private fun PreAssemblyStatusBackground(status: PreAssemblyStatus): Color = when (status) {
    PreAssemblyStatus.NOT_CHECKED -> FbsDarkBorder.copy(alpha = 0.72f)
    PreAssemblyStatus.AVAILABLE -> SuccessColor.copy(alpha = 0.14f)
    PreAssemblyStatus.NOT_AVAILABLE -> DangerColor.copy(alpha = 0.14f)
    PreAssemblyStatus.NEED_TRANSFER -> WarningColor.copy(alpha = 0.16f)
}

private fun PreAssemblyStatusHint(status: PreAssemblyStatus): String = when (status) {
    PreAssemblyStatus.NOT_CHECKED -> "позиция ещё не проверялась"
    PreAssemblyStatus.AVAILABLE -> "товар есть, перемещение не нужно"
    PreAssemblyStatus.NOT_AVAILABLE -> "товара нет, нужно указать количество"
    PreAssemblyStatus.NEED_TRANSFER -> "товар есть частично, нужно переместить"
}
