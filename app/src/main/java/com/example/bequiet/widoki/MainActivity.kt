package com.example.bequiet.widoki

import com.example.bequiet.permissions.CheckLocationPermissions
import android.app.ActivityManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bequiet.databinding.ActivityMainBinding
import com.example.bequiet.db.DBHelper
import com.example.bequiet.locationService.LocationService
import com.example.bequiet.permissions.AlertDialogDontDisurb
import com.example.bequiet.permissions.CheckConnection
import com.example.bequiet.permissions.CheckLocation


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // permissions i inne
        val checkLocationPermissions = CheckLocationPermissions()
        checkLocationPermissions.statusCheck(this)

        val checkConnection = CheckConnection()
        if(!checkConnection.isNetworkConnected(this)) {
            checkConnection.showAlertDialogButtonClicked(this)
        }

        val n = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (!n.isNotificationPolicyAccessGranted) {
            val alert = AlertDialogDontDisurb().showAlertDialogButtonClicked(this)
        }

        val checkLocation = CheckLocation()
        checkLocation.statusCheck(this)
        // end of checking permissions

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val addBtn = binding.addPlace
        val showBtn = binding.showPlaces

        addBtn.setOnClickListener {
            val myIntent = Intent(this, AddPlace::class.java)
            startActivity(myIntent)
        }

        showBtn.setOnClickListener {
            val myIntent = Intent(this, ListOfPlaces::class.java)
            startActivity(myIntent)
        }

        val db = DBHelper(this, null)
        if(!isMyServiceRunning(LocationService::class.java) && (db.listPlaces().size) > 0) {
            Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                startService(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val db = DBHelper(this, null)
        if(isMyServiceRunning(LocationService::class.java) && (db.listPlaces().size) == 0) {
            Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_STOP
                startService(this)
            }
        }

        if(!isMyServiceRunning(LocationService::class.java) && (db.listPlaces().size) > 0) {
            Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                startService(this)
            }
        }
    }

    override fun onRestart() {
        super.onRestart()

        val db = DBHelper(this, null)
        if(isMyServiceRunning(LocationService::class.java) && (db.listPlaces().size) == 0) {
            Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_STOP
                startService(this)
            }
        }

        if(!isMyServiceRunning(LocationService::class.java) && (db.listPlaces().size) > 0) {
            Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                startService(this)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

}