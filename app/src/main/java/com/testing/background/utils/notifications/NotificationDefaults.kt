package com.testingbackground.utils.notifications.models

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi

val DEFAULT_VIBRATION_PATTERN = longArrayOf(100, 200, 300, 200, 100)

@RequiresApi(Build.VERSION_CODES.O)
fun createForegroundNotificationChannel(id: String, title: String) = NotificationChannel(id,
        title, NotificationManager.IMPORTANCE_LOW).apply {
        enableLights(false)
        enableVibration(false)
        lockscreenVisibility = Notification.VISIBILITY_PRIVATE
}
