package com.treatout.travel.treatoutmobile

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setTitle("Settings")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        val configPreferences = getSharedPreferences("CONFIG", Context.MODE_PRIVATE)
        val address = configPreferences?.getString("IPADDRESS", "https://treatout.000webhostapp.com").toString()

        val ipaddress = findViewById<EditText>(R.id.edtIPaddress)
        val changeBtn = findViewById<Button>(R.id.btnChange)
        val btnCloud =  findViewById<Button>(R.id.btnCloud)
        val btnLocalHost = findViewById<Button>(R.id.btnLocalhost)

        ipaddress.setText(address)

        btnCloud.setOnClickListener{
            ipaddress.setText("https://treatout.000webhostapp.com")
            Toast.makeText(this, "Press Change to Finalize", Toast.LENGTH_SHORT).show()
        }

        btnLocalHost.setOnClickListener{
            ipaddress.setText("http://192.168.0.205/treatout")
            Toast.makeText(this, "Press Change to Finalize", Toast.LENGTH_SHORT).show()
        }

        changeBtn.setOnClickListener {
            val editor = configPreferences.edit()
            editor.putString("IPADDRESS" ,ipaddress.text.toString())
            editor.apply()

            Toast.makeText(this, "Changed IP Address", Toast.LENGTH_SHORT).show()
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
