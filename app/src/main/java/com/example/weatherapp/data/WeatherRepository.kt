package com.example.weatherapp.data

import com.example.weatherapp.model.RetrofitInstance

class WeatherRepository {
    private val api = RetrofitInstance.api

    suspend fun getWeather(city: String, apiKey: String): WeatherResponse {
        return api.getCurrentWeather(city, apiKey)
    }

    suspend fun get5DayForecast(city: String, apiKey: String): ForecastResponse {
        return api.get5DayForecast(city, apiKey)
    }

}
