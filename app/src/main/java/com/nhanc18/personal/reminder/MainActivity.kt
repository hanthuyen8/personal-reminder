package com.nhanc18.personal.reminder

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nhanc18.personal.reminder.data.reminderList
import com.nhanc18.personal.reminder.services.requestPermission
import com.nhanc18.personal.reminder.services.scheduleReminders
import com.nhanc18.personal.reminder.ui.ReminderUI
import com.nhanc18.personal.reminder.ui.theme.PersonalReminderTheme

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
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
        requestPermission(this, this.requestPermissionLauncher)
        scheduleReminders(this, reminderList)
    }
}