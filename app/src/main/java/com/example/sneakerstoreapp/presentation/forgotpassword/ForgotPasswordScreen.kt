/**
 * Этот файл описывает Compose-экран слоя presentation.
 *
 * Здесь собирается интерфейс, отображается состояние экрана и обрабатываются действия пользователя.
 */
package com.example.sneakerstoreapp.presentation.forgotpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sneakerstoreapp.presentation.auth.AuthHeader
import com.example.sneakerstoreapp.presentation.auth.BackButton
import com.example.sneakerstoreapp.presentation.auth.SimpleInputField
import com.example.sneakerstoreapp.ui.theme.ScreenBackground
import com.example.sneakerstoreapp.ui.theme.SecondaryText
import com.example.sneakerstoreapp.ui.theme.SneakerBlue

// Этот экран отправляет recovery-код на email и показывает popup перед переходом к OTP.
@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    onCodeSent: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: ForgotPasswordViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var email by rememberSaveable { mutableStateOf("") }
    var showSuccessPopup by rememberSaveable { mutableStateOf(false) }

    // После успешной отправки кода открываем popup.
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            showSuccessPopup = true
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            BackButton(onClick = onBackClick)

            Spacer(modifier = Modifier.height(54.dp))

            AuthHeader(
                title = "Забыл Пароль",
                subtitle = "Введите Свою Учетную Запись\nДля Сброса"
            )

            Spacer(modifier = Modifier.height(56.dp))

            SimpleInputField(
                value = email,
                onValueChange = { email = it },
                placeholder = "xyz@gmail.com",
                keyboardType = KeyboardType.Email
            )

            if (uiState.message != null && !uiState.isSuccess) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = uiState.message.orEmpty(),
                    fontSize = 13.sp,
                    color = Color(0xFFE74C3C)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { viewModel.resetPassword(email) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SneakerBlue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (uiState.isLoading) "Подождите..." else "Отправить",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        if (showSuccessPopup) {
            ForgotPasswordSuccessPopup(
                onContinueClick = {
                    showSuccessPopup = false
                    viewModel.clearMessage()
                    onCodeSent(email.trim())
                }
            )
        }
    }
}

@Composable
private fun ForgotPasswordSuccessPopup(
    onContinueClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.18f)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            shape = RoundedCornerShape(18.dp),
            color = Color.White,
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(SneakerBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "@",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Проверьте Ваш Email",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2B2B2B),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Мы отправили код восстановления пароля на вашу электронную почту.",
                    fontSize = 13.sp,
                    color = SecondaryText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Продолжить",
                    modifier = Modifier.clickable(onClick = onContinueClick),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = SneakerBlue
                )
            }
        }
    }
}
