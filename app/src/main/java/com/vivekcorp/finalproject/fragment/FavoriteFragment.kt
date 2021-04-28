package com.vivekcorp.finalproject.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.vivekcorp.finalproject.R
import com.vivekcorp.finalproject.adapter.FavoriteRecyclerAdapter
import com.vivekcorp.finalproject.database.Entities
import com.vivekcorp.finalproject.database.FoodDatabase

class FavoriteFragment : Fragment() {

    lateinit var recyclerFavourite: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavoriteRecyclerAdapter
    var dbFoodList = listOf<Entities>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_favorite, container, false)

        recyclerFavourite = view.findViewById(R.id.recyclerFavourite)
        layoutManager = GridLayoutManager(activity as Context, 2)

        dbFoodList = RetrieveFavorites(activity as Context).execute().get()

        if (activity != null){
            recyclerAdapter = FavoriteRecyclerAdapter(activity as Context,dbFoodList)
            recyclerFavourite.adapter = (recyclerAdapter)
            recyclerFavourite.layoutManager = layoutManager
        }

        return view
    }

    class RetrieveFavorites(val context: Context): AsyncTask<Void, Void, List<Entities>>(){
        override fun doInBackground(vararg params: Void?): List<Entities> {
            val db = Room.databaseBuilder(context, FoodDatabase::class.java,"foods-db").build()

            return db.foodDao().getAllFoods()
        }

    }

}