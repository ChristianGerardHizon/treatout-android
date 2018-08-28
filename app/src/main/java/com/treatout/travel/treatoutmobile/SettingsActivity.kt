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



        val sharedPreferences = getSharedPreferences("CONFIG", Context.MODE_PRIVATE)

        val ipaddress = findViewById<EditText>(R.id.edtIPaddress)
        val changeBtn = findViewById<Button>(R.id.btnChange)

        changeBtn.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putString("IPADDRESS" ,ipaddress.text.toString())
            editor.apply()

            Toast.makeText(this, "Changed IP Address", Toast.LENGTH_SHORT)
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
