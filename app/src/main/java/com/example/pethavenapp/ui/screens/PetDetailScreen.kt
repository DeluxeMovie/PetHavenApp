package com.example.pethavenapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethavenapp.R
import com.example.pethavenapp.data.model.Evolution
import com.example.pethavenapp.data.model.Pet
import com.example.pethavenapp.data.model.PetDetail
import com.example.pethavenapp.data.model.Species
import com.example.pethavenapp.data.model.Treatment
import com.example.pethavenapp.data.model.TreatmentDrug
import com.example.pethavenapp.ui.components.PetPhoto
import com.example.pethavenapp.ui.components.StatusChip
import com.example.pethavenapp.ui.theme.PetHavenAppTheme
import com.example.pethavenapp.ui.theme.brandBackground
import com.example.pethavenapp.util.ProvideScreenMetrics
import com.example.pethavenapp.viewmodels.PetDetailUiState
import com.example.pethavenapp.viewmodels.PetDetailViewModel

@Composable
fun PetDetailScreen(
    petId: Long,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PetDetailViewModel = viewModel(key = "pet-$petId", factory = PetDetailViewModel.factory(petId))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PetDetailContent(uiState, onBack = onBack, onRetry = viewModel::load, modifier = modifier)
}

@Composable
private fun PetDetailContent(
    uiState: PetDetailUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    ProvideScreenMetrics(modifier = modifier.background(brandBackground())) { metrics ->
        Scaffold(containerColor = Color.Transparent) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.detail_back),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
                val detail = uiState.detail
                when {
                    uiState.isLoading -> Centered { CircularProgressIndicator() }
                    detail == null -> Centered {
                        Text(
                            text = stringResource(uiState.error ?: R.string.auth_error_unknown),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(metrics.unit(0.04f)))
                        Button(onClick = onRetry) { Text(stringResource(R.string.home_retry)) }
                    }
                    else -> LazyColumn(
                        modifier = Modifier.fillMaxWidth(metrics.contentFraction),
                        contentPadding = PaddingValues(bottom = metrics.unit(0.06f)),
                        verticalArrangement = Arrangement.spacedBy(metrics.unit(0.035f))
                    ) {
                        item { PetHero(detail.pet) }
                        item { PetFacts(detail.pet) }
                        item {
                            Text(
                                text = stringResource(R.string.detail_treatments_title),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.semantics { heading() }
                            )
                        }
                        if (detail.treatments.isEmpty()) {
                            item {
                                Text(
                                    text = stringResource(R.string.detail_no_treatments),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            items(detail.treatments, key = { it.id }) { TreatmentCard(it) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Centered(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) { content() }
}

@Composable
private fun PetHero(pet: Pet) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        PetPhoto(
            url = pet.photoUrl,
            contentDescription = stringResource(R.string.pet_photo_description, pet.name),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.6f)
                .clip(RoundedCornerShape(20.dp))
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.semantics { heading() }
                )
                Text(
                    text = stringResource(R.string.pet_species_breed, speciesLabel(pet.species), pet.breed),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            PetStatusChip(pet.inClinic)
        }
    }
}

@Composable
private fun PetFacts(pet: Pet) {
    SectionCard {
        Row(modifier = Modifier.fillMaxWidth()) {
            Fact(
                label = stringResource(R.string.detail_age),
                value = pluralStringResource(R.plurals.pet_age, pet.age, pet.age),
                modifier = Modifier.weight(1f)
            )
            Fact(
                label = stringResource(R.string.detail_weight),
                value = stringResource(R.string.pet_weight, pet.weight),
                modifier = Modifier.weight(1f)
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Fact(
            label = stringResource(R.string.detail_current_diagnosis),
            value = pet.disease ?: stringResource(R.string.detail_no_diagnosis)
        )
    }
}

@Composable
private fun Fact(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun TreatmentCard(treatment: Treatment) {
    SectionCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = treatment.date,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            )
            EvolutionChip(treatment.evolution)
        }
        if (treatment.veterinarianName.isNotEmpty()) {
            Text(
                text = stringResource(R.string.treatment_vet, treatment.veterinarianName),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        treatment.diagnosis?.let { Fact(stringResource(R.string.treatment_diagnosis), it) }
        treatment.weight?.let { Fact(stringResource(R.string.detail_weight), stringResource(R.string.pet_weight, it)) }
        if (treatment.drugs.isNotEmpty()) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Text(
                text = stringResource(R.string.treatment_drugs),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            treatment.drugs.forEach { drug ->
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = drug.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = stringResource(R.string.treatment_units, drug.units),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        treatment.notes?.let {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Fact(stringResource(R.string.treatment_notes), it)
        }
    }
}

@Composable
private fun EvolutionChip(evolution: Evolution?) {
    val colors = MaterialTheme.colorScheme
    val (label, container, content) = when (evolution) {
        Evolution.Stable -> Triple(R.string.evolution_stable, colors.outlineVariant, colors.onSurface)
        Evolution.Improving -> Triple(R.string.evolution_improving, colors.primary, colors.onPrimary)
        Evolution.Worsening -> Triple(R.string.evolution_worsening, colors.errorContainer, colors.onErrorContainer)
        Evolution.Recovered -> Triple(R.string.evolution_recovered, colors.primary, colors.onPrimary)
        null -> Triple(R.string.evolution_intake, colors.secondaryContainer, colors.onSecondaryContainer)
    }
    StatusChip(text = stringResource(label), containerColor = container, contentColor = content)
}

@Composable
private fun SectionCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        border = BorderStroke(Dp.Hairline, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) { content() }
    }
}

private val previewDetail = PetDetail(
    pet = Pet(1, "Canela", Species.Dog, "Beagle", 5, 12.4, "Otitis externa", null, true),
    treatments = listOf(
        Treatment(
            2, "05/08/2026 · 10:30", "Dra. Valentina Soler", 12.4, "Otitis externa", Evolution.Improving,
            "Menos inflamación que en la consulta anterior.", listOf(TreatmentDrug("Amoxicilina 250mg", 3))
        ),
        Treatment(1, "01/08/2026 · 09:00", "Dra. Valentina Soler", 12.6, "Otitis externa", null, null, emptyList())
    )
)

@Preview(name = "Detalle", device = "spec:width=411dp,height=891dp", locale = "es")
@Composable
private fun PetDetailPreview() {
    PetHavenAppTheme { PetDetailContent(PetDetailUiState(previewDetail, isLoading = false), {}, {}) }
}
