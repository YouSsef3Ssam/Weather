package com.youssef.weather.framework.datasources.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import com.youssef.weather.business.entities.WeatherMain
import com.youssef.weather.business.entities.WeatherMetData
import com.youssef.weather.business.entities.WeatherWind
import com.youssef.weather.framework.datasources.local.mappers.WeatherMainConverter
import com.youssef.weather.framework.datasources.local.mappers.WeatherMetaDataConverter
import com.youssef.weather.framework.datasources.local.mappers.WeatherWindConverter
import com.youssef.weather.framework.utils.Constants.LocalDatabase.Tables

@Entity(tableName = Tables.WEATHER, primaryKeys = ["latitude", "longitude"])
class WeatherEntity(
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "dateTime") val dateTime: Long,
    @TypeConverters(WeatherMainConverter::class) @ColumnInfo(name = "main") val main: WeatherMain,
    @TypeConverters(WeatherWindConverter::class) @ColumnInfo(name = "wind") val wind: WeatherWind,
    @TypeConverters(WeatherMetaDataConverter::class) @ColumnInfo(name = "weather") val weather: List<WeatherMetData>
)