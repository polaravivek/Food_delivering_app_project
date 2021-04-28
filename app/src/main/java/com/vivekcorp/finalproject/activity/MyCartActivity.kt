package com.vivekcorp.finalproject.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vivekcorp.finalproject.R
import com.vivekcorp.finalproject.adapter.CartRecyclerAdapter
import com.vivekcorp.finalproject.database.MenuEntities
import com.vivekcorp.finalproject.database.menuItemDatabase
import com.vivekcorp.finalproject.util.ConnectionManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MyCartActivity : AppCompatActivity() {

    lateinit var recyclerMenu: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var fromRestaurant: TextView

    lateinit var recyclerAdapter: CartRecyclerAdapter

    lateinit var btnOrderPlace: Button

    lateinit var toolbar: Toolbar

    var cartItemList = listOf<MenuEntities>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)

        recyclerMenu = findViewById(R.id.cartItemRecyclerView)
        fromRestaurant = findViewById(R.id.txtFromRestaurant)
        btnOrderPlace = findViewById(R.id.btnPlaceOrder)
        toolbar = findViewById(R.id.toolbar)

        setUpToolbar()

        layoutManager = LinearLayoutManager(this)

        cartItemList = RetrieveCartItems(this).execute().get()

        val restaurantId = intent.getStringExtra("restaurantId")
        val restaurantName = intent.getStringExtra("restaurantName")
        val bundle = intent.getBundleExtra("details")

        var totalPrice = 0
        for (i in cartItemList.indices) {
            totalPrice += cartItemList[i].menuItemCost.toInt()
        }
        val totalPriceText = "Place Order(Total: Rs.$totalPrice)"

        btnOrderPlace.text = totalPriceText

        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, RestaurantMenuActivity::class.java)
            intent.putExtra("restaurant_id", restaurantId)
            intent.putExtra("restaurant_name", restaurantName)
            intent.putExtra("details", bundle)
            startActivity(intent)
        }

        recyclerAdapter = CartRecyclerAdapter(this, cartItemList)
        recyclerMenu.adapter = (recyclerAdapter)
        recyclerMenu.layoutManager = layoutManager

        fromRestaurant.text = "Ordering From: $restaurantName"

        btnOrderPlace.setOnClickListener {

            val queue = Volley.newRequestQueue(this@MyCartActivity)

            val url = "http://13.235.250.119/v2/place_order/fetch_result/"

            val jsonParams = JSONObject()
            val jsonParamFood = JSONArray()

            if (bundle != null) {
                jsonParams.put("user_id", bundle.getString("id"))
                jsonParams.put("restaurant_id", restaurantId)
                jsonParams.put("total_cost", totalPrice)
            }

            var cartitemid: String
            for (i in cartItemList.indices) {
                val jsonParamObj = JSONObject()
                cartitemid = cartItemList[i].menuItem_id.toString()
                jsonParamObj.put("food_item_id", cartitemid)

                jsonParamFood.put(i, jsonParamObj)
            }

            jsonParams.put("food", jsonParamFood)

            if (ConnectionManager().checkConnectivity(this@MyCartActivity)) {

                val jsonRequest =
                    object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                            try {
                                val success = it.getJSONObject("data").getBoolean("success")

                                if (success) {
                                    val intent = Intent(this, OrderSuccessActivity::class.java)
                                    Toast.makeText(
                                        this@MyCartActivity,
                                        "order placed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    println(jsonParams.toString())
                                    intent.putExtra("details",bundle)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    println("not getting success")
                                }
                            } catch (e: JSONException) {
                                Toast.makeText(
                                    this@MyCartActivity,
                                    "unexpected error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }, Response.ErrorListener {
                            Toast.makeText(
                                this@MyCartActivity,
                                "volly error $it",
                                Toast.LENGTH_SHORT
                            ).show()
                        }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "9bf534118365f1"
                            return headers
                        }
                    }
                queue.add(jsonRequest)
            } else {
                val dialog = AlertDialog.Builder(this@MyCartActivity)
                dialog.setTitle("Error!")
                dialog.setMessage("Internet connection is not found")
                dialog.setPositiveButton("Open Settings") { text, listner ->
                    val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingIntent)
                    this@MyCartActivity.finish()
                }
                dialog.setNegativeButton("Exit") { text, listner ->
                    ActivityCompat.finishAffinity(this@MyCartActivity)
                }
                dialog.create()
                dialog.show()
            }
        }
    }

    class RetrieveCartItems(val context: Context) :
        AsyncTask<Void, Void, List<MenuEntities>>() {
        override fun doInBackground(vararg params: Void?): List<MenuEntities>? {
            val db =
                Room.databaseBuilder(context, menuItemDatabase::class.java, "menuItems-db")
                    .build()

            return db.menuItemDao().getAllMenuItems()
        }
    }

    fun setUpToolbar() {

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}