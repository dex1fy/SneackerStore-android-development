/**
 * Этот файл описывает DTO-модель для слоя data.
 *
 * DTO нужен, чтобы читать и отправлять данные в Supabase в том виде, в котором их ожидает база данных.
 */
package com.example.sneakerstoreapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: String,
    val title: String,
    @SerialName("category_id")
    val categoryId: String? = null,
    @SerialName("cost")
    val cost: Double,
    val description: String,
    @SerialName("is_best_seller")
    val isBestSeller: Boolean = false
)
