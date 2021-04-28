package com.vivekcorp.finalproject.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vivekcorp.finalproject.R
import com.vivekcorp.finalproject.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var etNumber: EditText
    lateinit var etPassword: EditText

    lateinit var number: String
    lateinit var password: String

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etNumber = this.findViewById(R.id.etMobileNumber)
        etPassword = this.findViewById(R.id.etLoginPassword)

        sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)

        val checkLogin = sharedPreferences.getBoolean("login",false)

        if(checkLogin){
            number = sharedPreferences.getString("mobile number","nothing").toString()
            password = sharedPreferences.getString("password","nothing").toString()

            fetchData(number,password)
        }
    }

    fun forgotPassword(view: View) {
        val intent = Intent(this@LoginActivity,ForgotPasswordActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun signUp(view: View) {
        val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun login(view: View) {

        number = etNumber.text.toString()
        password = etPassword.text.toString()

        fetchData(number,password)
    }

    private fun fetchData(number: String, password: String){

        val queue = Volley.newRequestQueue(this@LoginActivity)

        val url = "http://13.235.250.119/v2/login/fetch_result"

        val jsonParams = JSONObject()

        jsonParams.put("mobile_number", number)
        jsonParams.put("password", password)

        if (ConnectionManager().checkConnectivity(this@LoginActivity)) {

            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                    Response.Listener {
                        try {
                            val success = it.getJSONObject("data").getBoolean("success")

                            if(success){

                                sharedPreferences.edit().putBoolean("login",true).apply()
                                sharedPreferences.edit().putString("mobile number",number).apply()
                                sharedPreferences.edit().putString("password",password).apply()

                                val json = it.getJSONObject("data").getJSONObject("data")
                                val id = json.getString("user_id")
                                val name =json.getString("name")
                                val email = json.getString("email")
                                val number = json.getString("mobile_number")
                                val address = json.getString("address")

                                val intent = Intent(this@LoginActivity,DashboardActivity::class.java)

                                val bundle = Bundle()
                                bundle.putString("data","login")
                                bundle.putString("id",id)
                                bundle.putString("name",name)
                                bundle.putString("mobile number",number)
                                bundle.putString("address",address)
                                bundle.putString("email",email)

                                intent.putExtra("details",bundle)

                                startActivity(intent)
                                this.finish()
                            }else{
                                Toast.makeText(this@LoginActivity,"not getting success", Toast.LENGTH_SHORT).show()
                                println("not getting success")
                            }

                        }catch (e: JSONException){
                            Toast.makeText(this@LoginActivity,"unexpected error", Toast.LENGTH_SHORT).show()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(this@LoginActivity,"volly error $it", Toast.LENGTH_SHORT).show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "9bf534118365f1"
                        return headers
                    }
                }
            queue.add(jsonRequest)

        }else{
            val dialog = AlertDialog.Builder(this@LoginActivity)
            dialog.setTitle("Error!")
            dialog.setMessage("Internet connection is not found")
            dialog.setPositiveButton("Open Settings") { text, listner ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                this@LoginActivity.finish()
            }
            dialog.setNegativeButton("Exit") { text, listner ->
                ActivityCompat.finishAffinity(this@LoginActivity)
            }
            dialog.create()
            dialog.show()
        }
    }
}