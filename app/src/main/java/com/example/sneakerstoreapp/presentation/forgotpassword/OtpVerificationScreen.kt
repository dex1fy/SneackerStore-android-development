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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sneakerstoreapp.presentation.auth.BackButton
import com.example.sneakerstoreapp.ui.theme.InputBackground
import com.example.sneakerstoreapp.ui.theme.InputText
import com.example.sneakerstoreapp.ui.theme.SecondaryText
import com.example.sneakerstoreapp.ui.theme.SneakerBlue

// Этот экран показывает ввод recovery-кода, таймер и повторную отправку.
@Composable
fun OtpVerificationScreen(
    email: String,
    onBackClick: () -> Unit,
    onVerificationSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OtpVerificationViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Передаем email в ViewModel один раз при открытии экрана.
    LaunchedEffect(email) {
        viewModel.setup(email)
    }

    // После успешной проверки открываем следующий экран смены пароля.
    LaunchedEffect(uiState.isVerified) {
        if (uiState.isVerified) {
            onVerificationSuccess()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        BackButton(onClick = onBackClick)

        Spacer(modifier = Modifier.height(42.dp))

        Text(
            text = "OTP Проверка",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            color = InputText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Пожалуйста, проверьте свою\nэлектронную почту, чтобы увидеть код\nподтверждения.",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 14.sp,
            color = SecondaryText,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "OTP Код",
            fontSize = 16.sp,
            color = InputText
        )

        Spacer(modifier = Modifier.height(12.dp))

        BasicTextField(
            value = uiState.code,
            onValueChange = viewModel::onCodeChange,
            textStyle = TextStyle(color = Color.Transparent),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            decorationBox = {
                // Рисуем код как отдельные ячейки, чтобы было похоже на макет.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(8) { index ->
                        val digit = uiState.code.getOrNull(index)?.toString().orEmpty()
                        val isActive = index == uiState.code.length.coerceAtMost(7)

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(86.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isActive && uiState.code.length < 8) {
                                        Color(0xFFFFF0EE)
                                    } else {
                                        InputBackground
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = digit,
                                fontSize = 22.sp,
                                color = InputText,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = formatTimer(uiState.secondsLeft),
            modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp,
            color = SecondaryText,
            textAlign = TextAlign.End
        )

        if (uiState.message != null) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = uiState.message.orEmpty(),
                fontSize = 13.sp,
                color = if (uiState.isVerified) SneakerBlue else Color(0xFFE74C3C)
            )
        }

        if (uiState.canResend) {
            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Отправить код заново",
                modifier = Modifier.clickable(onClick = viewModel::resendCode),
                fontSize = 14.sp,
                color = SneakerBlue
            )
        }
    }
}

private fun formatTimer(secondsLeft: Int): String {
    val minutes = secondsLeft / 60
    val seconds = secondsLeft % 60
    return "%02d:%02d".format(minutes, seconds)
}
