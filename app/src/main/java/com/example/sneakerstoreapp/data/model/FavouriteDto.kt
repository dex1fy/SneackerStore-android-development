/**
 * Этот файл описывает DTO-модель для слоя data.
 *
 * DTO нужен, чтобы читать и отправлять данные в Supabase в том виде, в котором их ожидает база данных.
 */
package com.example.sneakerstoreapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavouriteDto(
    val id: String,
    @SerialName("product_id")
    val productId: String? = null,
    @SerialName("user_id")
    val userId: String? = null
)
