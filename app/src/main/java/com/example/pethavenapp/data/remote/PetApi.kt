package com.example.pethavenapp.data.remote

import com.example.pethavenapp.data.remote.dto.PetDto
import com.example.pethavenapp.data.remote.dto.TreatmentDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class PetApi(private val client: HttpClient) {

    suspend fun getPetsByOwner(ownerId: Long): List<PetDto> =
        client.get("owners/$ownerId/pets").body()

    suspend fun getPet(petId: Long): PetDto =
        client.get("pets/$petId").body()

    suspend fun getTreatments(petId: Long): List<TreatmentDto> =
        client.get("pets/$petId/treatments").body()
}
