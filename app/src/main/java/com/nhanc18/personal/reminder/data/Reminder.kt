package com.nhanc18.personal.reminder.data

data class Reminder(
    val hour: Int,
    val minute: Int,
    val task: String, // "Drink 250ml" hoặc "Go for a walk"
    val requestCode: Int // Mã duy nhất để phân biệt từng alarm
)

val reminderList = listOf(
    Reminder(7, 0, "Drink 250ml", 1),
    Reminder(7, 20, "Drink 200ml", 2),
    Reminder(8, 45, "Drink 200ml", 3),
    Reminder(10, 0, "Drink 200ml", 4),
    Reminder(11, 30, "Drink 200ml", 5),
    Reminder(12, 45, "Drink 200ml", 6),
    Reminder(13, 30, "Drink 150ml", 7),
    Reminder(15, 0, "Drink 200ml", 8),
    Reminder(16, 30, "Drink 200ml", 9),
    Reminder(18, 30, "Drink 200ml", 10),
    Reminder(20, 40, "Drink 200ml", 11),
    Reminder(21, 0, "Go for a walk", 12),
    Reminder(22, 0, "Drink 200ml", 13),
    Reminder(22, 10, "Study", 14),
    Reminder(23, 30, "Drink 150ml", 15),
)
