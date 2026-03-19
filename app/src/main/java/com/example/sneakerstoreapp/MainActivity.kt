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
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = true

        setContent {
            SneakerStoreAppTheme {
                SneakerStoreApp()
            }
        }
    }
}
