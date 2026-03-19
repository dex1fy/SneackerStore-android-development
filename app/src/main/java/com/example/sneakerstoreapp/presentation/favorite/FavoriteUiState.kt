/**
 * Этот файл описывает состояние экрана.
 *
 * В UiState лежат все данные, которые нужны Compose для отрисовки текущего состояния интерфейса.
 */
package com.example.sneakerstoreapp.presentation.favorite

import com.example.sneakerstoreapp.domain.model.HomeProduct

data class FavoriteUiState(
    val isLoading: Boolean = true,
    val message: String? = null,
    val products: List<HomeProduct> = emptyList()
)
