package com.example.personalnutritionassistant
import android.content.Context

actual class PlatformContext actual constructor(context: Any?) {
    val context: Context = context as? Context
        ?: throw IllegalArgumentException("Android PlatformContext requires a non-null Context")
}