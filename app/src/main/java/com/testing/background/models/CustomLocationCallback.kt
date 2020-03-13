package com.testing.background.models

import android.location.Location

interface CustomLocationCallback {
    fun onLocationReceived(location: Location)
}