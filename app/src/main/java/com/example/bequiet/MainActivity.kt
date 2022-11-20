package com.example.bequiet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bequiet.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val addBtn = binding.addPlace
        val showBtn = binding.showPlaces

        addBtn.setOnClickListener() {
            val myIntent = Intent(this, AddPlace::class.java)
            startActivity(myIntent)
        }

        showBtn.setOnClickListener() {
            val myIntent = Intent(this, ListOfPlaces::class.java)
            startActivity(myIntent)
        }
    }
}