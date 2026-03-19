/**
 * Этот файл содержит ViewModel слоя presentation.
 *
 * ViewModel хранит состояние экрана, вызывает use case и подготавливает данные для интерфейса.
 */
package com.example.sneakerstoreapp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sneakerstoreapp.domain.model.ProfileData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val getProfileUseCase = ProfileModule.getProfileUseCase
    private val saveProfileUseCase = ProfileModule.saveProfileUseCase

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            // Перед загрузкой показываем progress и очищаем старое сообщение.
            _uiState.update { it.copy(isLoading = true, message = null) }

            runCatching { getProfileUseCase() }
                .onSuccess { profile ->
                    // Одновременно заполняем и модель профиля, и поля формы.
                    _uiState.value = ProfileUiState(
                        isLoading = false,
                        profile = profile,
                        message = if (profile == null) "Профиль не найден." else null,
                        firstName = profile?.firstName.orEmpty(),
                        lastName = profile?.lastName.orEmpty(),
                        address = profile?.address.orEmpty(),
                        phone = profile?.phone.orEmpty()
                    )
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = "Не удалось загрузить профиль."
                        )
                    }
                }
        }
    }

    fun startEditing() {
        _uiState.update { state ->
            state.copy(
                isEditing = true,
                message = null,
                // При входе в режим редактирования берем значения из текущего профиля.
                firstName = state.profile?.firstName.orEmpty(),
                lastName = state.profile?.lastName.orEmpty(),
                address = state.profile?.address.orEmpty(),
                phone = state.profile?.phone.orEmpty()
            )
        }
    }

    fun cancelEditing() {
        _uiState.update { state ->
            state.copy(
                isEditing = false,
                message = null,
                // При отмене возвращаем поля к сохраненным значениям.
                firstName = state.profile?.firstName.orEmpty(),
                lastName = state.profile?.lastName.orEmpty(),
                address = state.profile?.address.orEmpty(),
                phone = state.profile?.phone.orEmpty()
            )
        }
    }

    fun onFirstNameChange(value: String) {
        _uiState.update { it.copy(firstName = value) }
    }

    fun onLastNameChange(value: String) {
        _uiState.update { it.copy(lastName = value) }
    }

    fun onAddressChange(value: String) {
        _uiState.update { it.copy(address = value) }
    }

    fun onPhoneChange(value: String) {
        _uiState.update { it.copy(phone = value) }
    }

    fun saveProfile() {
        val currentProfile = _uiState.value.profile ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, message = null) }

            // Собираем новые значения из редактируемых полей.
            val profileToSave = ProfileData(
                userId = currentProfile.userId,
                photoUrl = currentProfile.photoUrl,
                firstName = _uiState.value.firstName,
                lastName = _uiState.value.lastName,
                address = _uiState.value.address,
                phone = _uiState.value.phone
            )

            runCatching { saveProfileUseCase(profileToSave) }
                .onSuccess { savedProfile ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            isEditing = false,
                            profile = savedProfile,
                            message = "Данные сохранены.",
                            firstName = savedProfile?.firstName.orEmpty(),
                            lastName = savedProfile?.lastName.orEmpty(),
                            address = savedProfile?.address.orEmpty(),
                            phone = savedProfile?.phone.orEmpty()
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            message = "Не удалось сохранить профиль."
                        )
                    }
                }
        }
    }
}
