package com.youssef.weather.framework.datasources.local.mappers

import com.youssef.weather.business.entities.Weather
import com.youssef.weather.framework.datasources.local.entities.WeatherEntity
import javax.inject.Inject

class Mapper @Inject constructor() : EntityMapper<WeatherEntity, Weather> {
    override fun mapFromEntity(entity: WeatherEntity): Weather =
        with(entity) { Weather(dateTime, main, wind, weather, latitude, longitude) }

    override fun mapToEntity(response: Weather): WeatherEntity = with(response) {
        if (latitude == null) throw RuntimeException("Latitude can't be null")
        if (longitude == null) throw RuntimeException("Longitude can't be null")
        WeatherEntity(latitude!!, longitude!!, dateTime, main, wind, weather)
    }
}