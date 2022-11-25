package com.example.bequiet

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.bequiet.databinding.ActivityCurrentLocationBinding
import com.google.android.gms.maps.model.LatLng

class CurrentLocation : AppCompatActivity(), LocationListener {
    var locationManager: LocationManager? = null
    var lat: LatLng? = null
    var counter: Int = 0
    private lateinit var binding: ActivityCurrentLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrentLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL)
        }
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                requestLocation()
                handler.postDelayed(this, HANDLER_DELAY.toLong())
            }
        }, START_HANDLER_DELAY.toLong())

    }

    override fun onLocationChanged(location: Location) {
        Log.d("mylog", "Got Location: " + location.latitude + ", " + location.longitude)

        binding.loc.text = location.latitude.toString() + ", " + location.longitude.toString() + "\n Aktualizacji: " + counter
        locationManager!!.removeUpdates(this)

        val latLng = LatLng(location.latitude, location.longitude)
        lat = latLng
    }

    private fun requestLocation() {
        if (locationManager == null) locationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager
        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    GPS_TIME_INTERVAL.toLong(), GPS_DISTANCE.toFloat(), this
                )
            }

            var location: Location
            if(locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!! != null) {
                location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!

                if(location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    lat = latLng
                    counter++
                    binding.loc.text = location.latitude.toString() + ", " + location.longitude.toString() + "\n Aktualizacji: " + counter
                }
            }
        }
    }

    fun getCurrentLocation(): LatLng? {
        return lat
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            val handler = Handler()
            handler.postDelayed(object : Runnable {
                override fun run() {
                    requestLocation()
                    handler.postDelayed(this, HANDLER_DELAY.toLong())
                }
            }, START_HANDLER_DELAY.toLong())
        } else {
            finish()
        }
    }

    companion object {
        private const val GPS_TIME_INTERVAL = 1000 * 2 * 1
        private const val GPS_DISTANCE = 1000 // set the distance value in meter
        private const val HANDLER_DELAY = 1000 * 2 * 1
        private const val START_HANDLER_DELAY = 0
        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        const val PERMISSION_ALL = 1
    }

    fun startBackgroundProcessButtonClick(view: View) {
        val intent = Intent(this, myBackgroundProcess::class.java)
        intent.action = "BackgroundProcess"

        //Set the repeated Task

        //Set the repeated Task
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 10, pendingIntent)

        finish()
    }
}