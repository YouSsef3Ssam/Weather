package com.youssef.weather.framework.presentation.features.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youssef.weather.business.entities.LatitudeLongitude
import com.youssef.weather.business.entities.TempUnit
import com.youssef.weather.business.entities.Weather
import com.youssef.weather.business.usecases.abstraction.WeatherUseCase
import com.youssef.weather.framework.datasources.local.abstraction.PrefDataSource
import com.youssef.weather.framework.utils.SingleLiveEvent
import com.youssef.weather.framework.utils.ext.catchError
import com.youssef.weather.framework.utils.ext.launchIdling
import com.youssef.weather.framework.utils.states.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val useCase: WeatherUseCase,
    private val prefDataSource: PrefDataSource
) : ViewModel() {

    private val _currentWeatherDataState: MutableLiveData<DataState<Weather>> =
        MutableLiveData(DataState.Loading)
    val currentWeatherDataState: LiveData<DataState<Weather>> get() = _currentWeatherDataState

    private val _canNotGetLocation: SingleLiveEvent<Unit> = SingleLiveEvent()
    val canNotGetLocation: LiveData<Unit> get() = _canNotGetLocation

    private var temperatureUnit = prefDataSource.getTemperatureUnit()

    fun locationAvailable(location: LatitudeLongitude) {
        viewModelScope.launchIdling {
            getCurrentWeather(location)
        }
    }

    private suspend fun getCurrentWeather(location: LatitudeLongitude) {
        useCase.getCurrentWeather(location, temperatureUnit)
            .catchError { _currentWeatherDataState.value = DataState.Failure(it) }
            .collect { _currentWeatherDataState.value = DataState.Success(it) }
    }

    fun locationNotAvailable() {
        getLocalLocation()
    }

    private fun getLocalLocation() {
        prefDataSource.getLocation()?.let { locationAvailable(it) } ?: run {
            _canNotGetLocation.value = Unit
        }
    }

    fun updateTempUnit(tempUnit: TempUnit) {
        temperatureUnit = tempUnit
        prefDataSource.saveTemperatureUnit(temperatureUnit)
        getLocalLocation()
    }

    fun getTempUnit(): TempUnit = temperatureUnit
}