package com.example.pethavenapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
            // El destino de "Comenzar" se conecta cuando exista la pantalla de inicio de sesión.
            WelcomeScreen(onGetStarted = {})
        }
    }
}
