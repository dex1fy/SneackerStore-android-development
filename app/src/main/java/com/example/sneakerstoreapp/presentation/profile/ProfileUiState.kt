/**
 * Этот файл описывает состояние экрана.
 *
 * В UiState лежат все данные, которые нужны Compose для отрисовки текущего состояния интерфейса.
 */
package com.example.sneakerstoreapp.presentation.profile

import com.example.sneakerstoreapp.domain.model.ProfileData

data class ProfileUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isEditing: Boolean = false,
    val message: String? = null,
    val profile: ProfileData? = null,
    val firstName: String = "",
    val lastName: String = "",
    val address: String = "",
    val phone: String = ""
)
