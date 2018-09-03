package com.treatout.travel.treatoutmobile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var lat:String
    private lateinit var lng:String
    private lateinit var name:String

    private var place_name:String = ""
    private var place_lat:String = ""
    private var place_lng:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if( intent.hasExtra("NAME") ) {
            name = intent.getStringExtra("NAME")
            setTitle(name)
        }
        if( intent.hasExtra("LAT") ) {
            lat = intent.getStringExtra("LAT")
            println(lat)
        }

        if( intent.hasExtra("LNG") ) {
            lng = intent.getStringExtra("LNG")
            println(lng)
        }

        if( intent.hasExtra("PLACE_LAT") ) {
            place_lat = intent.getStringExtra("PLACE_LAT")
            println(place_lng)
        }

        if( intent.hasExtra("PLACE_LNG") ) {
            place_lng = intent.getStringExtra("PLACE_LNG")
            println(place_lng)
        }

        if( intent.hasExtra("PLACE_NAME") ) {
            place_name = intent.getStringExtra("PLACE_NAME")
            println(place_lng)
        }



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

        if( place_lat != "" && place_lng != ""){
            val coordPlace = LatLng(place_lat.toDouble(), place_lng.toDouble())
            mMap.addMarker(MarkerOptions().position(coordPlace).title(place_name))
        }

        val coord = LatLng(lat.toDouble(), lng.toDouble())
        mMap.addMarker(MarkerOptions().position(coord).title(name))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord,10f))
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
