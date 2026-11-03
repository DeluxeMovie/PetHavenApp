package com.example.pethavenapp.data.repository

import com.example.pethavenapp.data.model.Pet
import com.example.pethavenapp.data.model.PetDetail
import com.example.pethavenapp.data.model.PetsError
import com.example.pethavenapp.data.model.PetsResult
import com.example.pethavenapp.data.remote.PetApi
import com.example.pethavenapp.data.remote.dto.toPet
import com.example.pethavenapp.data.remote.dto.toTreatment
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class KtorPetRepository(private val api: PetApi) : PetRepository {

    override suspend fun getPetsByOwner(ownerId: Long): PetsResult<List<Pet>> = safeCall {
        api.getPetsByOwner(ownerId).map { it.toPet() }
    }

    override suspend fun getPetDetail(petId: Long): PetsResult<PetDetail> = safeCall {
        coroutineScope {
            val pet = async { api.getPet(petId).toPet() }
            val treatments = async { api.getTreatments(petId).map { it.toTreatment() } }
            // La API los entrega del más antiguo al más reciente; al dueño le interesa lo último primero.
            PetDetail(pet.await(), treatments.await().asReversed())
        }
    }

    private suspend fun <T> safeCall(block: suspend () -> T): PetsResult<T> = try {
        PetsResult.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: IOException) {
        PetsResult.Failure(PetsError.Network)
    } catch (e: Exception) {
        PetsResult.Failure(PetsError.Unknown)
    }
}
