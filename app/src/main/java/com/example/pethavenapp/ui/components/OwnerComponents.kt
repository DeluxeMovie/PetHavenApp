package com.example.pethavenapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pethavenapp.R
import com.example.pethavenapp.ui.theme.PetHavenBrown
import com.example.pethavenapp.ui.theme.PetHavenCream
import com.example.pethavenapp.ui.theme.PetHavenInk
import com.example.pethavenapp.ui.theme.PetHavenOliveDark
import com.example.pethavenapp.ui.theme.PetHavenPink
import com.example.pethavenapp.ui.theme.PetHavenPinkSoft
import kotlinx.coroutines.launch

/** Secciones del área del dueño que se alcanzan desde el menú lateral. */
enum class OwnerSection { Pets, Profile }

/**
 * Estructura común de las pantallas del dueño: barra superior con el botón del menú, menú lateral
 * (Mis Mascotas, Mi Perfil y Cerrar sesión) y el contenido de la sección activa.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDrawerScaffold(
    section: OwnerSection,
    ownerName: String,
    onNavigate: (OwnerSection) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Cierra el menú y después ejecuta la acción elegida.
    fun closeThen(action: () -> Unit) {
        scope.launch {
            drawerState.close()
            action()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = modifier,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Color.White) {
                PetHavenLogo(Modifier.padding(horizontal = 28.dp, vertical = 28.dp).height(44.dp))
                Text(
                    text = stringResource(R.string.nav_account_label),
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 1.6.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 28.dp, bottom = 8.dp)
                )
                DrawerItem(
                    label = stringResource(R.string.nav_my_pets),
                    icon = Icons.Filled.Favorite,
                    selected = section == OwnerSection.Pets,
                    onClick = { closeThen { if (section != OwnerSection.Pets) onNavigate(OwnerSection.Pets) } }
                )
                DrawerItem(
                    label = stringResource(R.string.nav_profile),
                    icon = Icons.Filled.Person,
                    selected = section == OwnerSection.Profile,
                    onClick = { closeThen { if (section != OwnerSection.Profile) onNavigate(OwnerSection.Profile) } }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 12.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                DrawerItem(
                    label = stringResource(R.string.nav_logout),
                    icon = Icons.AutoMirrored.Filled.Logout,
                    selected = false,
                    onClick = { closeThen(onLogout) }
                )
            }
        }
    ) {
        Scaffold(
            containerColor = PetHavenCream,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(
                                if (section == OwnerSection.Pets) R.string.nav_my_pets else R.string.nav_profile
                            ),
                            style = MaterialTheme.typography.titleLarge,
                            color = PetHavenInk
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = stringResource(R.string.nav_open_menu),
                                tint = PetHavenInk
                            )
                        }
                    },
                    actions = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Text(
                                text = ownerName,
                                style = MaterialTheme.typography.labelLarge,
                                color = PetHavenInk,
                                maxLines = 1,
                                modifier = Modifier.padding(end = 10.dp)
                            )
                            InitialAvatar(initial = ownerName.trim().take(1).uppercase(), size = 36.dp)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            content = content
        )
    }
}

@Composable
private fun DrawerItem(label: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(label, style = MaterialTheme.typography.bodyLarge) },
        icon = { Icon(icon, contentDescription = null) },
        selected = selected,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = PetHavenPinkSoft,
            selectedIconColor = PetHavenInk,
            selectedTextColor = PetHavenInk,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = PetHavenInk
        )
    )
}

/** Círculo con la inicial del nombre, como el avatar del portal web. */
@Composable
fun InitialAvatar(initial: String, size: Dp, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(size)
            .background(PetHavenPink, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.titleLarge,
            fontSize = (size.value * 0.42f).sp,
            fontWeight = FontWeight.Bold,
            color = PetHavenBrown
        )
    }
}

/** Franja de color con dos círculos translúcidos, la cabecera decorativa de las tarjetas del portal web. */
@Composable
fun CoverBand(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color)
            .drawBehind {
                drawCircle(
                    color = Color.White.copy(alpha = 0.35f),
                    radius = 64.dp.toPx(),
                    center = Offset(size.width - 8.dp.toPx(), 0f)
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.25f),
                    radius = 40.dp.toPx(),
                    center = Offset(56.dp.toPx(), size.height)
                )
            }
    )
}

/** Forma de las tarjetas del área del dueño, con las esquinas muy redondeadas del portal web. */
val OwnerCardShape = RoundedCornerShape(24.dp)

/** Botón principal de las pantallas del dueño: verde oliva con esquinas completamente redondeadas. */
@Composable
fun PrimaryPillButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = PetHavenOliveDark, contentColor = Color.White)
    ) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
    }
}

/** Tarjeta con un mensaje centrado (vacío o error) y, opcionalmente, una acción debajo. */
@Composable
fun MessageCard(
    text: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    action: @Composable () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = OwnerCardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, PetHavenOliveDark.copy(alpha = 0.15f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            action()
        }
    }
}
