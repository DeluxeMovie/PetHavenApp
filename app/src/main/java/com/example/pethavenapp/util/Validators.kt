package com.example.pethavenapp.util

private val EmailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

fun String.isValidEmail(): Boolean = EmailRegex.matches(this)
