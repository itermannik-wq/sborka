package com.boldrex.postavki

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.content.Intent
import android.graphics.Color as AndroidColor
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

private val LightColors = lightColorScheme(
    primary = Color(0xFF2F5DFF),
    onPrimary = Color.White,
    secondary = Color(0xFF5C6AC4),
    onSecondary = Color.White,
    tertiary = Color(0xFF00A7C4),
    onTertiary = Color.White,
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF141B34),
    background = Color(0xFFEAF1FF),
    onBackground = Color(0xFF111930)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9EB2FF),
    onPrimary = Color(0xFF07113A),
    secondary = Color(0xFFB0BBFF),
    onSecondary = Color(0xFF101A44),
    tertiary = Color(0xFF63D9EE),
    onTertiary = Color(0xFF00262D),
    surface = Color(0xFF111730),
    onSurface = Color(0xFFE5EBFF),
    background = Color(0xFF0A1024),
    onBackground = Color(0xFFDEE6FF)
)

@Composable
private fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (androidx.compose.foundation.isSystemInDarkTheme()) DarkColors else LightColors,
        content = content
    )
}

class MainActivity : ComponentActivity() {
    companion object {
        const val ACTION_NEW_SHIPMENT = "com.boldrex.postavki.action.NEW_SHIPMENT"
        const val ACTION_IMPORT_REPORTS = "com.boldrex.postavki.action.IMPORT_REPORTS"
        const val SHORTCUT_ID_NEW_SHIPMENT = "new_shipment"
        const val SHORTCUT_ID_IMPORT_REPORTS = "import_reports"
    }
    private val vm: AppViewModel by viewModels { AppViewModel.factory(application) }
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) scheduleNotificationsIfAllowed()
    }
    private var notificationsScheduledThisSession = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureFullscreenChrome()
        FbsOrderNotificationService(applicationContext).ensureChannels()
        if (!requestNotificationPermission()) {
            scheduleNotificationsIfAllowed()
        }
        dispatchShortcut(intent)
        setContent {
            AppTheme {
                AppRoot(vm)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        scheduleNotificationsIfAllowed()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) configureFullscreenChrome()
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        dispatchShortcut(intent)
    }

    private fun configureFullscreenChrome() {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT)
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.systemBars())
        }
    }

    private fun dispatchShortcut(intent: Intent?) {
        val shortcutId = intent?.getStringExtra(Intent.EXTRA_SHORTCUT_ID)
        val trustedShortcut = shortcutId == SHORTCUT_ID_NEW_SHIPMENT || shortcutId == SHORTCUT_ID_IMPORT_REPORTS
        val trustedAction = intent?.action?.takeIf { action ->
            trustedShortcut && (action == ACTION_NEW_SHIPMENT || action == ACTION_IMPORT_REPORTS)
        }
        vm.handleLauncherShortcut(trustedAction, shortcutId)
    }

    private fun requestNotificationPermission(): Boolean {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            return true
        }
        return false
    }

    private fun scheduleNotificationsIfAllowed() {
        if (notificationsScheduledThisSession) return
        if (FbsOrderNotificationService(applicationContext).notificationsAllowed()) {
            notificationsScheduledThisSession = true
            FbsOrderNotificationService.schedule(applicationContext)
        }
    }
}
