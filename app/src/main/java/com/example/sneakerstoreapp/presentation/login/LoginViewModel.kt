/**
 * Этот файл содержит ViewModel слоя presentation.
 *
 * ViewModel хранит состояние экрана, вызывает use case и подготавливает данные для интерфейса.
 */
package com.example.sneakerstoreapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sneakerstoreapp.presentation.auth.AuthActionState
import com.example.sneakerstoreapp.presentation.auth.AuthModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val signInUseCase = AuthModule.signInUseCase

    private val _uiState = MutableStateFlow(AuthActionState())
    val uiState: StateFlow<AuthActionState> = _uiState.asStateFlow()

    fun signIn(email: String, password: String) {
        val trimmedEmail = email.trim()

        if (trimmedEmail.isBlank() || password.isBlank()) {
            _uiState.value = AuthActionState(
                message = "Заполните email и пароль."
            )
            return
        }

        if (!EMAIL_REGEX.matches(trimmedEmail)) {
            _uiState.value = AuthActionState(
                message = "Введите корректный email."
            )
            return
        }

        if (password.length < 8) {
            _uiState.value = AuthActionState(
                message = "Пароль должен быть не менее 8 символов."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthActionState(isLoading = true)

            val result = signInUseCase(
                email = trimmedEmail,
                password = password
            )

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

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
