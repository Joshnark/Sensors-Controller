package com.testing

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.testing.controllers.LocationController
import com.testing.controllers.SensorController
import com.testing.models.CustomLocationCallback
import com.testing.models.CustomSensorCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CustomLocationCallback, CustomSensorCallback {

    private lateinit var locationController: LocationController
    private lateinit var sensorController: SensorController

    private var orientation: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationController = LocationController(this)
        sensorController = SensorController(this)

        if(hasPermission()){
            startSensorUpdates()
        }
        else{
            requestPermission()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeSensorUpdates()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(hasPermission()){
            startSensorUpdates()
        }
        else{
            Toast.makeText(this, getString(R.string.needs_permissions), Toast.LENGTH_LONG).show()
            requestPermission()
        }
    }

    override fun onLocationReceived(location: Location) {
        Log.e(this::class.java.name, "long: ${location.longitude} - lat: ${location.latitude}")
    }

    override fun onOrientationChanged(radians: Float) {
        orientation = radians
        setCompass(radians)
    }

    private fun requestPermission() = ActivityCompat.requestPermissions(this,
        arrayOf(ACCESS_FINE_LOCATION), 42)

    private fun hasPermission(): Boolean {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.M ||
                checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun startSensorUpdates() {
        locationController.requestLocationUpdates(this)
        sensorController.registerListeners(this, Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_MAGNETIC_FIELD)
    }

    private fun removeSensorUpdates() {
        locationController.removeLocationUpdates()
        sensorController.unRegisterListeners()
    }

    private fun setCompass(orientation: Float){
        compass.rotation = orientation
        Log.e(this::class.java.name, orientation.toString())
    }

}
