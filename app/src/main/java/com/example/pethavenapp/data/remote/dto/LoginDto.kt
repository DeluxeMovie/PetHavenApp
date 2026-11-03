package com.example.pethavenapp.data.remote.dto

import com.example.pethavenapp.data.model.Session
import com.example.pethavenapp.data.model.User
import kotlinx.serialization.Serializable

// Contrato de POST /api/auth/owners/login.
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

/** AuthResponse de la API: el dueño autenticado, sin token. */
@Serializable
data class LoginResponse(
    val id: Long,
    val name: String,
    val email: String,
    val role: String
)

fun LoginResponse.toSession() = Session(User(id = id, name = name, email = email))
