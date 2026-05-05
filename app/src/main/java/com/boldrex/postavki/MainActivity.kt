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
