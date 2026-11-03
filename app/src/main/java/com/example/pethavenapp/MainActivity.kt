package com.example.pethavenapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pethavenapp.navigation.PetHavenNavHost
import com.example.pethavenapp.ui.theme.PetHavenAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // La interfaz es clara: los íconos de las barras del sistema deben ser oscuros.
        val lightBars = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        enableEdgeToEdge(statusBarStyle = lightBars, navigationBarStyle = lightBars)
        setContent {
            PetHavenAppTheme {
                PetHavenNavHost()
            }
        }
    }
}
