package com.youssef.weather.framework.datasources.local


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.youssef.weather.framework.datasources.local.daos.WeatherDao
import com.youssef.weather.framework.datasources.local.entities.WeatherEntity
import com.youssef.weather.framework.datasources.local.mappers.WeatherMainConverter
import com.youssef.weather.framework.datasources.local.mappers.WeatherMetaDataConverter
import com.youssef.weather.framework.datasources.local.mappers.WeatherWindConverter


@Database(entities = [WeatherEntity::class], version = 1, exportSchema = false)
@TypeConverters(
    WeatherMainConverter::class,
    WeatherWindConverter::class,
    WeatherMetaDataConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}