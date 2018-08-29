package com.treatout.travel.treatoutmobile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.treatout.travel.treatoutmobile.Classes.Place
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class RegisterActivity : AppCompatActivity() {

    var configPreferences: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle("Registration")

        val sharedPreferences = getSharedPreferences("ACCOUNTS", Context.MODE_PRIVATE)
        configPreferences = getSharedPreferences("CONFIG", Context.MODE_PRIVATE)


        val email = findViewById<EditText>(R.id.edtEmail)
        val password = findViewById<EditText>(R.id.edtPassword)
        val register = findViewById<Button>(R.id.btnRegister)
        val name = findViewById<EditText>(R.id.edtName)
        val confirmPassword = findViewById<EditText>(R.id.edtConfirmPassword)


        val editor = sharedPreferences.edit()

        register.setOnClickListener{
            if( isInternetAvailable() ) {

                fetchJSON(this, email.text.toString(), password.text.toString(), name.text.toString(), confirmPassword.text.toString())
                editor.putString("STATUS", "TRUE")
                editor.apply()

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

    fun fetchJSON ( context: Context, email:String, password:String, name:String, confirmPass :String) {

        val address = configPreferences?.getString("IPADDRESS", "https://treatout.000webhostapp.com")


        val url = "$address/modules/api/register.php?email=$email&password=$password&name=$name&confirm_password=$confirmPass"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        try {
            client.newCall(request).enqueue(object : Callback {

                override fun onResponse(call: Call?, response: Response?) {
                    val body = response?.body()?.string()
                    if (body != null) {
                        val result = JSONDecoder(body)
                        if (result.get("response").toString() == "User successfuly added!") {
                            runOnUiThread {
                                Toast.makeText(context, "Registration Complete", Toast.LENGTH_SHORT).show()
                            }
                            val intent = Intent(context, MainActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            context.startActivity(intent)
                        } else {
                            runOnUiThread {
                                Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    runOnUiThread {
                        Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show()
                    }

                }

            })
        }catch(e:IOException ) {
            runOnUiThread {
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
