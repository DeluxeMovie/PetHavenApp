package com.example.pethavenapp.data.repository

import com.example.pethavenapp.data.model.AuthError
import com.example.pethavenapp.data.model.AuthResult
import com.example.pethavenapp.data.model.Session
import com.example.pethavenapp.data.model.User
import kotlinx.coroutines.delay

/** Reemplaza al servidor cuando NetworkConfig.USE_FAKE_BACKEND es true. Usa el dueño de prueba del README web. */
class FakeAuthRepository : AuthRepository {

    private val user = User(1, "John Doe", "john.doe@example.com")

    override suspend fun login(email: String, password: String): AuthResult {
        delay(SIMULATED_LATENCY_MILLIS)
        return if (user.email.equals(email, ignoreCase = true) && password == "password123") {
            AuthResult.Success(Session(user, token = "fake-token"))
        } else {
            AuthResult.Failure(AuthError.InvalidCredentials)
        }
    }

    private companion object {
        const val SIMULATED_LATENCY_MILLIS = 1_200L
    }
}
