package com.example.bequiet

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.bequiet.databinding.ActivityAddMarkerBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

// [START_EXCLUDE silent]
//import com.google.maps.example.R
// [END_EXCLUDE]

internal class AddMarker : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityAddMarkerBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddMarkerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val mMarkerArray: MutableList<MarkerOptions> = ArrayList()

        var latitude: Double = 0.0
        var longtitude: Double = 0.0



        mMap.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}
            override fun onMarkerDrag(marker: Marker) {}
            override fun onMarkerDragEnd(marker: Marker) {
                latitude = marker.position.latitude
                longtitude = marker.position.longitude
                mMarkerArray[0].position(LatLng(latitude, longtitude))
//                binding.counter.text = mMarkerArray[0].position.latitude.toString() + " " + mMarkerArray[0].position.longitude.toString()
            }
        })

        val extras: Bundle? = intent.extras

        if(extras != null) {
            Toast.makeText(this, "Extras przekazane x: ${extras.getDouble("x")}, y: ${extras.getDouble("y")}", Toast.LENGTH_LONG).show()
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val locationToCompare: Location = Location(LocationManager.GPS_PROVIDER)

                locationToCompare.latitude = extras.getDouble("x")
                locationToCompare.longitude = extras.getDouble("y")

                val latlng: LatLng = LatLng(locationToCompare.latitude, locationToCompare.longitude)

                latitude = locationToCompare.latitude
                longtitude = locationToCompare.longitude

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 11F))

                val cameraPos: CameraPosition = CameraPosition.Builder()
                    .target(latlng)
                    .zoom(11F)
                    .bearing(90F)
                    .build()

                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPos))

                val marker = MarkerOptions().position(latlng)
                    .title("New Marker").draggable(true)

                mMarkerArray.add(marker)
                mMap.addMarker(mMarkerArray[0])
            }
        } else {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latlng: LatLng = LatLng(location.latitude, location.longitude)

                    latitude = location.latitude
                    longtitude = location.longitude

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 11F))

                    val cameraPos: CameraPosition = CameraPosition.Builder()
                        .target(latlng)
                        .zoom(11F)
                        .bearing(90F)
                        .build()

                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPos))

                    val marker = MarkerOptions().position(latlng)
                        .title("New Marker").draggable(true)

                    mMarkerArray.add(marker)
                    mMap.addMarker(mMarkerArray[0])
                } else {
                    Log.d(ContentValues.TAG, "no location")
                }
            }
        }
    }

        mMap.setOnMapClickListener {
                point ->
            if(mMarkerArray.size == 0) {
                val marker = MarkerOptions().position(LatLng(point.latitude, point.longitude))
                    .title("New Marker").draggable(true)
                latitude = marker.position.latitude
                longtitude = marker.position.longitude
                mMarkerArray.add(marker)
                mMap.addMarker(mMarkerArray[0])
            } else {
                latitude = 0.0
                longtitude = 0.0
                mMap.clear()
                mMarkerArray.removeAll(mMarkerArray)
            }
        }


        binding.confirm.setOnClickListener() {
            val returnIntent = Intent().apply {
                putExtra("x", latitude)
                putExtra("y", longtitude)
            }
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

    }
}
