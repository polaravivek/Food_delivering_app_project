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

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var phoneNumber: EditText
    lateinit var email: EditText

    lateinit var number: String
    lateinit var emailAddress: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        phoneNumber = findViewById(R.id.et_forgot_mobile_number)
        email = findViewById(R.id.et_forget_email)
    }

    fun nextToEmpty(view: View) {

        number = phoneNumber.text.toString()
        emailAddress = email.text.toString()

        val queue = Volley.newRequestQueue(this@ForgotPasswordActivity)

        val url = "http://13.235.250.119/v2/forgot_password/fetch_result"

        val jsonParams = JSONObject()

        jsonParams.put("mobile_number", number)
        jsonParams.put("email", emailAddress)

        if (ConnectionManager().checkConnectivity(this@ForgotPasswordActivity)) {

            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                    Response.Listener {
                        try {
                            val success = it.getJSONObject("data").getBoolean("success")

                            if (success) {

                                val firstTry = it.getJSONObject("data").getBoolean("first_try")

                                if (firstTry) {
                                    Toast.makeText(
                                        this,
                                        "you will get OTP",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "you already got OTP",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                val intent =
                                    Intent(this@ForgotPasswordActivity, OtpActivity::class.java)
                                intent.putExtra("mobile_number",number)
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
                        Toast.makeText(this,"volly error $it", Toast.LENGTH_SHORT).show()
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