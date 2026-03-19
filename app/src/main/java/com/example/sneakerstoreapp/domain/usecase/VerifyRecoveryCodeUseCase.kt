/**
 * Этот файл содержит use case слоя domain.
 *
 * Use case инкапсулирует одно конкретное действие приложения и делает вызовы из ViewModel понятными и короткими.
 */
package com.example.sneakerstoreapp.domain.usecase

import com.example.sneakerstoreapp.domain.repository.AuthRepository

class VerifyRecoveryCodeUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, code: String) = repository.verifyRecoveryCode(email, code)
}
