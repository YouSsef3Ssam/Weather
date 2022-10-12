package com.youssef.weather.framework.datasources.remote.services

import com.youssef.weather.business.entities.Weather
import com.youssef.weather.framework.utils.Constants.Network.EndPoints
import com.youssef.weather.framework.utils.Constants.Network.Query.LATITUDE
import com.youssef.weather.framework.utils.Constants.Network.Query.LONGITUDE
import com.youssef.weather.framework.utils.Constants.Network.Query.UNITS
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET(EndPoints.WEATHER)
    suspend fun getCurrentWeather(
        @Query(LATITUDE) latitude: Double,
        @Query(LONGITUDE) longitude: Double,
        @Query(UNITS) unit: String
    ): Weather
}