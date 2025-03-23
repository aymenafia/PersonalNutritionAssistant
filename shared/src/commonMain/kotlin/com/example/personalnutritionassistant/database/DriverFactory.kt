package com.example.personalnutritionassistant.database
import com.example.personalnutritionassistant.PlatformContext

import com.squareup.sqldelight.db.SqlDriver


expect class DriverFactory() {
    fun createDriver(): SqlDriver
}