package com.example.bequiet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bequiet.databinding.ActivityListOfPlacesBinding

class ListOfPlaces : AppCompatActivity() {
    private lateinit var binding: ActivityListOfPlacesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper(this, null)

        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = MyAdapter(db.listPlaces().size)
    }
}