package com.example.sneakerstoreapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = SneakerBlue,
    background = SneakerBlue,
    surface = White,
    onPrimary = White,
    onBackground = White,
    onSurface = DarkText
)

@Composable
fun SneakerStoreAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}
