/**
 * Этот файл описывает контракт репозитория в слое domain.
 *
 * Интерфейс нужен, чтобы presentation работал не с конкретной реализацией, а с простым договором.
 */
package com.example.sneakerstoreapp.domain.repository

import com.example.sneakerstoreapp.domain.model.FavoriteData

interface FavoriteRepository {
    suspend fun getFavoriteData(): FavoriteData
    suspend fun toggleFavorite(productId: String): Boolean
}
