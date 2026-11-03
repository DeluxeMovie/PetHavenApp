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

/** El ingreso de la mascota no tiene evolución: en ese caso el tratamiento guarda null. */
enum class Evolution { Stable, Improving, Worsening, Recovered }

data class TreatmentDrug(
    val name: String,
    val units: Int
)

data class Treatment(
    val id: Long,
    /** Fecha y hora ya listas para mostrar, p. ej. "05/08/2026 · 10:30". */
    val date: String,
    val veterinarianName: String,
    val weight: Double?,
    val diagnosis: String?,
    val evolution: Evolution?,
    val notes: String?,
    val drugs: List<TreatmentDrug>
)

/** Ficha completa de una mascota: sus datos y su historial de tratamientos (el más reciente primero). */
data class PetDetail(
    val pet: Pet,
    val treatments: List<Treatment>
)

enum class PetsError { Network, Unknown }

sealed interface PetsResult<out T> {
    data class Success<T>(val data: T) : PetsResult<T>
    data class Failure(val error: PetsError) : PetsResult<Nothing>
}
