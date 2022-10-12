package com.youssef.weather.framework.datasources.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.youssef.weather.framework.datasources.local.entities.WeatherEntity
import com.youssef.weather.framework.utils.Constants.LocalDatabase.Tables

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherEntity): Long

    @Query("SELECT * FROM ${Tables.WEATHER} WHERE latitude = :latitude AND longitude = :longitude")
    suspend fun getWeather(latitude: Double, longitude: Double): WeatherEntity?
}