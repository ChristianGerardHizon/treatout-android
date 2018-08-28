package com.treatout.travel.treatoutmobile


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

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

        titleTxt.text = name
        addressTxt.text = address
        ratingTxt.text = "★★★★★"

        val image1 = findViewById<ImageView>(R.id.img1)
        val image2 = findViewById<ImageView>(R.id.img2)
        val image3 = findViewById<ImageView>(R.id.img3)


        Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(image1)
        Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(image2)
        Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(image3)

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
