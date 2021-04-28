package com.vivekcorp.finalproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vivekcorp.finalproject.R
import com.vivekcorp.finalproject.adapter.MenuRecyclerAdapter
import com.vivekcorp.finalproject.model.Menu
import com.vivekcorp.finalproject.util.ConnectionManager
import org.json.JSONException

class RestaurantMenuActivity : AppCompatActivity() {

    lateinit var recyclerMenu: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var toolbar: Toolbar

    val menuItemList = arrayListOf<Menu>()

    lateinit var recycleAdapter: MenuRecyclerAdapter

    lateinit var proceedToCart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_menu)

        recyclerMenu = findViewById(R.id.restaurantsMenuRecyclerView)
        proceedToCart = findViewById(R.id.btnProceedToCart)

        layoutManager = LinearLayoutManager(this)

        toolbar = findViewById(R.id.toolbar)

        val restaurantId = intent.getStringExtra("restaurant_id")
        val restaurantName = intent.getStringExtra("restaurant_name")
        val bundle = intent.getBundleExtra("details")

        setUpToolbar(restaurantName)

        toolbar.setNavigationOnClickListener {
            val intent = Intent(this,DashboardActivity::class.java)
            intent.putExtra("details",bundle)
            startActivity(intent)
        }

        val queue = Volley.newRequestQueue(this)

        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$restaurantId"

        if (ConnectionManager().checkConnectivity(this)) {

            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                    val dataJson = it.getJSONObject("data")

                    try {
                        val success = dataJson.getBoolean("success")

                        if (success) {

                            println("success")
                            val data = dataJson.getJSONArray("data")
                            var num: Int
                            for (i in 0 until data.length()) {
                                val menuJsonObject = data.getJSONObject(i)
                                num = i+1
                                val menuobject = Menu(
                                    num.toString(),
                                    menuJsonObject.getString("id"),
                                    menuJsonObject.getString("name"),
                                    menuJsonObject.getString("cost_for_one")
                                )

                                menuItemList.add(menuobject)

                                recycleAdapter = MenuRecyclerAdapter(this, menuItemList,bundle)

                                recyclerMenu.adapter = recycleAdapter

                                recyclerMenu.layoutManager = layoutManager

                            }
                        } else {
                            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                        }
                        println("Response is $it")
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this,
                            "some unexpected error occurred!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this, "volly error occured", Toast.LENGTH_SHORT).show()
                }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "9bf534118365f1"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
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

        proceedToCart.setOnClickListener {
            val intent = Intent(this,MyCartActivity::class.java)
            intent.putExtra("restaurantName",restaurantName)
            intent.putExtra("restaurantId",restaurantId)
            intent.putExtra("details",bundle)
            startActivity(intent)
        }
    }

    private fun setUpToolbar(restaurantName: String?) {

        setSupportActionBar(toolbar)
        supportActionBar?.title = restaurantName
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}