package com.example.pethavenapp.viewmodels

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.example.pethavenapp.data.model.Pet

@Immutable
data class HomeUiState(
    val ownerName: String = "",
    val pets: List<Pet> = emptyList(),
    val isLoading: Boolean = true,
    @param:StringRes val error: Int? = null
)
