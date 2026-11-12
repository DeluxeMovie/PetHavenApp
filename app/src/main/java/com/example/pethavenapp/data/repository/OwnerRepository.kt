package com.example.pethavenapp.data.repository

import com.example.pethavenapp.data.model.OwnerProfile
import com.example.pethavenapp.data.model.PetsResult

interface OwnerRepository {
    suspend fun getOwner(ownerId: Long): PetsResult<OwnerProfile>
}
