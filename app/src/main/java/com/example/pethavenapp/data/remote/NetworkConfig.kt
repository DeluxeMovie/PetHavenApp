package com.example.pethavenapp.data.remote

object NetworkConfig {
    /** Cambia a false cuando el servidor Ktor esté corriendo para usar la API real. */
    const val USE_FAKE_BACKEND = true

    /** 10.0.2.2 es el localhost de tu computador visto desde el emulador de Android. */
    const val BASE_URL = "http://10.0.2.2:8080/"

    const val CONNECT_TIMEOUT_MILLIS = 10_000L
    const val REQUEST_TIMEOUT_MILLIS = 15_000L
}
