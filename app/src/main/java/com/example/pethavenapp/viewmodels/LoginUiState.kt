package com.example.pethavenapp.viewmodels

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.example.pethavenapp.data.model.Session

@Immutable
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    @param:StringRes val emailError: Int? = null,
    @param:StringRes val passwordError: Int? = null,
    @param:StringRes val authError: Int? = null,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false
)

sealed interface LoginEvent {
    data class LoggedIn(val session: Session) : LoginEvent
}
