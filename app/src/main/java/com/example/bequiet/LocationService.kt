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
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.bequiet.db.DBHelper
import com.example.bequiet.db.Place
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
    private var previousStreamMusicVolume = 0
    private var previousStreamMusicAlarm = 0
    private var previousStreamMusicNotfication = 0
    private var previousStreamMusicDTMF = 0
    private var previousStreamMusicSystem = 0
    private var previousStreamMusicRing = 0
    private var previousStreamMusicAccessbility= 0
    private var n: Boolean = false
    private var workingPlace: Place? = null


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
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        previousStreamMusicVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        previousStreamMusicAccessbility = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        previousStreamMusicDTMF = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        previousStreamMusicRing = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        previousStreamMusicSystem = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        previousStreamMusicNotfication = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        previousStreamMusicAlarm = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        locationClient.getLocationUpdates(100L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                counter++

                val lat = location.latitude.toString()
                val long = location.longitude.toString()

                val db = DBHelper(this, null)
                val places: ArrayList<Place> = db.listPlaces()

                val locationToCompare = Location(LocationManager.GPS_PROVIDER)

                Log.d(ContentValues.TAG, counter.toString())

                for (place in places) {

                    locationToCompare.latitude = place.x
                    locationToCompare.longitude = place.y

                    val distance = location.distanceTo(locationToCompare)

                    if (location.distanceTo(locationToCompare) <= place.range) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, place.volume, 0)
                        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, place.volume, 0)
                        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, place.volume, 0)
                        audioManager.setStreamVolume(AudioManager.STREAM_RING, place.volume, 0)
                        audioManager.setStreamVolume(AudioManager.STREAM_DTMF, place.volume, 0)
                        audioManager.setStreamVolume(AudioManager.STREAM_ACCESSIBILITY, place.volume, 0)
                        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, place.volume, 0)

                        workingPlace = place
                        n = true

                        Log.d(ContentValues.TAG, "Place: ${place.name}, distanceTo in meters: ${distance}, TRUE")
                        break
                    } else {
                        Log.d(ContentValues.TAG, "Place: ${place.name}, distanceTo in meters: ${distance}, FALSE")

                        n = false
                    }
                }

                if (workingPlace != null && !n) {
                    workingPlace = null

                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, previousStreamMusicVolume, 0)
                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM, previousStreamMusicAlarm, 0)
                    audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, previousStreamMusicNotfication, 0)
                    audioManager.setStreamVolume(AudioManager.STREAM_DTMF, previousStreamMusicDTMF, 0)
                    audioManager.setStreamVolume(AudioManager.STREAM_ACCESSIBILITY, previousStreamMusicAccessbility, 0)
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, previousStreamMusicRing, 0)
                    audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, previousStreamMusicSystem, 0)
                }

                val updatedNotfication = notification.setContentText(
                    """Location: ($lat, $long),Counter: $counter"""
                )
                notificationManager.notify(1, updatedNotfication.build())
//                Log.d(ContentValues.TAG, "triggered changed")
            }
            .launchIn(serviceScope)


        startForeground(1, notification.build())
    }

    private fun stop() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, previousStreamMusicVolume, 0)
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, previousStreamMusicAlarm, 0)
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, previousStreamMusicNotfication, 0)
        audioManager.setStreamVolume(AudioManager.STREAM_DTMF, previousStreamMusicDTMF, 0)
        audioManager.setStreamVolume(AudioManager.STREAM_ACCESSIBILITY, previousStreamMusicAccessbility, 0)
        audioManager.setStreamVolume(AudioManager.STREAM_RING, previousStreamMusicRing, 0)
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, previousStreamMusicSystem, 0)

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