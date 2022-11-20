package com.example.bequiet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bequiet.databinding.ActivityAddMarkerBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


// [START_EXCLUDE silent]
//import com.google.maps.example.R
// [END_EXCLUDE]

internal class AddMarker : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityAddMarkerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMarkerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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

        mMap.setOnMapClickListener {
                point ->
            if(mMarkerArray.size == 0) {
                val marker = MarkerOptions().position(LatLng(point.latitude, point.longitude))
                    .title("New Marker").draggable(true)
                latitude = marker.position.latitude
                longtitude = marker.position.longitude
                mMarkerArray.add(marker)
                mMap.addMarker(mMarkerArray[0])
//                binding.counter.text = point.latitude.toString() + " " + point.longitude.toString()
            } else {
                latitude = 0.0
                longtitude = 0.0
                mMap.clear()
                mMarkerArray.removeAll(mMarkerArray)
//                binding.counter.text =""
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
