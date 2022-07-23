package com.positivehc.reminder.presentation.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.positivehc.reminder.presentation.main.MainActivity
import com.positivehc.reminder.presentation.utils.BundleUtils.Companion.KEY_DESCRIPTION
import com.positivehc.reminder.presentation.utils.NotificationUtils.Companion.NOTIFICATION_ID
import com.positivehc.reminder.presentation.utils.NotificationUtils.Companion.createNotification
import com.positivehc.reminder.presentation.utils.NotificationUtils.Companion.createNotificationChannel

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) {
            return
        }
        val notificationIntent = Intent(context.applicationContext, MainActivity::class.java)

        val pendingNotificationIntent = PendingIntent.getActivity(
            context.applicationContext,
            NOTIFICATION_ID,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or FLAG_MUTABLE
        )

        val notificationManager =
            ContextCompat.getSystemService(context, NotificationManager::class.java) ?: return
        createNotificationChannel(notificationManager)
        val title = intent.getStringExtra(KEY_DESCRIPTION) ?: ""
        val notification = createNotification(context, title, title, pendingNotificationIntent)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AlarmReceiver::class.java)
        }
    }
}