package com.example.pethavenapp.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.example.pethavenapp.ui.theme.PetHavenBrown
import com.example.pethavenapp.ui.theme.PetHavenCream
import com.example.pethavenapp.ui.theme.PetHavenInk
import com.example.pethavenapp.ui.theme.PetHavenOlive
import com.example.pethavenapp.ui.theme.PetHavenOutline
import com.example.pethavenapp.ui.theme.PetHavenPink

// Dibujo vectorial (mismo sistema de rutas que SVG) en un lienzo de 300 x 200, mirando a la derecha.
private const val ViewportWidth = 300f
private const val ViewportHeight = 200f

private val OliveDark = Color(0xFF8A8655)
private val Snout = Color(0xFFD9D3B0)
private val Tongue = Color(0xFFDE9C9A)

private fun ellipse(cx: Float, cy: Float, rx: Float, ry: Float): List<PathNode> =
    addPathNodes("M${cx - rx} $cy A$rx $ry 0 1 0 ${cx + rx} $cy A$rx $ry 0 1 0 ${cx - rx} $cy Z")

private fun circle(cx: Float, cy: Float, r: Float) = ellipse(cx, cy, r, r)

private val ShadowPath = ellipse(150f, 184f, 86f, 5f)
private val BodyPath = ellipse(138f, 112f, 68f, 35f)
private val ChestPath = ellipse(184f, 100f, 30f, 30f)
private val HeadPath = circle(210f, 78f, 31f)
private val SnoutPath = ellipse(238f, 91f, 21f, 15f)
private val LipPath = ellipse(238f, 94f, 21f, 3f)
private val TonguePath = ellipse(243f, 105f, 6.5f, 10f)
private val NosePath = circle(256f, 84f, 6f)
private val EyePath = circle(219f, 70f, 4.6f)
private val EyeGlintPath = circle(220.6f, 68.4f, 1.4f)
private val TagPath = circle(198f, 111f, 5f)
private val EarPath = ellipse(196f, 80f, 11f, 25f)
private val TailPath = addPathNodes("M78 102 Q44 98 46 62")
private val CollarPath = addPathNodes("M178 92 Q192 112 212 108")
private val GroundClipPath = addPathNodes("M20 170 H280 V200 H20 Z")

private const val DashLength = 22f
private const val DashPeriod = 40f
private val GroundDashPaths = List(8) { addPathNodes("M${20f + it * DashPeriod} 184 h$DashLength") }
private val SpeedLinePaths = listOf(
    SpeedLine(addPathNodes("M14 92 H46"), phase = 0f),
    SpeedLine(addPathNodes("M6 116 H40"), phase = 0.4f),
    SpeedLine(addPathNodes("M18 140 H44"), phase = 0.7f)
)

private class SpeedLine(val path: List<PathNode>, val phase: Float)

private class DogMotion(
    val stride: State<Float>,
    val bounce: State<Float>,
    val wag: State<Float>,
    val flap: State<Float>,
    val ground: State<Float>,
    val speed: State<Float>
)

@Composable
private fun rememberDogMotion(): DogMotion {
    val transition = rememberInfiniteTransition(label = "runningDog")
    @Composable
    fun pingPong(from: Float, to: Float, millis: Int) = transition.animateFloat(
        initialValue = from,
        targetValue = to,
        animationSpec = infiniteRepeatable(tween(millis, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "dogPingPong"
    )
    @Composable
    fun loop(to: Float, millis: Int) = transition.animateFloat(
        initialValue = 0f,
        targetValue = to,
        animationSpec = InfiniteRepeatableSpec(tween(millis, easing = LinearEasing)),
        label = "dogLoop"
    )
    return DogMotion(
        stride = pingPong(-38f, 38f, 620),
        bounce = pingPong(0f, -5f, 310),
        wag = pingPong(-14f, 14f, 310),
        flap = pingPong(-4f, 16f, 310),
        ground = loop(DashPeriod, 500),
        speed = loop(1f, 500)
    )
}

/** Perrito corriendo en bucle. El tamaño lo define quien lo usa: solo conserva su proporción. */
@Composable
fun RunningDog(modifier: Modifier = Modifier) {
    val motion = rememberDogMotion()
    val painter = rememberVectorPainter(
        defaultWidth = ViewportWidth.dp,
        defaultHeight = ViewportHeight.dp,
        viewportWidth = ViewportWidth,
        viewportHeight = ViewportHeight,
        autoMirror = false
    ) { _, _ -> Dog(motion) }
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier.aspectRatio(ViewportWidth / ViewportHeight)
    )
}

@Composable
private fun Dog(motion: DogMotion) {
    Ground(motion)
    Group(translationY = motion.bounce.value) {
        DogLeg(x = 94f, y = 128f, angle = motion.stride.value, color = OliveDark)
        DogLeg(x = 188f, y = 128f, angle = -motion.stride.value, color = OliveDark)
        Group(rotation = motion.wag.value, pivotX = 78f, pivotY = 102f) {
            Path(TailPath, stroke = SolidColor(PetHavenOlive), strokeLineWidth = 13f, strokeLineCap = StrokeCap.Round)
        }
        Path(BodyPath, fill = SolidColor(PetHavenOlive))
        Path(ChestPath, fill = SolidColor(PetHavenOlive))
        DogLeg(x = 112f, y = 130f, angle = -motion.stride.value, color = PetHavenOlive)
        DogLeg(x = 168f, y = 130f, angle = motion.stride.value, color = PetHavenOlive)
        DogHead(motion)
    }
}

@Composable
private fun DogLeg(x: Float, y: Float, angle: Float, color: Color) {
    Group(rotation = angle, pivotX = x, pivotY = y) {
        Path(
            pathData = addPathNodes("M$x $y L$x ${y + 44f}"),
            stroke = SolidColor(color),
            strokeLineWidth = 15f,
            strokeLineCap = StrokeCap.Round
        )
    }
}

@Composable
private fun DogHead(motion: DogMotion) {
    Path(HeadPath, fill = SolidColor(PetHavenOlive))
    Path(SnoutPath, fill = SolidColor(Snout))
    Path(TonguePath, fill = SolidColor(Tongue))
    Path(LipPath, fill = SolidColor(Snout))
    Path(NosePath, fill = SolidColor(PetHavenBrown))
    Path(EyePath, fill = SolidColor(PetHavenInk))
    Path(EyeGlintPath, fill = SolidColor(PetHavenCream))
    Path(CollarPath, stroke = SolidColor(PetHavenPink), strokeLineWidth = 8f, strokeLineCap = StrokeCap.Round)
    Path(TagPath, fill = SolidColor(PetHavenBrown))
    Group(rotation = motion.flap.value, pivotX = 198f, pivotY = 58f) {
        Path(EarPath, fill = SolidColor(PetHavenBrown))
    }
}

@Composable
private fun Ground(motion: DogMotion) {
    Path(ShadowPath, fill = SolidColor(PetHavenBrown), fillAlpha = 0.12f)
    Group(clipPathData = GroundClipPath, translationX = -motion.ground.value) {
        GroundDashPaths.forEach {
            Path(it, stroke = SolidColor(PetHavenOutline), strokeLineWidth = 3f, strokeLineCap = StrokeCap.Round)
        }
    }
    SpeedLinePaths.forEach { line ->
        val progress = (motion.speed.value + line.phase) % 1f
        // Aparece, se estira hacia atrás y se desvanece, como el rastro del movimiento.
        val alpha = if (progress < 0.3f) progress / 0.3f * 0.7f else (1f - progress) / 0.7f * 0.7f
        Group(translationX = 8f - progress * 22f) {
            Path(
                pathData = line.path,
                stroke = SolidColor(PetHavenOlive),
                strokeAlpha = alpha,
                strokeLineWidth = 4f,
                strokeLineCap = StrokeCap.Round
            )
        }
    }
}
