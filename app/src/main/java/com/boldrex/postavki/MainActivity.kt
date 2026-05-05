package com.boldrex.postavki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

private val LightColors = lightColorScheme(
    primary = Color(0xFF00B8D4),
    onPrimary = Color(0xFF001018),
    secondary = Color(0xFF009FB7),
    onSecondary = Color(0xFF001419),
    tertiary = Color(0xFF46E3D6),
    onTertiary = Color(0xFF001514),
    surface = Color(0xFF0A1118),
    onSurface = Color(0xFFD9F6FF),
    background = Color(0xFF05080D),
    onBackground = Color(0xFFC5F4F6)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF1AD5FF),
    onPrimary = Color(0xFF001018),
    secondary = Color(0xFF22BFD8),
    onSecondary = Color(0xFF001216),
    tertiary = Color(0xFF6CFFF2),
    onTertiary = Color(0xFF00110F),
    surface = Color(0xFF020508),
    onSurface = Color(0xFFD8F7FF),
    background = Color(0xFF000000),
    onBackground = Color(0xFFB7F5F2)
)

@Composable
private fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (androidx.compose.foundation.isSystemInDarkTheme()) DarkColors else LightColors,
        content = content
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val vm: AppViewModel = viewModel(factory = AppViewModel.factory(application))
                AppRoot(vm)
            }
        }
    }
}
