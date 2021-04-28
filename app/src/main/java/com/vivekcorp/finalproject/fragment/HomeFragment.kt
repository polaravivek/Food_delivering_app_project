package com.vivekcorp.finalproject.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vivekcorp.finalproject.R
import com.vivekcorp.finalproject.adapter.HomeRecyclerAdapter
import com.vivekcorp.finalproject.model.Food
import com.vivekcorp.finalproject.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HomeFragment : Fragment() {

    lateinit var bundle: Bundle

    lateinit var recyclerHome: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    var restaurantName =  ArrayList<String>()

    lateinit var searchView: SearchView

    val foodInfoList = arrayListOf<Food>()

    var ratingComparator = Comparator<Food>{food1, food2 ->

        if (food1.foodRating.compareTo(food2.foodRating, true) == 0) {
            // sort according to name if rating is same
            food1.foodName.compareTo(food2.foodName, true)
        } else {
            food1.foodRating.compareTo(food2.foodRating, true)
        }
    }

    lateinit var recyclerAdapter: HomeRecyclerAdapter

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

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        setHasOptionsMenu(true)

        recyclerHome = view.findViewById(R.id.recyclerHome)

        layoutManager = LinearLayoutManager(activity)

        searchView = view.findViewById(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerAdapter.filter.filter(newText)
                return false
            }

        })

        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                    val dataJson = it.getJSONObject("data")

                    try {
                        val success = dataJson.getBoolean("success")

                        if (success) {

                            println("success")
                            val data = dataJson.getJSONArray("data")

                            for (i in 0 until data.length()) {

                                val foodJsonObject = data.getJSONObject(i)

                                restaurantName.add(i,foodJsonObject.getString("name"))

                                val foodObject = Food(
                                    foodJsonObject.getString("id"),
                                    foodJsonObject.getString("name"),
                                    foodJsonObject.getString("rating"),
                                    foodJsonObject.getString("cost_for_one"),
                                    foodJsonObject.getString("image_url")
                                )

                                foodInfoList.add(foodObject)

                            }
                            recyclerAdapter =
                                HomeRecyclerAdapter(activity as Context, foodInfoList,bundle)

                            recyclerHome.adapter = recyclerAdapter

                            recyclerHome.layoutManager = layoutManager
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

        } else {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.action_sort){
            Collections.sort(foodInfoList, ratingComparator)
            foodInfoList.reverse()
        }

        recyclerAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }
}