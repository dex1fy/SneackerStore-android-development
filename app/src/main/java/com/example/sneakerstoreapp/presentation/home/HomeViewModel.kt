/**
 * Этот файл содержит ViewModel слоя presentation.
 *
 * ViewModel хранит состояние экрана, вызывает use case и подготавливает данные для интерфейса.
 */
package com.example.sneakerstoreapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sneakerstoreapp.domain.model.HomeProduct
import com.example.sneakerstoreapp.presentation.favorite.FavoriteSyncManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val getHomeDataUseCase = HomeModule.getHomeDataUseCase
    private val toggleFavoriteUseCase = com.example.sneakerstoreapp.presentation.favorite.FavoriteModule.toggleFavoriteUseCase

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeFavoriteChanges()
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            // Перед новой загрузкой очищаем старое сообщение и показываем progress.
            _uiState.update { it.copy(isLoading = true, message = null) }

            runCatching { getHomeDataUseCase() }
                .onSuccess { homeData ->
                    // После успешной загрузки полностью обновляем состояние экрана.
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        categories = homeData.categories,
                        products = homeData.products
                    )
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = "Не удалось загрузить главную страницу."
                        )
                    }
                }
        }
    }

    fun selectCategory(categoryId: String?) {
        _uiState.update { state ->
            // null означает режим "Все категории".
            state.copy(selectedCategoryId = categoryId)
        }
    }

    fun filteredProducts(): List<HomeProduct> {
        val state = uiState.value

        // Если категория не выбрана, показываем весь список.
        return if (state.selectedCategoryId == null) {
            state.products
        } else {
            state.products.filter { it.categoryId == state.selectedCategoryId }
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
            // Слушаем общие изменения избранного, чтобы синхронизировать все экраны.
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
                // Меняем флаг только у нужного товара.
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
