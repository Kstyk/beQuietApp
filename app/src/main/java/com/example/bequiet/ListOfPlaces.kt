package com.example.bequiet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bequiet.databinding.ActivityListOfPlacesBinding

class ListOfPlaces : AppCompatActivity() {
    private lateinit var binding: ActivityListOfPlacesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}