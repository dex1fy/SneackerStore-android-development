/**
 * Этот файл содержит ViewModel слоя presentation.
 *
 * ViewModel хранит состояние экрана, вызывает use case и подготавливает данные для интерфейса.
 */
package com.example.sneakerstoreapp.presentation.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sneakerstoreapp.presentation.auth.AuthModule
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Этот ViewModel управляет проверкой кода из email и таймером повторной отправки.
class OtpVerificationViewModel : ViewModel() {

    private val verifyRecoveryCodeUseCase = AuthModule.verifyRecoveryCodeUseCase
    private val resendRecoveryCodeUseCase = AuthModule.resendRecoveryCodeUseCase

    private val _uiState = MutableStateFlow(OtpVerificationUiState())
    val uiState: StateFlow<OtpVerificationUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var currentEmail: String = ""

    // Запоминаем email и запускаем таймер при открытии экрана.
    fun setup(email: String) {
        if (currentEmail == email && timerJob?.isActive == true) return

        currentEmail = email
        _uiState.value = OtpVerificationUiState()
        startTimer()
    }

    // Разрешаем вводить только цифры и проверяем код, когда он введен полностью.
    fun onCodeChange(value: String) {
        val formatted = value.filter { it.isDigit() }.take(8)
        _uiState.update { it.copy(code = formatted, message = null) }

        if (formatted.length == 8 && !_uiState.value.isLoading) {
            verifyCode()
        }
    }

    // Проверяем recovery-код через Supabase.
    fun verifyCode() {
        val code = _uiState.value.code
        if (code.length != 8 || currentEmail.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }

            val result = verifyRecoveryCodeUseCase(currentEmail, code)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isVerified = result.isSuccess,
                    message = result.message
                )
            }
        }
    }

    // Повторно отправляем код, когда таймер истек.
    fun resendCode() {
        if (!uiState.value.canResend || currentEmail.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }

            val result = resendRecoveryCodeUseCase(currentEmail)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    message = result.message,
                    code = if (result.isSuccess) "" else it.code
                )
            }

            if (result.isSuccess) {
                startTimer()
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }

    // Простой таймер на 30 секунд для повторной отправки кода.
    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (seconds in 30 downTo 0) {
                _uiState.update {
                    it.copy(
                        secondsLeft = seconds,
                        canResend = seconds == 0
                    )
                }
                delay(1_000)
            }
        }
    }
}
