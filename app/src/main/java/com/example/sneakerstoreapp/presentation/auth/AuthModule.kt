package com.example.sneakerstoreapp.presentation.auth

import com.example.sneakerstoreapp.data.repository.AuthRepositoryImpl
import com.example.sneakerstoreapp.domain.usecase.ResendRecoveryCodeUseCase
import com.example.sneakerstoreapp.domain.usecase.ResetPasswordUseCase
import com.example.sneakerstoreapp.domain.usecase.SignInUseCase
import com.example.sneakerstoreapp.domain.usecase.SignUpUseCase
import com.example.sneakerstoreapp.domain.usecase.UpdatePasswordUseCase
import com.example.sneakerstoreapp.domain.usecase.VerifyRecoveryCodeUseCase

// Этот файл собирает простые use case для auth-сценариев приложения.
object AuthModule {
    private val repository = AuthRepositoryImpl()

    val signUpUseCase = SignUpUseCase(repository)
    val signInUseCase = SignInUseCase(repository)
    val resetPasswordUseCase = ResetPasswordUseCase(repository)
    val resendRecoveryCodeUseCase = ResendRecoveryCodeUseCase(repository)
    val verifyRecoveryCodeUseCase = VerifyRecoveryCodeUseCase(repository)
    val updatePasswordUseCase = UpdatePasswordUseCase(repository)
}
