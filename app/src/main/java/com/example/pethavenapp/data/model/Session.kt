package com.example.pethavenapp.data.model

/** Dueño que inició sesión y el token JWT que la API exige en cada petición (vigencia de 2 horas). */
data class Session(
    val user: User,
    val token: String
)
