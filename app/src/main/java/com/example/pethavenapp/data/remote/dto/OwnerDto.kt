package com.example.pethavenapp.data.remote.dto

import com.example.pethavenapp.data.model.OwnerProfile
import kotlinx.serialization.Serializable

/** OwnerResponse de GET /api/owners/{id}. */
@Serializable
data class OwnerDto(
    val id: Long,
    val document: String? = null,
    val name: String,
    val email: String? = null,
    val phone: String? = null
)

fun OwnerDto.toProfile() = OwnerProfile(
    id = id,
    name = name,
    email = email.orEmpty(),
    document = document?.takeIf { it.isNotBlank() },
    phone = phone?.takeIf { it.isNotBlank() }
)
