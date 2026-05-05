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
    primary = Color(0xFF6D5EF7),
    secondary = Color(0xFF28B6A8),
    tertiary = Color(0xFFFF7A59),
    surface = Color(0xFFFFFFFF),
    background = Color(0xFFF4F7FF)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFA89DFF),
    secondary = Color(0xFF67D9CD),
    tertiary = Color(0xFFFFAD94)
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
