/**
 * Этот файл описывает DTO-модель для слоя data.
 *
 * DTO нужен, чтобы читать и отправлять данные в Supabase в том виде, в котором их ожидает база данных.
 */
package com.example.sneakerstoreapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    val photo: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val address: String? = null,
    val phone: String? = null
)
