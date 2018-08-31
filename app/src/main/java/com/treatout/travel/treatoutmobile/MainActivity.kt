package com.treatout.travel.treatoutmobile

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toTouristSpot = findViewById<ImageButton>(R.id.toTouristSpots)
        val toRestaurants = findViewById<ImageButton>(R.id.toRestaurants)
        val toLogout = findViewById<ImageButton>(R.id.toLogout)
        val search = findViewById<SearchView>(R.id.search)
        val btnSearch = findViewById<Button>(R.id.btnSearch)

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
        val editname = findViewById<EditText>(R.id.edtname)
        val editmin = findViewById<EditText>(R.id.edtmin)
        val editmax = findViewById<EditText>(R.id.edtmax)
        btnSearch.setOnClickListener{

            if( editmin.text.isEmpty() || editmax.text.isEmpty() ){
                val intent = Intent(this, PlacesActivity::class.java)
                println(edtmin.text.toString())
                intent.putExtra("QUERY", true)
                intent.putExtra("MIN", "")
                intent.putExtra("MAX", "")
                intent.putExtra("NAME", edtname.text.toString())
                startActivity(intent)
            }else{
                if( editmin.text.toString().toLong() > editmax.text.toString().toLong()){
                    Toast.makeText(this,"Min must be higher", Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(this, PlacesActivity::class.java)
                    println(edtmin.text.toString())
                    intent.putExtra("QUERY", true)
                    intent.putExtra("MIN", edtmin.text.toString())
                    intent.putExtra("MAX", edtmax.text.toString())
                    intent.putExtra("NAME", edtname.text.toString())
                    startActivity(intent)
                }
            }
        }

    }
}