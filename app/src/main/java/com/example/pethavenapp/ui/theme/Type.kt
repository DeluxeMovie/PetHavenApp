package com.example.pethavenapp.ui.theme

import androidx.annotation.FontRes
import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.example.pethavenapp.R

private val weights = listOf(
    FontWeight.Normal,
    FontWeight.Medium,
    FontWeight.SemiBold,
    FontWeight.Bold
)

// Fuentes variables: el grosor se fija por eje (API 26+); en versiones anteriores se usa el grosor por defecto.
@OptIn(ExperimentalTextApi::class)
private fun variableFamily(@FontRes fontRes: Int) = FontFamily(
    weights.map { weight ->
        Font(
            resId = fontRes,
            weight = weight,
            variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight))
        )
    }
)

private val Fraunces = variableFamily(R.font.fraunces)
private val NunitoSans = variableFamily(R.font.nunito_sans)

private fun Typography.withFamilies(heading: FontFamily, body: FontFamily) = copy(
    displayLarge = displayLarge.copy(fontFamily = heading),
    displayMedium = displayMedium.copy(fontFamily = heading),
    displaySmall = displaySmall.copy(fontFamily = heading),
    headlineLarge = headlineLarge.copy(fontFamily = heading),
    headlineMedium = headlineMedium.copy(fontFamily = heading),
    headlineSmall = headlineSmall.copy(fontFamily = heading),
    titleLarge = titleLarge.copy(fontFamily = heading),
    titleMedium = titleMedium.copy(fontFamily = body),
    titleSmall = titleSmall.copy(fontFamily = body),
    bodyLarge = bodyLarge.copy(fontFamily = body),
    bodyMedium = bodyMedium.copy(fontFamily = body),
    bodySmall = bodySmall.copy(fontFamily = body),
    labelLarge = labelLarge.copy(fontFamily = body),
    labelMedium = labelMedium.copy(fontFamily = body),
    labelSmall = labelSmall.copy(fontFamily = body)
)

// Fraunces para títulos y encabezados, Nunito Sans para texto de cuerpo y apoyo.
val Typography = Typography().withFamilies(heading = Fraunces, body = NunitoSans)
