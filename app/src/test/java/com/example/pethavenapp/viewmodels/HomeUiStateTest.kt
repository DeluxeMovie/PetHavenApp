package com.example.pethavenapp.viewmodels

import com.example.pethavenapp.data.model.Pet
import com.example.pethavenapp.data.model.Species
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeUiStateTest {

    private fun pet(id: Long, inClinic: Boolean) =
        Pet(id, "Mascota $id", Species.Dog, "Mestizo", 2, 10.0, null, null, inClinic)

    @Test
    fun countsPetsInClinicAndAtHome() {
        val state = HomeUiState(pets = listOf(pet(1, true), pet(2, false), pet(3, true)), isLoading = false)

        assertEquals(3, state.totalPets)
        assertEquals(2, state.inClinicPets)
        assertEquals(1, state.atHomePets)
    }

    @Test
    fun emptyListHasNoCounts() {
        val state = HomeUiState(isLoading = false)

        assertEquals(0, state.totalPets)
        assertEquals(0, state.inClinicPets)
        assertEquals(0, state.atHomePets)
    }
}
