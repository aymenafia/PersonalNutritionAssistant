package com.example.personalnutritionassistant.domain.usecase

import com.example.personalnutritionassistant.domain.model.Meal
import com.example.personalnutritionassistant.domain.repository.MealRepository

class LogMealUseCase(private val repository: MealRepository) {
    suspend fun logMeal(meal: Meal): Int {
        repository.insertMeal(meal)
        val meals = repository.getMealsFromDb()
        val uniqueDates = meals.map { it.timestamp.take(10) }.toSet()
        return uniqueDates.size
    }
}