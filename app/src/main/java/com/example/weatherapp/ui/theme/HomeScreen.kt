package com.example.weatherapp.ui.theme

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.data.ForecastResponse
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.viewModel.WeatherViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {

    val weather by viewModel.weather.observeAsState()
    val forecast by viewModel.forecast.observeAsState()

    val errorMessage by viewModel.errorMessage.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    val saveCity by viewModel.getSavedCity().collectAsState(initial = null)

    var showDialog by remember { mutableStateOf(false) }
    var userCity by remember { mutableStateOf(saveCity ?: "Karachi") }


    // Get today's date and day of the week
    val today = LocalDate.now()
    val dayOfWeek = today.dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault())

    // Fetch weather data when the city changes
    LaunchedEffect(saveCity) {
        saveCity?.let {
            userCity = it
        }
    }

    // Save city and update weather when the city changes from the dialog
    fun updateCity(newCity: String) {
        userCity = newCity
        viewModel.fetchWeather(userCity)
        viewModel.fetch5DayForecast(userCity)
        viewModel.saveCity(userCity) // Persist the new city
        showDialog = false
    }

    // Main content layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(verticalGradient())
    ) {
        when {
            isLoading -> {
                // Show loading spinner
                Box(
                    modifier = Modifier
                        .padding(top = 400.dp)
                        .size(120.dp, 80.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            errorMessage != null -> {

                Text(
                    text = errorMessage!!,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .padding(top = 400.dp)
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
                IconButton(
                    onClick = {
                        viewModel.fetchWeather(userCity)
                        viewModel.fetch5DayForecast(userCity)
                        viewModel.saveCity(userCity) // Ensure city is saved after fetching weather
                        showDialog = false
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        tint = Color.Gray
                    )
                }
                IconButton(
                    onClick = {
                        showDialog = true
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        tint = Color.Gray
                    )
                }

            }

            else -> {
                weather?.let { currentWeather ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 50.dp,
                                start = 16.dp,
                                end = 16.dp
                            ), // Add horizontal and vertical padding
                        verticalAlignment = Alignment.CenterVertically, // Center the items vertically
                        horizontalArrangement = Arrangement.SpaceBetween // Space between the name and icon
                    ) {
                        // City Name Text
                        Text(
                            text = currentWeather.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.weight(1f) // Take available space
                        )

                        // Refresh Icon Button
                        IconButton(
                            onClick = {
                                viewModel.fetchWeather(userCity)
                                viewModel.fetch5DayForecast(userCity)
                                viewModel.saveCity(userCity) // Ensure city is saved after fetching weather
                                showDialog = false
                            },
                            modifier = Modifier.size(60.dp) // Set a size for the button
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh",
                                tint = Color.Gray,
                                modifier = Modifier.size(30.dp) // Set a size for the icon
                            )
                        }
                    }
                    IconButton(
                        onClick = { showDialog = true },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            tint = Color.White
                        )
                    }



                    Spacer(modifier = Modifier.height(220.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    ) {
                        Text(
                            text = "${currentWeather.main.temp.roundToInt()}°C",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.Black,
                            fontSize = 60.sp,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = currentWeather.weather[0].description.capitalize(Locale.ROOT),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black,
                        )
                    }
                }

                // Today's forecast
                val todayForecast = forecast?.list?.filter {
                    it.dt_txt.startsWith(today.toString())
                }
                val minTemp =
                    todayForecast?.minByOrNull { it.main.temp }?.main?.temp?.roundToInt()
                val maxTemp =
                    todayForecast?.maxByOrNull { it.main.temp }?.main?.temp?.roundToInt()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "Today",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                    Text(
                        text = dayOfWeek,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .weight(1f)
                    )
                    Text(
                        text = "${minTemp}°C/${maxTemp}°C",  // Display min/max temp
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                }

                // Now let's display the forecast
                forecast?.let {
                    WeatherForecastRow(forecast = it)
                }
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        text = "Enter City Name",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                text = {
                    OutlinedTextField(
                        value = userCity,
                        onValueChange = { userCity = it },
                        label = { Text("City") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.5f
                            )
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            updateCity(userCity)
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        enabled = userCity.isNotBlank() // Disable button if the city is empty
                    ) {
                        Text("Confirm", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Cancel", color = Color.White)
                    }
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(16.dp) // Adding padding around the dialog
            )
        }
    }
}


@Composable
fun WeatherForecastRow(forecast: ForecastResponse) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
        //.padding(bottom = 100.dp)

    ) {
        items(forecast.list.take(24)) { forecastItem ->
            val formattedTime = formatTimeTo12Hour(forecastItem.dt_txt)
            WeatherForecastItem(
                time = formattedTime,
                weatherConditions = forecastItem.weather[0].description.capitalize(Locale.ROOT),
                temperature = "${forecastItem.main.temp.roundToInt()}°C",
                iconRes = getWeatherIcon(forecastItem.weather[0].icon)
            )
        }
    }
}

@Composable
fun WeatherForecastItem(
    time: String,
    weatherConditions: String,
    temperature: String,
    iconRes: Int
) {
    Column(
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        Text(
            text = time,
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,
            modifier = Modifier
                .padding(bottom = 10.dp)
        )
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = weatherConditions,
            modifier = Modifier
                .size(40.dp)
                .padding(top = 8.dp),
            tint = Color.Black,

            )
        Text(
            text = temperature,
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = weatherConditions,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 17.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

fun getWeatherIcon(iconCode: String): Int {
    return when (iconCode) {
        "01d", "01n" -> R.drawable.sunny
        "02d", "02n" -> R.drawable.cloudy
        "03d", "03n", "04d", "04n" -> R.drawable.cloudy
        "09d", "09n", "10d", "10n" -> R.drawable.rainy
        "11d", "11n" -> R.drawable.thunderstorm
        "13d", "13n" -> R.drawable.snowy
        "50d", "50n" -> R.drawable.misty
        else -> R.drawable.cloudy  // Default fallback
    }
}

@Composable
fun verticalGradient(): Brush {
    return Brush.verticalGradient(
        colors = listOf(
            SkyBlue, SkyLight, Color.White
        ), startY = 100f, endY = 1800f
    )
    //Box(modifier = Modifier.background(gradient))
}

@Preview
@Composable
fun WeatherScreenPreview() {
    WeatherScreen(
        viewModel = WeatherViewModel(
            repository = WeatherRepository(),
            application = Application()
        )
    )
}


// Example of formatting time to 12-hour format
fun formatTimeTo12Hour(dateTimeString: String): String {
    val formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // Adjust the pattern based on your input format
    val dateTime = LocalDateTime.parse(dateTimeString, formatter)
    val outputFormatter = DateTimeFormatter.ofPattern("hh:mm a") // 12-hour format
    return dateTime.format(outputFormatter)
}


/*
Box(
modifier = Modifier
.padding(top = 400.dp)
.size(120.dp, 80.dp)
.align(Alignment.CenterHorizontally)
.background(CircularProgress, RoundedCornerShape(8.dp)),
) {
    CircularProgressIndicator(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(16.dp),
        color = MaterialTheme.colorScheme.primary
    )
}*/
