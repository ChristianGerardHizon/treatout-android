package com.treatout.travel.treatoutmobile


import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.squareup.picasso.Picasso
import com.treatout.travel.treatoutmobile.Classes.Place
import com.treatout.travel.treatoutmobile.Classes.Terminal
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class PlaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val name:String = intent.getStringExtra("name")
        val address:String = intent.getStringExtra("address")
        val rating:String = intent.getStringExtra("rating")
        val placeid:String = intent.getStringExtra("id")

        println(name)
        println(address)
        println(rating)

        val titleTxt = findViewById<TextView>(R.id.title)
        val addressTxt = findViewById<TextView>(R.id.address)
        val ratingTxt = findViewById<TextView>(R.id.rating)
//        val btnTerminal = findViewById<Button>(R.id.btnTerminal)

        titleTxt.text = name
        addressTxt.text = address
        ratingTxt.text = "★★★★★"

        val image1 = findViewById<ImageView>(R.id.img1)
        val image2 = findViewById<ImageView>(R.id.img2)
        val image3 = findViewById<ImageView>(R.id.img3)


//        Picasso.get().load("https://placeimg.com/640/480/nature").into(image1)
//        Picasso.get().load("https://placeimg.com/640/480/arch").into(image2)
//        Picasso.get().load("https://placeimg.com/640/480/tech").into(image3)

        fetchTerminal ( this, "place_id=$placeid")
        fetchRate(this, "placeid=$placeid")


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    fun fetchTerminal (context: Context, query:String) {
        runOnUiThread {
            transitionPage(true)
            findViewById<TextView>(R.id.loadingText).text = "Loading Terminals..."
        }
        val url = "https://treatout.000webhostapp.com/modules/api/geterminal.php?$query"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue( object: Callback {

            override fun onResponse(call: Call?, response: Response?) {
                var body = response?.body()?.string()
                println(body)
                if (body != null) {
                    body = body.drop(1)
                    body = body.dropLast(1)

                    val result = JSONObject(body)
                    if( result.has("terminals")){
                        val arr = JSONArray(result.get("terminals").toString())
                        val terminals = JSONArrayToArray(arr)
                        for( terminal in terminals){
                            val obj = JSONObject(terminal)
                            println(obj.toString())
                            val term = Terminal(
                                    obj.getString("place_id"),
                                    obj.getString("fare_rate_max"),
                                    obj.getString("latitude"),
                                    obj.getString("longitude"),
                                    obj.getString("transportation"),
                                    obj.getString("description"))
                            runOnUiThread {
                                val termLayout = findViewById<LinearLayout>(R.id.terminalLayout)
                                val termBtn = Button(context)
                                termBtn.text = term.transportation
                                termBtn.setOnClickListener{
                                    val intent = Intent(context, MapsActivity::class.java)
                                    intent.putExtra("LAT", term.lat)
                                    intent.putExtra("LNG", term.lng)
                                    intent.putExtra("NAME", term.transportation)

                                    startActivity(intent)

                                }
                                termLayout.addView(termBtn)
                            }
                        }
                    }else{

                    }

                }
                fetchFullDetails(context)
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed Request")
                fetchFullDetails(context)
                runOnUiThread {
                    Toast.makeText(context,"Unable to get Terminals",Toast.LENGTH_SHORT).show()
                }

            }

        })

    }

    fun fetchRate (context: Context, query:String) {
        runOnUiThread {
            transitionPage(true)
            findViewById<TextView>(R.id.loadingText).text = "Loading Terminals..."
        }
        val url = "https://treatout.000webhostapp.com/modules/api/getplacedetails.php?$query"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue( object: Callback {

            override fun onResponse(call: Call?, response: Response?) {
                var body = response?.body()?.string()
                if (body != null) {
                    body = body.drop(1)
                    body = body.dropLast(1)
                    println(body)
                    val result = JSONObject(body.toString())
                    if( result.has("data")){
                        try{
                            val arr = JSONArray(result.get("data").toString())
                            val formattedArr = JSONArrayToArray(arr)

                            for (res in formattedArr){
                                val obj = JSONObject(res)
                                val min = obj.getString("rate_min")
                                val max = obj.getString("rate_max")

                                runOnUiThread{
                                    findViewById<TextView>(R.id.placeRate).text = "P$min to P$max"
                                }
                            }
                        }catch (e: IOException){

                        }


                    }else{
                        runOnUiThread{
                            findViewById<TextView>(R.id.placeRate).visibility = View.GONE
                        }
                    }

                }
                fetchFullDetails(context)
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed Request")
                fetchFullDetails(context)
                runOnUiThread {
                    Toast.makeText(context,"Unable to get Terminals",Toast.LENGTH_SHORT).show()
                }

            }

        })

    }

    fun fetchFullDetails ( context: Context ) {
        val placeid:String = intent.getStringExtra("id")

        runOnUiThread{
            findViewById<TextView>(R.id.loadingText).text = "Loading Place Details..."
        }

        val url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=$placeid&key=AIzaSyDWJ95wDORvWwB6B8kNzSNDfVSOeQc8W7k"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue( object: Callback{

            override fun onResponse(call: Call?, response: Response?) {
                var body = response?.body()?.string()
                if (body != null) {

                    val main = JSONObject(body).get("result").toString()
                    val result = JSONObject(main)
                    var phone = ""
                    if( result.has("formatted_phone_number")){
                        phone = result.getString("formatted_phone_number")
                    }else{
                        runOnUiThread{
                            findViewById<TextView>(R.id.contactNumber).visibility = View.GONE
                        }
                    }


//                    val schedule = result.get("weekday_text")

                    if(result.has("photos")){
                        val photos = result.getJSONArray("photos")
                        val rating = result.getDouble("rating")
                        val photoArray = JSONArrayToArray(photos)
//                    println(schedule.toString())

                        val layoutLinear = findViewById<LinearLayout>(R.id.photoGallery)

                        runOnUiThread {
                            findViewById<TextView>(R.id.contactNumber).text = phone
                            findViewById<RatingBar>(R.id.ratingBar).rating = rating.toFloat()
                            layoutLinear.removeAllViews()

                        }


                        for( photo in photoArray){

                            val ref = JSONObject(photo)
                            val photoRef = ref.getString("photo_reference")
                            val imageHolder = ImageView(context)
                            imageHolder.scaleType = ImageView.ScaleType.CENTER_CROP

                            runOnUiThread{
                                Picasso.get().load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$photoRef&key=AIzaSyDWJ95wDORvWwB6B8kNzSNDfVSOeQc8W7k").into(imageHolder)
                                layoutLinear.addView(imageHolder)
                            }
                        }
                    }else{

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

    fun transitionPage( status: Boolean ){
        val progress = findViewById<ConstraintLayout>(R.id.progress)
        val recView = findViewById<ScrollView>(R.id.scView)

        if( status ){
            progress.visibility = View.VISIBLE
            recView.visibility = View.INVISIBLE
        }else{
            progress.visibility = View.INVISIBLE
            recView.visibility = View.VISIBLE
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
}
