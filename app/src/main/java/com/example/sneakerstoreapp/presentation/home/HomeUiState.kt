/**
 * Этот файл описывает состояние экрана.
 *
 * В UiState лежат все данные, которые нужны Compose для отрисовки текущего состояния интерфейса.
 */
package com.example.sneakerstoreapp.presentation.home

import com.example.sneakerstoreapp.domain.model.HomeCategory
import com.example.sneakerstoreapp.domain.model.HomeProduct

data class HomeUiState(
    val isLoading: Boolean = true,
    val message: String? = null,
    val categories: List<HomeCategory> = emptyList(),
    val products: List<HomeProduct> = emptyList(),
    val selectedCategoryId: String? = null
)
