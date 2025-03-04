package com.nhanc18.personal.reminder.services

import android.Manifest
import android.content.Context
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.widget.Toast
import android.provider.Settings
import com.nhanc18.personal.reminder.data.Reminder
import java.util.Calendar

object AlarmService {
    fun getPermission(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return Manifest.permission.USE_EXACT_ALARM
        }
        return ""
    }
}

fun scheduleReminders(context: Context, reminderList: List<Reminder>) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    reminderList.forEach { reminder ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, reminder.hour)
        calendar.set(Calendar.MINUTE, reminder.minute)
        calendar.set(Calendar.SECOND, 0)

        // Nếu thời gian đã qua trong ngày, đặt cho ngày hôm sau
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        setAlarm(context, alarmManager, reminder.task, reminder.requestCode, calendar.timeInMillis)
    }
}

fun showTestNotification(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    setAlarm(context, alarmManager, "Test Notification", 998, System.currentTimeMillis() + 3000)
}

private fun canScheduleExactAlarm(
    context: Context,
): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (alarmManager.canScheduleExactAlarms()) {
            return true
        } else {
            Toast.makeText(
                context,
                "Please enable exact alarm permission in Settings",
                Toast.LENGTH_LONG
            ).show()
            val settingsIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            context.startActivity(settingsIntent)
            return false
        }
    }
    return true
}

private fun setAlarm(
    context: Context,
    alarmManager: AlarmManager,
    displayText: String,
    requestCode: Int,
    timeInMillis: Long
) {
    val intent = Intent(context, ReminderReceiver::class.java).apply {
        putExtra("task", displayText)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    if (canScheduleExactAlarm(context)) {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
        Toast.makeText(context, "Alarm scheduled!", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Could not schedule alarm!", Toast.LENGTH_SHORT).show()
    }
}