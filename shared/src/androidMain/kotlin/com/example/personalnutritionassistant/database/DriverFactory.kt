package com.example.personalnutritionassistant.database

import com.example.personalnutritionassistant.database.MealsDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

object AppContext {
    lateinit var context: android.content.Context
}

actual class DriverFactory actual constructor() {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(MealsDatabase.Schema, AppContext.context, "meals.db")
    }
}