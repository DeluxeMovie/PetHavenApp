package com.example.pethavenapp.data.repository

import com.example.pethavenapp.data.model.PetsResult

interface PetRepository {
    suspend fun getPetsByOwner(ownerId: Long): PetsResult
}
