package com.youssef.weather.business.usecases.abstraction

import com.youssef.weather.business.entities.LatitudeLongitude
import com.youssef.weather.business.entities.TempUnit
import com.youssef.weather.business.entities.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherUseCase {
    suspend fun getCurrentWeather(location: LatitudeLongitude, unit: TempUnit): Flow<Weather>
}