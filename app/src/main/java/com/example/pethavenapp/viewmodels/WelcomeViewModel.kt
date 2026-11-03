package com.example.pethavenapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.pethavenapp.data.repository.LocalWelcomeRepository
import com.example.pethavenapp.data.repository.WelcomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WelcomeViewModel(
    repository: WelcomeRepository = LocalWelcomeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(WelcomeUiState(features = repository.getFeatures()))
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()
}
