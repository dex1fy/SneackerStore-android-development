/**
 * Этот файл содержит use case слоя domain.
 *
 * Use case инкапсулирует одно конкретное действие приложения и делает вызовы из ViewModel понятными и короткими.
 */
package com.example.sneakerstoreapp.domain.usecase

import com.example.sneakerstoreapp.domain.repository.AuthRepository

class SignUpUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ) = repository.signUp(
        name = name,
        email = email,
        password = password
    )
}
