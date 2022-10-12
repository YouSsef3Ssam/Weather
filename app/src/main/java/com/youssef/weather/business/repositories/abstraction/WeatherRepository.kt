package com.youssef.weather.business.repositories.abstraction

import com.youssef.weather.business.entities.LatitudeLongitude
import com.youssef.weather.business.entities.TempUnit
import com.youssef.weather.business.entities.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(location: LatitudeLongitude, unit: TempUnit): Flow<Weather>
    suspend fun getRemoteWeather(location: LatitudeLongitude, unit: TempUnit): Weather
    suspend fun getLocalWeather(location: LatitudeLongitude): Weather?
}