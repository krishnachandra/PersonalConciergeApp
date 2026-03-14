package com.davinci.app.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davinci.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState(error = "Please enter both email and password.")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)
            authRepository.login(email, password)
                .onSuccess {
                    _uiState.value = LoginUiState(isSuccess = true)
                }
                .onFailure { e ->
                    _uiState.value = LoginUiState(error = e.message ?: "Login failed")
                }
        }
    }

    fun loginWithGoogle() {
        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)
            authRepository.loginWithGoogle()
                .onSuccess {
                    _uiState.value = LoginUiState(isSuccess = true)
                }
                .onFailure { e ->
                    _uiState.value = LoginUiState(error = e.message ?: "Google sign-in failed")
                }
        }
    }
}
