package com.youssef.weather.framework.datasources.local.mappers

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.youssef.weather.business.entities.WeatherMain

class WeatherMainConverter {

    @TypeConverter
    fun from(data: WeatherMain?): String? {
        if (data == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<WeatherMain?>() {}.type
        return gson.toJson(data, type)
    }

    @TypeConverter
    fun to(dataString: String?): WeatherMain? {
        if (dataString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<WeatherMain?>() {}.type
        return gson.fromJson(dataString, type)
    }
}