/**
 * Этот файл содержит ViewModel слоя presentation.
 *
 * ViewModel хранит состояние экрана, вызывает use case и подготавливает данные для интерфейса.
 */
package com.example.sneakerstoreapp.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sneakerstoreapp.domain.model.HomeCategory
import com.example.sneakerstoreapp.domain.model.HomeProduct
import com.example.sneakerstoreapp.presentation.favorite.FavoriteSyncManager
import com.example.sneakerstoreapp.presentation.home.HomeModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogViewModel : ViewModel() {

    private val getHomeDataUseCase = HomeModule.getHomeDataUseCase
    private val toggleFavoriteUseCase = com.example.sneakerstoreapp.presentation.favorite.FavoriteModule.toggleFavoriteUseCase

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    init {
        observeFavoriteChanges()
    }

    fun loadData(selectedCategoryId: String?, initialQuery: String = "") {
        // Если каталог уже загружен, повторный запрос не нужен.
        if (_uiState.value.categories.isNotEmpty()) {
            applySelection(selectedCategoryId, initialQuery)
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    selectedCategoryId = selectedCategoryId,
                    searchQuery = initialQuery
                )
            }

            runCatching { getHomeDataUseCase() }
                .onSuccess { homeData ->
                    // Каталог использует те же категории и товары, что и главный экран.
                    _uiState.value = CatalogUiState(
                        isLoading = false,
                        categories = homeData.categories,
                        products = homeData.products,
                        selectedCategoryId = selectedCategoryId,
                        searchQuery = initialQuery,
                        title = resolveTitle(
                            categories = homeData.categories,
                            selectedCategoryId = selectedCategoryId
                        )
                    )
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = "Не удалось загрузить каталог."
                        )
                    }
                }
        }
    }

    fun onCategoryClick(categoryId: String?) {
        _uiState.update { state ->
            state.copy(
                selectedCategoryId = categoryId,
                // Заголовок сверху зависит от выбранной категории.
                title = resolveTitle(state.categories, categoryId)
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun filteredProducts(): List<HomeProduct> {
        val state = _uiState.value

        return state.products
            // Сначала отсекаем товары по категории.
            .filterByCategory(state.selectedCategoryId)
            // Потом дополнительно применяем поиск по названию.
            .filterByQuery(state.searchQuery)
    }

    private fun applySelection(selectedCategoryId: String?, initialQuery: String) {
        _uiState.update { state ->
            state.copy(
                selectedCategoryId = selectedCategoryId,
                searchQuery = initialQuery,
                title = resolveTitle(state.categories, selectedCategoryId)
            )
        }
    }

    private fun resolveTitle(
        categories: List<HomeCategory>,
        selectedCategoryId: String?
    ): String {
        return categories.firstOrNull { it.id == selectedCategoryId }?.title ?: "Каталог"
    }

    private fun List<HomeProduct>.filterByCategory(categoryId: String?): List<HomeProduct> {
        return if (categoryId == null) {
            this
        } else {
            filter { it.categoryId == categoryId }
        }
    }

    private fun List<HomeProduct>.filterByQuery(query: String): List<HomeProduct> {
        if (query.isBlank()) return this

        // Поиск делаем без учета регистра, чтобы пользователю было проще.
        val normalizedQuery = query.trim().lowercase()
        return filter { product ->
            product.title.lowercase().contains(normalizedQuery)
        }
    }

    fun toggleFavorite(productId: String) {
        viewModelScope.launch {
            runCatching { toggleFavoriteUseCase(productId) }
                .onSuccess { isFavorite ->
                    updateFavoriteState(productId, isFavorite)
                }
        }
    }

    private fun observeFavoriteChanges() {
        viewModelScope.launch {
            // Подписываемся на общие изменения избранного из других экранов.
            FavoriteSyncManager.changes.collect { change ->
                updateFavoriteState(
                    productId = change.productId,
                    isFavorite = change.isFavorite
                )
            }
        }
    }

    private fun updateFavoriteState(productId: String, isFavorite: Boolean) {
        _uiState.update { state ->
            state.copy(
                products = state.products.map { product ->
                    if (product.id == productId) {
                        product.copy(isFavorite = isFavorite)
                    } else {
                        product
                    }
                }
            )
        }
    }
}
