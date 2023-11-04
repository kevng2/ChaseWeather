package com.chase.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.chase.weather.ui.theme.ChaseWeatherTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<WeatherInfoViewModel>

    // I would liked to have used real persistence, such as jetpack datastore or room database
    // to store the searched cities
    private val searchedCities = mutableListOf<String>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ChaseWeatherApplication.getAppComponent().inject(this)

        setContent {
            ChaseWeatherTheme {
                val weatherInfoViewModel =
                    viewModel<WeatherInfoViewModel>(factory = viewModelFactory)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var text by remember { mutableStateOf("") }
                    var active by remember { mutableStateOf(false) }

                    // Anytime the user changes the text, this composable will recompose and will
                    // call the api with a new value. Ideally this would be able to take in more
                    // values such as state and country
                    val weatherInfo = weatherInfoViewModel.getWeatherInfoFlow(text)
                        .collectAsState(initial = null)
                        .value
                    val scope = rememberCoroutineScope()

                    // TODO() Separate this into a separate composable
                    Column(modifier = Modifier.fillMaxSize()) {
                        SearchBar(modifier = Modifier.fillMaxWidth(),
                            query = text,
                            onQueryChange = { text = it },
                            onSearch = { queryText ->
                                active = false
                                scope.launch {
                                    // everytime the user searches, add it to the list
                                    searchedCities.add(queryText)
                                }
                            },
                            active = active,
                            onActiveChange = { active = it },
                            placeholder = { Text(text = "Enter your query") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null
                                )
                            }) {

                            LazyColumn {
                                items(searchedCities) { city ->
                                    Text(city, modifier = Modifier.clickable {
                                        text = city
                                        active = false
                                    })
                                }
                            }
                        }

                        if (weatherInfo != null) {
                            AsyncImage(
                                model = "https://openweathermap.org/img/wn/${weatherInfo.weather[0].icon}@2x.png",
                                contentDescription = null
                            )

                            // TODO(): Add more fields here to populate screen
                            Text(text = weatherInfo.name)
                            Text(text = weatherInfo.main.temp.toString())
                            Text(text = "High: ${weatherInfo.main.temp_max}, Low: ${weatherInfo.main.temp_min}")

                        }
                    }
                    if (weatherInfo == null) return@Surface
                }
            }
        }
    }
}

class WeatherInfoViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {
    // The responsibility of this viewmodel to to communicate the repository and fetch the latest
    // data. Business logic will be in the repository itself
    fun getWeatherInfoFlow(cityName: String): Flow<WeatherInfo> =
        weatherRepository.getWeatherInfo(cityName)
}
