package com.nhanc18.personal.reminder.services

import android.content.Context
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.widget.Toast
import android.provider.Settings
import com.nhanc18.personal.reminder.data.Reminder
import java.util.Calendar

fun scheduleReminders(context: Context, reminderList: List<Reminder>) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val calendar = Calendar.getInstance()

    reminderList.forEach {
        schedule(context, alarmManager, calendar, it)
    }
}

fun showTestNotification(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, ReminderReceiver::class.java).apply {
        putExtra("task", "Button Test!")
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        998,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    if (canScheduleExactAlarm(context, alarmManager)) {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 3000, // 3 giây sau khi nhấn
            pendingIntent
        )
        Toast.makeText(context, "Alarm scheduled!", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Cannot schedule exact alarm", Toast.LENGTH_SHORT).show()
    }
}

private fun canScheduleExactAlarm(
    context: Context,
    alarmManager: AlarmManager
): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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

private fun schedule(
    context: Context,
    alarmManager: AlarmManager,
    calendar: Calendar,
    reminder: Reminder
) {
    calendar.set(Calendar.HOUR_OF_DAY, reminder.hour)
    calendar.set(Calendar.MINUTE, reminder.minute)
    calendar.set(Calendar.SECOND, 0)

    // Nếu thời gian đã qua trong ngày, đặt cho ngày hôm sau
    if (calendar.timeInMillis < System.currentTimeMillis()) {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    val intent = Intent(context, ReminderReceiver::class.java).apply {
        putExtra("task", reminder.task)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        reminder.requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY, // Lặp lại mỗi ngày
        pendingIntent
    )
}