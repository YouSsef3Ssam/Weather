package com.youssef.weather

import com.youssef.weather.business.entities.TempUnit
import com.youssef.weather.business.entities.Weather
import com.youssef.weather.framework.datasources.remote.abstraction.WeatherDataSource
import com.youssef.weather.framework.datasources.remote.impl.WeatherDataSourceImpl
import com.youssef.weather.framework.datasources.remote.services.WeatherApi
import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LocalWeatherDataSourceTest {
    private lateinit var dataSource: WeatherDataSource
    private val api = mockkClass(WeatherApi::class)
    private val weather = mockkClass(Weather::class)

    private val expectedWeatherSuccessResult = weather
    private val expectedFailureResult = RuntimeException("Can't get weather data")


    @Before
    fun setUp() {
        dataSource = WeatherDataSourceImpl(api)
    }

    @Test
    fun `getCurrentWeather with success response then return success`() = runBlocking {
        coEvery {
            api.getCurrentWeather(any(), any(), any())
        } answers { expectedWeatherSuccessResult }
        val response = dataSource.getCurrentWeather(1.0, 2.0, TempUnit.CELSIUS)

        assertNotNull(response)
        assertEquals(expectedWeatherSuccessResult, response)
    }

    @Test
    fun `getCurrentWeather with failure response then return error`() = runBlocking {
        coEvery { api.getCurrentWeather(any(), any(), any()) } throws expectedFailureResult
        var response: RuntimeException? = null
        try {
            dataSource.getCurrentWeather(1.0, 2.0, TempUnit.CELSIUS)
        } catch (e: RuntimeException) {
            response = e
        }

        assertNotNull(response)
        assertEquals(expectedFailureResult, response)
        assertEquals(expectedFailureResult.message, response?.message)
    }

}