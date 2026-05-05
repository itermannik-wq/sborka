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
    primary = Color(0xFF0B6E4F),
    secondary = Color(0xFF4FA3D1),
    tertiary = Color(0xFFF4A259),
    surface = Color(0xFFFFFFFF),
    background = Color(0xFFF1F7F4)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF55D6A9),
    secondary = Color(0xFF8ECDF5),
    tertiary = Color(0xFFFFC78D)
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
