package com.example.personalnutritionassistant.data.repository

import com.example.personalnutritionassistant.domain.model.Meal
import com.example.personalnutritionassistant.domain.repository.MealRepository
import com.example.personalnutritionassistant.database.MealsDatabase
import com.example.personalnutritionassistant.domain.repository.MealsResponse
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class MealRepositoryImpl(
    driver: SqlDriver,
    private val httpClient: HttpClient
) : MealRepository {

    private val database = MealsDatabase(driver)
    private val mealQueries = database.mealsQueries

    override suspend fun fetchMeals(): List<Meal> {
        val response: MealsResponse = httpClient.get("https://example.com/mockMeals").body()
        response.meals.forEach { insertMeal(it) }
        return response.meals
    }

    override suspend fun insertMeal(meal: Meal) {
        mealQueries.insertMeal(
            id = meal.id,
            name = meal.name,
            calories = meal.calories.toLong(),
            timestamp = meal.timestamp
        )
    }

    override suspend fun getMealsFromDb(): List<Meal> {
        return mealQueries.selectAll().executeAsList().map { row ->
            Meal(
                id = row.id,
                name = row.name,
                calories = row.calories.toInt(),
                timestamp = row.timestamp
            )
        }
    }

    constructor(driver: SqlDriver) : this(
        driver,
        HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            engine {
                addHandler { request ->
                    val responseHeaders = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                    val mockJson = """
                    {
                      "meals": [
                        { "id": "1", "name": "Oatmeal", "calories": 250, "timestamp": "2025-03-20T08:00:00Z" },
                        { "id": "2", "name": "Salad", "calories": 150, "timestamp": "2025-03-20T12:00:00Z" },
                        { "id": "3", "name": "Grilled Chicken", "calories": 300, "timestamp": "2025-03-20T18:00:00Z" }
                      ]
                    }
                    """.trimIndent()
                    respond(
                        content = mockJson,
                        status = HttpStatusCode.OK,
                        headers = responseHeaders
                    )
                }
            }
        }
    )

    fun fetchMealsWithCompletion(completion: (meals: List<Meal>?, error: Throwable?) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val meals = fetchMeals()
                completion(meals, null)
            } catch (e: Throwable) {
                completion(null, e)
            }
        }
    }
}