/**
 * Этот файл описывает модель слоя domain.
 *
 * Domain-модель хранит данные в удобном для приложения виде и не зависит от конкретного источника данных.
 */
package com.example.sneakerstoreapp.domain.model

data class HomeProduct(
    val id: String,
    val title: String,
    val price: Double,
    val isBestSeller: Boolean,
    val categoryId: String?,
    val imageUrl: String?,
    val isFavorite: Boolean
)
