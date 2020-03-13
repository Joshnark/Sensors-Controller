package com.testing.background.controllers

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.testing.background.models.CustomSensorCallback

class SensorController(context: Context): SensorEventListener{

    private val sensorManager: SensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
    private var listener: CustomSensorCallback? = null

    fun registerListeners(listener: CustomSensorCallback, vararg sensors: Int){
        for(sensor in sensors){
            sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(sensor),
                SensorManager.SENSOR_DELAY_NORMAL)
        }
        this.listener = listener
    }

    fun unRegisterListeners(){
        sensorManager.unregisterListener(this)
        this.listener = null
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i(this::class.java.name, "level of accuracy: $accuracy")
    }

    var gravity: FloatArray? = null
    var geoMagnetic: FloatArray? = null

    override fun onSensorChanged(event: SensorEvent?) {

        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values
        }

        if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
            geoMagnetic = event.values
        }

        if (gravity != null && geoMagnetic != null) {

            val identityMatrixR = FloatArray(9)
            val rotationMatrixI = FloatArray(9)

            val success: Boolean = SensorManager.getRotationMatrix(identityMatrixR, rotationMatrixI, gravity, geoMagnetic)
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(identityMatrixR, orientation)
                val azimuth = orientation[0]
                val pitch = orientation[1]
                val roll = orientation[2]

                listener?.onOrientationChanged(azimuth)
            }

        }
    }
}