package com.example.bequiet

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
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

        binding.location.setOnClickListener() {
            val myIntent = Intent(this, CurrentLocation::class.java)
            startActivity(myIntent)
        }



//        val db = DBHelper(this, null)
//        Toast.makeText(applicationContext, db.listPlaces().toString(), Toast.LENGTH_LONG).show()
    }

//    fun startBackgroundProcessButtonClick(view: View) {
//        val intent = Intent(this, myBackgroundProcess::class.java)
//        intent.action = "BackgroundProcess"
//
//        //Set the repeated Task
//
//        //Set the repeated Task
//        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
//        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 10, pendingIntent)
//
//        finish()
//    }
}