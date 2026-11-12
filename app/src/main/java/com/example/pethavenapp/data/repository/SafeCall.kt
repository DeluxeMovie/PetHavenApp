package com.example.pethavenapp.data.repository

import com.example.pethavenapp.data.model.PetsError
import com.example.pethavenapp.data.model.PetsResult
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

/** Ejecuta una llamada a la API y traduce sus fallos a un error de dominio; la cancelación se propaga. */
internal suspend fun <T> safeCall(block: suspend () -> T): PetsResult<T> = try {
    PetsResult.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: IOException) {
    PetsResult.Failure(PetsError.Network)
} catch (e: Exception) {
    PetsResult.Failure(PetsError.Unknown)
}
