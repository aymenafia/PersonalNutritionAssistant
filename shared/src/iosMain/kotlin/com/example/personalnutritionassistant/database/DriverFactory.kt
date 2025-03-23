package com.example.personalnutritionassistant.database

import com.example.personalnutritionassistant.database.MealsDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver


actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(MealsDatabase.Schema, "meals.db")
    }
}