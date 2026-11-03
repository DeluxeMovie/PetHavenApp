package com.example.pethavenapp.data.repository

import com.example.pethavenapp.data.model.AuthResult

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthResult
}
