package com.example.weatherapp.viewModel

import android.app.Application
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.ForecastResponse
import com.example.weatherapp.data.PreferencesKeys
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.data.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository(),
    application: Application
) : AndroidViewModel(application) {

    private val dataStore = application.dataStore

    private val _weather = MutableLiveData<WeatherResponse>()
    val weather: LiveData<WeatherResponse> get() = _weather


    private val _forecast = MutableLiveData<ForecastResponse>()
    val forecast: LiveData<ForecastResponse> get() = _forecast


    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val apiKey = "80c6783cb438f798a6850d8585284797" // Use your API key

    // Define a default city, like Karachi
    private val defaultCity = "Karachi"

    init {
        viewModelScope.launch {
            getSavedCity().collect { savedCity ->
                val cityToFetch = savedCity ?: defaultCity // Use saved city or default to 'city'
                fetchWeather(cityToFetch)
                fetch5DayForecast(cityToFetch)
            }
        }

    }

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = repository.getWeather(city, apiKey)
                _weather.value = response
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather", e)
                _errorMessage.value = "Failed to fetch weather data. Please try again."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetch5DayForecast(city: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = repository.get5DayForecast(city, apiKey)
                _forecast.value = response
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching 5-day forecast", e)
                _errorMessage.value = "Failed to fetch weather data. Please try again."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveCity(city: String) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.CITY] = city
            }
            Log.d("WeatherViewModel", "City saved: $city")
        }
    }


    fun getSavedCity(): Flow<String?> {
        return dataStore.data.map {
            it[PreferencesKeys.CITY]
        }.onEach { savedCity ->
            Log.d("WeatherViewModel", "Retrieved city: $savedCity")
        }
    }



}



