package com.testing

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.testing.background.controllers.LocationController
import com.testing.background.controllers.SensorController
import com.testing.background.models.CustomLocationCallback
import com.testing.background.models.CustomSensorCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CustomLocationCallback, CustomSensorCallback {

    private lateinit var locationController: LocationController
    private lateinit var sensorController: SensorController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationController = LocationController(this)
        sensorController = SensorController(this)


        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if(!(checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
                ActivityCompat.requestPermissions(this,
                    arrayOf(ACCESS_FINE_LOCATION), 42)
            }
        }
    }

    override fun onResume(){
        super.onResume()
        setLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        removeLocationUpdates()
    }

    private fun setLocationUpdates(){
        locationController.requestLocationUpdates(this)
        sensorController.registerListeners(this, Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_MAGNETIC_FIELD)
    }

    private fun removeLocationUpdates() {
        locationController.removeLocationUpdates()
        sensorController.unRegisterListeners()
    }

    var orientation: Float? = null

    private fun log(vararg text: Any){

        var source = String()

        text.forEach {
            Log.d(this::class.java.name, it.toString())
            source += " $it"
        }

        logText.text = source
    }

    private fun setCompass(orientation: Float){
        val degrees: Double = (Math.toDegrees( orientation.toDouble() ) + 360 ) % 360
        compass.rotation = degrees.toFloat()
        Log.e(this::class.java.name, degrees.toString())
    }

    override fun onLocationReceived(location: Location) {
        this.log(location.longitude, location.latitude)

    }

    override fun onOrientationChanged(radians: Float) {
        super.onOrientationChanged(radians)
        orientation = radians
        setCompass(radians)
    }

}
