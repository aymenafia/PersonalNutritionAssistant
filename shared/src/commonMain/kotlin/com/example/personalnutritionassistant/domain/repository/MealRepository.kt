package com.example.personalnutritionassistant.domain.repository

import com.example.personalnutritionassistant.domain.model.Meal


interface MealRepository {
    suspend fun fetchMeals(): List<Meal>
    suspend fun insertMeal(meal: Meal)
    suspend fun getMealsFromDb(): List<Meal>
}