package com.nhanc18.personal.reminder.services

import android.Manifest
import android.os.Build

object NotificationService {
    fun getPermission(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return Manifest.permission.POST_NOTIFICATIONS
        }
        return ""
    }
}