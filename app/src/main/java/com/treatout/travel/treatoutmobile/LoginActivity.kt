package com.treatout.travel.treatoutmobile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.os.CountDownTimer
import android.view.View
import java.net.InetAddress
import android.net.ConnectivityManager
import android.widget.*
import com.treatout.travel.treatoutmobile.Classes.Place
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class LoginActivity : AppCompatActivity() {

    var configPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPreferences = getSharedPreferences("ACCOUNTS", Context.MODE_PRIVATE)
         configPreferences = getSharedPreferences("CONFIG", Context.MODE_PRIVATE)

        setTitle("Login")

        val username = findViewById<EditText>(R.id.edtUsername)
        val password = findViewById<EditText>(R.id.edtPassword)
        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val registernBtn = findViewById<Button>(R.id.btnRegister)
        val settingsBtn = findViewById<Button>(R.id.btnSettings)

        var user = username.text
        var pass = password.text

        val status = sharedPreferences.getString("STATUS", "FALSE").toBoolean()
        if( status ){
            val  intent = Intent( this,MainActivity::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            if( isInternetAvailable() ){
                transitionPage(true)

                if (username.text.toString() == "admin" && password.text.toString() == "admin"){
                    startTimer( this )
                }else{

                    fetchJSON( this, username.text.toString(), password.text.toString())
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
        val content = findViewById<ScrollView>(R.id.content)
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

    fun fetchJSON ( context: Context, email:String, password:String) {

        val address = configPreferences?.getString("IPADDRESS", "https://treatout.000webhostapp.com")

        val url = "$address/modules/api/login.php?email=$email&password=$password"

        try {

            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()

            client.newCall(request).enqueue(object : Callback {

                override fun onResponse(call: Call?, response: Response?) {
                    val body = response?.body()?.string()
                    if (body != null) {
                        val result = JSONDecoder(body)
                        if (result.get("login").toString().toBoolean()) {
                            runOnUiThread {
                                val sharedPreferences = getSharedPreferences("ACCOUNTS", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("USERID", result.getString("userid"))
                                editor.putString("NAME", result.getString("name"))
                                editor.apply()
                                Toast.makeText(context, "Login Complete", Toast.LENGTH_SHORT).show()
                                val intent = Intent(context, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                context.startActivity(intent)
                                transitionPage(false)
                            }

                        } else {
                            runOnUiThread {
                                transitionPage(false)
                                Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    runOnUiThread {
                        transitionPage(false)
                        Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                    }

                }

            })
        }catch(e:IOException ) {
            runOnUiThread {
                transitionPage(false)
                Toast.makeText(context, "Please Check URl $url", Toast.LENGTH_SHORT).show()
            }
            e.printStackTrace()
        }

    }

    fun JSONDecoder ( str : String): JSONObject {
        val newstr = str.substring(1, str.length -1)
        return  JSONObject( newstr )
    }
}
