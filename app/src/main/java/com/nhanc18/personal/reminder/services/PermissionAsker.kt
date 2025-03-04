package com.nhanc18.personal.reminder.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object PermissionAsker {
    suspend fun requestPermission(
        context: Context,
        requestPermissionLauncher: ActivityResultLauncher<String>,
        permission: String
    ) = suspendCoroutine { cont ->
        if (permission != "") {
            val isGranted = ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
            Log.d("nhanc18", "Permission $permission isGranted $isGranted")
            if (!isGranted) {
                Log.d("nhanc18", "Requesting permission $permission")
                requestPermissionLauncher.launch(permission)
                cont.resume(false)
            } else {
                cont.resume(true)
            }
        } else {
            cont.resume(true)
        }
    }

    fun onPermissionResponse(
        activity: Activity,
        permission: String,
        onNeedToAskAgain: () -> Unit,
    ) {
        val isFirstDenied = activity.shouldShowRequestPermissionRationale(permission)
        if (isFirstDenied) { // mới denied 1 lần
            Log.d("nhanc18", "Is first denied, ask again")
            Toast.makeText(
                activity,
                "We need notification permission to remind you!",
                Toast.LENGTH_LONG
            ).show()
            onNeedToAskAgain()
        } else { // chặn vĩnh viễn
            Log.d("nhanc18", "Is really denied, open setting")
            Toast.makeText(
                activity,
                "Please enable notification permission in Settings",
                Toast.LENGTH_LONG
            ).show()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", activity.packageName, null)
            }
            activity.startActivity(intent)
        }
    }
}

