package com.example.pethavenapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pethavenapp.ui.screens.LoginScreen
import com.example.pethavenapp.ui.screens.WelcomeScreen

@Composable
fun PetHavenNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Destination.Welcome.route,
        modifier = modifier
    ) {
        composable(Destination.Welcome.route) {
            WelcomeScreen(onGetStarted = { navController.navigate(Destination.Login.route) })
        }
        composable(Destination.Login.route) {
            // Al iniciar sesión se navegará al inicio según el rol del usuario (cliente, veterinario o administrador).
            LoginScreen(onLoginSuccess = {})
        }
    }
}
