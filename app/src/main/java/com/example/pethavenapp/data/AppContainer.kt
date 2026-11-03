package com.example.pethavenapp.data

import com.example.pethavenapp.data.remote.AuthApi
import com.example.pethavenapp.data.remote.NetworkConfig
import com.example.pethavenapp.data.remote.createHttpClient
import com.example.pethavenapp.data.repository.AuthRepository
import com.example.pethavenapp.data.repository.FakeAuthRepository
import com.example.pethavenapp.data.repository.KtorAuthRepository

/** Punto único donde se decide qué implementación de datos usa la app. */
object AppContainer {

    val authRepository: AuthRepository by lazy {
        if (NetworkConfig.USE_FAKE_BACKEND) {
            FakeAuthRepository()
        } else {
            KtorAuthRepository(AuthApi(createHttpClient()))
        }
    }
}
