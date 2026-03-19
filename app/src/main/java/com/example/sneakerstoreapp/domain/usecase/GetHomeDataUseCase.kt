/**
 * Этот файл содержит use case слоя domain.
 *
 * Use case инкапсулирует одно конкретное действие приложения и делает вызовы из ViewModel понятными и короткими.
 */
package com.example.sneakerstoreapp.domain.usecase

import com.example.sneakerstoreapp.domain.model.HomeData
import com.example.sneakerstoreapp.domain.repository.HomeRepository

class GetHomeDataUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): HomeData {
        return repository.getHomeData()
    }
}
