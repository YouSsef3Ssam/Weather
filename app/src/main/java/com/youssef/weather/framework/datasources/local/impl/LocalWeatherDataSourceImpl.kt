package com.youssef.weather.framework.datasources.local.impl

import com.youssef.weather.business.entities.Weather
import com.youssef.weather.framework.datasources.local.abstraction.LocalWeatherDataSource
import com.youssef.weather.framework.datasources.local.daos.WeatherDao
import com.youssef.weather.framework.datasources.local.mappers.Mapper
import javax.inject.Inject

class LocalWeatherDataSourceImpl @Inject constructor(
    private val weatherDao: WeatherDao,
    private val mapper: Mapper
) : LocalWeatherDataSource {

    override suspend fun insert(weather: Weather): Long =
        weatherDao.insert(mapper.mapToEntity(weather))

    override suspend fun getWeather(latitude: Double, longitude: Double) =
        weatherDao.getWeather(latitude, longitude)?.run { mapper.mapFromEntity(this) }
}