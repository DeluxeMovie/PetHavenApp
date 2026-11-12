package com.example.pethavenapp.data.remote.dto

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OwnerDtoTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun mapsOwnerResponseToProfile() {
        val body = """{"id":1,"document":"123456789","name":"John Doe","email":"john.doe@example.com","phone":"1234567890"}"""

        val profile = json.decodeFromString<OwnerDto>(body).toProfile()

        assertEquals(1L, profile.id)
        assertEquals("John Doe", profile.name)
        assertEquals("john.doe@example.com", profile.email)
        assertEquals("123456789", profile.document)
        assertEquals("1234567890", profile.phone)
    }

    @Test
    fun blankOptionalFieldsBecomeNull() {
        val body = """{"id":2,"name":"Jane Doe","document":" ","phone":""}"""

        val profile = json.decodeFromString<OwnerDto>(body).toProfile()

        assertNull(profile.document)
        assertNull(profile.phone)
        assertEquals("", profile.email)
    }
}
