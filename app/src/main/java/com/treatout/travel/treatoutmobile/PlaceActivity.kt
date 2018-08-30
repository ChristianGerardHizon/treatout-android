package com.treatout.travel.treatoutmobile


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class PlaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val name:String = intent.getStringExtra("name")
        val address:String = intent.getStringExtra("address")
        val rating:String = intent.getStringExtra("rating")

        println(name)
        println(address)
        println(rating)

        val titleTxt = findViewById<TextView>(R.id.title)
        val addressTxt = findViewById<TextView>(R.id.address)
        val ratingTxt = findViewById<TextView>(R.id.rating)
        val btnTerminal = findViewById<Button>(R.id.btnTerminal)

        titleTxt.text = name
        addressTxt.text = address
        ratingTxt.text = "★★★★★"

        val image1 = findViewById<ImageView>(R.id.img1)
        val image2 = findViewById<ImageView>(R.id.img2)
        val image3 = findViewById<ImageView>(R.id.img3)


        Picasso.get().load("https://placeimg.com/640/480/nature").into(image1)
        Picasso.get().load("https://placeimg.com/640/480/arch").into(image2)
        Picasso.get().load("https://placeimg.com/640/480/tech").into(image3)

        btnTerminal.setOnClickListener{
            startActivity(Intent(this, MapsActivity::class.java))
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("LAT", 10.706304)
            intent.putExtra("LNG", 122.963013)
            startActivity(intent)
//            ,
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
