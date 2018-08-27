package com.treatout.travel.treatoutmobile

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.google.gson.Gson
import com.treatout.travel.treatoutmobile.Adapters.PlacesRecAdapter
import com.treatout.travel.treatoutmobile.Classes.Place
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class RestaurantActivity : AppCompatActivity() {

    val placeList: ArrayList<Place> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val title:String = intent.getStringExtra("SERVICE")
        setTitle(title)

        val recView = findViewById<RecyclerView>(R.id.placeList)

        if( title == "Restaurants"){
            addRestaurants()
        }else{
            addTouristSpots()
        }

        recView.layoutManager = LinearLayoutManager(this)
        recView.adapter = PlacesRecAdapter(this,placeList)

        recView.setOnClickListener{
            Toast.makeText(this,"Test",Toast.LENGTH_SHORT)
        }

        fetchJSON( this)

    }

    fun fetchJSON ( context: Context) {

        val url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=restaurants+in+bacolod&key=AIzaSyDWJ95wDORvWwB6B8kNzSNDfVSOeQc8W7k"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue( object: Callback{

            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                Log.d("MONITOR", body)
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed Request")
            }

        })

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    fun addTouristSpots() {
        placeList.add( Place("NEGROS FORESTS AND ECOLOGICAL FOUNDATION,INC.","S Capitol Rd, Bacolod, 6100 Negros Occidental, Philippines",3.0))
        placeList.add( Place("THE RUINS","Talisay City, Negros Occidental, Philippines",4.0))
        placeList.add( Place("BACOLOD BAYWALK","Bacolod, 6100 Negros Occidental, Philippines",3.0))
        placeList.add( Place("BANTUG LAKE RANCH","Granada Alangilan Road, Bacolod, Negros Occidental, Philippines",3.0))
        placeList.add( Place("BALAY NI TANA DICANG","Enrique Lizares St, Talisay City, Negros Occidental, Philippines",4.0))
        placeList.add( Place("BANTUG LAKE RANCH","Granada Alangilan Road, Bacolod, Negros Occidental, Philippines",4.0))
        placeList.add( Place("NEGROS FORESTS AND ECOLOGICAL FOUNDATION,INC.","S Capitol Rd, Bacolod, 6100 Negros Occidental, Philippines",3.0))
        placeList.add( Place("THE RUINS","Talisay City, Negros Occidental, Philippines",4.0))
        placeList.add( Place("BACOLOD BAYWALK","Bacolod, 6100 Negros Occidental, Philippines",3.0))
        placeList.add( Place("BANTUG LAKE RANCH","Granada Alangilan Road, Bacolod, Negros Occidental, Philippines",3.0))
        placeList.add( Place("BALAY NI TANA DICANG","Enrique Lizares St, Talisay City, Negros Occidental, Philippines",4.0))
        placeList.add( Place("BANTUG LAKE RANCH","Granada Alangilan Road, Bacolod, Negros Occidental, Philippines",4.0))
    }

    fun addRestaurants() {
        placeList.add( Place("BODEGA CAFÉ & GRILL","Buri Road, Mandalagan, Bacolod, 6100 Negros Occidental, Philippines",4.0))
        placeList.add( Place("DELICIOSO","Lacson St, Bacolod, 6100 Negros Occidental, Philippines",4.0))
        placeList.add( Place("21 Restaurant","21 Lacson St, Bacolod, 6100 Negros Occidental, Philippines",3.0))
        placeList.add( Place("Fogo","Lacson St, Bacolod, Negros Occidental, Philippines",3.0))
        placeList.add( Place("RIPPLES","Ground Floor, L'Fisher Hotel, 14th, Lacson St, Bacolod, 6100 Negros Occidental, Philippines\n",4.0))
        placeList.add( Place("SHABU NIKU KOREAN RESTAURANT","12th St, Bacolod, 6100 Negros Occidental, Philippines",4.0))
        placeList.add( Place("BODEGA CAFÉ & GRILL","Buri Road, Mandalagan, Bacolod, 6100 Negros Occidental, Philippines",4.0))
        placeList.add( Place("DELICIOSO","Lacson St, Bacolod, 6100 Negros Occidental, Philippines",4.0))
        placeList.add( Place("21 Restaurant","21 Lacson St, Bacolod, 6100 Negros Occidental, Philippines",3.0))
        placeList.add( Place("Fogo","Lacson St, Bacolod, Negros Occidental, Philippines",3.0))
        placeList.add( Place("RIPPLES","Ground Floor, L'Fisher Hotel, 14th, Lacson St, Bacolod, 6100 Negros Occidental, Philippines\n",4.0))
        placeList.add( Place("SHABU NIKU KOREAN RESTAURANT","12th St, Bacolod, 6100 Negros Occidental, Philippines",4.0))
    }
}
