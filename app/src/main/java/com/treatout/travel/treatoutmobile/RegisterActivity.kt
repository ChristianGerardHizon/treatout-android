package com.treatout.travel.treatoutmobile

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle("Registration")

        val sharedPreferences = getSharedPreferences("ACCOUNTS", Context.MODE_PRIVATE)


        val email = findViewById<EditText>(R.id.edtEmail)
        val password = findViewById<EditText>(R.id.edtEmail)
        val register = findViewById<Button>(R.id.btnRegister)

        val editor = sharedPreferences.edit()

        register.setOnClickListener{
            if( isInternetAvailable() ) {
                if (!(email.text.toString() != "" || password.text.toString() != "")) {
                    Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show()
                } else {
                    editor.putString(email.text.toString(), email.text.toString())
                    editor.putString(email.text.toString() + password.text.toString(), password.text.toString())
                    editor.apply()
                    registerTimer(this)
                }
            }else{
                Toast.makeText(this, "Network Not Available", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun registerTimer( context: Context){
        Toast.makeText(this,"Please Wait...", Toast.LENGTH_LONG).show()
        object : CountDownTimer(3000, 5000) {

            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                Toast.makeText(context,"Added to Database", Toast.LENGTH_LONG).show()
            }
        }.start()
    }

    fun isInternetAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return cm.activeNetworkInfo != null
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
