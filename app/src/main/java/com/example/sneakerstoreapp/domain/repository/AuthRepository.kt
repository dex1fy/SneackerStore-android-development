/**
 * Этот файл описывает контракт репозитория в слое domain.
 *
 * Интерфейс нужен, чтобы presentation работал не с конкретной реализацией, а с простым договором.
 */
package com.example.sneakerstoreapp.domain.repository

import com.example.sneakerstoreapp.domain.model.AuthResult

interface AuthRepository {
    suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): AuthResult

    suspend fun signIn(
        email: String,
        password: String
    ): AuthResult

    suspend fun resetPassword(email: String): AuthResult
    suspend fun resendRecoveryCode(email: String): AuthResult
    suspend fun verifyRecoveryCode(email: String, code: String): AuthResult
    suspend fun updatePassword(password: String): AuthResult
}
