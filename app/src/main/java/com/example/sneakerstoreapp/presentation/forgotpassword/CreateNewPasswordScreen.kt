/**
 * Этот файл описывает Compose-экран слоя presentation.
 *
 * Здесь собирается интерфейс, отображается состояние экрана и обрабатываются действия пользователя.
 */
package com.example.sneakerstoreapp.presentation.forgotpassword

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sneakerstoreapp.presentation.auth.AuthHeader
import com.example.sneakerstoreapp.presentation.auth.BackButton
import com.example.sneakerstoreapp.presentation.auth.PasswordInputField
import com.example.sneakerstoreapp.presentation.auth.SimpleLabel
import com.example.sneakerstoreapp.ui.theme.ScreenBackground
import com.example.sneakerstoreapp.ui.theme.SneakerBlue

// Этот экран нужен для ввода и сохранения нового пароля после успешного OTP.
@Composable
fun CreateNewPasswordScreen(
    onBackClick: () -> Unit,
    onPasswordSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateNewPasswordViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isConfirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.message) {
        val message = uiState.message ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        if (uiState.isSuccess) {
            onPasswordSaved()
        }

        viewModel.clearMessage()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        BackButton(onClick = onBackClick)

        Spacer(modifier = Modifier.height(54.dp))

        AuthHeader(
            title = "Задать Новый Пароль",
            subtitle = "Установите новый пароль для входа в\nвашу учетную запись"
        )

        Spacer(modifier = Modifier.height(52.dp))

        SimpleLabel(text = "Пароль")
        Spacer(modifier = Modifier.height(12.dp))
        PasswordInputField(
            value = password,
            onValueChange = { password = it },
            placeholder = "••••••••",
            isPasswordVisible = isPasswordVisible,
            onPasswordVisibilityChange = { isPasswordVisible = !isPasswordVisible }
        )

        Spacer(modifier = Modifier.height(24.dp))

        SimpleLabel(text = "Подтверждение пароля")
        Spacer(modifier = Modifier.height(12.dp))
        PasswordInputField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = "••••••••",
            isPasswordVisible = isConfirmPasswordVisible,
            onPasswordVisibilityChange = { isConfirmPasswordVisible = !isConfirmPasswordVisible }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.savePassword(password, confirmPassword) },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            enabled = !uiState.isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = SneakerBlue,
                contentColor = Color.White
            )
        ) {
            Text(
                text = if (uiState.isLoading) "Сохранение..." else "Сохранить",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
