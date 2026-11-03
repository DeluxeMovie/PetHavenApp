package com.example.pethavenapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/** Línea, corazón, línea: el separador de la identidad de marca. Todo se reparte por pesos. */
@Composable
fun TaglineDivider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.weight(0.06f))
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .weight(0.1f)
                .aspectRatio(1f)
        )
        Spacer(Modifier.weight(0.06f))
        HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.secondary)
    }
}
