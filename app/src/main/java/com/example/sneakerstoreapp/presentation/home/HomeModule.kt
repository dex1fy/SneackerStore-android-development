/**
 * Этот файл хранит простые зависимости для экрана или группы экранов.
 *
 * Здесь вручную создаются репозитории и use case, чтобы не усложнять проект лишним DI на старте.
 */
package com.example.sneakerstoreapp.presentation.home

import com.example.sneakerstoreapp.data.repository.HomeRepositoryImpl
import com.example.sneakerstoreapp.domain.usecase.GetHomeDataUseCase

object HomeModule {
    private val repository = HomeRepositoryImpl()

    val getHomeDataUseCase = GetHomeDataUseCase(repository)
}
