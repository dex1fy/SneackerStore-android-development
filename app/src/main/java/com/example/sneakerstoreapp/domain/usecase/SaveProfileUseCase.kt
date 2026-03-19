/**
 * Этот файл содержит use case слоя domain.
 *
 * Use case инкапсулирует одно конкретное действие приложения и делает вызовы из ViewModel понятными и короткими.
 */
package com.example.sneakerstoreapp.domain.usecase

import com.example.sneakerstoreapp.domain.model.ProfileData
import com.example.sneakerstoreapp.domain.repository.ProfileRepository

class SaveProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(profile: ProfileData): ProfileData? {
        return repository.saveProfile(profile)
    }
}
