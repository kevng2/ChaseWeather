package com.chase.weather

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    // https://api.openweathermap.org/data/2.5/weather?appid={API key}&q={city name}

    @GET("weather?appid=ec0b83bfe835a5f5dc6337341b64ba0c&units=imperial")
    suspend fun getWeatherInfo(
        @Query("q") cityName: String
    ): Response<WeatherInfo>
}