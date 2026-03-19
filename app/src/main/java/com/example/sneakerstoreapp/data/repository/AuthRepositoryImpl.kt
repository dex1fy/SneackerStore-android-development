package com.example.sneakerstoreapp.data.repository

import com.example.sneakerstoreapp.data.remote.SupabaseClientProvider
import com.example.sneakerstoreapp.domain.model.AuthResult
import com.example.sneakerstoreapp.domain.repository.AuthRepository
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.OtpVerifyResult
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

// Этот файл отвечает за все простые auth-операции приложения через Supabase Auth.
class AuthRepositoryImpl : AuthRepository {

    // Регистрация нового пользователя через email и пароль.
    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): AuthResult {
        return runCatching {
            SupabaseClientProvider.client.auth.signUpWith(Email) {
                this.email = email
                this.password = password

                // Сохраняем имя в metadata пользователя.
                data = buildJsonObject {
                    put("name", name)
                }
            }

            AuthResult(
                isSuccess = true,
                message = "Регистрация выполнена. Если нужно подтверждение email, проверьте почту."
            )
        }.getOrElse { error ->
            AuthResult(
                isSuccess = false,
                message = mapAuthError(error.message, "Не удалось зарегистрироваться.")
            )
        }
    }

    // Вход пользователя в приложение.
    override suspend fun signIn(
        email: String,
        password: String
    ): AuthResult {
        return runCatching {
            SupabaseClientProvider.client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            AuthResult(
                isSuccess = true,
                message = "Вход выполнен успешно."
            )
        }.getOrElse { error ->
            AuthResult(
                isSuccess = false,
                message = mapAuthError(error.message, "Не удалось войти.")
            )
        }
    }

    // Отправка recovery-кода на email.
    override suspend fun resetPassword(email: String): AuthResult {
        return runCatching {
            SupabaseClientProvider.client.auth.resetPasswordForEmail(email)

            AuthResult(
                isSuccess = true,
                message = "Код для восстановления пароля отправлен на почту."
            )
        }.getOrElse { error ->
            AuthResult(
                isSuccess = false,
                message = mapAuthError(error.message, "Не удалось отправить код восстановления.")
            )
        }
    }

    // Повторная отправка recovery-кода.
    override suspend fun resendRecoveryCode(email: String): AuthResult {
        return runCatching {
            SupabaseClientProvider.client.auth.resendEmail(
                type = OtpType.Email.RECOVERY,
                email = email
            )

            AuthResult(
                isSuccess = true,
                message = "Код отправлен повторно."
            )
        }.getOrElse { error ->
            AuthResult(
                isSuccess = false,
                message = mapAuthError(error.message, "Не удалось отправить код повторно.")
            )
        }
    }

    // Проверка recovery-кода из письма.
    override suspend fun verifyRecoveryCode(email: String, code: String): AuthResult {
        return runCatching {
            when (
                SupabaseClientProvider.client.auth.verifyEmailOtp(
                    type = OtpType.Email.RECOVERY,
                    email = email,
                    token = code
                )
            ) {
                is OtpVerifyResult.Authenticated,
                OtpVerifyResult.VerifiedNoSession -> {
                    AuthResult(
                        isSuccess = true,
                        message = "Код подтвержден."
                    )
                }
            }
        }.getOrElse { error ->
            AuthResult(
                isSuccess = false,
                message = mapAuthError(error.message, "Неверный код подтверждения.")
            )
        }
    }

    // Обновление пароля после успешного recovery flow.
    override suspend fun updatePassword(password: String): AuthResult {
        return runCatching {
            SupabaseClientProvider.client.auth.updateUser {
                this.password = password
            }

            AuthResult(
                isSuccess = true,
                message = "Пароль успешно изменен."
            )
        }.getOrElse { error ->
            AuthResult(
                isSuccess = false,
                message = mapAuthError(error.message, "Не удалось изменить пароль.")
            )
        }
    }

    // Переводим сырые ошибки Supabase в простые сообщения для пользователя.
    private fun mapAuthError(rawMessage: String?, fallback: String): String {
        val message = rawMessage.orEmpty().lowercase()

        return when {
            message.contains("password should be at least 8 characters") ->
                "Пароль должен быть не менее 8 символов."

            message.contains("invalid login credentials") ->
                "Пользователь с таким email не найден или пароль введен неверно."

            message.contains("user already registered") ->
                "Пользователь с таким email уже существует."

            message.contains("email not confirmed") ->
                "Подтвердите email через письмо на почте."

            message.contains("unable to validate email address") ->
                "Введите корректный email."

            message.contains("email address") && message.contains("invalid") ->
                "Введите корректный email."

            message.contains("user not found") ->
                "Пользователь с таким email не найден."

            message.contains("otp") && message.contains("expired") ->
                "Срок действия кода истек. Запросите новый код."

            message.contains("token") && message.contains("expired") ->
                "Срок действия кода истек. Запросите новый код."

            message.contains("invalid") && message.contains("otp") ->
                "Неверный код подтверждения."

            else -> fallback
        }
    }
}
