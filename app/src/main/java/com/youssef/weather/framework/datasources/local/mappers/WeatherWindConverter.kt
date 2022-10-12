package com.youssef.weather.framework.datasources.local.mappers

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.youssef.weather.business.entities.WeatherWind

class WeatherWindConverter {

    @TypeConverter
    fun from(data: WeatherWind?): String? {
        if (data == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<WeatherWind?>() {}.type
        return gson.toJson(data, type)
    }

    @TypeConverter
    fun to(dataString: String?): WeatherWind? {
        if (dataString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<WeatherWind?>() {}.type
        return gson.fromJson(dataString, type)
    }
}