/**
 * Этот файл описывает модель слоя domain.
 *
 * Domain-модель хранит данные в удобном для приложения виде и не зависит от конкретного источника данных.
 */
package com.example.sneakerstoreapp.domain.model

data class FavoriteData(
    val products: List<HomeProduct>
)
