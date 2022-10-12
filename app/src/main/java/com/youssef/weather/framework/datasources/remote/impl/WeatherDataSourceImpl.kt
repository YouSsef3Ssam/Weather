package com.youssef.weather.framework.datasources.remote.impl

import com.youssef.weather.business.entities.TempUnit
import com.youssef.weather.framework.datasources.remote.abstraction.WeatherDataSource
import com.youssef.weather.framework.datasources.remote.services.WeatherApi
import javax.inject.Inject

class WeatherDataSourceImpl @Inject constructor(private val api: WeatherApi) : WeatherDataSource {

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double, unit: TempUnit) =
        api.getCurrentWeather(latitude, longitude, unit.value)

}