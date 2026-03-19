/**
 * Этот файл содержит ViewModel слоя presentation.
 *
 * ViewModel хранит состояние экрана, вызывает use case и подготавливает данные для интерфейса.
 */
package com.example.sneakerstoreapp.presentation.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sneakerstoreapp.presentation.auth.AuthModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Этот ViewModel отвечает за сохранение нового пароля после recovery-подтверждения.
class CreateNewPasswordViewModel : ViewModel() {

    private val updatePasswordUseCase = AuthModule.updatePasswordUseCase

    private val _uiState = MutableStateFlow(CreateNewPasswordUiState())
    val uiState: StateFlow<CreateNewPasswordUiState> = _uiState.asStateFlow()

    fun savePassword(password: String, confirmPassword: String) {
        if (password.isBlank() || confirmPassword.isBlank()) {
            _uiState.value = CreateNewPasswordUiState(
                message = "Заполните оба поля пароля."
            )
            return
        }

        if (password.length < 8) {
            _uiState.value = CreateNewPasswordUiState(
                message = "Пароль должен быть не менее 8 символов."
            )
            return
        }

        if (password != confirmPassword) {
            _uiState.value = CreateNewPasswordUiState(
                message = "Пароли не совпадают."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = CreateNewPasswordUiState(isLoading = true)

            val result = updatePasswordUseCase(password)

            _uiState.value = CreateNewPasswordUiState(
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
