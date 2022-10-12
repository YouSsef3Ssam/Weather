package com.youssef.weather.framework.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.youssef.weather.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.random.Random

const val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID
const val NOTIFICATION_CHANNEL_NAME = "WEATHER_NOTIFICATION"
const val NOTIFICATION_CHANNEL_DESCRIPTION = "WEATHER"

class NotificationBuilder @Inject constructor(@ApplicationContext private val context: Context) {
    fun build(title: String, description: String, @DrawableRes icon: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_MAX
                )
            notificationChannel.description = NOTIFICATION_CHANNEL_DESCRIPTION
            notificationChannel.enableLights(true)
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val defaultSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(description)
            .setStyle(NotificationCompat.BigTextStyle().bigText(description))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSound(defaultSound)
        notificationManager.notify(getRandomId(), notificationBuilder.build())
    }

    private fun getRandomId(): Int {
        return Random.nextInt(9999 - 1000) + 1000
    }
}