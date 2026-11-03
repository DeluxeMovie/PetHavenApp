package com.example.pethavenapp.data.remote

object NetworkConfig {
    /** Cambia a true para trabajar sin el servidor Spring Boot (solo simula el login). */
    const val USE_FAKE_BACKEND = false

    /**
     * 10.0.2.2 es el localhost de tu computador visto desde el emulador de Android.
     * En un teléfono físico usa la IP de tu PC en la red WiFi (p. ej. http://192.168.1.20:8080/api/).
     * Debe terminar en "/" para que las rutas relativas de las APIs se resuelvan bien.
     */
    const val BASE_URL = "http://10.0.2.2:8080/api/"

    const val CONNECT_TIMEOUT_MILLIS = 10_000L
    const val REQUEST_TIMEOUT_MILLIS = 15_000L
}
