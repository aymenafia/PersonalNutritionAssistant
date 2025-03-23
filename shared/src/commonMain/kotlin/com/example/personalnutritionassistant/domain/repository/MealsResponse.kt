package com.example.personalnutritionassistant.domain.repository



import com.example.personalnutritionassistant.domain.model.Meal

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json



@Serializable
data class MealsResponse(val meals: List<Meal>)