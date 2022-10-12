package com.youssef.weather.business.usecases.impl

import com.youssef.weather.business.entities.LatitudeLongitude
import com.youssef.weather.business.entities.TempUnit
import com.youssef.weather.business.repositories.abstraction.WeatherRepository
import com.youssef.weather.business.usecases.abstraction.WeatherUseCase
import javax.inject.Inject

class WeatherUseCaseImpl @Inject constructor(private val repository: WeatherRepository) :
    WeatherUseCase {

    override suspend fun getCurrentWeather(location: LatitudeLongitude, unit: TempUnit) =
        repository.getCurrentWeather(location, unit)

}