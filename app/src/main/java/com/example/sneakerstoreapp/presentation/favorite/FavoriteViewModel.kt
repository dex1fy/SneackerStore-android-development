/**
 * Этот файл содержит ViewModel слоя presentation.
 *
 * ViewModel хранит состояние экрана, вызывает use case и подготавливает данные для интерфейса.
 */
package com.example.sneakerstoreapp.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

    private val getFavoriteDataUseCase = FavoriteModule.getFavoriteDataUseCase
    private val toggleFavoriteUseCase = FavoriteModule.toggleFavoriteUseCase

    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()

    init {
        observeFavoriteChanges()
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            // Каждый раз заново подтягиваем список из базы, чтобы он был актуальным.
            _uiState.update { it.copy(isLoading = true, message = null) }

            runCatching { getFavoriteDataUseCase() }
                .onSuccess { favoriteData ->
                    _uiState.value = FavoriteUiState(
                        isLoading = false,
                        products = favoriteData.products
                    )
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = "Не удалось загрузить избранное."
                        )
                    }
                }
        }
    }

    fun toggleFavorite(productId: String) {
        viewModelScope.launch {
            runCatching { toggleFavoriteUseCase(productId) }
                .onSuccess { isFavorite ->
                    // Сразу отражаем изменение на текущем экране.
                    applyFavoriteChange(productId, isFavorite)
                }
        }
    }

    private fun observeFavoriteChanges() {
        viewModelScope.launch {
            FavoriteSyncManager.changes.collectLatest { change ->
                // Экран избранного слушает изменения из Home и Catalog.
                applyFavoriteChange(
                    productId = change.productId,
                    isFavorite = change.isFavorite
                )
            }
        }
    }

    private fun applyFavoriteChange(productId: String, isFavorite: Boolean) {
        if (isFavorite) {
            // Если товар добавили на другом экране, проще обновить список целиком.
            loadFavorites()
            return
        }

        _uiState.update { state ->
            state.copy(
                products = state.products.filterNot { it.id == productId }
            )
        }
    }
}
