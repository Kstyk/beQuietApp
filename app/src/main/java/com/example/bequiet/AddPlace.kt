package com.example.bequiet

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
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

        val addBtn = binding.place
        var x: Double = 0.0
        var y: Double = 0.0

        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                binding.x.text = data?.getDoubleExtra("x", 0.0).toString()
                x = data!!.getDoubleExtra("x", 0.0)
                binding.y.text = data?.getDoubleExtra("y", 0.0).toString()
                y = data!!.getDoubleExtra("y", 0.0)
            } else {
                binding.x.text = "wrong"
            }
        }

        addBtn.setOnClickListener() {
            val intent = Intent(this, AddMarker::class.java)
            resultLauncher.launch(intent)
        }



        binding.Name.setOnFocusChangeListener(View.OnFocusChangeListener{
            view, b->hasWindowFocus()
        })

        binding.add.setOnClickListener() {
            val name = binding.Name.text.toString()
            val volume = binding.seekBar2.progress

            val db = DBHelper(this, null)

            db.addPlace(name, volume, x, y)

            Toast.makeText(this, name + " added to database", Toast.LENGTH_LONG).show()
        }

    }

}