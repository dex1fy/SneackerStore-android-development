/**
 * Этот файл описывает контракт репозитория в слое domain.
 *
 * Интерфейс нужен, чтобы presentation работал не с конкретной реализацией, а с простым договором.
 */
package com.example.sneakerstoreapp.domain.repository

import com.example.sneakerstoreapp.domain.model.HomeData

interface HomeRepository {
    suspend fun getHomeData(): HomeData
}
