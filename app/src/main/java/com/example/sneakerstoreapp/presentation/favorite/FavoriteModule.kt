/**
 * Этот файл хранит простые зависимости для экрана или группы экранов.
 *
 * Здесь вручную создаются репозитории и use case, чтобы не усложнять проект лишним DI на старте.
 */
package com.example.sneakerstoreapp.presentation.favorite

import com.example.sneakerstoreapp.data.repository.FavoriteRepositoryImpl
import com.example.sneakerstoreapp.domain.usecase.GetFavoriteDataUseCase
import com.example.sneakerstoreapp.domain.usecase.ToggleFavoriteUseCase

object FavoriteModule {
    private val repository = FavoriteRepositoryImpl()

    val getFavoriteDataUseCase = GetFavoriteDataUseCase(repository)
    val toggleFavoriteUseCase = ToggleFavoriteUseCase(repository)
}
