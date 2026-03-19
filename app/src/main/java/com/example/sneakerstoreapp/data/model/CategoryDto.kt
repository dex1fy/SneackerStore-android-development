/**
 * Этот файл описывает DTO-модель для слоя data.
 *
 * DTO нужен, чтобы читать и отправлять данные в Supabase в том виде, в котором их ожидает база данных.
 */
package com.example.sneakerstoreapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: String,
    val title: String
)
