package com.example.sneakerstoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.example.sneakerstoreapp.presentation.splash.SplashScreen
import com.example.sneakerstoreapp.ui.theme.SneakerStoreAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = true

        setContent {
            SneakerStoreAppTheme {
                SplashScreen()
            }
        }
    }
}
