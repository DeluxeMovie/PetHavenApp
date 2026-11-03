package com.example.pethavenapp.data.remote.dto

import com.example.pethavenapp.data.model.Evolution
import com.example.pethavenapp.data.model.Treatment
import com.example.pethavenapp.data.model.TreatmentDrug
import kotlinx.serialization.Serializable

/** TreatmentDrugResponse. Los precios de compra y venta existen en la API pero el dueño no los necesita. */
@Serializable
data class TreatmentDrugDto(
    val drugName: String,
    val units: Int = 0
)

/** TreatmentResponse de GET /api/pets/{id}/treatments. */
@Serializable
data class TreatmentDto(
    val id: Long,
    /** LocalDateTime ISO sin zona, p. ej. 2026-08-05T10:30:00. */
    val date: String,
    val veterinarianName: String? = null,
    val weight: Double? = null,
    val diagnosis: String? = null,
    /** STABLE, IMPROVING, WORSENING, RECOVERED o null en el ingreso. */
    val evolution: String? = null,
    val notes: String? = null,
    val drugs: List<TreatmentDrugDto> = emptyList()
)

fun TreatmentDto.toTreatment() = Treatment(
    id = id,
    date = date.toDisplayDate(),
    veterinarianName = veterinarianName.orEmpty(),
    weight = weight,
    diagnosis = diagnosis?.takeIf { it.isNotBlank() },
    evolution = evolution.toEvolution(),
    notes = notes?.takeIf { it.isNotBlank() },
    drugs = drugs.map { TreatmentDrug(it.drugName, it.units) }
)

private fun String?.toEvolution() = when (this?.uppercase()) {
    "STABLE" -> Evolution.Stable
    "IMPROVING" -> Evolution.Improving
    "WORSENING" -> Evolution.Worsening
    "RECOVERED" -> Evolution.Recovered
    else -> null
}

/** "2026-08-05T10:30:00" -> "05/08/2026 · 10:30". Si el formato no es el esperado se muestra tal cual. */
private fun String.toDisplayDate(): String {
    val match = Regex("""(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2})""").find(this) ?: return this
    val (year, month, day, hour, minute) = match.destructured
    return "$day/$month/$year · $hour:$minute"
}
