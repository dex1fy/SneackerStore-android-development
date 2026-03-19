/**
 * Этот файл описывает состояние экрана.
 *
 * В UiState лежат все данные, которые нужны Compose для отрисовки текущего состояния интерфейса.
 */
package com.example.sneakerstoreapp.presentation.forgotpassword

data class OtpVerificationUiState(
    val code: String = "",
    val isLoading: Boolean = false,
    val secondsLeft: Int = 30,
    val canResend: Boolean = false,
    val message: String? = null,
    val isVerified: Boolean = false
)
