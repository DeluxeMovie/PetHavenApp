package com.example.pethavenapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethavenapp.R
import com.example.pethavenapp.data.repository.LocalWelcomeRepository
import com.example.pethavenapp.ui.components.FeatureCard
import com.example.pethavenapp.ui.components.GetStartedButton
import com.example.pethavenapp.ui.components.PetHavenLogo
import com.example.pethavenapp.ui.components.TaglineDivider
import com.example.pethavenapp.ui.theme.PetHavenAppTheme
import com.example.pethavenapp.util.LocalScreenMetrics
import com.example.pethavenapp.util.ProvideScreenMetrics
import com.example.pethavenapp.util.ScreenMetrics
import com.example.pethavenapp.util.entrance
import com.example.pethavenapp.util.rememberEntranceProgress
import com.example.pethavenapp.viewmodels.WelcomeUiState
import com.example.pethavenapp.viewmodels.WelcomeViewModel

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    WelcomeContent(uiState = uiState, onGetStarted = onGetStarted, modifier = modifier)
}

@Composable
private fun WelcomeContent(
    uiState: WelcomeUiState,
    onGetStarted: () -> Unit,
    modifier: Modifier = Modifier
) {
    ProvideScreenMetrics(modifier = modifier.background(welcomeBackground())) { metrics ->
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = { if (!metrics.isWide) GetStartedBar(onGetStarted) }
        ) { innerPadding ->
            if (metrics.isWide) {
                WideLayout(uiState, onGetStarted, metrics, Modifier.padding(innerPadding))
            } else {
                SinglePaneLayout(uiState, metrics, Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
private fun welcomeBackground(): Brush = Brush.verticalGradient(
    colors = listOf(
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.background
    )
)

/** Pantallas verticales: una columna que se desplaza si el contenido no cabe (textos grandes, ventanas bajas). */
@Composable
private fun SinglePaneLayout(
    uiState: WelcomeUiState,
    metrics: ScreenMetrics,
    modifier: Modifier = Modifier
) {
    val featuresEntrance = rememberEntranceProgress(delayMillis = 300)
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = metrics.unit(0.04f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        WelcomeHero(modifier = Modifier.fillMaxWidth(metrics.contentFraction))
        Spacer(Modifier.height(metrics.unit(0.08f)))
        FeatureCard(
            features = uiState.features,
            modifier = Modifier
                .fillMaxWidth(metrics.contentFraction)
                .entrance(featuresEntrance)
        )
    }
}

/** Pantallas horizontales y anchas: dos paneles del mismo peso, marca a la izquierda y beneficios a la derecha. */
@Composable
private fun WideLayout(
    uiState: WelcomeUiState,
    onGetStarted: () -> Unit,
    metrics: ScreenMetrics,
    modifier: Modifier = Modifier
) {
    val featuresEntrance = rememberEntranceProgress(delayMillis = 300)
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = metrics.unit(0.08f)),
        horizontalArrangement = Arrangement.spacedBy(metrics.unit(0.08f)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(vertical = metrics.unit(0.04f)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            WelcomeHero(modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(metrics.unit(0.06f)))
            GetStartedButton(onClick = onGetStarted, modifier = Modifier.fillMaxWidth(0.8f))
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(vertical = metrics.unit(0.04f)),
            verticalArrangement = Arrangement.Center
        ) {
            FeatureCard(
                features = uiState.features,
                modifier = Modifier
                    .fillMaxWidth()
                    .entrance(featuresEntrance)
            )
        }
    }
}

@Composable
private fun WelcomeHero(modifier: Modifier = Modifier) {
    val metrics = LocalScreenMetrics.current
    val logoEntrance = rememberEntranceProgress(delayMillis = 0)
    val messageEntrance = rememberEntranceProgress(delayMillis = 150)
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PetHavenLogo(modifier = Modifier
            .fillMaxWidth()
            .entrance(logoEntrance))
        Spacer(Modifier.height(metrics.unit(0.06f)))
        TaglineDivider(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .entrance(messageEntrance)
        )
        Spacer(Modifier.height(metrics.unit(0.04f)))
        Text(
            text = stringResource(R.string.welcome_tagline),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.tertiary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .semantics { heading() }
                .entrance(messageEntrance)
        )
        Spacer(Modifier.height(metrics.unit(0.02f)))
        Text(
            text = stringResource(R.string.welcome_description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.entrance(messageEntrance)
        )
    }
}

@Composable
private fun GetStartedBar(
    onGetStarted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val metrics = LocalScreenMetrics.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(vertical = metrics.unit(0.04f)),
        contentAlignment = Alignment.Center
    ) {
        GetStartedButton(
            onClick = onGetStarted,
            modifier = Modifier.fillMaxWidth(metrics.contentFraction)
        )
    }
}

private val previewState = WelcomeUiState(features = LocalWelcomeRepository().getFeatures())

@Preview(name = "Teléfono", device = "spec:width=411dp,height=891dp", locale = "es")
@Composable
private fun WelcomePhonePreview() {
    PetHavenAppTheme { WelcomeContent(previewState, onGetStarted = {}) }
}

@Preview(name = "Teléfono pequeño y texto grande", device = "spec:width=320dp,height=568dp", fontScale = 1.3f, locale = "es")
@Composable
private fun WelcomeSmallPreview() {
    PetHavenAppTheme { WelcomeContent(previewState, onGetStarted = {}) }
}

@Preview(name = "Teléfono horizontal", device = "spec:width=891dp,height=411dp", locale = "es")
@Composable
private fun WelcomeLandscapePreview() {
    PetHavenAppTheme { WelcomeContent(previewState, onGetStarted = {}) }
}

@Preview(name = "Tableta vertical", device = "spec:width=800dp,height=1280dp", locale = "es")
@Composable
private fun WelcomeTabletPreview() {
    PetHavenAppTheme { WelcomeContent(previewState, onGetStarted = {}) }
}
