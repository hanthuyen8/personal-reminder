package com.nhanc18.personal.reminder

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.nhanc18.personal.reminder.data.reminderList
import com.nhanc18.personal.reminder.services.AlarmService
import com.nhanc18.personal.reminder.services.NotificationService
import com.nhanc18.personal.reminder.services.PermissionAsker
import com.nhanc18.personal.reminder.services.scheduleReminders
import com.nhanc18.personal.reminder.ui.ReminderUI
import com.nhanc18.personal.reminder.ui.theme.PersonalReminderTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val _allPermissions =
        listOf(NotificationService.getPermission(), AlarmService.getPermission())

    private val _requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            _allPermissions.forEach {
                PermissionAsker.onPermissionResponse(this, it) {
                    askPermission(it)
                }
            }
        }
        Log.d("nhanc18", "Permission $isGranted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PersonalReminderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ReminderUI(
                        name = "Personal Reminder",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        confirmPermissionGranted(_allPermissions) {
            Log.d("nhanc18", "All permissions granted")
            scheduleReminders(this, reminderList)
        }
    }

    private fun confirmPermissionGranted(permission: List<String>, onGranted: () -> Unit) {
        val context = this
        lifecycleScope.launch {
            var state = true
            permission.forEach {
                if (state) {
                    state = PermissionAsker.requestPermission(
                        context,
                        context._requestPermissionLauncher,
                        it
                    )
                }
            }
            if (state) {
                onGranted()
            }
        }
    }

    private fun askPermission(permission: String) {
        val context = this
        lifecycleScope.launch {
            PermissionAsker.requestPermission(
                context,
                context._requestPermissionLauncher,
                permission
            )
        }
    }
}