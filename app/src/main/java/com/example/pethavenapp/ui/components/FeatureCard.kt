package com.example.pethavenapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.example.pethavenapp.data.model.FeatureIcon
import com.example.pethavenapp.data.model.WelcomeFeature
import com.example.pethavenapp.util.LocalScreenMetrics

@Composable
fun FeatureCard(
    features: List<WelcomeFeature>,
    modifier: Modifier = Modifier
) {
    val metrics = LocalScreenMetrics.current
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        border = BorderStroke(Dp.Hairline, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(vertical = metrics.unit(0.015f))) {
            features.forEach { FeatureItem(it) }
        }
    }
}

@Composable
private fun FeatureItem(feature: WelcomeFeature) {
    val metrics = LocalScreenMetrics.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = metrics.unit(0.04f), vertical = metrics.unit(0.025f)),
        horizontalArrangement = Arrangement.spacedBy(metrics.unit(0.035f)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconBadge(icon = feature.icon.toImageVector(), modifier = Modifier.weight(1f))
        Column(modifier = Modifier.weight(5f)) {
            Text(
                text = stringResource(feature.title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(feature.description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun IconBadge(icon: ImageVector, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.fillMaxSize(0.55f)
        )
    }
}

private fun FeatureIcon.toImageVector(): ImageVector = when (this) {
    FeatureIcon.Care -> Icons.Filled.Pets
    FeatureIcon.Trust -> Icons.Filled.VerifiedUser
    FeatureIcon.Support -> Icons.Filled.Favorite
}
