package com.example.pethavenapp.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidatorsTest {

    @Test
    fun acceptsCommonEmails() {
        assertTrue("john.doe@example.com".isValidEmail())
        assertTrue("valentina.soler@pethaven.com".isValidEmail())
        assertTrue("a+tag@sub.dominio.co".isValidEmail())
    }

    @Test
    fun rejectsMalformedEmails() {
        assertFalse("".isValidEmail())
        assertFalse("sin-arroba.com".isValidEmail())
        assertFalse("usuario@".isValidEmail())
        assertFalse("usuario@dominio".isValidEmail())
        assertFalse("con espacio@dominio.com".isValidEmail())
    }
}
