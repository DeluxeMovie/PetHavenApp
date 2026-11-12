package com.example.pethavenapp.viewmodels

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val document: String? = null,
    val phone: String? = null,
    val isLoading: Boolean = true,
    @param:StringRes val error: Int? = null
) {
    /** Inicial del nombre para el avatar. */
    val initial: String get() = name.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "?"
}
