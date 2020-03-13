package com.testing.background.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.testing.MainActivity
import com.testing.background.utils.notifications.NotificationHelper
import com.testingbackground.utils.notifications.models.createForegroundNotificationChannel
import com.testing.background.controllers.LocationController
import com.testing.R
import com.testing.background.controllers.SensorController
import com.testing.background.models.*

class LocationForegroundService: BaseService(1), CustomLocationCallback, CustomSensorCallback{

    private val notificationHelper: NotificationHelper = NotificationHelper(this)
    private val locationController: LocationController = LocationController(this)
    private val sensorController: SensorController = SensorController(this)

    var orientation: Float? = null

    override fun onLocationReceived(location: Location) {
        orientation?.let{
            Log.d(this::class.java.name + "orientation", orientation.toString())
        }
        Log.d(this::class.java.name + "location", location.toString())
    }

    override fun onOrientationChanged(radians: Float) {
        super.onOrientationChanged(radians)
        orientation = radians
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        setLocationUpdates()
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun setLocationUpdates(){
        locationController.requestLocationUpdates(this)
        sensorController.registerListeners(this, Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onDestroy() {
        locationController.removeLocationUpdates()
        sensorController.unRegisterListeners()
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setNotification(): Notification {
        notificationHelper.notificationChannel = createForegroundNotificationChannel(
            resources.getString(R.string.location_notification_channel_id),
            resources.getString(R.string.location_notification_channel_name)
        )

        val notificationData: NotificationData = NotificationData().apply {
            id = LOCATION_NOTIFICATION_ID
            title = LOCATION_NOTIFICATION_TITLE
            message = LOCATION_NOTIFICATION_MESAGE
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            LOCATION_NOTIFICATION_ID,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        return notificationHelper.buildNotification(notificationData, pendingIntent)
    }

    companion object{

        fun startLocationService(context: Context){
            Intent(context, LocationForegroundService::class.java).run {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                    context.startForegroundService(this)
                } else {
                    context.startService(this)
                }
            }
        }

        fun stopService(context: Context){
            context.stopService(Intent(context, LocationForegroundService::class.java))
        }

    }

}