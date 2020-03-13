package com.testing.background.utils.notifications

import android.app.*
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.testing.background.models.NotificationData
import com.testing.R
import com.testingbackground.utils.notifications.models.createForegroundNotificationChannel

class NotificationHelper(private val context: Context){

    @RequiresApi(Build.VERSION_CODES.O)
    var notificationChannel: NotificationChannel? = null

    private var notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun sendNotification(notification: NotificationData, pendingIntent: PendingIntent) {
        notificationManager.notify(notification.id, buildNotification(notification, pendingIntent))
    }

    fun buildNotification(notification: NotificationData, pendingIntent: PendingIntent): Notification =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel: NotificationChannel = notificationChannel?: createForegroundNotificationChannel(context.packageName, context.getString(
                R.string.app_name))

            notificationManager.createNotificationChannel(channel)

            NotificationCompat.Builder(context, channel.id).apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setContentTitle(notification.title)
                        .setContentText(notification.message)
                        .setAutoCancel(notification.autoCancel)
                        .setSound(notification.ringTone)
                        .setContentIntent(pendingIntent)
            }.build()
        }
        else{
            NotificationCompat.Builder(context).apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setContentTitle(notification.title)
                    .setContentText(notification.message)
                    .setAutoCancel(notification.autoCancel)
                    .setSound(notification.ringTone)
                    .setContentIntent(pendingIntent)
            }.build()

        }

}