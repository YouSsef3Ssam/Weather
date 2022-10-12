package com.youssef.weather.di

import android.content.Context
import androidx.room.Room
import com.youssef.weather.framework.datasources.local.AppDatabase
import com.youssef.weather.framework.datasources.local.daos.WeatherDao
import com.youssef.weather.framework.utils.Constants.LocalDatabase.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WeatherDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideWeatherDao(siaDb: AppDatabase): WeatherDao = siaDb.weatherDao()

}