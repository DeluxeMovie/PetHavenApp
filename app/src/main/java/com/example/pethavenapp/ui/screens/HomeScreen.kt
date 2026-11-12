package com.example.pethavenapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethavenapp.R
import com.example.pethavenapp.data.model.Pet
import com.example.pethavenapp.data.model.Species
import com.example.pethavenapp.data.model.User
import com.example.pethavenapp.ui.components.CoverBand
import com.example.pethavenapp.ui.components.MessageCard
import com.example.pethavenapp.ui.components.OwnerCardShape
import com.example.pethavenapp.ui.components.OwnerDrawerScaffold
import com.example.pethavenapp.ui.components.OwnerSection
import com.example.pethavenapp.ui.components.PetPhoto
import com.example.pethavenapp.ui.components.PrimaryPillButton
import com.example.pethavenapp.ui.components.StatusChip
import com.example.pethavenapp.ui.theme.PetHavenAppTheme
import com.example.pethavenapp.ui.theme.PetHavenBrown
import com.example.pethavenapp.ui.theme.PetHavenHoney
import com.example.pethavenapp.ui.theme.PetHavenHoneyDark
import com.example.pethavenapp.ui.theme.PetHavenHoneySoft
import com.example.pethavenapp.ui.theme.PetHavenInk
import com.example.pethavenapp.ui.theme.PetHavenOliveDark
import com.example.pethavenapp.ui.theme.PetHavenPink
import com.example.pethavenapp.ui.theme.PetHavenPinkSoft
import com.example.pethavenapp.ui.theme.PetHavenSage
import com.example.pethavenapp.ui.theme.PetHavenSageDark
import com.example.pethavenapp.ui.theme.PetHavenSageSoft
import com.example.pethavenapp.ui.theme.PetHavenTerracotta
import com.example.pethavenapp.viewmodels.HomeUiState
import com.example.pethavenapp.viewmodels.HomeViewModel

// Color de la franja superior de cada tarjeta; rota entre cuatro tonos, igual que el portal web.
private val coverColors = listOf(PetHavenHoney, PetHavenSage, PetHavenPink, PetHavenTerracotta.copy(alpha = 0.7f))

private val PhotoSize = 112.dp

@Composable
fun HomeScreen(
    owner: User,
    onPetClick: (Long) -> Unit,
    onNavigate: (OwnerSection) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(key = "home-${owner.id}", factory = HomeViewModel.factory(owner))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    OwnerDrawerScaffold(
        section = OwnerSection.Pets,
        ownerName = owner.name,
        onNavigate = onNavigate,
        onLogout = onLogout,
        modifier = modifier
    ) { innerPadding ->
        HomeContent(
            uiState = uiState,
            onPetClick = onPetClick,
            onRetry = viewModel::loadPets,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onPetClick: (Long) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 300.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        fullWidthItem { HomeHeader(uiState.ownerName) }
        when {
            uiState.isLoading -> fullWidthItem {
                Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PetHavenOliveDark)
                }
            }
            uiState.error != null -> fullWidthItem {
                MessageCard(text = stringResource(uiState.error), isError = true) {
                    PrimaryPillButton(label = stringResource(R.string.home_retry), onClick = onRetry)
                }
            }
            uiState.pets.isEmpty() -> fullWidthItem { MessageCard(text = stringResource(R.string.home_empty)) }
            else -> {
                fullWidthItem { StatsRow(uiState) }
                itemsIndexed(uiState.pets, key = { _, pet -> pet.id }) { index, pet ->
                    PetCard(pet = pet, coverColor = coverColors[index % coverColors.size], onClick = { onPetClick(pet.id) })
                }
            }
        }
    }
}

private fun LazyGridScope.fullWidthItem(content: @Composable () -> Unit) {
    item(span = { GridItemSpan(maxLineSpan) }) { content() }
}

@Composable
private fun HomeHeader(ownerName: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = stringResource(R.string.home_greeting, ownerName),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = PetHavenTerracotta
        )
        Text(
            text = stringResource(R.string.home_title),
            style = MaterialTheme.typography.headlineMedium,
            color = PetHavenInk,
            modifier = Modifier.semantics { heading() }
        )
        Text(
            text = stringResource(R.string.home_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatsRow(uiState: HomeUiState) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        StatCard(Icons.Filled.Favorite, PetHavenSage, uiState.totalPets, R.string.home_stat_total, Modifier.weight(1f))
        StatCard(Icons.Filled.MonitorHeart, PetHavenTerracotta, uiState.inClinicPets, R.string.home_stat_in_clinic, Modifier.weight(1f))
        StatCard(Icons.Filled.Home, PetHavenPink, uiState.atHomePets, R.string.home_stat_at_home, Modifier.weight(1f))
    }
}

/** Tarjeta de resumen: icono sobre un recuadro de color, la cifra y su descripción. */
@Composable
private fun StatCard(icon: ImageVector, tint: Color, value: Int, labelRes: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, PetHavenOliveDark.copy(alpha = 0.10f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(tint.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = PetHavenBrown, modifier = Modifier.size(20.dp))
            }
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = PetHavenInk
            )
            Text(
                text = stringResource(labelRes),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PetCard(pet: Pet, coverColor: Color, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = OwnerCardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, PetHavenOliveDark.copy(alpha = 0.10f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            CoverBand(color = coverColor, modifier = Modifier.fillMaxWidth().height(88.dp))
            // La foto queda a caballo entre la franja y el contenido, con un anillo blanco.
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = PhotoSize / 2)
                    .size(PhotoSize)
                    .shadow(6.dp, CircleShape)
                    .background(Color.White, CircleShape)
                    .padding(4.dp)
                    .clip(CircleShape)
            ) {
                PetPhoto(
                    url = pet.photoUrl,
                    contentDescription = stringResource(R.string.pet_photo_description, pet.name),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = PhotoSize / 2 + 12.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = PetHavenInk,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(
                        R.string.pet_summary,
                        speciesLabel(pet.species),
                        pet.breed,
                        pluralStringResource(R.plurals.pet_age, pet.age, pet.age)
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PetStatusChip(pet.inClinic)
                pet.disease?.let {
                    StatusChip(text = it, containerColor = PetHavenHoneySoft, contentColor = PetHavenHoneyDark)
                }
            }
            PrimaryPillButton(label = stringResource(R.string.pet_view_details), onClick = onClick)
        }
    }
}

@Composable
fun speciesLabel(species: Species): String =
    stringResource(if (species == Species.Cat) R.string.species_cat else R.string.species_dog)

/** Estado de la mascota con los colores del portal web: verde suave en la veterinaria, rosa suave en casa. */
@Composable
fun PetStatusChip(inClinic: Boolean, modifier: Modifier = Modifier) {
    if (inClinic) {
        StatusChip(
            text = stringResource(R.string.pet_in_clinic),
            containerColor = PetHavenSageSoft,
            contentColor = PetHavenSageDark,
            modifier = modifier
        )
    } else {
        StatusChip(
            text = stringResource(R.string.pet_at_home),
            containerColor = PetHavenPinkSoft,
            contentColor = PetHavenBrown,
            modifier = modifier
        )
    }
}

private val previewPets = listOf(
    Pet(1, "Buddy", Species.Dog, "Golden Retriever", 3, 15.0, "Otitis externa", null, true),
    Pet(2, "Misu", Species.Cat, "Siamés", 1, 4.2, null, null, false),
    Pet(3, "Canela", Species.Dog, "Beagle", 5, 12.4, null, null, true)
)

@Preview(name = "Con mascotas", device = "spec:width=411dp,height=891dp", locale = "es")
@Composable
private fun HomePreview() {
    PetHavenAppTheme {
        HomeContent(HomeUiState(ownerName = "John Doe", pets = previewPets, isLoading = false), {}, {})
    }
}

@Preview(name = "Error", device = "spec:width=411dp,height=891dp", locale = "es")
@Composable
private fun HomeErrorPreview() {
    PetHavenAppTheme {
        HomeContent(
            HomeUiState(ownerName = "John Doe", isLoading = false, error = R.string.auth_error_network), {}, {}
        )
    }
}
