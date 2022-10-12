package com.youssef.weather

import android.content.Context
import com.youssef.weather.business.entities.*
import com.youssef.weather.business.repositories.abstraction.WeatherRepository
import com.youssef.weather.business.repositories.impl.WeatherRepositoryImpl
import com.youssef.weather.framework.datasources.local.abstraction.LocalWeatherDataSource
import com.youssef.weather.framework.datasources.local.abstraction.PrefDataSource
import com.youssef.weather.framework.datasources.remote.abstraction.WeatherDataSource
import com.youssef.weather.framework.utils.NetworkUtils
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryTest {

    private lateinit var repository: WeatherRepository
    private val context: Context = mockkClass(Context::class)
    private val dataSource: WeatherDataSource = mockkClass(WeatherDataSource::class)
    private val localDataSource: LocalWeatherDataSource = mockkClass(LocalWeatherDataSource::class)
    private val prefDataSource: PrefDataSource = mockkClass(PrefDataSource::class)
    private val networkUtils: NetworkUtils = mockkClass(NetworkUtils::class)

    private val input = LatitudeLongitude(1.0, 2.0)
    private val weather = Weather(
        1L,
        mockkClass(WeatherMain::class),
        mockkClass(WeatherWind::class),
        listOf(),
        1.0,
        1.0
    )
    private val expectedSuccessResult = weather
    private val expectedFailureResult = RuntimeException("Can't get weather data")
    private val noLocalWeatherMessage = "Can't get weather from local"

    @Before
    fun setUp() {
        repository =
            WeatherRepositoryImpl(
                context,
                dataSource,
                localDataSource,
                prefDataSource,
                networkUtils
            )
        every { context.getString(any()) } returns noLocalWeatherMessage
        every { prefDataSource.saveLocation(any()) } returns Unit
    }

    @Test
    fun `getCurrentWeather with internet connection then get weather from remote`() = runBlocking {
        every { networkUtils.isNetworkAvailable() } returns true
        coEvery { dataSource.getCurrentWeather(any(), any(), any()) } returns weather
        coEvery { localDataSource.insert(any()) } returns 1L
        val response = repository.getCurrentWeather(input, TempUnit.CELSIUS)

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
    fun `getCurrentWeather with no internet connection then get weather from local`() =
        runBlocking {
            every { networkUtils.isNetworkAvailable() } returns false
            coEvery { localDataSource.getWeather(any(), any()) } returns weather
            val response = repository.getCurrentWeather(input, TempUnit.CELSIUS)

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
    fun `getCurrentWeather with no internet connection and no available data in local then return can not get local weather`() =
        runBlocking {
            every { networkUtils.isNetworkAvailable() } returns false
            coEvery { localDataSource.getWeather(any(), any()) } returns null
            val response = repository.getCurrentWeather(input, TempUnit.CELSIUS)

            var success: Weather? = null
            var error: Throwable? = null
            response
                .catch { error = it }
                .collect { success = it }

            assertNotNull(error)
            assertNull(success)
            assertEquals(noLocalWeatherMessage, error?.message)
        }


    @Test
    fun `getCurrentWeather with internet connection and failure response then return error`() =
        runBlocking {
            every { networkUtils.isNetworkAvailable() } returns true
            coEvery {
                dataSource.getCurrentWeather(any(), any(), any())
            } throws expectedFailureResult
            val response = repository.getCurrentWeather(input, TempUnit.CELSIUS)

            var success: Weather? = null
            var error: Throwable? = null
            response
                .catch { error = it }
                .collect { success = it }

            assertNotNull(error)
            assertNull(success)
            assertEquals(expectedFailureResult.message, error?.message)
        }


    @Test
    fun `getCurrentWeather with no internet connection and failure response then return error`() =
        runBlocking {
            every { networkUtils.isNetworkAvailable() } returns false
            coEvery { localDataSource.getWeather(any(), any()) } throws expectedFailureResult
            val response = repository.getCurrentWeather(input, TempUnit.CELSIUS)

            var success: Weather? = null
            var error: Throwable? = null
            response
                .catch { error = it }
                .collect { success = it }

            assertNotNull(error)
            assertNull(success)
            assertEquals(expectedFailureResult.message, error?.message)
        }

    @Test
    fun `getRemoteWeather with success response then return success`() = runBlocking {
        coEvery { localDataSource.insert(any()) } returns 1L
        coEvery { dataSource.getCurrentWeather(any(), any(), any()) } returns weather
        val response = repository.getRemoteWeather(input, TempUnit.CELSIUS)
        assertNotNull(response)
        assertEquals(expectedSuccessResult, response)
    }

    @Test
    fun `getRemoteWeather with failure response then return error`() = runBlocking {
        coEvery {
            dataSource.getCurrentWeather(any(), any(), any())
        } answers { throw expectedFailureResult }

        var success: Weather? = null
        var error: Throwable? = null
        try {
            success = repository.getRemoteWeather(input, TempUnit.CELSIUS)
        } catch (e: Exception) {
            error = e
        }
        assertNotNull(error)
        assertNull(success)
        assertEquals(expectedFailureResult.message, error?.message)
    }

    @Test
    fun `getLocalWeather with success response then return success`() = runBlocking {
        coEvery { localDataSource.getWeather(any(), any()) } returns weather
        val response = repository.getLocalWeather(input)
        assertNotNull(response)
        assertEquals(expectedSuccessResult, response)
    }

    @Test
    fun `getLocalWeather with no available data in local then return can not get local weather`() =
        runBlocking {
            coEvery { localDataSource.getWeather(any(), any()) } returns null

            var success: Weather? = null
            var error: Throwable? = null
            try {
                success = repository.getLocalWeather(input)
            } catch (e: Exception) {
                error = e
            }

            assertNotNull(error)
            assertNull(success)
            assertEquals(noLocalWeatherMessage, error?.message)
        }

    @Test
    fun `getLocalWeather with failure response then return error`() = runBlocking {
        coEvery { localDataSource.getWeather(any(), any()) } answers { throw expectedFailureResult }

        var success: Weather? = null
        var error: Throwable? = null
        try {
            success = repository.getLocalWeather(input)
        } catch (e: Exception) {
            error = e
        }

        assertNotNull(error)
        assertNull(success)
        assertEquals(expectedFailureResult.message, error?.message)
    }


}