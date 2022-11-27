package com.example.bequiet

import android.app.NotificationManager
import android.app.Service
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class LocationService: Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private var counter = 0


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
//        val wakeLock: PowerManager.WakeLock =
//            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
//                newWakeLock(PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyApp::MyWakelockTag").apply {
//                    acquire()
//                }
//            }

        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {

        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

//        val db = DBHelper(this, null)
//        var places: ArrayList<Place> = db.listPlaces()

        locationClient.getLocationUpdates(100L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                counter++
                val lat = location.latitude.toString()
                val long = location.longitude.toString()

//                val locationToCompare: Location = Location(LocationManager.GPS_PROVIDER)

//                locationToCompare.latitude = places[db.listPlaces().size - 1].x.toDouble()
//                locationToCompare.longitude = places[db.listPlaces().size - 1].y.toDouble()
//
//                val distanceTo: Float = location.distanceTo(locationToCompare)

//                Location: ($lat, $long), \nCounter: $counter\n
                val updatedNotfication = notification.setContentText(
                    """Location: ($lat, $long),Counter: $counter"""
                )
                notificationManager.notify(1, updatedNotfication.build())
                Log.d(ContentValues.TAG, "triggered changed")
            }
            .launchIn(serviceScope)


        startForeground(1, notification.build())
    }

    private fun stop() {
//        val wakeLock: PowerManager.WakeLock =
//            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
//                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag").apply {
//                    release()
//                }
//            }
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}