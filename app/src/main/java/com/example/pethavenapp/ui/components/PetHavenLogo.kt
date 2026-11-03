package com.example.pethavenapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.pethavenapp.R

@Composable
fun PetHavenLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.pethaven_logo),
        contentDescription = stringResource(R.string.welcome_logo_description),
        contentScale = ContentScale.Fit,
        modifier = modifier
    )
}
