package com.loiko.android_lab17_19.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random

class WeatherRepository {
    private var simulateError = false

    fun toggleErrorSimulation() {
        simulateError = !simulateError
    }

    suspend fun fetchTemperature(): Int {
        delay(2000)
        if (simulateError) throw Exception("Сервер недоступен")
        return Random.nextInt(15, 35)
    }

    suspend fun fetchHumidity(): Int {
        delay(1500)
        return Random.nextInt(30, 90)
    }

    suspend fun fetchWindSpeed(): Int {
        delay(1000)
        return Random.nextInt(0, 30)
    }

    suspend fun calculateWeatherIndex(): Int {
        return withContext(Dispatchers.Default) {
            var result = 0
            for (i in 0 until 1_000_000) {
                result += i % 100
            }
            result % 100
        }
    }
}
