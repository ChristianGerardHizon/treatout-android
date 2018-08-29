package com.treatout.travel.treatoutmobile

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toTouristSpot = findViewById<Button>(R.id.toTouristSpots)
        val toRestaurants = findViewById<Button>(R.id.toRestaurants)
        val toLogout = findViewById<Button>(R.id.btnLogout)
        val search = findViewById<SearchView>(R.id.search)

        val sharedPreferences = getSharedPreferences("ACCOUNTS", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()


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

        toLogout.setOnClickListener{
            editor.remove("STATUS")
            editor.apply()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        search.setOnSearchClickListener{
            Toast.makeText(this, search.query , Toast.LENGTH_SHORT)
        }

    }
}