package com.testing.models

import android.location.Location

interface CustomLocationCallback {
    fun onLocationReceived(location: Location)
}