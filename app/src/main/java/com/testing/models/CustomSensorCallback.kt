package com.testing.models

interface CustomSensorCallback {
    fun onOrientationChanged(radians: Float)
    fun onAccelerationChanged(acceleration: Float){}
}