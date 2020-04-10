package com.testing.controllers

import android.content.Context
import android.os.Looper
import android.util.Log
import com.testing.models.FASTEST_INTERVAL
import com.testing.models.CustomLocationCallback
import com.testing.models.UPDATE_INTERVAL
import com.google.android.gms.location.*

class LocationController constructor(context: Context){

    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    private var locationRequest: LocationRequest = LocationRequest().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = UPDATE_INTERVAL
        fastestInterval = FASTEST_INTERVAL
    }

    var serviceLocationCallback: CustomLocationCallback? = null

    private val locationCallback = object: LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            locationResult?.lastLocation?.let { unwrappedLocation ->
                Log.i(this::class.java.name, unwrappedLocation.toString())
                serviceLocationCallback?.onLocationReceived(unwrappedLocation)
            }
        }
    }

    fun requestLocationUpdates(serviceLocationCallback: CustomLocationCallback){
        this.serviceLocationCallback = serviceLocationCallback
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    fun removeLocationUpdates(){
        this.serviceLocationCallback = null
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}