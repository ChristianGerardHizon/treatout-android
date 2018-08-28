package com.treatout.travel.treatoutmobile

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.widget.Button
import android.widget.EditText
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import java.net.InetAddress
import android.net.ConnectivityManager




class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPreferences = getSharedPreferences("ACCOUNTS", Context.MODE_PRIVATE)

        setTitle("Login")

        val username = findViewById<EditText>(R.id.edtUsername)
        val password = findViewById<EditText>(R.id.edtPassword)
        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val registernBtn = findViewById<Button>(R.id.btnRegister)
        val settingsBtn = findViewById<Button>(R.id.btnSettings)

        var user = username.text
        var pass = password.text

        loginBtn.setOnClickListener {
            if( isInternetAvailable() ){
                transitionPage(true)

                if (username.text.toString() == "admin" && password.text.toString() == "admin"){
                    startTimer( this )
                }else{

                    if( sharedPreferences.contains(username.text.toString()) && sharedPreferences.contains(username.text.toString() + password.text.toString()) ) {
                        startTimer( this )
                    }else{
                        failedTimer(this)
                    }

                }
            }else{
                Toast.makeText( this, "Network Not Available", Toast.LENGTH_SHORT).show()
            }

        }

        registernBtn.setOnClickListener {
            val  intent = Intent( this, RegisterActivity::class.java)
            startActivity(intent)
        }

        settingsBtn.setOnClickListener{
            val  intent = Intent( this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    fun startTimer( context: Context){
        object : CountDownTimer(10000, 1000) {

            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                val  intent = Intent( context, MainActivity::class.java)
                context.startActivity(intent)
            }
        }.start()
    }

    fun failedTimer( context: Context){
        object : CountDownTimer(7000, 8000) {

            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                transitionPage(false)
                Toast.makeText(context, "Account Error", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    fun transitionPage( status: Boolean ){
        val content = findViewById<ConstraintLayout>(R.id.content)
        val spinner = findViewById<ConstraintLayout>(R.id.spinner)

        if( status ){
            spinner.visibility = View.VISIBLE
            content.visibility = View.INVISIBLE
        }else{
            spinner.visibility = View.INVISIBLE
            content.visibility = View.VISIBLE
        }
    }

    fun isInternetAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return cm.activeNetworkInfo != null
    }
}
