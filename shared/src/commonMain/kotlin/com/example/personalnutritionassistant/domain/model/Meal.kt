package com.example.personalnutritionassistant.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Meal(
    val id: String,
    val name: String,
    val calories: Int,
    val timestamp: String
)