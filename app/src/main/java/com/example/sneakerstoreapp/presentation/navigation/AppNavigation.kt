package com.example.sneakerstoreapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.sneakerstoreapp.presentation.forgotpassword.ForgotPasswordScreen
import com.example.sneakerstoreapp.presentation.login.LoginScreen
import com.example.sneakerstoreapp.presentation.register.RegisterScreen

private enum class AppScreen {
    Login,
    Register,
    ForgotPassword
}

@Composable
fun SneakerStoreApp() {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.Login) }

    when (currentScreen) {
        AppScreen.Login -> {
            LoginScreen(
                onBackClick = { currentScreen = AppScreen.Register },
                onCreateAccountClick = { currentScreen = AppScreen.Register },
                onForgotPasswordClick = { currentScreen = AppScreen.ForgotPassword }
            )
        }

        AppScreen.Register -> {
            RegisterScreen(
                onBackClick = { currentScreen = AppScreen.Login },
                onSignInClick = { currentScreen = AppScreen.Login }
            )
        }

        AppScreen.ForgotPassword -> {
            ForgotPasswordScreen(
                onBackClick = { currentScreen = AppScreen.Login }
            )
        }
    }
}
