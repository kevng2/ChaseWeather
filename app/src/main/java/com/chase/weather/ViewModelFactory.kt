package com.chase.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import dagger.Lazy
import javax.inject.Inject

/**
 * A Factory class that reduces the boilerplate to initialize the ViewModel within the Activity or a Fragment.
 *
 * Usage example:
 *
 *    @Inject
 *    lateinit var viewModelFactory: ViewModelFactory<WeatherInfoViewModel>
 *
 *    private val viewModel: WeatherInfoViewModel by lazy {
 *        viewModelFactory.get<WeatherInfoViewModel>(
 *            requireActivity()
 *        )
 *    }
 */
class ViewModelFactory<T: ViewModel>
@Inject constructor(private val viewModel: Lazy<T>) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModel.get() as T

    /**
     * Returns an instance of a defined ViewModel class.
     */
    inline fun <reified R: T> get(viewModelStoreOwner: ViewModelStoreOwner): T {
        return ViewModelProvider(viewModelStoreOwner, this)[R::class.java]
    }
}
