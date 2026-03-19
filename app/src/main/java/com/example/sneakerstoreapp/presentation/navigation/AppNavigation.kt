package com.example.sneakerstoreapp.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.sneakerstoreapp.data.remote.SupabaseClientProvider
import com.example.sneakerstoreapp.presentation.catalog.CatalogScreen
import com.example.sneakerstoreapp.presentation.favorite.FavoriteScreen
import com.example.sneakerstoreapp.presentation.forgotpassword.CreateNewPasswordScreen
import com.example.sneakerstoreapp.presentation.forgotpassword.ForgotPasswordScreen
import com.example.sneakerstoreapp.presentation.forgotpassword.OtpVerificationScreen
import com.example.sneakerstoreapp.presentation.home.HomeScreen
import com.example.sneakerstoreapp.presentation.login.LoginScreen
import com.example.sneakerstoreapp.presentation.profile.LoyaltyCardScreen
import com.example.sneakerstoreapp.presentation.profile.ProfileScreen
import com.example.sneakerstoreapp.presentation.register.RegisterScreen
import com.example.sneakerstoreapp.ui.theme.ScreenBackground
import com.example.sneakerstoreapp.ui.theme.SneakerBlue
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

// Этот файл хранит простую навигацию приложения между основными экранами.
private enum class AppScreen {
    Login,
    Register,
    ForgotPassword,
    OtpVerification,
    CreateNewPassword,
    Home,
    Catalog,
    Favorite,
    Profile,
    LoyaltyCard
}

@Composable
fun SneakerStoreApp() {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.Login) }
    var catalogCategoryId by rememberSaveable { mutableStateOf<String?>(null) }
    var catalogQuery by rememberSaveable { mutableStateOf("") }
    var recoveryEmail by rememberSaveable { mutableStateOf("") }
    var isSessionChecked by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // При старте пробуем восстановить сохраненную auth-сессию.
    LaunchedEffect(Unit) {
        runCatching {
            SupabaseClientProvider.client.auth.loadFromStorage()
        }
        if (SupabaseClientProvider.client.auth.currentUserOrNull() != null) {
            currentScreen = AppScreen.Home
        }
        isSessionChecked = true
    }

    // Выход очищает сохраненную сессию и возвращает пользователя на логин.
    val onLogout = {
        scope.launch {
            SupabaseClientProvider.client.auth.signOut()
            currentScreen = AppScreen.Login
        }
        Unit
    }

    if (!isSessionChecked) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ScreenBackground),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SneakerBlue)
        }
        return
    }

    when (currentScreen) {
        AppScreen.Login -> {
            LoginScreen(
                onBackClick = { currentScreen = AppScreen.Register },
                onCreateAccountClick = { currentScreen = AppScreen.Register },
                onForgotPasswordClick = { currentScreen = AppScreen.ForgotPassword },
                onLoginSuccess = { currentScreen = AppScreen.Home }
            )
        }

        AppScreen.Register -> {
            RegisterScreen(
                onBackClick = { currentScreen = AppScreen.Login },
                onSignInClick = { currentScreen = AppScreen.Login },
                onRegisterSuccess = { currentScreen = AppScreen.Home }
            )
        }

        AppScreen.ForgotPassword -> {
            ForgotPasswordScreen(
                onBackClick = { currentScreen = AppScreen.Login },
                onCodeSent = { email ->
                    recoveryEmail = email
                    currentScreen = AppScreen.OtpVerification
                }
            )
        }

        AppScreen.OtpVerification -> {
            OtpVerificationScreen(
                email = recoveryEmail,
                onBackClick = { currentScreen = AppScreen.ForgotPassword },
                onVerificationSuccess = { currentScreen = AppScreen.CreateNewPassword }
            )
        }

        AppScreen.CreateNewPassword -> {
            CreateNewPasswordScreen(
                onBackClick = { currentScreen = AppScreen.OtpVerification },
                onPasswordSaved = { currentScreen = AppScreen.Home }
            )
        }

        AppScreen.Home -> {
            HomeScreen(
                onSearchClick = {
                    catalogCategoryId = null
                    catalogQuery = ""
                    currentScreen = AppScreen.Catalog
                },
                onCategoryClick = { categoryId ->
                    catalogCategoryId = categoryId
                    catalogQuery = ""
                    currentScreen = AppScreen.Catalog
                },
                onShowAllPopularClick = {
                    catalogCategoryId = null
                    catalogQuery = ""
                    currentScreen = AppScreen.Catalog
                },
                onFavoriteClick = {
                    currentScreen = AppScreen.Favorite
                },
                onProfileClick = {
                    currentScreen = AppScreen.Profile
                },
                onLogoutClick = onLogout
            )
        }

        AppScreen.Catalog -> {
            CatalogScreen(
                selectedCategoryId = catalogCategoryId,
                initialQuery = catalogQuery,
                onBackClick = { currentScreen = AppScreen.Home }
            )
        }

        AppScreen.Favorite -> {
            FavoriteScreen(
                onBackClick = { currentScreen = AppScreen.Home },
                onHomeClick = { currentScreen = AppScreen.Home },
                onProfileClick = { currentScreen = AppScreen.Profile }
            )
        }

        AppScreen.Profile -> {
            ProfileScreen(
                onHomeClick = { currentScreen = AppScreen.Home },
                onFavoriteClick = { currentScreen = AppScreen.Favorite },
                onBarcodeClick = { currentScreen = AppScreen.LoyaltyCard },
                onLogoutClick = onLogout
            )
        }

        AppScreen.LoyaltyCard -> {
            LoyaltyCardScreen(
                onBackClick = { currentScreen = AppScreen.Profile }
            )
        }
    }
}
