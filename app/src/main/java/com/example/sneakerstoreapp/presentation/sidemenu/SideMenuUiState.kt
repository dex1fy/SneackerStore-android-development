/**
 * Этот файл описывает состояние экрана.
 *
 * В UiState лежат все данные, которые нужны Compose для отрисовки текущего состояния интерфейса.
 */
package com.example.sneakerstoreapp.presentation.sidemenu

import com.example.sneakerstoreapp.domain.model.ProfileData

data class SideMenuUiState(
    val profile: ProfileData? = null
)
