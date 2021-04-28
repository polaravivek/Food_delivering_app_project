package com.vivekcorp.finalproject.activity

import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.EditText
import android.widget.TextView
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

class RegisterActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etDelivery: EditText
    lateinit var etNumber: EditText
    lateinit var etPassword: EditText
    lateinit var etConfirmPassword: EditText

    lateinit var name: String
    lateinit var email: String
    lateinit var delivery: String
    lateinit var number: String
    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = this.findViewById(R.id.etName)
        etEmail = this.findViewById(R.id.etEmail)
        etDelivery = this.findViewById(R.id.etDelivery)
        etNumber = this.findViewById(R.id.etNumber)
        etPassword = this.findViewById(R.id.etPassword)
        etConfirmPassword = this.findViewById(R.id.etConfirmPassword)

    }

    fun register(view: View) {
        if (etPassword.text.toString().equals(etConfirmPassword.text.toString())) {

            name = etName.text.toString()
            email = etEmail.text.toString()
            delivery = etDelivery.text.toString()
            number = etNumber.text.toString()
            password = etPassword.text.toString()

            val queue = Volley.newRequestQueue(this@RegisterActivity)

            val url = "http://13.235.250.119/v2/register/fetch_result"

            val jsonParams = JSONObject()
            jsonParams.put("name", name)
            jsonParams.put("mobile_number", number)
            jsonParams.put("password", password)
            jsonParams.put("address", delivery)
            jsonParams.put("email", email)


            if (ConnectionManager().checkConnectivity(this@RegisterActivity)) {

                val jsonRequest =
                    object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                        Response.Listener {
                                          try {
                                              val success = it.getJSONObject("data").getBoolean("success")

                                              if(success){
                                                  val foodName =it.getJSONObject("data").getJSONObject("data").getString("user_id")

                                                  Toast.makeText(this@RegisterActivity,"registered successfully",Toast.LENGTH_SHORT).show()

                                                  val intent = Intent(this@RegisterActivity,DashboardActivity::class.java)

                                                  val bundle = Bundle()

                                                  bundle.putString("data","register")

                                                  bundle.putString("name",name)
                                                  bundle.putString("mobile number",number)
                                                  bundle.putString("address",delivery)
                                                  bundle.putString("email",email)

                                                  intent.putExtra("details",bundle)

                                                  startActivity(intent)
                                                  finish()
                                              }else{
                                                  Toast.makeText(this@RegisterActivity,"something went wrong",Toast.LENGTH_SHORT).show()
                                              }

                                          }catch (e: JSONException){
                                              Toast.makeText(this@RegisterActivity,"unexpected error",Toast.LENGTH_SHORT).show()
                                          }
                        }, Response.ErrorListener {
                            Toast.makeText(this@RegisterActivity,"volly error $it",Toast.LENGTH_SHORT).show()

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
                val dialog = AlertDialog.Builder(this@RegisterActivity)
                dialog.setTitle("Error!")
                dialog.setMessage("Internet connection is not found")
                dialog.setPositiveButton("Open Settings") { text, listner ->
                    val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingIntent)
                    this@RegisterActivity.finish()
                }
                dialog.setNegativeButton("Exit") { text, listner ->
                    ActivityCompat.finishAffinity(this@RegisterActivity)
                }
                dialog.create()
                dialog.show()
            }

        } else {
            Toast.makeText(this@RegisterActivity, "password not matched", Toast.LENGTH_SHORT).show()
        }
    }

    fun backToLogin(view: View) {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}