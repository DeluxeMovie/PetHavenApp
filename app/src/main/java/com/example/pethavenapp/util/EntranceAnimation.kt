package com.example.pethavenapp.util

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun rememberEntranceProgress(delayMillis: Int = 0): State<Float> {
    var started by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { started = true }
    return animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(durationMillis = 600, delayMillis = delayMillis, easing = FastOutSlowInEasing),
        label = "entrance"
    )
}

/** Aparece con fundido y un leve desplazamiento vertical proporcional a la altura del elemento. */
fun Modifier.entrance(progress: State<Float>, offsetFraction: Float = 0.25f): Modifier = graphicsLayer {
    alpha = progress.value
    translationY = (1f - progress.value) * size.height * offsetFraction
}
