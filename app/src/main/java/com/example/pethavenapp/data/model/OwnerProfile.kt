package com.example.pethavenapp.data.model

/** Datos de contacto del dueño registrados en la clínica. */
data class OwnerProfile(
    val id: Long,
    val name: String,
    val email: String,
    val document: String?,
    val phone: String?
)
