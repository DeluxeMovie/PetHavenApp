package com.example.pethavenapp.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethavenapp.R
import com.example.pethavenapp.data.model.Evolution
import com.example.pethavenapp.data.model.Pet
import com.example.pethavenapp.data.model.PetDetail
import com.example.pethavenapp.data.model.Species
import com.example.pethavenapp.data.model.Treatment
import com.example.pethavenapp.data.model.TreatmentDrug
import com.example.pethavenapp.ui.components.CoverBand
import com.example.pethavenapp.ui.components.MessageCard
import com.example.pethavenapp.ui.components.OwnerCardShape
import com.example.pethavenapp.ui.components.PetPhoto
import com.example.pethavenapp.ui.components.PrimaryPillButton
import com.example.pethavenapp.ui.components.StatusChip
import com.example.pethavenapp.ui.theme.PetHavenAppTheme
import com.example.pethavenapp.ui.theme.PetHavenBrown
import com.example.pethavenapp.ui.theme.PetHavenCream
import com.example.pethavenapp.ui.theme.PetHavenHoney
import com.example.pethavenapp.ui.theme.PetHavenInk
import com.example.pethavenapp.ui.theme.PetHavenOliveDark
import com.example.pethavenapp.ui.theme.PetHavenPinkSoft
import com.example.pethavenapp.ui.theme.PetHavenSageDark
import com.example.pethavenapp.ui.theme.PetHavenSageSoft
import com.example.pethavenapp.viewmodels.PetDetailUiState
import com.example.pethavenapp.viewmodels.PetDetailViewModel

private val PhotoShape = RoundedCornerShape(16.dp)
private val TreatmentShape = RoundedCornerShape(16.dp)

// Etiqueta neutra para el número de medicamentos, el equivalente al tag "secondary" del portal web.
private val NeutralChip = Color(0xFFEDE6D6)

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PetDetailContent(
    uiState: PetDetailUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = PetHavenCream,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.detail_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = PetHavenInk
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.detail_back),
                            tint = PetHavenInk
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        val detail = uiState.detail
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.padding(top = 80.dp),
                    color = PetHavenOliveDark
                )
                detail == null -> MessageCard(
                    text = stringResource(uiState.error ?: R.string.auth_error_unknown),
                    isError = true,
                    modifier = Modifier
                        .padding(16.dp)
                        .widthIn(max = 720.dp)
                ) {
                    PrimaryPillButton(label = stringResource(R.string.home_retry), onClick = onRetry)
                }
                else -> LazyColumn(
                    modifier = Modifier
                        .widthIn(max = 720.dp)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { PetHero(detail.pet) }
                    item {
                        Text(
                            text = stringResource(R.string.detail_treatments_title),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = PetHavenInk,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .semantics { heading() }
                        )
                    }
                    if (detail.treatments.isEmpty()) {
                        item { MessageCard(text = stringResource(R.string.detail_no_treatments)) }
                    } else {
                        // El más reciente va primero y arranca desplegado, como en el portal web.
                        itemsIndexed(detail.treatments, key = { _, treatment -> treatment.id }) { index, treatment ->
                            TreatmentTimelineItem(
                                treatment = treatment,
                                isLast = index == detail.treatments.lastIndex,
                                startExpanded = index == 0
                            )
                        }
                    }
                }
            }
        }
    }
}

/** Tarjeta de cabecera: franja de color, foto con anillo blanco, datos de la mascota y diagnóstico. */
@Composable
private fun PetHero(pet: Pet) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = OwnerCardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, PetHavenOliveDark.copy(alpha = 0.10f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            CoverBand(color = PetHavenHoney, modifier = Modifier.fillMaxWidth().height(96.dp))
            Column(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(192.dp)
                        .shadow(6.dp, PhotoShape)
                        .background(Color.White, PhotoShape)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    PetPhoto(
                        url = pet.photoUrl,
                        contentDescription = stringResource(R.string.pet_photo_description, pet.name),
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = pet.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = PetHavenInk,
                        modifier = Modifier.semantics { heading() }
                    )
                    Text(
                        text = stringResource(
                            R.string.pet_summary_weight,
                            speciesLabel(pet.species),
                            pet.breed,
                            pluralStringResource(R.plurals.pet_age, pet.age, pet.age),
                            stringResource(R.string.pet_weight, pet.weight)
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                PetStatusChip(pet.inClinic)
                pet.disease?.let { DiagnosisBlock(it) }
            }
        }
    }
}

@Composable
private fun DiagnosisBlock(disease: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PetHavenHoney.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.treatment_diagnosis).uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = disease,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = PetHavenInk
        )
    }
}

/** Un tratamiento sobre el riel del historial: el punto lo marca y la línea lo enlaza con el siguiente. */
@Composable
private fun TreatmentTimelineItem(treatment: Treatment, isLast: Boolean, startExpanded: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier
                .width(28.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(18.dp)
                    .background(PetHavenHoney.copy(alpha = 0.5f), CircleShape)
                    .padding(1.5.dp)
                    .background(Color.White, CircleShape)
                    .padding(2.dp)
                    .background(PetHavenHoney, CircleShape)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(PetHavenHoney.copy(alpha = 0.4f), CircleShape)
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else 24.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = treatment.date.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TreatmentCard(treatment, startExpanded)
        }
    }
}

/** Tratamiento desplegable: la cabecera muestra al veterinario y una etiqueta; el detalle, al tocarla. */
@Composable
private fun TreatmentCard(treatment: Treatment, startExpanded: Boolean) {
    var expanded by rememberSaveable(treatment.id) { mutableStateOf(startExpanded) }
    val isDischarge = treatment.evolution == Evolution.Recovered

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = TreatmentShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, PetHavenOliveDark.copy(alpha = 0.12f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                tint = PetHavenOliveDark,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = treatment.veterinarianName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = PetHavenInk,
                modifier = Modifier.weight(1f)
            )
            if (isDischarge) {
                StatusChip(
                    text = stringResource(R.string.evolution_recovered),
                    containerColor = PetHavenSageSoft,
                    contentColor = PetHavenSageDark
                )
            } else {
                StatusChip(
                    text = pluralStringResource(
                        R.plurals.treatment_drug_count, treatment.drugs.size, treatment.drugs.size
                    ),
                    containerColor = NeutralChip,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (expanded) {
            HorizontalDivider(color = PetHavenOliveDark.copy(alpha = 0.12f))
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (isDischarge) {
                    Text(
                        text = stringResource(R.string.treatment_discharge_note, treatment.diagnosis.orEmpty()),
                        style = MaterialTheme.typography.bodyMedium,
                        color = PetHavenInk
                    )
                } else {
                    EvolutionChip(treatment.evolution)
                    treatment.diagnosis?.let { Fact(stringResource(R.string.treatment_diagnosis), it) }
                    treatment.weight?.let {
                        Fact(stringResource(R.string.detail_weight), stringResource(R.string.pet_weight, it))
                    }
                    DrugsTable(treatment.drugs)
                    treatment.notes?.let { Fact(stringResource(R.string.treatment_notes), it) }
                }
            }
        }
    }
}

@Composable
private fun DrugsTable(drugs: List<TreatmentDrug>) {
    if (drugs.isEmpty()) {
        Text(
            text = stringResource(R.string.detail_no_drugs),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        return
    }
    Column {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
            TableHeader(stringResource(R.string.treatment_drug_column), Modifier.weight(1f))
            TableHeader(stringResource(R.string.treatment_units_column))
        }
        HorizontalDivider(color = PetHavenOliveDark.copy(alpha = 0.15f))
        drugs.forEachIndexed { index, drug ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = drug.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = PetHavenInk,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = drug.units.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (index != drugs.lastIndex) HorizontalDivider(color = PetHavenOliveDark.copy(alpha = 0.10f))
        }
    }
}

@Composable
private fun TableHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    )
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
            color = PetHavenInk
        )
    }
}

/** Evolución del tratamiento con los tonos suaves del portal web; sin evolución es el ingreso. */
@Composable
private fun EvolutionChip(evolution: Evolution?) {
    val colors = MaterialTheme.colorScheme
    val (label, container, content) = when (evolution) {
        Evolution.Stable -> Triple(R.string.evolution_stable, NeutralChip, colors.onSurfaceVariant)
        Evolution.Improving -> Triple(R.string.evolution_improving, PetHavenSageSoft, PetHavenSageDark)
        Evolution.Worsening -> Triple(R.string.evolution_worsening, colors.errorContainer, colors.onErrorContainer)
        Evolution.Recovered -> Triple(R.string.evolution_recovered, PetHavenSageSoft, PetHavenSageDark)
        null -> Triple(R.string.evolution_intake, PetHavenPinkSoft, PetHavenBrown)
    }
    StatusChip(text = stringResource(label), containerColor = container, contentColor = content)
}

private val previewDetail = PetDetail(
    pet = Pet(1, "Canela", Species.Dog, "Beagle", 5, 12.4, "Otitis externa", null, true),
    treatments = listOf(
        Treatment(
            3, "10/08/2026 · 11:00", "Dra. Valentina Soler", 12.4, "Otitis externa", Evolution.Recovered,
            null, emptyList()
        ),
        Treatment(
            2, "05/08/2026 · 10:30", "Dra. Valentina Soler", 12.4, "Otitis externa", Evolution.Improving,
            "Menos inflamación que en la consulta anterior.", listOf(TreatmentDrug("Amoxicilina 250mg", 3))
        ),
        Treatment(1, "01/08/2026 · 09:00", "Dr. Esteban Ruiz", 12.6, "Otitis externa", null, null, emptyList())
    )
)

@Preview(name = "Detalle", device = "spec:width=411dp,height=891dp", locale = "es")
@Composable
private fun PetDetailPreview() {
    PetHavenAppTheme { PetDetailContent(PetDetailUiState(previewDetail, isLoading = false), {}, {}) }
}
