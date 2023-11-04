package com.chase.weather

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val weatherApi: WeatherApi) {
    // Using flow to make it easy to manipulate the data fetched from the server and return it in a
    // form that we want. Using flows also makes it easier to handle error events such as no
    // no internet connection
    fun getWeatherInfo(city: String): Flow<WeatherInfo> =
        flow {
            try {
                val response = weatherApi.getWeatherInfo(city)
                Log.d("WeatherRepository", "getWeatherInfo: $response")
                if (response.isSuccessful) {
                    val weatherInfo = response.body()
                    if (weatherInfo != null) {
                        emit(weatherInfo)
                    }
                }
            } catch (e: Exception) {
                Log.e("WeatherRepository", "Could not fetch weather info", e)
                e.printStackTrace()
            }
        }
}