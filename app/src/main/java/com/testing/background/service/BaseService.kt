package com.testing.background.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder

abstract class BaseService(private val id: Int): Service(){

    override fun onCreate() {
        super.onCreate()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            setNotification()
            startForeground(id, setNotification())
        }
    }

    abstract fun setNotification(): Notification

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}