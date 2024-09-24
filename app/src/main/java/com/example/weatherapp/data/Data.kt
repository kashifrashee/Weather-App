package com.example.weatherapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore


data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val name: String
)

data class ForecastResponse(
    val list: List<ForecastItem>
)


data class ForecastItem(
    val main: Main,
    val weather: List<Weather>,
    val dt_txt: String
)

data class Main(
    val temp: Float,
    val humidity: Int
)

data class Weather(
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Float
)

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
object PreferencesKeys {
    val CITY = stringPreferencesKey("city")
}



