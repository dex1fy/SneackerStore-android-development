/**
 * Этот файл описывает модель слоя domain.
 *
 * Domain-модель хранит данные в удобном для приложения виде и не зависит от конкретного источника данных.
 */
package com.example.sneakerstoreapp.domain.model

data class ProfileData(
    val userId: String,
    val photoUrl: String?,
    val firstName: String,
    val lastName: String,
    val address: String,
    val phone: String
)
