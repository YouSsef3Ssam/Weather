package com.youssef.weather.framework.datasources.local.mappers

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.youssef.weather.business.entities.WeatherMetData

class WeatherMetaDataConverter {

    @TypeConverter
    fun from(data: List<WeatherMetData>?): String? {
        if (data == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<WeatherMetData>?>() {}.type
        return gson.toJson(data, type)
    }

    @TypeConverter
    fun to(dataString: String?): List<WeatherMetData>? {
        if (dataString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<WeatherMetData>?>() {}.type
        return gson.fromJson(dataString, type)
    }
}