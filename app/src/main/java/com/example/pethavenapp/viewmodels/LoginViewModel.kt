package com.example.pethavenapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pethavenapp.R
import com.example.pethavenapp.data.AppContainer
import com.example.pethavenapp.data.model.AuthError
import com.example.pethavenapp.data.model.AuthResult
import com.example.pethavenapp.data.repository.AuthRepository
import com.example.pethavenapp.util.isValidEmail
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = Channel<LoginEvent>(Channel.BUFFERED)
    val events: Flow<LoginEvent> = _events.receiveAsFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null, authError = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null, authError = null) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick() {
        val state = _uiState.value
        if (state.isLoading) return

        val email = state.email.trim()
        val emailError = when {
            email.isEmpty() -> R.string.login_error_email_empty
            !email.isValidEmail() -> R.string.login_error_email_invalid
            else -> null
        }
        val passwordError = if (state.password.isEmpty()) R.string.login_error_password_empty else null
        if (emailError != null || passwordError != null) {
            _uiState.update { it.copy(emailError = emailError, passwordError = passwordError) }
            return
        }

        _uiState.update { it.copy(isLoading = true, authError = null) }
        viewModelScope.launch {
            when (val result = repository.login(email, state.password)) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(LoginEvent.LoggedIn(result.session))
                }
                is AuthResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false, authError = result.error.messageRes()) }
                }
            }
        }
    }

    private fun AuthError.messageRes(): Int = when (this) {
        AuthError.InvalidCredentials -> R.string.auth_error_invalid_credentials
        AuthError.Network -> R.string.auth_error_network
        AuthError.Unknown -> R.string.auth_error_unknown
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { LoginViewModel(AppContainer.authRepository) }
        }
    }
}
