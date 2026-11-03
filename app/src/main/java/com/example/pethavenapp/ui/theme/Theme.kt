package com.example.pethavenapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// La identidad de marca es clara; no se usa color dinámico para conservar la paleta.
private val PetHavenColorScheme = lightColorScheme(
    primary = PetHavenGreen,
    onPrimary = PetHavenCream,
    primaryContainer = PetHavenOlive,
    onPrimaryContainer = PetHavenInk,
    secondary = PetHavenOlive,
    onSecondary = PetHavenInk,
    secondaryContainer = PetHavenPink,
    onSecondaryContainer = PetHavenBrown,
    tertiary = PetHavenBrown,
    onTertiary = PetHavenCream,
    background = PetHavenCream,
    onBackground = PetHavenInk,
    surface = PetHavenCream,
    onSurface = PetHavenInk,
    surfaceVariant = PetHavenSurface,
    onSurfaceVariant = PetHavenInkSoft,
    surfaceContainerLow = PetHavenSurface,
    error = PetHavenError,
    onError = PetHavenCream,
    errorContainer = PetHavenErrorContainer,
    onErrorContainer = PetHavenError,
    outline = PetHavenInkSoft,
    outlineVariant = PetHavenOutline
)

@Composable
fun PetHavenAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PetHavenColorScheme,
        typography = Typography,
        content = content
    )
}
