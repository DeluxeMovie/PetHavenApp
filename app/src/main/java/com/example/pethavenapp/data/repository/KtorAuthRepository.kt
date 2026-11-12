package com.example.pethavenapp.data.repository

import com.example.pethavenapp.data.model.AuthError
import com.example.pethavenapp.data.model.AuthResult
import com.example.pethavenapp.data.remote.AuthApi
import com.example.pethavenapp.data.remote.dto.LoginRequest
import com.example.pethavenapp.data.remote.dto.toSession
import io.ktor.client.plugins.ClientRequestException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class KtorAuthRepository(private val api: AuthApi) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthResult = try {
        val response = api.login(LoginRequest(email, password))
        // El login es común a todos los roles; esta app es solo para dueños.
        if (response.role.equals(OWNER_ROLE, ignoreCase = true)) {
            AuthResult.Success(response.toSession())
        } else {
            AuthResult.Failure(AuthError.NotAnOwner)
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: ClientRequestException) {
        // 4xx: el servidor rechazó las credenciales (p. ej. 400, 401 o 403).
        AuthResult.Failure(AuthError.InvalidCredentials)
    } catch (e: IOException) {
        AuthResult.Failure(AuthError.Network)
    } catch (e: Exception) {
        AuthResult.Failure(AuthError.Unknown)
    }

    private companion object {
        const val OWNER_ROLE = "OWNER"
    }
}
