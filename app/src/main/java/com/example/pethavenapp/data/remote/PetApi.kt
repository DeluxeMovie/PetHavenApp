package com.example.pethavenapp.data.remote

import com.example.pethavenapp.data.remote.dto.PetDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class PetApi(private val client: HttpClient) {

    suspend fun getPetsByOwner(ownerId: Long): List<PetDto> =
        client.get("owners/$ownerId/pets").body()
}
