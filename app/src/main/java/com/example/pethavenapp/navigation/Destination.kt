package com.example.pethavenapp.navigation

sealed class Destination(val route: String) {
    data object Welcome : Destination("welcome")
    data object Login : Destination("login")
    data object Home : Destination("home")
    data object Profile : Destination("profile")
    data object PetDetail : Destination("pet/{$PET_ID_ARG}") {
        fun createRoute(petId: Long) = "pet/$petId"
    }

    companion object {
        const val PET_ID_ARG = "petId"
    }
}
