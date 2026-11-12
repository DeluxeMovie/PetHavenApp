package com.example.pethavenapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pethavenapp.data.AppContainer
import com.example.pethavenapp.data.model.PetsResult
import com.example.pethavenapp.data.model.User
import com.example.pethavenapp.data.repository.OwnerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val owner: User,
    private val repository: OwnerRepository
) : ViewModel() {

    // Nombre y correo ya se conocen por la sesión: se muestran de inmediato mientras llegan documento y teléfono.
    private val _uiState = MutableStateFlow(ProfileUiState(name = owner.name, email = owner.email))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            when (val result = repository.getOwner(owner.id)) {
                is PetsResult.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        name = result.data.name,
                        email = result.data.email.ifBlank { it.email },
                        document = result.data.document,
                        phone = result.data.phone
                    )
                }
                is PetsResult.Failure -> _uiState.update { it.copy(isLoading = false, error = result.error.messageRes()) }
            }
        }
    }

    companion object {
        /** Solo se crea con un dueño que ya inició sesión. */
        fun factory(owner: User) = viewModelFactory {
            initializer { ProfileViewModel(owner, AppContainer.ownerRepository) }
        }
    }
}
