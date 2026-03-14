package com.davinci.app.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davinci.app.domain.repository.AuthRepository
import com.davinci.app.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val displayName: String = "User",
    val email: String = "user@email.com",
    val initials: String = "U",
    val avatarUrl: String? = null,
    val biometricEnabled: Boolean = false,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            userRepository.getProfile()
                .onSuccess { profile ->
                    _uiState.update {
                        it.copy(
                            displayName = profile.displayName,
                            email = profile.email,
                            initials = profile.displayName
                                .split(" ")
                                .mapNotNull { part -> part.firstOrNull()?.uppercase() }
                                .take(2)
                                .joinToString(""),
                            avatarUrl = profile.avatarUrl,
                        )
                    }
                }
        }
    }

    fun toggleBiometric(enabled: Boolean) {
        _uiState.update { it.copy(biometricEnabled = enabled) }
        // TODO: Persist biometric preference
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
