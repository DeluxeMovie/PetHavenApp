package com.example.pethavenapp.data.remote

import com.example.pethavenapp.data.remote.dto.LoginRequest
import com.example.pethavenapp.data.remote.dto.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthApi(private val client: HttpClient) {

    suspend fun login(request: LoginRequest): LoginResponse =
        client.post("auth/login") { setBody(request) }.body()
}
