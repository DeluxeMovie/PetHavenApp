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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethavenapp.R
import com.example.pethavenapp.data.model.Pet
import com.example.pethavenapp.data.model.Species
import com.example.pethavenapp.data.model.User
import com.example.pethavenapp.ui.components.PetPhoto
import com.example.pethavenapp.ui.components.StatusChip
import com.example.pethavenapp.ui.theme.PetHavenAppTheme
import com.example.pethavenapp.ui.theme.brandBackground
import com.example.pethavenapp.util.ProvideScreenMetrics
import com.example.pethavenapp.viewmodels.HomeUiState
import com.example.pethavenapp.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    owner: User,
    onPetClick: (Long) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(key = "home-${owner.id}", factory = HomeViewModel.factory(owner))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        uiState = uiState,
        onPetClick = onPetClick,
        onRetry = viewModel::loadPets,
        onLogout = onLogout,
        modifier = modifier
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onPetClick: (Long) -> Unit,
    onRetry: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    ProvideScreenMetrics(modifier = modifier.background(brandBackground())) { metrics ->
        Scaffold(containerColor = Color.Transparent) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = metrics.unit(0.05f))
            ) {
                HomeHeader(uiState.ownerName, onLogout)
                when {
                    uiState.isLoading -> CenteredMessage { CircularProgressIndicator() }
                    uiState.error != null -> CenteredMessage {
                        Text(
                            text = stringResource(uiState.error),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(metrics.unit(0.04f)))
                        Button(onClick = onRetry) { Text(stringResource(R.string.home_retry)) }
                    }
                    uiState.pets.isEmpty() -> CenteredMessage {
                        Text(
                            text = stringResource(R.string.home_empty),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                    else -> LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        contentPadding = PaddingValues(vertical = metrics.unit(0.03f)),
                        horizontalArrangement = Arrangement.spacedBy(metrics.unit(0.035f)),
                        verticalArrangement = Arrangement.spacedBy(metrics.unit(0.035f))
                    ) {
                        items(uiState.pets, key = { it.id }) { pet ->
                            PetCard(pet, onClick = { onPetClick(pet.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(ownerName: String, onLogout: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.home_greeting, ownerName),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.semantics { heading() }
            )
            Text(
                text = stringResource(R.string.home_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        TextButton(onClick = onLogout) { Text(stringResource(R.string.home_logout)) }
    }
}

@Composable
private fun CenteredMessage(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) { content() }
}

@Composable
private fun PetCard(pet: Pet, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        border = BorderStroke(Dp.Hairline, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        PetPhoto(
            url = pet.photoUrl,
            contentDescription = stringResource(R.string.pet_photo_description, pet.name),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.15f)
        )
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = pet.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.tertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(R.string.pet_species_breed, speciesLabel(pet.species), pet.breed),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(6.dp))
            PetStatusChip(pet.inClinic)
        }
    }
}

@Composable
fun speciesLabel(species: Species): String =
    stringResource(if (species == Species.Cat) R.string.species_cat else R.string.species_dog)

@Composable
fun PetStatusChip(inClinic: Boolean, modifier: Modifier = Modifier) {
    if (inClinic) {
        StatusChip(
            text = stringResource(R.string.pet_in_clinic),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = modifier
        )
    } else {
        StatusChip(
            text = stringResource(R.string.pet_at_home),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = modifier
        )
    }
}

private val previewPets = listOf(
    Pet(1, "Buddy", Species.Dog, "Golden Retriever", 3, 15.0, "Otitis externa", null, true),
    Pet(2, "Misu", Species.Cat, "Siamés", 1, 4.2, null, null, false),
    Pet(3, "Canela", Species.Dog, "Beagle", 5, 12.4, null, null, false)
)

@Preview(name = "Con mascotas", device = "spec:width=411dp,height=891dp", locale = "es")
@Composable
private fun HomePreview() {
    PetHavenAppTheme {
        HomeContent(HomeUiState(ownerName = "John Doe", pets = previewPets, isLoading = false), {}, {}, {})
    }
}

@Preview(name = "Error", device = "spec:width=411dp,height=891dp", locale = "es")
@Composable
private fun HomeErrorPreview() {
    PetHavenAppTheme {
        HomeContent(
            HomeUiState(ownerName = "John Doe", isLoading = false, error = R.string.auth_error_network), {}, {}, {}
        )
    }
}
