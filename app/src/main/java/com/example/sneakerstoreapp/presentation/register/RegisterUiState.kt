/**
 * Этот файл описывает состояние экрана.
 *
 * В UiState лежат все данные, которые нужны Compose для отрисовки текущего состояния интерфейса.
 */
package com.example.sneakerstoreapp.presentation.register

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val message: String? = null,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null
)
