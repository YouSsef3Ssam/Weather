package com.youssef.weather.di

import android.content.Context
import com.iamhabib.easy_preference.EasyPreference
import com.youssef.weather.framework.datasources.local.abstraction.PrefDataSource
import com.youssef.weather.framework.datasources.local.impl.PrefDataSourceImpl
import com.youssef.weather.framework.notification.NotificationBuilder
import com.youssef.weather.framework.notification.RemindersManager
import com.youssef.weather.framework.utils.Constants.PrefKeys.PREF_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideEasyPreference(@ApplicationContext context: Context): EasyPreference.Builder =
        EasyPreference.with(context, PREF_KEY)

    @Provides
    @Singleton
    fun providePrefDataSource(preference: EasyPreference.Builder): PrefDataSource =
        PrefDataSourceImpl(preference)

    @Provides
    @Singleton
    fun provideNotificationBuilder(@ApplicationContext context: Context): NotificationBuilder =
        NotificationBuilder(context)

    @Provides
    @Singleton
    fun provideRemindersManager(@ApplicationContext context: Context): RemindersManager =
        RemindersManager(context)
}