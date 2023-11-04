package com.chase.weather.dagger

import com.chase.weather.ChaseWeatherApplication
import com.chase.weather.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun appModule(module: AppModule): Builder
        fun build(): AppComponent
    }

    fun inject(app: ChaseWeatherApplication)
    fun inject(activity: MainActivity)
}