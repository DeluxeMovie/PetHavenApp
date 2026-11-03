package com.example.pethavenapp.data.model

import androidx.annotation.StringRes

enum class FeatureIcon { Care, Trust, Support }

data class WelcomeFeature(
    val icon: FeatureIcon,
    @param:StringRes val title: Int,
    @param:StringRes val description: Int
)
