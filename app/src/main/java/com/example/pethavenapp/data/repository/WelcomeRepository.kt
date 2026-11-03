package com.example.pethavenapp.data.repository

import com.example.pethavenapp.R
import com.example.pethavenapp.data.model.FeatureIcon
import com.example.pethavenapp.data.model.WelcomeFeature

interface WelcomeRepository {
    fun getFeatures(): List<WelcomeFeature>
}

class LocalWelcomeRepository : WelcomeRepository {
    override fun getFeatures() = listOf(
        WelcomeFeature(FeatureIcon.Care, R.string.feature_love_title, R.string.feature_love_description),
        WelcomeFeature(FeatureIcon.Trust, R.string.feature_trust_title, R.string.feature_trust_description),
        WelcomeFeature(FeatureIcon.Support, R.string.feature_support_title, R.string.feature_support_description)
    )
}
