/**
 * Этот файл содержит ViewModel слоя presentation.
 *
 * ViewModel хранит состояние экрана, вызывает use case и подготавливает данные для интерфейса.
 */
package com.example.sneakerstoreapp.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sneakerstoreapp.presentation.auth.AuthModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Этот ViewModel отвечает за простую валидацию полей регистрации и отправку данных в Supabase.
class RegisterViewModel : ViewModel() {

    private val signUpUseCase = AuthModule.signUpUseCase

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // Проверяем поля до отправки, чтобы сразу показать понятные ошибки пользователю.
    fun signUp(name: String, email: String, password: String) {
        val trimmedName = name.trim()
        val trimmedEmail = email.trim()

        val nameError = if (trimmedName.isBlank()) "Введите имя." else null
        val emailError = when {
            trimmedEmail.isBlank() -> "Введите email."
            !EMAIL_REGEX.matches(trimmedEmail) -> "Введите корректный email."
            else -> null
        }
        val passwordError = when {
            password.isBlank() -> "Введите пароль."
            password.length < 8 -> "Пароль должен быть не менее 8 символов."
            else -> null
        }

        if (nameError != null || emailError != null || passwordError != null) {
            _uiState.value = RegisterUiState(
                nameError = nameError,
                emailError = emailError,
                passwordError = passwordError
            )
            return
        }

        // Если локальная валидация прошла, отправляем запрос на регистрацию.
        viewModelScope.launch {
            _uiState.value = RegisterUiState(isLoading = true)

            val result = signUpUseCase(
                name = trimmedName,
                email = trimmedEmail,
                password = password
            )

            _uiState.value = RegisterUiState(
                isLoading = false,
                isSuccess = result.isSuccess,
                message = result.message
            )
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    fun clearNameError() {
        _uiState.value = _uiState.value.copy(nameError = null)
    }

    fun clearEmailError() {
        _uiState.value = _uiState.value.copy(emailError = null)
    }

    fun clearPasswordError() {
        _uiState.value = _uiState.value.copy(passwordError = null)
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
