package com.example.pethavenapp.data.remote.dto

import com.example.pethavenapp.data.model.Session
import com.example.pethavenapp.data.model.User
import kotlinx.serialization.Serializable

// Contrato de POST /api/auth/login, el login único de dueños, veterinarios y administradores.
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

/** AuthResponse de la API: token JWT y perfil del usuario autenticado. */
@Serializable
data class LoginResponse(
    val token: String,
    val expiresIn: Long = 0,
    /** Para un dueño es su id de dueño, el mismo que usa GET /api/owners/{id}/pets. */
    val profileId: Long,
    val name: String,
    val email: String,
    /** OWNER, VETERINARIAN o ADMIN. */
    val role: String
)

fun LoginResponse.toSession() = Session(User(id = profileId, name = name, email = email), token)
