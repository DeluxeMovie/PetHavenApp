package com.example.pethavenapp.util

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Puntos de quiebre de Material Design para el ancho de ventana.
private val MediumBreakpoint = 600.dp
private val ExpandedBreakpoint = 840.dp

enum class WidthClass { Compact, Medium, Expanded }

/**
 * Medidas de la ventana disponible. Toda dimensión de la UI se expresa como fracción de
 * estas medidas, así el diseño se adapta a cualquier pantalla, orientación o ventana dividida.
 */
@Immutable
class ScreenMetrics(private val width: Dp, private val height: Dp) {

    val isWide: Boolean get() = width > height

    val widthClass: WidthClass
        get() = when {
            width < MediumBreakpoint -> WidthClass.Compact
            width < ExpandedBreakpoint -> WidthClass.Medium
            else -> WidthClass.Expanded
        }

    /** Fracción del ancho que ocupa el contenido de una sola columna. */
    val contentFraction: Float
        get() = when (widthClass) {
            WidthClass.Compact -> 0.88f
            WidthClass.Medium -> 0.6f
            WidthClass.Expanded -> 0.5f
        }

    /** Fracción de la dimensión menor: da la misma proporción en vertical y en horizontal. */
    fun unit(fraction: Float): Dp = minOf(width, height) * fraction
}

val LocalScreenMetrics = staticCompositionLocalOf<ScreenMetrics> {
    error("ScreenMetrics no disponible: envuelve la pantalla en ProvideScreenMetrics")
}

@Composable
fun ProvideScreenMetrics(
    modifier: Modifier = Modifier,
    content: @Composable (ScreenMetrics) -> Unit
) {
    BoxWithConstraints(modifier = modifier) {
        val metrics = remember(maxWidth, maxHeight) { ScreenMetrics(maxWidth, maxHeight) }
        CompositionLocalProvider(LocalScreenMetrics provides metrics) {
            content(metrics)
        }
    }
}
