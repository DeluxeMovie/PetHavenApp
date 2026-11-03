package com.example.pethavenapp.data.model

enum class Species { Dog, Cat }

data class Pet(
    val id: Long,
    val name: String,
    val species: Species,
    val breed: String,
    val age: Int,
    val weight: Double,
    val disease: String?,
    /** URL de la foto servida por nuestra API, o null si la mascota no tiene foto. */
    val photoUrl: String?,
    /** true: está en la veterinaria; false: está en su casa. */
    val inClinic: Boolean
)

enum class PetsError { Network, Unknown }

sealed interface PetsResult {
    data class Success(val pets: List<Pet>) : PetsResult
    data class Failure(val error: PetsError) : PetsResult
}
