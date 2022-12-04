package com.example.bequiet

import android.app.NotificationManager
import android.app.Service
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.media.AudioManager
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
        val wakeLock: PowerManager.WakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyApp::MyWakelockTag").apply {
                    acquire()
                }
            }
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

//        val db = DBHelper(this, null)
//        var places: ArrayList<Place> = db.listPlaces()

        locationClient.getLocationUpdates(100L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                counter++

                val lat = location.latitude.toString()
                val long = location.longitude.toString()

                val db = DBHelper(this, null)
                var places: ArrayList<Place> = db.listPlaces()

                val locationToCompare: Location = Location(LocationManager.GPS_PROVIDER)

                Log.d(ContentValues.TAG, counter.toString())

                for (place in places) {

                    locationToCompare.latitude = place.x
                    locationToCompare.longitude = place.y

                    var distance = location.distanceTo(locationToCompare)

                    if (location.distanceTo(locationToCompare) <= place.range) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, place.volume, 0)
                        Log.d(ContentValues.TAG, "Place: ${place.name}, distanceTo in meters: ${distance.toString()}, TRUE")
                    } else {
                        Log.d(ContentValues.TAG, "Place: ${place.name}, distanceTo in meters: ${distance.toString()}, FALSE")
                    }
                }

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
        val wakeLock: PowerManager.WakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag").apply {
                    release()
                }
            }

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