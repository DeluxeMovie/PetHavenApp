package com.example.pethavenapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pethavenapp.R
import com.example.pethavenapp.data.model.Pet
import com.example.pethavenapp.data.model.Species
import com.example.pethavenapp.data.model.User
import com.example.pethavenapp.ui.theme.PetHavenAppTheme
import com.example.pethavenapp.ui.theme.brandBackground
import com.example.pethavenapp.util.ProvideScreenMetrics
import com.example.pethavenapp.viewmodels.HomeUiState
import com.example.pethavenapp.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    owner: User,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(key = "home-${owner.id}", factory = HomeViewModel.factory(owner))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(uiState, onRetry = viewModel::loadPets, onLogout = onLogout, modifier = modifier)
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
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
                        Spacer(Modifier.height(16.dp))
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
                    else -> LazyColumn(
                        contentPadding = PaddingValues(vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.pets, key = { it.id }) { PetCard(it) }
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
private fun PetCard(pet: Pet, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        border = BorderStroke(Dp.Hairline, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val placeholder = rememberVectorPainter(Icons.Filled.Pets)
            AsyncImage(
                model = pet.photoUrl,
                contentDescription = stringResource(R.string.home_pet_photo, pet.name),
                placeholder = placeholder,
                error = placeholder,
                fallback = placeholder,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(pet.name, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.tertiary)
                Text(
                    text = stringResource(
                        R.string.home_pet_summary,
                        stringResource(if (pet.species == Species.Cat) R.string.species_cat else R.string.species_dog),
                        pet.breed
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = pluralStringResource(R.plurals.home_pet_age, pet.age, pet.age) +
                        " · " + stringResource(R.string.home_pet_weight, pet.weight),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                pet.disease?.let {
                    Text(
                        text = stringResource(R.string.home_pet_disease, it),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = stringResource(if (pet.inClinic) R.string.home_pet_in_clinic else R.string.home_pet_at_home),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (pet.inClinic) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private val previewPets = listOf(
    Pet(1, "Buddy", Species.Dog, "Golden Retriever", 3, 15.0, "Otitis externa", null, true),
    Pet(2, "Misu", Species.Cat, "Siamés", 1, 4.2, null, null, false)
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
