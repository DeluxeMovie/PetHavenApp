package com.example.pethavenapp.data.remote.dto

import com.example.pethavenapp.data.model.Session
import com.example.pethavenapp.data.model.User
import com.example.pethavenapp.data.model.UserRole
import kotlinx.serialization.Serializable

// Contrato esperado de POST /auth/login. Ajusta los nombres aquí si tu API Ktor usa otros.
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    /** CLIENT, VETERINARIAN o ADMIN. */
    val role: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val user: UserDto
)

fun LoginResponse.toSession() = Session(
    token = token,
    user = User(
        id = user.id,
        name = user.name,
        email = user.email,
        role = user.role.toUserRole()
    )
)

private fun String.toUserRole() = when (uppercase()) {
    "CLIENT" -> UserRole.Client
    "VETERINARIAN" -> UserRole.Veterinarian
    "ADMIN" -> UserRole.Admin
    else -> throw IllegalArgumentException("Rol desconocido: $this")
}
