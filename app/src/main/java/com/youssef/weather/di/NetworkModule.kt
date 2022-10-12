package com.youssef.weather.di

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.youssef.weather.BuildConfig
import com.youssef.weather.framework.utils.Constants
import com.youssef.weather.framework.utils.Constants.Network.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.MINUTES
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val baseUrl: String
        get() = BASE_URL

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


    @Provides
    @Singleton
    fun provideOkHttpClient(
        headerInterceptor: Interceptor,
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            httpClientBuilder.addInterceptor(chuckerInterceptor)
        }
        httpClientBuilder
            .addInterceptor(headerInterceptor)
            .connectTimeout(1, MINUTES)
            .readTimeout(1, MINUTES)
            .writeTimeout(1, MINUTES)

        return httpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideChuckInterceptor(application: Application): ChuckerInterceptor =
        ChuckerInterceptor.Builder(application.applicationContext).build()

    @Provides
    @Singleton
    fun provideHeaderInterceptor() =
        Interceptor { chain ->
            val originalRequest = chain.request()
            val url: HttpUrl = originalRequest.url.newBuilder()
                .addQueryParameter(Constants.Network.API_KEY, Constants.Network.API_KEY_VALUE)
                .build()
            val requestBuilder = originalRequest.newBuilder().url(url)
            val newRequest = requestBuilder.build()
            chain.proceed(newRequest)
        }
}