package com.example.bequiet

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bequiet.databinding.ActivityAddPlaceBinding

class AddPlace : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAddPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras: Bundle? = intent.extras

        var name = binding.Name
        var volume = binding.seekBar2
        var range = binding.range
        var addBtn = binding.add
        var setPlace = binding.place
//        var xTv = binding.x
//        var yTv = binding.y

        var id = 0

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        volume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC))

        var x: Double = 0.0
        var y: Double = 0.0

        if(extras != null) {
            addBtn.setText("Edytuj miejsce")
            name.setText(extras.getString("name"))
            range.setText(extras.getInt("range").toString())
            volume.progress = extras.getInt("volume")
            id = extras.getInt("id")

            var resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        // There are no request codes
                        val data: Intent? = result.data
//                        xTv.text = data?.getDoubleExtra("x", 0.0).toString()
                        x = data!!.getDoubleExtra("x", 0.0)
//                        yTv.text = data?.getDoubleExtra("y", 0.0).toString()
                        y = data!!.getDoubleExtra("y", 0.0)
                    } else {
//                        xTv.text = "wrong"
                    }
                }

            setPlace.setOnClickListener() {
                val intent = Intent(this, AddMarker::class.java)

                intent.putExtra("x", extras.getDouble("x"))
                intent.putExtra("y", extras.getDouble("y"))

                resultLauncher.launch(intent)
            }

            name.setOnFocusChangeListener(View.OnFocusChangeListener { view, b ->
                hasWindowFocus()
            })

            addBtn.setOnClickListener() {
                var name = name.text.toString()
                var volume = volume.progress
                var range = range.text.toString().toInt()

                val db = DBHelper(this, null)

                db.editPlace(id, name, volume, x, y, range)

                Toast.makeText(this, name + " edited in database", Toast.LENGTH_LONG).show()

                val int: Intent = Intent(this, ListOfPlaces::class.java)
                int.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(int)
                finish()
            }
        } else {
            var resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        // There are no request codes
                        val data: Intent? = result.data
//                        xTv.text = data?.getDoubleExtra("x", 0.0).toString()
                        x = data!!.getDoubleExtra("x", 0.0)
//                        yTv.text = data?.getDoubleExtra("y", 0.0).toString()
                        y = data!!.getDoubleExtra("y", 0.0)
                    } else {
//                        xTv.text = "wrong"
                    }
                }

            setPlace.setOnClickListener() {
                val intent = Intent(this, AddMarker::class.java)
                resultLauncher.launch(intent)
            }

            name.setOnFocusChangeListener(View.OnFocusChangeListener { view, b ->
                hasWindowFocus()
            })

            addBtn.setOnClickListener() {
                var name = name.text.toString()
                var volume = volume.progress
                var range = range.text.toString().toInt()

                val db = DBHelper(this, null)

                db.addPlace(name, volume, x, y, range)

                Toast.makeText(this, name + " added to database", Toast.LENGTH_LONG).show()

                val int: Intent = Intent(this, ListOfPlaces::class.java)
                int.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(int)
                finish()
            }
        }
    }

}