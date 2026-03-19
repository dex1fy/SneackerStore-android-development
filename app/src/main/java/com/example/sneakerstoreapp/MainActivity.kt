/**
 * Этот файл содержит главную activity приложения.
 *
 * Здесь подключается системный splash screen и запускается корневой Compose-граф приложения.
 */
package com.example.sneakerstoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.sneakerstoreapp.presentation.navigation.SneakerStoreApp
import com.example.sneakerstoreapp.ui.theme.SneakerStoreAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Подключаем системный splash до создания activity.
        installSplashScreen()
        super.onCreate(savedInstanceState)
        // Разрешаем контенту рисоваться под системными барами.
        enableEdgeToEdge()
        // Делаем иконки статус-бара темными под светлый интерфейс.
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = true

        setContent {
            SneakerStoreAppTheme {
                // Запускаем корневую Compose-навигацию приложения.
                SneakerStoreApp()
            }
        }
    }
}
