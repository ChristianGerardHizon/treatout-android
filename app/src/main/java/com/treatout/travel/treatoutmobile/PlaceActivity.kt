package com.treatout.travel.treatoutmobile


import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.squareup.picasso.Picasso
import com.treatout.travel.treatoutmobile.Adapters.PlacesRecAdapter
import com.treatout.travel.treatoutmobile.Adapters.ReviewAdapter
import com.treatout.travel.treatoutmobile.Classes.Place
import com.treatout.travel.treatoutmobile.Classes.Review
import com.treatout.travel.treatoutmobile.Classes.Terminal
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder

class PlaceActivity : AppCompatActivity() {

    private lateinit var name:String
    val reviewList: ArrayList<Review> = ArrayList()
    private lateinit var adapter: ReviewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        name = intent.getStringExtra("name")
        val address:String = intent.getStringExtra("address")
        val rating:String = intent.getStringExtra("rating")
        val placeid:String = intent.getStringExtra("id")

        val titleTxt = findViewById<TextView>(R.id.title)
        val addressTxt = findViewById<TextView>(R.id.address)
        val ratingTxt = findViewById<TextView>(R.id.rating)
//        val btnTerminal = findViewById<Button>(R.id.btnTerminal)

        val recView = findViewById<RecyclerView>(R.id.reviewRec)
        recView.layoutManager = LinearLayoutManager(this)
        adapter = ReviewAdapter(this,reviewList)
        recView.adapter = adapter

        titleTxt.text = name
        addressTxt.text = address
        ratingTxt.text = "★★★★★"

        val image1 = findViewById<ImageView>(R.id.img1)
        val image2 = findViewById<ImageView>(R.id.img2)
        val image3 = findViewById<ImageView>(R.id.img3)


        fetchFullDetails(this)

        val sharedPreferences = getSharedPreferences("ACCOUNTS", Context.MODE_PRIVATE)
        var userid =""
        var name =""
        if(sharedPreferences.contains("USERID")){
            userid = sharedPreferences.getString("USERID","");
        }
        if(sharedPreferences.contains("NAME")){
            name = sharedPreferences.getString("NAME","");
        }
        println("userID: $name")

        val comment = findViewById<EditText>(R.id.edtComment)

        findViewById<Button>(R.id.btnComment).setOnClickListener{
            addComment(this, comment.text.toString()
                    ,placeid,userid,name)
        }

        getComments(this, placeid, name)


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    fun fetchTerminal (context: Context, query:String, lat:String, lng:String) {
        runOnUiThread {
            transitionPage(true)
            findViewById<TextView>(R.id.loadingText).text = "Loading ...."
        }
        val url = "https://treatout.000webhostapp.com/modules/api/geterminal.php?$query"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue( object: Callback {

            override fun onResponse(call: Call?, response: Response?) {
                var body = response?.body()?.string()
//                println(body)
                if (body != null) {
                    body = body.drop(1)
                    body = body.dropLast(1)

                    val result = JSONObject(body)
                    if( result.has("terminals")){
                        val arr = JSONArray(result.get("terminals").toString())
                        val terminals = JSONArrayToArray(arr)
                        for( terminal in terminals){
                            val obj = JSONObject(terminal)
//                            println(obj.toString())
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
                                    intent.putExtra("PLACE_LAT", lat)
                                    intent.putExtra("PLACE_LNG", lng)
                                    intent.putExtra("PLACE_NAME", name)
                                    intent.putExtra("NAME", term.transportation)
                                    startActivity(intent)
                                }
                                termLayout.addView(termBtn)
                            }
                        }
                    }else{

                    }

                }
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
            findViewById<TextView>(R.id.loadingText).text = "Loading Ride Informations...."
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
                    body = body.drop(1)
                    body = body.dropLast(1)
                    body = "$body}"
                    println(body)
                    try{

                    val result = JSONObject(body.toString())
                    if( result.has("data")){

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
                    }else{
                        runOnUiThread{
                            findViewById<TextView>(R.id.placeRate).visibility = View.GONE
                        }
                    }
                        }catch (e: IOException){
                            runOnUiThread{
                                findViewById<TextView>(R.id.placeRate).visibility = View.GONE
                            }
                        }



                }
               runOnUiThread{
                   transitionPage(false)

               }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed Request")
                fetchFullDetails(context)
                runOnUiThread {
                    transitionPage(false)
                    Toast.makeText(context,"Unable to get Terminals",Toast.LENGTH_SHORT).show()
                }

            }

        })

    }

    fun fetchFullDetails ( context: Context ) {
        transitionPage(true)
        val placeid:String = intent.getStringExtra("id")

        runOnUiThread{
            findViewById<TextView>(R.id.loadingText).text = "Loading Details About Place..."
        }

        val url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=$placeid&key=AIzaSyDWJ95wDORvWwB6B8kNzSNDfVSOeQc8W7k"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue( object: Callback{

            override fun onResponse(call: Call?, response: Response?) {
                var lat: String
                var lng: String
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

                    val geo =  JSONObject(result.get("geometry").toString())
                    val location = JSONObject(geo.get("location").toString())

                    lat = location.getString("lat")
                    lng = location.getString("lng")

//                    println("LAT"+lat)
//                    println("LNG"+lng)


//                    val schedule = result.get("weekday_text")

                    if(result.has("photos")){
                        val photos = result.getJSONArray("photos")
                        val rating = result.getDouble("rating")
                        val photoArray = JSONArrayToArray(photos)
                        val reviewArray = JSONArrayToArray(result.getJSONArray("reviews"))

//                        println(reviewArray.toString())

                        for( review in reviewArray){
                            val rev = JSONObject(review)

                            reviewList.add( Review(rev.getString("author_name"),rev.getString("text"),rev.get("rating") as Number))
                            runOnUiThread {
                                adapter.notifyItemInserted(reviewList.size)
                            }

                        }

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

                    fetchTerminal ( context, "place_id=$placeid", lat, lng)
                    fetchRate( context, "placeid=$placeid")

                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
            }

        })

    }

    fun getComments(context: Context,placeid: String,name:String){
        val url = "https://treatout.000webhostapp.com/modules/api/getcommentsandrate.php?placeid=$placeid"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue( object: Callback {

            override fun onResponse(call: Call?, response: Response?) {
                var body = response?.body()?.string()

                if( body != null ){
                    body = "{\"result\":$body}"
                    println("BODY: $body")
                    val jsonBody = JSONObject(body)
                    try{
                        val jArrayBody = jsonBody.getJSONArray("result")
                        val jArray = JSONArrayToArray(jArrayBody)

                        for( comment in jArray){
                            println(comment)
                            val commentObj = JSONObject(comment)
                            runOnUiThread {
                                reviewList.add( Review(commentObj.getString("username"),commentObj.getString("comment"),commentObj.get("rate")  as Number))
                                adapter.notifyItemInserted(reviewList.size)
                            }
                        }

                    }catch (e: IOException){
                        println(e)
                    }

                 }

            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed Request")
                fetchFullDetails(context)
                runOnUiThread {
                    Toast.makeText(context,"Unable to Fetch Comments",Toast.LENGTH_SHORT).show()
                }

            }

        })

    }

    fun addComment(context: Context, comment:String, placeid:String ,userid:String, name:String){
        runOnUiThread {
            transitionPage(true)
            findViewById<TextView>(R.id.loadingText).text = "Saving Comment...."
        }
        val commentEnc = URLEncoder.encode(comment, "UTF-8");
       var url = "https://treatout.000webhostapp.com/modules/api/addcommenttoplace.php?place_id=$placeid&comment=$commentEnc&user_id=$userid"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue( object: Callback {

            override fun onResponse(call: Call?, response: Response?) {

                println(response)

                runOnUiThread {
                    reviewList.add( Review(name,comment,0  as Number))
                    adapter.notifyItemInserted(reviewList.size)
                    transitionPage(false)
                    Toast.makeText(context,"Added Comment",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed Request")
                fetchFullDetails(context)
                runOnUiThread {
                    transitionPage(false)
                    Toast.makeText(context,"Unable to Add Comment",Toast.LENGTH_SHORT).show()
                }

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
