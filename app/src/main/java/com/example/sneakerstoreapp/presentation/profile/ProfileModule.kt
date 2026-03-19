/**
 * Этот файл хранит простые зависимости для экрана или группы экранов.
 *
 * Здесь вручную создаются репозитории и use case, чтобы не усложнять проект лишним DI на старте.
 */
package com.example.sneakerstoreapp.presentation.profile

import com.example.sneakerstoreapp.data.repository.ProfileRepositoryImpl
import com.example.sneakerstoreapp.domain.usecase.GetProfileUseCase
import com.example.sneakerstoreapp.domain.usecase.SaveProfileUseCase

object ProfileModule {
    private val repository = ProfileRepositoryImpl()

    val getProfileUseCase = GetProfileUseCase(repository)
    val saveProfileUseCase = SaveProfileUseCase(repository)
}
