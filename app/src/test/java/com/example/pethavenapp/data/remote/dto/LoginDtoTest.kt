package com.example.pethavenapp.data.remote.dto

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class LoginDtoTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun mapsAuthResponseToSessionWithTokenAndOwnerId() {
        val body = """
            {"token":"abc.def.ghi","expiresIn":7200,"profileId":7,"name":"John Doe",
             "email":"john.doe@example.com","role":"OWNER","campoNuevo":"se ignora"}
        """.trimIndent()

        val session = json.decodeFromString<LoginResponse>(body).toSession()

        assertEquals("abc.def.ghi", session.token)
        assertEquals(7L, session.user.id)
        assertEquals("John Doe", session.user.name)
        assertEquals("john.doe@example.com", session.user.email)
    }
}
