/**
 * Этот файл содержит ViewModel слоя presentation.
 *
 * ViewModel хранит состояние экрана, вызывает use case и подготавливает данные для интерфейса.
 */
package com.example.sneakerstoreapp.presentation.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sneakerstoreapp.presentation.auth.AuthActionState
import com.example.sneakerstoreapp.presentation.auth.AuthModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {

    private val resetPasswordUseCase = AuthModule.resetPasswordUseCase

    private val _uiState = MutableStateFlow(AuthActionState())
    val uiState: StateFlow<AuthActionState> = _uiState.asStateFlow()

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _uiState.value = AuthActionState(
                message = "Введите email."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthActionState(isLoading = true)

            val result = resetPasswordUseCase(email.trim())

            _uiState.value = AuthActionState(
                isLoading = false,
                isSuccess = result.isSuccess,
                message = result.message
            )
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}
