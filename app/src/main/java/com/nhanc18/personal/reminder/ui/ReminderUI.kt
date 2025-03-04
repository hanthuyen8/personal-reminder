package com.nhanc18.personal.reminder.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhanc18.personal.reminder.data.Reminder
import com.nhanc18.personal.reminder.data.reminderList
import com.nhanc18.personal.reminder.services.showTestNotification
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderUI(name: String, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(name) })
        }, modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TestItem()
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                items(reminderList) { reminder ->
                    ReminderItem(reminder)
                }
            }
        }
    }
}

@Composable
fun ReminderItem(reminder: Reminder) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    reminder.hour,
                    reminder.minute
                ),
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = reminder.task, fontSize = 16.sp
            )
        }
    }
}

@Composable
fun TestItem(
    context: Context = LocalContext.current,
) {
    Column(
        modifier = Modifier.fillMaxWidth(), // Đảm bảo chiếm toàn bộ chiều rộng
        horizontalAlignment = Alignment.CenterHorizontally // Căn giữa nút
    ) {
        Button(
            onClick = {
                showTestNotification(context)
            }
        ) {
            Text("Test Notification")
        }
    }
}