package com.testing.background.models

interface CustomSensorCallback {
    fun onOrientationChanged(radians: Float){

    }

    fun onAccelerationChanged(acceleration: Float){

    }
}