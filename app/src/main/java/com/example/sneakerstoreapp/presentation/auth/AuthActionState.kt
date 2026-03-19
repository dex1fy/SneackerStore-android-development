/**
 * Этот файл относится к слою presentation.
 *
 * Он помогает экрану отображать данные, обрабатывать действия пользователя или синхронизировать состояние.
 */
package com.example.sneakerstoreapp.presentation.auth

data class AuthActionState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val message: String? = null
)
