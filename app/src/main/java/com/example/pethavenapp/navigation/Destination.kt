package com.example.pethavenapp.navigation

sealed class Destination(val route: String) {
    data object Welcome : Destination("welcome")
    data object Login : Destination("login")
}
