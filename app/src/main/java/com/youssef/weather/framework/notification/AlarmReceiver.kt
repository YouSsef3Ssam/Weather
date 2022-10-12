package com.youssef.weather.framework.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.youssef.weather.R
import com.youssef.weather.business.entities.LatitudeLongitude
import com.youssef.weather.business.entities.TempUnit
import com.youssef.weather.business.entities.Weather
import com.youssef.weather.framework.datasources.local.abstraction.PrefDataSource
import com.youssef.weather.framework.datasources.remote.abstraction.WeatherDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var remindersManager: RemindersManager

    @Inject
    lateinit var notificationBuilder: NotificationBuilder

    @Inject
    lateinit var prefDataSource: PrefDataSource

    @Inject
    lateinit var weatherDataSource: WeatherDataSource

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    /**
     * sends notification when receives alarm
     * and then reschedule the reminder again
     * */
    override fun onReceive(context: Context, intent: Intent) {
        prefDataSource.getLocation()?.let {
            coroutineScope.launch {
                val tempUnit = prefDataSource.getTemperatureUnit()
                val weather = getWeather(it, tempUnit)
                notificationBuilder.build(
                    context.getString(R.string.title_notification_reminder),
                    context.getString(
                        R.string.message_notification_reminder,
                        weather.main.temp,
                        tempUnit.sign
                    ), R.drawable.logo
                )
                remindersManager.startReminder()
            }
        }
    }

    private suspend fun getWeather(location: LatitudeLongitude, tempUnit: TempUnit): Weather =
        weatherDataSource.getCurrentWeather(location.latitude, location.longitude, tempUnit)

}