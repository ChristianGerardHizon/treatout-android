package com.treatout.travel.treatoutmobile

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.treatout.travel.treatoutmobile.Adapters.PlacesRecAdapter
import com.treatout.travel.treatoutmobile.Classes.Place
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import org.json.JSONObject
import java.net.URLEncoder


class PlacesActivity : AppCompatActivity() {

    val placeList: ArrayList<Place> = ArrayList()
    private lateinit var adapter: PlacesRecAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val progress = findViewById<ConstraintLayout>(R.id.progress)
        val recView = findViewById<RecyclerView>(R.id.placeList)
        recView.layoutManager = LinearLayoutManager(this)
        adapter = PlacesRecAdapter(this,placeList)
        recView.adapter = adapter


        if( intent.hasExtra("QUERY") ){
            setTitle("Search")
            val query = intent.getBooleanExtra("QUERY", false)
            var min:String
            var max:String
            var name:String

            if( intent.hasExtra("MIN")){
                min = intent.getStringExtra("MIN")
            }else{
                min = ""
            }

            if( intent.hasExtra("MAX")){
                max = intent.getStringExtra("MAX")
            }else{
                max = ""
            }

            if(intent.hasExtra("NAME")){
                name = URLEncoder.encode(intent.getStringExtra("NAME") , "UTF-8")
                println( name )
            }else{
                name = ""
            }
            println("searchvalue=$name&minrate=$min&maxrate=$max")
            fetchQueryJSON(this, "searchvalue=$name&minrate=$min&maxrate=$max")

        }else{
            val title:String = intent.getStringExtra("SERVICE")
            setTitle(title)
            if( title == "Restaurants"){
                fetchJSON( this, "restaurants+in+bacolod")

            }else{
                fetchJSON( this, "tourist+spots+in+bacolod")
            }
        }


    }

    fun fetchJSON ( context: Context, query:String) {
        runOnUiThread {
            transitionPage(true)
        }
        val url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=$query&key=AIzaSyDWJ95wDORvWwB6B8kNzSNDfVSOeQc8W7k"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue( object: Callback{

            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                if (body != null) {
                    val result = JSONDecoder(body)
                    if (result != null) {
                        val resultArray = JSONArrayToArray( result )
                        for ( res in resultArray){
                            val resObj = JSONObject(res)
                            val formattedAddress = resObj.get("formatted_address").toString()
                            val name = resObj.get("name").toString()
                            val placeId = resObj.get("place_id").toString()
                            val rating = resObj.get("rating")
                            var imageArr: ArrayList<String> = ArrayList()

                            if( resObj.has("photos") ){
                                val photos = resObj.getJSONArray("photos")
                                val photoArr = JSONArrayToArray( photos )

                                for ( photo in photoArr ){
                                    val photoObj = JSONObject(photo)
                                    imageArr.add( photoObj.get("photo_reference").toString() )
                                }
                            }

                            placeList.add( Place( name, formattedAddress, rating as Number, imageArr, placeId))
                            runOnUiThread {
                                adapter.notifyItemInserted(placeList.size)
                            }
                        }
                    }
                }
                runOnUiThread {
                    transitionPage(false)
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                runOnUiThread {
                    transitionPage(false)
                }
                println("Failed Request")
            }

        })

    }

    fun fetchQueryJSON ( context: Context, query:String) {
        runOnUiThread {
            transitionPage(true)
        }
        val url = "https://treatout.000webhostapp.com/modules/api/searchplaces.php?$query"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue( object: Callback{

            override fun onResponse(call: Call?, response: Response?) {
                var body = response?.body()?.string()
                println(body)
                if (body != null && body != "[{\"data\":\"No results found.\"}]") {
                    body = body.drop(1)
                    body = body.dropLast(1)

                    val result = JSONObject(body)
                    if(result.get("data").toString().equals("No results found.")){

                    }else{
                        val arr = JSONArray(result.get("data").toString())
                        val arrList = JSONArrayToArray(arr)
                        val image: ArrayList<String> = ArrayList()
                        for( arr in arrList){
                            val obj = JSONObject(arr)
                            placeList.add( Place( obj.getString("name"), "Bacolod City",  5 as Number, image, obj.getString("place_id")))
                            runOnUiThread {
                                adapter.notifyItemInserted(placeList.size)
                            }
                        }
                    }

                }else{
                    runOnUiThread {
                        findViewById<ConstraintLayout>(R.id.noItem).visibility = View.VISIBLE
                        findViewById<RecyclerView>(R.id.placeList).visibility = View.GONE
                    }
                }
                runOnUiThread {
                    transitionPage(false)
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                runOnUiThread {
                    transitionPage(false)
                }
                println("Failed Request")
            }

        })

    }

    fun JSONDecoder ( str : String): JSONArray? {
        val mainObject = JSONObject(str)
       return mainObject.getJSONArray("results")
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    fun JSONArrayToArray ( jArray:JSONArray ): ArrayList<String> {
        val list = ArrayList<String>()
        val jsonArray = jArray
        if (jsonArray != null) {
            val len = jsonArray.length()
            for (i in 0 until len) {
                list.add(jsonArray.get(i).toString())
            }
        }
        return list
    }

    fun transitionPage( status: Boolean ){
        val progress = findViewById<ConstraintLayout>(R.id.progress)
        val recView = findViewById<RecyclerView>(R.id.placeList)

        if( status ){
            progress.visibility = View.VISIBLE
            recView.visibility = View.INVISIBLE
        }else{
            progress.visibility = View.INVISIBLE
            recView.visibility = View.VISIBLE
        }
    }

//    fun addTouristSpots() {
//        placeList.add( Place("NEGROS FORESTS AND ECOLOGICAL FOUNDATION,INC.","S Capitol Rd, Bacolod, 6100 Negros Occidental, Philippines",3.0))
//        placeList.add( Place("THE RUINS","Talisay City, Negros Occidental, Philippines",4.0))
//        placeList.add( Place("BACOLOD BAYWALK","Bacolod, 6100 Negros Occidental, Philippines",3.0))
//        placeList.add( Place("BANTUG LAKE RANCH","Granada Alangilan Road, Bacolod, Negros Occidental, Philippines",3.0))
//        placeList.add( Place("BALAY NI TANA DICANG","Enrique Lizares St, Talisay City, Negros Occidental, Philippines",4.0))
//        placeList.add( Place("BANTUG LAKE RANCH","Granada Alangilan Road, Bacolod, Negros Occidental, Philippines",4.0))
//        placeList.add( Place("NEGROS FORESTS AND ECOLOGICAL FOUNDATION,INC.","S Capitol Rd, Bacolod, 6100 Negros Occidental, Philippines",3.0))
//        placeList.add( Place("THE RUINS","Talisay City, Negros Occidental, Philippines",4.0))
//        placeList.add( Place("BACOLOD BAYWALK","Bacolod, 6100 Negros Occidental, Philippines",3.0))
//        placeList.add( Place("BANTUG LAKE RANCH","Granada Alangilan Road, Bacolod, Negros Occidental, Philippines",3.0))
//        placeList.add( Place("BALAY NI TANA DICANG","Enrique Lizares St, Talisay City, Negros Occidental, Philippines",4.0))
//        placeList.add( Place("BANTUG LAKE RANCH","Granada Alangilan Road, Bacolod, Negros Occidental, Philippines",4.0))
//    }

//    fun addRestaurants() {
//        placeList.add( Place("BODEGA CAFÉ & GRILL","Buri Road, Mandalagan, Bacolod, 6100 Negros Occidental, Philippines",4.0))
//        placeList.add( Place("DELICIOSO","Lacson St, Bacolod, 6100 Negros Occidental, Philippines",4.0))
//        placeList.add( Place("21 Restaurant","21 Lacson St, Bacolod, 6100 Negros Occidental, Philippines",3.0))
//        placeList.add( Place("Fogo","Lacson St, Bacolod, Negros Occidental, Philippines",3.0))
//        placeList.add( Place("RIPPLES","Ground Floor, L'Fisher Hotel, 14th, Lacson St, Bacolod, 6100 Negros Occidental, Philippines\n",4.0))
//        placeList.add( Place("SHABU NIKU KOREAN RESTAURANT","12th St, Bacolod, 6100 Negros Occidental, Philippines",4.0))
//        placeList.add( Place("BODEGA CAFÉ & GRILL","Buri Road, Mandalagan, Bacolod, 6100 Negros Occidental, Philippines",4.0))
//        placeList.add( Place("DELICIOSO","Lacson St, Bacolod, 6100 Negros Occidental, Philippines",4.0))
//        placeList.add( Place("21 Restaurant","21 Lacson St, Bacolod, 6100 Negros Occidental, Philippines",3.0))
//        placeList.add( Place("Fogo","Lacson St, Bacolod, Negros Occidental, Philippines",3.0))
//        placeList.add( Place("RIPPLES","Ground Floor, L'Fisher Hotel, 14th, Lacson St, Bacolod, 6100 Negros Occidental, Philippines\n",4.0))
//        placeList.add( Place("SHABU NIKU KOREAN RESTAURANT","12th St, Bacolod, 6100 Negros Occidental, Philippines",4.0))
//    }
}