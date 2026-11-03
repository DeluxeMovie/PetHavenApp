package com.example.pethavenapp.data.remote.dto

import com.example.pethavenapp.data.model.Pet
import com.example.pethavenapp.data.model.Species
import com.example.pethavenapp.data.remote.NetworkConfig
import kotlinx.serialization.Serializable

/** PetResponse de GET /api/owners/{id}/pets. */
@Serializable
data class PetDto(
    val id: Long,
    val name: String,
    /** DOG o CAT. */
    val species: String,
    val breed: String? = null,
    val age: Int? = null,
    val weight: Double? = null,
    val disease: String? = null,
    val photoUrl: String? = null,
    val active: Boolean = false,
    val ownerId: Long? = null,
    val ownerName: String? = null
)

fun PetDto.toPet() = Pet(
    id = id,
    name = name,
    species = if (species.equals("CAT", ignoreCase = true)) Species.Cat else Species.Dog,
    breed = breed.orEmpty(),
    age = age ?: 0,
    weight = weight ?: 0.0,
    disease = disease?.takeIf { it.isNotBlank() },
    // La API descarga y sirve la foto ella misma (GET /api/pets/{id}/photo): no dependemos del host externo.
    photoUrl = photoUrl?.let { "${NetworkConfig.BASE_URL}pets/$id/photo" },
    inClinic = active
)
