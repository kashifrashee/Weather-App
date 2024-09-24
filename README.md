---

# Weather App

## Overview

The Weather App is a modern Android application built using Jetpack Compose that provides users with real-time weather information, a 5-day forecast, and a user-friendly interface for selecting and saving locations. This application fetches weather data from OpenWeatherMap and offers an interactive experience with features such as refreshing the weather, viewing hourly forecasts, and changing cities dynamically.

## Features

- **Real-Time Weather Data**: Displays current weather conditions for a specified city.
- **Dynamic City Selection**: Users can easily change the city and save it for future use.
- **Error Handling**: Displays appropriate error messages in case of failed data fetching.
- **Loading Indicators**: Visual feedback during data loading states.
- **Responsive Design**: A clean and user-friendly interface optimized for various screen sizes.
- **Swipe to Refresh**: Users can refresh the weather data by swiping down on the main screen.
- **Persistent City Storage**: Remembers the last searched city even when the app is closed.

## Technology Stack

- **Android Development**: Jetpack Compose for UI.
- **Kotlin**: Programming language for app development.
- **MVVM Architecture**: For a clean separation of concerns and better testability.
- **Coroutines**: For asynchronous programming and data fetching.
- **OpenWeatherMap API**: To fetch weather data.

## Getting Started

### Prerequisites

- Android Studio (latest version)
- Basic knowledge of Kotlin and Jetpack Compose
- An API key from [OpenWeatherMap](https://openweathermap.org/)

### Setup Instructions

1. **Clone the repository**:

   ```bash
   git clone https://github.com/kashifrashee/Weather-App.git
   ```

2. **Open the project in Android Studio**.

3. **Add your OpenWeatherMap API key**:
    - In your `WeatherViewModel`, replace the placeholder for the API key with your actual API key.

4. **Run the app**:
    - Connect your Android device or start an emulator.
    - Click on the "Run" button in Android Studio.

## Usage

- **Launch the App**: Open the app on your device.
- **View Current Weather**: The app will display the current weather for the default city (Karachi).
- **Change City**: Click the location icon to enter a new city name in the dialog that appears. Click "Confirm" to update the weather.
- **Refresh Weather**: Use the refresh button to reload the current weather data.
- **View Forecast**: The app will display a 5-day weather forecast below the current weather information.

## Contributing

Contributions are welcome! If you have suggestions for improvements or features, feel free to fork the repository and submit a pull request.

1. Fork the repository.
2. Create your feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some amazing feature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
