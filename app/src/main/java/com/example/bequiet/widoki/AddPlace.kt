package com.example.bequiet.widoki

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bequiet.db.DBHelper
import com.example.bequiet.databinding.ActivityAddPlaceBinding
import com.example.bequiet.permissions.isServiceRunning
import com.example.bequiet.locationService.LocationService

class AddPlace : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlaceBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAddPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras: Bundle? = intent.extras

        val name = binding.Name
        val volume = binding.seekBar2
        val range = binding.range
        val addBtn = binding.add
        val setPlace = binding.place

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        volume.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        volume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.textView.text = "Stopień głośności: ${progress}"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }
        })

        var x = 0.0
        var y = 0.0

        if(extras != null) {
            addBtn.text = "Edytuj miejsce"
            name.setText(extras.getString("name"))
            range.setText(extras.getInt("range").toString())
            volume.progress = extras.getInt("volume")
            x = extras.getDouble("x")
            y = extras.getDouble("y")
            val id = extras.getInt("id")

            val resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        // There are no request codes
                        val data: Intent? = result.data
//                        xTv.text = data?.getDoubleExtra("x", 0.0).toString()
                        x = data!!.getDoubleExtra("x", 0.0)
//                        yTv.text = data?.getDoubleExtra("y", 0.0).toString()
                        y = data.getDoubleExtra("y", 0.0)
                    } else {
//                        xTv.text = "wrong"
                    }
                }

            setPlace.setOnClickListener {
                val intent = Intent(this, AddMarker::class.java)

                intent.putExtra("x", extras.getDouble("x"))
                intent.putExtra("y", extras.getDouble("y"))

                resultLauncher.launch(intent)
            }

            name.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
                hasWindowFocus()
            }

            addBtn.setOnClickListener {
                val nameField = name.text.toString()
                val volumeBar = volume.progress
                val rangeField = range.text.toString()

                if (validator(nameField, rangeField, x, y)) {
                    val db = DBHelper(this, null)

                    val rangeToInt: Int = rangeField.toInt()

                    db.editPlace(id, nameField, volumeBar, x, y, rangeToInt)

                    Toast.makeText(this, "$nameField edited in database", Toast.LENGTH_LONG).show()
                    val service = isServiceRunning()
                    val ifRun = service.isMyServiceRunning(LocationService::class.java, this)

                    if(!ifRun  && (db.listPlaces().size) > 0) {
                        Intent(applicationContext, LocationService::class.java).apply {
                            action = LocationService.ACTION_START
                            startService(this)
                        }
                    }

                    val int = Intent(this, ListOfPlaces::class.java)
                    int.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(int)
                    finish()
            }
            }
        } else {
            val resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        // There are no request codes
                        val data: Intent? = result.data
//                        xTv.text = data?.getDoubleExtra("x", 0.0).toString()
                        x = data!!.getDoubleExtra("x", 0.0)
//                        yTv.text = data?.getDoubleExtra("y", 0.0).toString()
                        y = data.getDoubleExtra("y", 0.0)
                    } else {
//                        xTv.text = "wrong"
                    }
                }

            setPlace.setOnClickListener {
                val intent = Intent(this, AddMarker::class.java)
                resultLauncher.launch(intent)
            }

            name.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
                hasWindowFocus()
            }

            addBtn.setOnClickListener {
                val nameField = name.text.toString()
                val volumeBar = volume.progress
                val rangeField = range.text.toString()


                if (validator(nameField, rangeField, x, y)) {
                    val rangeInt: Int = rangeField.toInt()

                    val db = DBHelper(this, null)

                    db.addPlace(nameField, volumeBar, x, y, rangeInt)

                    Toast.makeText(this, "$nameField added to database", Toast.LENGTH_LONG).show()

                    val service = isServiceRunning()
                    val ifRun = service.isMyServiceRunning(LocationService::class.java, this)

                    if(!ifRun && (db.listPlaces().size) > 0) {
                        Intent(applicationContext, LocationService::class.java).apply {
                            action = LocationService.ACTION_START
                            startService(this)
                        }
                    }

                    val int = Intent(this, ListOfPlaces::class.java)
                    int.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(int)
                    finish()
                }
            }
        }
    }

    private fun validator(name: String, range: String, x: Double, y: Double): Boolean {
        if(name.trim().isEmpty()) {
            Toast.makeText(this, "Podaj jakąś nazwę!", Toast.LENGTH_SHORT).show()
            return false
        }
         else if(range.isEmpty() || range.toIntOrNull() == null) {
            Toast.makeText(this, "Podaj jakąś liczbę", Toast.LENGTH_SHORT).show()
            return false
        } else if(x == 0.0 && y == 0.0) {
            Toast.makeText(this, "Podaj jakieś współrzędne", Toast.LENGTH_SHORT).show()
        }
        else {
            return true
        }
        return true
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