package com.example.pethavenapp.data.remote

import com.example.pethavenapp.data.remote.dto.OwnerDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class OwnerApi(private val client: HttpClient) {

    suspend fun getOwner(ownerId: Long): OwnerDto =
        client.get("owners/$ownerId").body()
}
