package com.example.pethavenapp.data.repository

import com.example.pethavenapp.data.model.PetsError
import com.example.pethavenapp.data.model.PetsResult
import com.example.pethavenapp.data.remote.PetApi
import com.example.pethavenapp.data.remote.dto.toPet
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class KtorPetRepository(private val api: PetApi) : PetRepository {

    override suspend fun getPetsByOwner(ownerId: Long): PetsResult = try {
        PetsResult.Success(api.getPetsByOwner(ownerId).map { it.toPet() })
    } catch (e: CancellationException) {
        throw e
    } catch (e: IOException) {
        PetsResult.Failure(PetsError.Network)
    } catch (e: Exception) {
        PetsResult.Failure(PetsError.Unknown)
    }
}
