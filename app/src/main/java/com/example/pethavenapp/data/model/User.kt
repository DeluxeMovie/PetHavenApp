package com.example.pethavenapp.data.model

/** Dueño autenticado. La app solo la usan dueños, así que no se guarda el rol. */
data class User(
    val id: Long,
    val name: String,
    val email: String
)
