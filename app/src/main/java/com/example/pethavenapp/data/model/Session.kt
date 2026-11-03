package com.example.pethavenapp.data.model

/** La API no emite token: la sesión es solo el dueño que inició sesión. */
data class Session(
    val user: User
)
