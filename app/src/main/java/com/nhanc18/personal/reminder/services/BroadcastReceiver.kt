package com.nhanc18.personal.reminder.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import android.app.NotificationManager
import android.app.NotificationChannel
import android.util.Log
import com.nhanc18.personal.reminder.data.Reminder
import com.nhanc18.personal.reminder.data.reminderList

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ReminderReceiver", "onReceive called!")
        val task = intent.getStringExtra("task") ?: "Time to do something!"
        val requestCode = intent.getIntExtra("requestCode", -1)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            "reminder_channel",
            "Reminder Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Reminder")
            .setContentText(task)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)

        // Đặt lại alarm cho ngày tiếp theo
        if (requestCode != -1) {
            val reminder = reminderList.find { it.requestCode == requestCode }
            reminder?.let {
                scheduleReminders(context, listOf(reminder))
            }
        }
    }
}