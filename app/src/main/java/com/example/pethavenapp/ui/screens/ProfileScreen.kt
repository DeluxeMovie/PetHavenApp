package com.example.pethavenapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethavenapp.R
import com.example.pethavenapp.data.model.User
import com.example.pethavenapp.ui.components.CoverBand
import com.example.pethavenapp.ui.components.InitialAvatar
import com.example.pethavenapp.ui.components.OwnerDrawerScaffold
import com.example.pethavenapp.ui.components.OwnerSection
import com.example.pethavenapp.ui.theme.PetHavenAppTheme
import com.example.pethavenapp.ui.theme.PetHavenHoney
import com.example.pethavenapp.ui.theme.PetHavenInk
import com.example.pethavenapp.ui.theme.PetHavenOliveDark
import com.example.pethavenapp.ui.theme.PetHavenPink
import com.example.pethavenapp.ui.theme.PetHavenSage
import com.example.pethavenapp.ui.theme.PetHavenTerracotta
import com.example.pethavenapp.viewmodels.ProfileUiState
import com.example.pethavenapp.viewmodels.ProfileViewModel

private val AvatarSize = 80.dp
private val AvatarRing = 4.dp

@Composable
fun ProfileScreen(
    owner: User,
    onNavigate: (OwnerSection) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(key = "profile-${owner.id}", factory = ProfileViewModel.factory(owner))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    OwnerDrawerScaffold(
        section = OwnerSection.Profile,
        ownerName = uiState.name,
        onNavigate = onNavigate,
        onLogout = onLogout,
        modifier = modifier
    ) { innerPadding ->
        ProfileContent(
            uiState = uiState,
            onRetry = viewModel::load,
            onLogout = onLogout,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun ProfileContent(
    uiState: ProfileUiState,
    onRetry: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = stringResource(R.string.profile_title),
                style = MaterialTheme.typography.headlineMedium,
                color = PetHavenInk,
                modifier = Modifier.semantics { heading() }
            )
            Text(
                text = stringResource(R.string.profile_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, PetHavenOliveDark.copy(alpha = 0.10f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                CoverBand(color = PetHavenHoney, modifier = Modifier.fillMaxWidth().height(104.dp))
                // El avatar queda a caballo entre la franja y el contenido, con un anillo blanco.
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 20.dp)
                        .offset(y = (AvatarSize + AvatarRing * 2) / 2)
                        .shadow(6.dp, CircleShape)
                        .background(Color.White, CircleShape)
                        .padding(AvatarRing)
                ) {
                    InitialAvatar(initial = uiState.initial, size = AvatarSize)
                }
            }
            Column(
                modifier = Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = (AvatarSize + AvatarRing * 2) / 2 + 12.dp,
                    bottom = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = uiState.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = PetHavenInk
                )
                InfoTile(R.string.profile_document, uiState.document, uiState.isLoading, PetHavenPink)
                InfoTile(R.string.profile_phone, uiState.phone, uiState.isLoading, PetHavenSage)
                InfoTile(R.string.profile_email, uiState.email, false, PetHavenHoney)
                if (uiState.error != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.profile_error),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = onRetry) { Text(stringResource(R.string.home_retry)) }
                    }
                }
                Spacer(Modifier.height(4.dp))
                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, PetHavenOliveDark.copy(alpha = 0.5f))
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = PetHavenOliveDark)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.nav_logout),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = PetHavenOliveDark
                    )
                }
            }
        }
    }
}

/** Dato del perfil sobre un fondo de color suave; mientras carga muestra puntos suspensivos. */
@Composable
private fun InfoTile(labelRes: Int, value: String?, isLoading: Boolean, tint: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(tint.copy(alpha = 0.30f), RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value ?: stringResource(if (isLoading) R.string.profile_value_loading else R.string.profile_value_missing),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = PetHavenInk
        )
    }
}

@Preview(name = "Perfil", device = "spec:width=411dp,height=891dp", locale = "es")
@Composable
private fun ProfilePreview() {
    PetHavenAppTheme {
        ProfileContent(
            ProfileUiState(
                name = "John Doe",
                email = "john.doe@example.com",
                document = "123456789",
                phone = "1234567890",
                isLoading = false
            ),
            {}, {}
        )
    }
}
