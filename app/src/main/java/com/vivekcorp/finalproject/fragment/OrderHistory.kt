package com.vivekcorp.finalproject.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vivekcorp.finalproject.R
import com.vivekcorp.finalproject.adapter.OrderHistoryAdapter
import com.vivekcorp.finalproject.model.AllDetails
import com.vivekcorp.finalproject.model.Menu
import com.vivekcorp.finalproject.util.ConnectionManager
import org.json.JSONException


class OrderHistory : Fragment() {

    lateinit var bundle: Bundle

    lateinit var recyclerOrderHistory: RecyclerView

    lateinit var recyclerAdapter: OrderHistoryAdapter

    val menuInfoList = arrayListOf<AllDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bundle = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_history, container, false)

        recyclerOrderHistory = view.findViewById(R.id.orderHistoryRecyclerView)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)

        val data: String = bundle.getString("data").toString()
        var userId: String? = null
        if(data == "login"){
            userId = bundle.getString("id")
            println("login id $userId")
        }else if(data == "register"){
            userId = bundle.getString("id")
            println("register id $userId")
        }

        recyclerOrderHistory.addItemDecoration(
            DividerItemDecoration(
                recyclerOrderHistory.context,
                DividerItemDecoration.VERTICAL
            )
        )

        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v2/orders/fetch_result/$userId"

        if(ConnectionManager().checkConnectivity(activity as Context)){

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    val dataJson = it.getJSONObject("data")

                    try {
                        val success = dataJson.getBoolean("success")

                        if (success) {
                            println("Success")
                            val data = dataJson.getJSONArray("data")

                            for (i in 0 until data.length()) {

                                val dataJsonObject = data.getJSONObject(i)

                                val foodItem = dataJsonObject.getJSONArray("food_items")
                                var menuObject = ArrayList<Menu>()

                                for (j in 0 until foodItem.length()) {

                                    val foodItemJsonObject = foodItem.getJSONObject(j)

                                    menuObject.add(
                                        Menu(
                                            j.toString(),
                                            foodItemJsonObject.getString("food_item_id"),
                                            foodItemJsonObject.getString("name"),
                                            foodItemJsonObject.getString("cost")
                                        )
                                    )
                                }

                                val text = dataJsonObject.getString("order_placed_at")

                                val words = text.split("\\s+".toRegex())

                                val restaurantObject = AllDetails(
                                    dataJsonObject.getString("restaurant_name"),
                                    words[0],
                                    menuObject
                                )

                                menuInfoList.add(restaurantObject)

                                recyclerAdapter = OrderHistoryAdapter(
                                    activity as Context,
                                    menuInfoList
                                )

                                recyclerOrderHistory.adapter = recyclerAdapter

                                recyclerOrderHistory.layoutManager = layoutManager
                            }
                        } else {
                            Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
                        }
                        println("Response is $it")
                    } catch (e: JSONException) {
                        Toast.makeText(
                            context,
                            "some unexpected error occurred!!!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }, Response.ErrorListener {
                    if (activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "Volly error occured",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "9bf534118365f1"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        }else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error!")
            dialog.setMessage("Internet connection is not found")
            dialog.setPositiveButton("Open Settings") { text, listner ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listner ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
        return view
    }
}