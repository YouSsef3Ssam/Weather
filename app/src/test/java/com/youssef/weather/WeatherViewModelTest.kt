package com.youssef.weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.youssef.weather.business.entities.LatitudeLongitude
import com.youssef.weather.business.entities.TempUnit
import com.youssef.weather.business.entities.Weather
import com.youssef.weather.business.usecases.abstraction.WeatherUseCase
import com.youssef.weather.framework.datasources.local.abstraction.PrefDataSource
import com.youssef.weather.framework.presentation.features.weather.WeatherViewModel
import com.youssef.weather.framework.utils.states.DataState
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val useCase: WeatherUseCase = mockkClass(WeatherUseCase::class)
    private val prefDataSource: PrefDataSource = mockkClass(PrefDataSource::class)
    private lateinit var viewModel: WeatherViewModel

    private val input = LatitudeLongitude(1.0, 2.0)
    private val weather = mockkClass(Weather::class)
    private val expectedSuccessResult = weather

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        every { prefDataSource.getTemperatureUnit() } returns TempUnit.CELSIUS
        viewModel = WeatherViewModel(useCase, prefDataSource)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `when locationAvailable then get current weather`() = runBlocking {
        coEvery { useCase.getCurrentWeather(any(), any()) } answers { flow { emit(weather) } }
        viewModel.locationAvailable(input)
        assertTrue(viewModel.currentWeatherDataState.value is DataState.Success)
        val responseValue = (viewModel.currentWeatherDataState.value as DataState.Success).data
        assertEquals(expectedSuccessResult, responseValue)
    }

    @Test
    fun `when updateTempUnit and location saved locally then get current weather`() = runBlocking {
        coEvery { prefDataSource.saveTemperatureUnit(any()) } returns Unit
        coEvery { prefDataSource.getLocation() } returns input
        coEvery { useCase.getCurrentWeather(any(), any()) } answers { flow { emit(weather) } }
        viewModel.updateTempUnit(TempUnit.FAHRENHEIT)

        assertTrue(viewModel.currentWeatherDataState.value is DataState.Success)
        val responseValue = (viewModel.currentWeatherDataState.value as DataState.Success).data
        assertEquals(expectedSuccessResult, responseValue)

        verify { prefDataSource.saveTemperatureUnit(TempUnit.FAHRENHEIT) }
        verify { prefDataSource.getTemperatureUnit() }
    }

    @Test
    fun `when updateTempUnit and location not saved locally then nothing happens`() =
        runBlocking {
            coEvery { prefDataSource.saveTemperatureUnit(any()) } returns Unit
            coEvery { prefDataSource.getLocation() } returns null
            coEvery { useCase.getCurrentWeather(any(), any()) } answers { flow { emit(weather) } }
            viewModel.updateTempUnit(TempUnit.FAHRENHEIT)

            assertTrue(viewModel.currentWeatherDataState.value is DataState.Loading)
        }

    @Test
    fun `when locationNotAvailable and location saved locally then get current weather`() =
        runBlocking {
            coEvery { prefDataSource.getLocation() } returns input
            coEvery { useCase.getCurrentWeather(any(), any()) } answers { flow { emit(weather) } }
            viewModel.locationNotAvailable()

            assertTrue(viewModel.currentWeatherDataState.value is DataState.Success)
            val responseValue = (viewModel.currentWeatherDataState.value as DataState.Success).data
            assertEquals(expectedSuccessResult, responseValue)

            verify { prefDataSource.getTemperatureUnit() }
        }

    @Test
    fun `when locationNotAvailable and location not saved locally then nothing happens`() =
        runBlocking {
            coEvery { prefDataSource.saveTemperatureUnit(any()) } returns Unit
            coEvery { prefDataSource.getLocation() } returns null
            coEvery { useCase.getCurrentWeather(any(), any()) } answers { flow { emit(weather) } }
            viewModel.locationNotAvailable()

            assertTrue(viewModel.currentWeatherDataState.value is DataState.Loading)
        }
}