package com.example.pethavenapp.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(): HttpClient = HttpClient(OkHttp) {
    // Las respuestas 4xx y 5xx lanzan excepción para que el repositorio las traduzca a un error de dominio.
    expectSuccess = true

    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(HttpTimeout) {
        connectTimeoutMillis = NetworkConfig.CONNECT_TIMEOUT_MILLIS
        requestTimeoutMillis = NetworkConfig.REQUEST_TIMEOUT_MILLIS
    }
    defaultRequest {
        url(NetworkConfig.BASE_URL)
        contentType(ContentType.Application.Json)
    }
}
