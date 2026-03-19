/**
 * Этот файл содержит ViewModel слоя presentation.
 *
 * ViewModel хранит состояние экрана, вызывает use case и подготавливает данные для интерфейса.
 */
package com.example.sneakerstoreapp.presentation.sidemenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SideMenuViewModel : ViewModel() {

    private val getProfileUseCase = SideMenuModule.getProfileUseCase

    private val _uiState = MutableStateFlow(SideMenuUiState())
    val uiState: StateFlow<SideMenuUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = SideMenuUiState(profile = getProfileUseCase())
        }
    }
}
