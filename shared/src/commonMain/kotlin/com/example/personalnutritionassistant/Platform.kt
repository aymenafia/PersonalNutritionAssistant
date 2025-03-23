package com.example.personalnutritionassistant

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
