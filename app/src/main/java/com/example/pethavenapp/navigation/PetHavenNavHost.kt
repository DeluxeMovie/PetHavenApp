package com.example.pethavenapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.pethavenapp.ui.screens.PetDetailScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pethavenapp.data.AppContainer
import com.example.pethavenapp.ui.screens.HomeScreen
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
            LoginScreen(onLoginSuccess = { session ->
                AppContainer.session = session
                navController.navigate(Destination.Home.route) {
                    popUpTo(Destination.Welcome.route)
                }
            })
        }
        composable(Destination.Home.route) {
            val session = AppContainer.session
            if (session == null) {
                // El proceso se reinició y se perdió la sesión: hay que volver a iniciar sesión.
                LaunchedEffect(Unit) {
                    navController.navigate(Destination.Login.route) {
                        popUpTo(Destination.Home.route) { inclusive = true }
                    }
                }
            } else {
                HomeScreen(
                    owner = session.user,
                    onPetClick = { petId -> navController.navigate(Destination.PetDetail.createRoute(petId)) },
                    onLogout = {
                        AppContainer.session = null
                        navController.navigate(Destination.Login.route) {
                            popUpTo(Destination.Home.route) { inclusive = true }
                        }
                    }
                )
            }
        }
        composable(
            route = Destination.PetDetail.route,
            arguments = listOf(navArgument(Destination.PET_ID_ARG) { type = NavType.LongType })
        ) { entry ->
            PetDetailScreen(
                petId = entry.arguments?.getLong(Destination.PET_ID_ARG) ?: return@composable,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
