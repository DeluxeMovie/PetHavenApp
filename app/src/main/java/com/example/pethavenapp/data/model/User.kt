package com.example.pethavenapp.data.model

enum class UserRole { Client, Veterinarian, Admin }

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole
)
