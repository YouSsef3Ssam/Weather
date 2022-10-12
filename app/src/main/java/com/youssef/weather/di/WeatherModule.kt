package com.youssef.weather.di

import android.content.Context
import com.youssef.weather.business.repositories.abstraction.WeatherRepository
import com.youssef.weather.business.repositories.impl.WeatherRepositoryImpl
import com.youssef.weather.business.usecases.abstraction.WeatherUseCase
import com.youssef.weather.business.usecases.impl.WeatherUseCaseImpl
import com.youssef.weather.framework.datasources.local.abstraction.LocalWeatherDataSource
import com.youssef.weather.framework.datasources.local.abstraction.PrefDataSource
import com.youssef.weather.framework.datasources.local.daos.WeatherDao
import com.youssef.weather.framework.datasources.local.impl.LocalWeatherDataSourceImpl
import com.youssef.weather.framework.datasources.local.mappers.Mapper
import com.youssef.weather.framework.datasources.remote.abstraction.WeatherDataSource
import com.youssef.weather.framework.datasources.remote.impl.WeatherDataSourceImpl
import com.youssef.weather.framework.datasources.remote.services.WeatherApi
import com.youssef.weather.framework.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WeatherModule {

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi =
        retrofit.create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideLocalWeatherDataSource(
        weatherDao: WeatherDao,
        mapper: Mapper
    ): LocalWeatherDataSource =
        LocalWeatherDataSourceImpl(weatherDao, mapper)


    @Provides
    @Singleton
    fun provideWeatherDataSource(weatherApi: WeatherApi): WeatherDataSource =
        WeatherDataSourceImpl(weatherApi)

    @Provides
    @Singleton
    fun provideWeatherRepository(
        @ApplicationContext context: Context,
        dataSource: WeatherDataSource,
        localDataSource: LocalWeatherDataSource,
        prefDataSource: PrefDataSource,
        networkUtils: NetworkUtils,
    ): WeatherRepository =
        WeatherRepositoryImpl(context, dataSource, localDataSource, prefDataSource, networkUtils)

    @Provides
    @Singleton
    fun provideWeatherUseCase(weatherRepository: WeatherRepository): WeatherUseCase =
        WeatherUseCaseImpl(weatherRepository)
}