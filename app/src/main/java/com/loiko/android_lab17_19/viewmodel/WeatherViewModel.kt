package com.loiko.android_lab17_19.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loiko.android_lab17_19.data.WeatherData
import com.loiko.android_lab17_19.data.WeatherRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()

    private val _weatherState = MutableStateFlow(WeatherData())
    val weatherState: StateFlow<WeatherData> = _weatherState.asStateFlow()

    init {
        loadWeatherData()
        startAutoRefresh()
    }

    fun loadWeatherData() {
        viewModelScope.launch {
            _weatherState.value = _weatherState.value.copy(
                isLoading = true,
                error = null
            )
            try {
                coroutineScope {
                    _weatherState.value = _weatherState.value.copy(
                        loadingProgress = "Загружается температура..."
                    )
                    val tempDeferred = async { repository.fetchTemperature() }
                    val humDeferred = async { repository.fetchHumidity() }
                    val windDeferred = async { repository.fetchWindSpeed() }
                    val temperature = tempDeferred.await()
                    val humidity = humDeferred.await()
                    val windSpeed = windDeferred.await()

                    _weatherState.value = _weatherState.value.copy(
                        loadingProgress = "Вычисляется индекс погоды..."
                    )
                    val weatherIndex = repository.calculateWeatherIndex()

                    _weatherState.value = WeatherData(
                        temperature = temperature,
                        humidity = humidity,
                        windSpeed = windSpeed,
                        isLoading = false,
                        error = null,
                        loadingProgress = "Загрузка завершена!",
                        weatherIndex = weatherIndex
                    )
                }
            } catch (e: Exception) {
                _weatherState.value = _weatherState.value.copy(
                    isLoading = false,
                    error = "Ошибка загрузки: ${e.message}",
                    loadingProgress = ""
                )
            }
        }
    }

    fun toggleErrorSimulation() {
        repository.toggleErrorSimulation()
    }

    private fun startAutoRefresh() {
        viewModelScope.launch {
            flow {
                while (true) {
                    delay(10000)
                    emit(Unit)
                }
            }.collect {
                loadWeatherData()
            }
        }
    }
}
