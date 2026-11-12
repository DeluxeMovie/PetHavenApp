package com.example.pethavenapp.viewmodels

import androidx.annotation.StringRes
import com.example.pethavenapp.R
import com.example.pethavenapp.data.model.PetsError

/** Texto que la pantalla muestra para cada error al cargar datos de la API. */
@StringRes
internal fun PetsError.messageRes(): Int = when (this) {
    PetsError.Network -> R.string.auth_error_network
    PetsError.Unknown -> R.string.auth_error_unknown
}
