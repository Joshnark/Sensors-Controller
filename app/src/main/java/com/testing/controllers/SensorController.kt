package com.testing.controllers

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.testing.models.CustomSensorCallback

class SensorController constructor(context: Context): SensorEventListener{

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

    var gravity: FloatArray = FloatArray(3)
    var geoMagnetic: FloatArray = FloatArray(3)

    private val identityMatrixR = FloatArray(9)
    private val rotationMatrixI = FloatArray(9)

    private val alpha = 0.97f

    override fun onSensorChanged(event: SensorEvent?) {

        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            gravity[0] = alpha * gravity.get(0) + (1 - alpha) * event.values[0]
            gravity[1] = alpha * gravity.get(1) + (1 - alpha) * event.values[1]
            gravity[2] = alpha * gravity.get(2) + (1 - alpha) * event.values[2]
        }

        if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
            geoMagnetic[0] = alpha * geoMagnetic[0] + (1 - alpha) * event.values[0]
            geoMagnetic[1] = alpha * geoMagnetic[1] + (1 - alpha) * event.values[1]
            geoMagnetic[2] = alpha * geoMagnetic[2] + (1 - alpha) * event.values[2]
        }

        val success: Boolean = SensorManager.getRotationMatrix(
            identityMatrixR,
            rotationMatrixI,
            gravity,
            geoMagnetic
        )

        if (success) {
            val orientation = FloatArray(3)
            SensorManager.getOrientation(identityMatrixR, orientation)
            val azimuth = orientation[0]

            val degrees: Float = (Math.toDegrees(azimuth.toDouble()).toFloat() + 360 ) % 360

            listener?.onOrientationChanged(degrees)
        }
    }
}