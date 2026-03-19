/**
 * Этот файл относится к слою presentation.
 *
 * Он помогает экрану отображать данные, обрабатывать действия пользователя или синхронизировать состояние.
 */
package com.example.sneakerstoreapp.presentation.favorite

import com.example.sneakerstoreapp.domain.model.FavoriteChange
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object FavoriteSyncManager {

    private val _changes = MutableSharedFlow<FavoriteChange>(extraBufferCapacity = 1)
    val changes: SharedFlow<FavoriteChange> = _changes.asSharedFlow()

    fun notifyFavoriteChanged(productId: String, isFavorite: Boolean) {
        _changes.tryEmit(
            FavoriteChange(
                productId = productId,
                isFavorite = isFavorite
            )
        )
    }
}
