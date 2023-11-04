package com.chase.weather

import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertSame
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@RunWith(JUnit4::class)
class WeatherInfoViewModelTest {
    lateinit var weatherInfoViewModel: WeatherInfoViewModel

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var weatherRepository: WeatherRepository

    @Before
    fun setUp() {
        weatherInfoViewModel = WeatherInfoViewModel(weatherRepository)
    }

    @Test
    fun weatherRepositoryReturnsValidData_viewModelHasCorrectData() = runTest {
        whenever(weatherRepository.getWeatherInfo("Atlanta")).thenReturn(flowOf(WEATHER_INFO))

        weatherInfoViewModel.getWeatherInfoFlow("Atlanta").collect { weatherInfo ->
            assertSame(weatherInfo, WEATHER_INFO)
        }
    }

    companion object {
        val WEATHER_INFO = WeatherInfo(
            "base",
            Clouds(1),
            2,
            Coord(3.0, 4.0),
            5,
            6,
            Main(7.0, 8, 0, 10.0, 11.0, 12.0),
            "name",
            Sys("country", 13, 14, 15, 16),
            17,
            18,
            listOf(),
            Wind(18, 19.0)
        )
    }
}