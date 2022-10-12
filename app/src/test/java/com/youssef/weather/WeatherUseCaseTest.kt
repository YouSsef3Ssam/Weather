package com.youssef.weather

import com.youssef.weather.business.entities.LatitudeLongitude
import com.youssef.weather.business.entities.TempUnit
import com.youssef.weather.business.entities.Weather
import com.youssef.weather.business.repositories.abstraction.WeatherRepository
import com.youssef.weather.business.usecases.abstraction.WeatherUseCase
import com.youssef.weather.business.usecases.impl.WeatherUseCaseImpl
import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WeatherUseCaseTest {

    private val repository: WeatherRepository = mockkClass(WeatherRepository::class)
    private lateinit var useCase: WeatherUseCase

    private val weather = mockkClass(Weather::class)
    private val expectedSuccessResult = weather
    private val exception = RuntimeException("Can't get weather data")

    @Before
    fun setUp() {
        useCase = WeatherUseCaseImpl(repository)
    }

    @Test
    fun `getCurrentWeather with success response then return success`() = runBlocking {
        val input = mockkClass(LatitudeLongitude::class)
        coEvery { repository.getCurrentWeather(any(), any()) } answers { flow { emit(weather) } }
        val response = useCase.getCurrentWeather(input, TempUnit.CELSIUS)

        var success: Weather? = null
        var error: Throwable? = null

        response
            .catch { error = it }
            .collect { success = it }

        assertNotNull(success)
        assertNull(error)

        assertEquals(expectedSuccessResult, success)
    }

    @Test
    fun `getCurrentWeather with failure response then return error`() = runBlocking {
        val input = mockkClass(LatitudeLongitude::class)
        coEvery {
            repository.getCurrentWeather(any(), any())
        } answers { flow { throw exception } }
        val response = useCase.getCurrentWeather(input, TempUnit.CELSIUS)

        var success: Weather? = null
        var error: Throwable? = null

        response
            .catch { error = it }
            .collect { success = it }

        assertNull(success)
        assertNotNull(error)
        assertEquals(exception.message, error!!.message)
    }

}