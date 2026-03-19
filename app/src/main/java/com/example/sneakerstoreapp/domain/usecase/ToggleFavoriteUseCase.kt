/**
 * Этот файл содержит use case слоя domain.
 *
 * Use case инкапсулирует одно конкретное действие приложения и делает вызовы из ViewModel понятными и короткими.
 */
package com.example.sneakerstoreapp.domain.usecase

import com.example.sneakerstoreapp.domain.repository.FavoriteRepository

class ToggleFavoriteUseCase(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(productId: String): Boolean {
        return repository.toggleFavorite(productId)
    }
}
