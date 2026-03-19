/**
 * Этот файл отвечает за часть темы приложения.
 *
 * Здесь лежат цвета, типографика и общая настройка внешнего вида Compose.
 */
package com.example.sneakerstoreapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    // Основные цвета приложения.
    primary = SneakerBlue,
    background = ScreenBackground,
    surface = White,
    onPrimary = White,
    onBackground = DarkText,
    onSurface = DarkText
)

@Composable
fun SneakerStoreAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        // Передаем в общую тему цвета и типографику приложения.
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}
