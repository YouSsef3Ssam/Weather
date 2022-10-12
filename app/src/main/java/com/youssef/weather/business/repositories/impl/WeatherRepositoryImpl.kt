package com.youssef.weather.business.repositories.impl

import android.content.Context
import com.youssef.weather.R
import com.youssef.weather.business.entities.LatitudeLongitude
import com.youssef.weather.business.entities.TempUnit
import com.youssef.weather.business.entities.Weather
import com.youssef.weather.business.repositories.abstraction.WeatherRepository
import com.youssef.weather.framework.datasources.local.abstraction.LocalWeatherDataSource
import com.youssef.weather.framework.datasources.local.abstraction.PrefDataSource
import com.youssef.weather.framework.datasources.remote.abstraction.WeatherDataSource
import com.youssef.weather.framework.utils.NetworkUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataSource: WeatherDataSource,
    private val localDataSource: LocalWeatherDataSource,
    private val prefDataSource: PrefDataSource,
    private val networkUtils: NetworkUtils
) : WeatherRepository {

    override suspend fun getCurrentWeather(location: LatitudeLongitude, unit: TempUnit) =
        flow {
            val weather = if (networkUtils.isNetworkAvailable()) getRemoteWeather(location, unit)
            else getLocalWeather(location)
            emit(weather)
        }.flowOn(IO)

    override suspend fun getRemoteWeather(location: LatitudeLongitude, unit: TempUnit): Weather {
        prefDataSource.saveLocation(location)
        val weather = dataSource.getCurrentWeather(location.latitude, location.longitude, unit)
        weather.latitude = location.latitude
        weather.longitude = location.longitude
        localDataSource.insert(weather)
        return weather
    }

    override suspend fun getLocalWeather(location: LatitudeLongitude): Weather {
        return localDataSource.getWeather(location.latitude, location.longitude)
            ?: throw RuntimeException(context.getString(R.string.no_local_weather_message))
    }

}