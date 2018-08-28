package com.treatout.travel.treatoutmobile

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toTouristSpot = findViewById<Button>(R.id.toTouristSpots)
        val toRestaurants = findViewById<Button>(R.id.toRestaurants)

        toTouristSpot.setOnClickListener{
            val intent = Intent(this, PlacesActivity::class.java)
            intent.putExtra("SERVICE", "Tourist Spots")
            startActivity(intent)
        }

        toRestaurants.setOnClickListener{
            val intent = Intent(this, PlacesActivity::class.java)
            intent.putExtra("SERVICE", "Restaurants")
            startActivity(intent)
        }

    }
}