package com.example.pethavenapp.viewmodels

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pethavenapp.data.AppContainer
import com.example.pethavenapp.data.model.PetDetail
import com.example.pethavenapp.data.model.PetsResult
import com.example.pethavenapp.data.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Immutable
data class PetDetailUiState(
    val detail: PetDetail? = null,
    val isLoading: Boolean = true,
    @param:StringRes val error: Int? = null
)

class PetDetailViewModel(
    private val petId: Long,
    private val repository: PetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PetDetailUiState())
    val uiState: StateFlow<PetDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            when (val result = repository.getPetDetail(petId)) {
                is PetsResult.Success -> _uiState.update { it.copy(isLoading = false, detail = result.data) }
                is PetsResult.Failure -> _uiState.update { it.copy(isLoading = false, error = result.error.messageRes()) }
            }
        }
    }

    companion object {
        fun factory(petId: Long) = viewModelFactory {
            initializer { PetDetailViewModel(petId, AppContainer.petRepository) }
        }
    }
}
