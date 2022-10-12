package com.youssef.weather.framework.datasources.local.abstraction

import com.youssef.weather.business.entities.LatitudeLongitude
import com.youssef.weather.business.entities.TempUnit

interface PrefDataSource {

    fun saveLocation(location: LatitudeLongitude)
    fun getLocation(): LatitudeLongitude?

    fun saveTemperatureUnit(tempUnit: TempUnit)
    fun getTemperatureUnit(): TempUnit
}