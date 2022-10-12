package com.youssef.weather.framework.datasources.local.abstraction

import com.youssef.weather.business.entities.Weather

interface LocalWeatherDataSource {
    suspend fun insert(weather: Weather): Long
    suspend fun getWeather(latitude: Double, longitude: Double): Weather?
}