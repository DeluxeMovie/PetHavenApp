package com.example.pethavenapp.data.repository

import com.example.pethavenapp.data.model.Pet
import com.example.pethavenapp.data.model.PetDetail
import com.example.pethavenapp.data.model.PetsResult

interface PetRepository {
    suspend fun getPetsByOwner(ownerId: Long): PetsResult<List<Pet>>

    suspend fun getPetDetail(petId: Long): PetsResult<PetDetail>
}
