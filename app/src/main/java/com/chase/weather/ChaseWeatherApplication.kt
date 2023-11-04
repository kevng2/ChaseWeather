package com.chase.weather

import android.app.Application
import com.chase.weather.dagger.AppComponent
import com.chase.weather.dagger.AppModule
import com.chase.weather.dagger.DaggerAppComponent

class ChaseWeatherApplication : Application() {
    companion object {
        private lateinit var appComponent: AppComponent

        fun getAppComponent(): AppComponent {
            return appComponent
        }
    }

    override fun onCreate() {
        super.onCreate()
        initDaggerAppComponent()
    }

    private fun initDaggerAppComponent(): AppComponent {
        appComponent =
            DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        return appComponent
    }
}