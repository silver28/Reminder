package com.positivehc.reminder.presentation.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.content.ContextCompat

class AlarmUtils {
    companion object {

        fun setExact(context: Context, timeInMillis: Long, pendingIntent: PendingIntent) {
            getAlarmManager(context).setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        }

        fun cancel(context: Context, pendingIntent: PendingIntent) {
            getAlarmManager(context).cancel(pendingIntent)
        }

        private fun getAlarmManager(context: Context): AlarmManager {
            return ContextCompat.getSystemService(context, AlarmManager::class.java) as AlarmManager
        }
    }
}