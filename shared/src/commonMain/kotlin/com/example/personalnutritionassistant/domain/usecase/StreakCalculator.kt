package com.example.personalnutritionassistant.domain.usecase

class StreakCalculator {

    fun calculateStreak(dates: List<String>): Int {
        return dates.toSet().size
    }
}