package com.example.pethavenapp.data.repository

import com.example.pethavenapp.data.model.AuthError
import com.example.pethavenapp.data.model.AuthResult
import com.example.pethavenapp.data.model.Session
import com.example.pethavenapp.data.model.User
import com.example.pethavenapp.data.model.UserRole
import kotlinx.coroutines.delay

/** Reemplaza al servidor mientras la API Ktor no existe. Usa los usuarios de prueba del README del proyecto web. */
class FakeAuthRepository : AuthRepository {

    private class Account(val password: String, val user: User)

    private val accounts = listOf(
        Account("password123", User("1", "John Doe", "john.doe@example.com", UserRole.Client)),
        Account("vet12345", User("2", "Valentina Soler", "valentina.soler@pethaven.com", UserRole.Veterinarian)),
        Account("admin12345", User("3", "Administrador", "admin@pethaven.com", UserRole.Admin))
    )

    override suspend fun login(email: String, password: String): AuthResult {
        delay(SIMULATED_LATENCY_MILLIS)
        val account = accounts.firstOrNull {
            it.user.email.equals(email, ignoreCase = true) && it.password == password
        } ?: return AuthResult.Failure(AuthError.InvalidCredentials)
        return AuthResult.Success(Session(token = "fake-token", user = account.user))
    }

    private companion object {
        const val SIMULATED_LATENCY_MILLIS = 1_200L
    }
}
