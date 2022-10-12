package com.youssef.weather.framework.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

const val REMINDER_NOTIFICATION_REQUEST_CODE = 123

class RemindersManager @Inject constructor(@ApplicationContext private val context: Context) {
    fun startReminder() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context.applicationContext,
                REMINDER_NOTIFICATION_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }

        val calendar: Calendar = Calendar.getInstance(Locale.ENGLISH).apply {
            set(Calendar.HOUR_OF_DAY, 6)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        if (inPast(calendar.timeInMillis)) {
            calendar.add(Calendar.DATE, 1)
        }

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(calendar.timeInMillis, intent), intent
        )
    }

    private fun inPast(targetTime: Long): Boolean {
        val currentTime =
            Calendar.getInstance(Locale.ENGLISH).apply { add(Calendar.MINUTE, 1) }.timeInMillis
        return currentTime - targetTime > 0
    }
}