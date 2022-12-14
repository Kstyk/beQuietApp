package com.example.bequiet.permissions

import android.app.ActivityManager
import android.content.Context

class isServiceRunning {
    @Suppress("DEPRECATION")
    fun isMyServiceRunning(serviceClass: Class<*>, context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}