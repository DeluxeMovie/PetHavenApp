package com.example.pethavenapp.data

import com.example.pethavenapp.data.model.Session
import com.example.pethavenapp.data.remote.AuthApi
import com.example.pethavenapp.data.remote.NetworkConfig
import com.example.pethavenapp.data.remote.OwnerApi
import com.example.pethavenapp.data.remote.PetApi
import com.example.pethavenapp.data.remote.createHttpClient
import com.example.pethavenapp.data.repository.AuthRepository
import com.example.pethavenapp.data.repository.FakeAuthRepository
import com.example.pethavenapp.data.repository.KtorAuthRepository
import com.example.pethavenapp.data.repository.KtorOwnerRepository
import com.example.pethavenapp.data.repository.KtorPetRepository
import com.example.pethavenapp.data.repository.OwnerRepository
import com.example.pethavenapp.data.repository.PetRepository

/** Punto único donde se decide qué implementación de datos usa la app. */
object AppContainer {

    private val httpClient by lazy { createHttpClient { session?.token } }

    val authRepository: AuthRepository by lazy {
        if (NetworkConfig.USE_FAKE_BACKEND) {
            FakeAuthRepository()
        } else {
            KtorAuthRepository(AuthApi(httpClient))
        }
    }

    val petRepository: PetRepository by lazy { KtorPetRepository(PetApi(httpClient)) }

    val ownerRepository: OwnerRepository by lazy { KtorOwnerRepository(OwnerApi(httpClient)) }

    /** Dueño con sesión iniciada; null si no ha iniciado sesión (o si el proceso se reinició). */
    @Volatile
    var session: Session? = null
}
