package com.example.pethavenapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush

/** Fondo de las pantallas de marca: un rosado suave arriba que se funde con el crema. */
@Composable
fun brandBackground(): Brush = Brush.verticalGradient(
    colors = listOf(
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.background
    )
)
