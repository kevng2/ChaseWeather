package com.chase.weather.dagger

import android.app.Application
import com.chase.weather.ChaseWeatherApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: ChaseWeatherApplication) {
    @Provides
    @Singleton
    fun getApplication(): Application = application
}
