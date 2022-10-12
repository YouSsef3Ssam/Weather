package com.youssef.weather.framework.datasources.remote.abstraction

import com.youssef.weather.business.entities.TempUnit
import com.youssef.weather.business.entities.Weather

interface WeatherDataSource {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double, unit: TempUnit): Weather
}