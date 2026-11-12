package com.example.pethavenapp.data.repository

import com.example.pethavenapp.data.model.OwnerProfile
import com.example.pethavenapp.data.model.PetsResult
import com.example.pethavenapp.data.remote.OwnerApi
import com.example.pethavenapp.data.remote.dto.toProfile

class KtorOwnerRepository(private val api: OwnerApi) : OwnerRepository {

    override suspend fun getOwner(ownerId: Long): PetsResult<OwnerProfile> = safeCall {
        api.getOwner(ownerId).toProfile()
    }
}
