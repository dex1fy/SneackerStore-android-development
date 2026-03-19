/**
 * Этот файл описывает состояние экрана.
 *
 * В UiState лежат все данные, которые нужны Compose для отрисовки текущего состояния интерфейса.
 */
package com.example.sneakerstoreapp.presentation.catalog

import com.example.sneakerstoreapp.domain.model.HomeCategory
import com.example.sneakerstoreapp.domain.model.HomeProduct

data class CatalogUiState(
    val isLoading: Boolean = true,
    val message: String? = null,
    val title: String = "Каталог",
    val categories: List<HomeCategory> = emptyList(),
    val products: List<HomeProduct> = emptyList(),
    val selectedCategoryId: String? = null,
    val searchQuery: String = ""
)
