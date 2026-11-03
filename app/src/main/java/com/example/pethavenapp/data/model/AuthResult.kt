package com.example.pethavenapp.data.model

enum class AuthError { InvalidCredentials, Network, Unknown }

sealed interface AuthResult {
    data class Success(val session: Session) : AuthResult
    data class Failure(val error: AuthError) : AuthResult
}
