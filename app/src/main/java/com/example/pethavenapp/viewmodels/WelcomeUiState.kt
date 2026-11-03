package com.example.pethavenapp.viewmodels

import androidx.compose.runtime.Immutable
import com.example.pethavenapp.data.model.WelcomeFeature

@Immutable
data class WelcomeUiState(
    val features: List<WelcomeFeature> = emptyList()
)
