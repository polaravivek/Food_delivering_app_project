package com.vivekcorp.finalproject.activity

import android.content.Intent
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

class OtpActivity : AppCompatActivity() {

    lateinit var etOtp: EditText
    lateinit var etNewPassword: EditText
    lateinit var etConfirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        etOtp = findViewById(R.id.etOtp)
        etNewPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)

    }

    fun otpSubmit(view: View) {

        val otp = etOtp.text.toString()
        val pass = etNewPassword.text.toString()
        val confirmPass = etConfirmPassword.text.toString()
        val mobileNum = intent.getStringExtra("mobile_number")

        if (pass == confirmPass){
            val queue = Volley.newRequestQueue(this)

            val url = "http://13.235.250.119/v2/reset_password/fetch_result"

            val jsonParams = JSONObject()

            jsonParams.put("mobile_number", mobileNum)
            jsonParams.put("password", pass)
            jsonParams.put("otp",otp)

            if (ConnectionManager().checkConnectivity(this)) {

                val jsonRequest =
                    object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                        Response.Listener {
                            try {
                                val success = it.getJSONObject("data").getBoolean("success")

                                if (success) {
                                    Toast.makeText(
                                        this,
                                        "password has successfully changed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent =
                                        Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        this,
                                        "not getting success",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }catch (e: JSONException){
                                Toast.makeText(this,"unexpected error", Toast.LENGTH_SHORT).show()
                            }
                        }, Response.ErrorListener {
                            Toast.makeText(this,"volley error $it", Toast.LENGTH_SHORT).show()
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
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Error!")
                dialog.setMessage("Internet connection is not found")
                dialog.setPositiveButton("Open Settings") { text, listner ->
                    val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingIntent)
                    this.finish()
                }
                dialog.setNegativeButton("Exit") { text, listner ->
                    ActivityCompat.finishAffinity(this)
                }
                dialog.create()
                dialog.show()
            }
        }
    }
}