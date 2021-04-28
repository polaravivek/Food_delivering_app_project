package com.vivekcorp.finalproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vivekcorp.finalproject.R
import com.vivekcorp.finalproject.database.Entities

class FavoriteRecyclerAdapter (val context: Context, val foodList: List<Entities>): RecyclerView.Adapter<FavoriteRecyclerAdapter.FavouriteViewHolder>() {

    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val favFoodName: TextView = view.findViewById(R.id.favFoodName)
        val favFoodCost: TextView = view.findViewById(R.id.favFoodCost)
        val favFoodRating: TextView = view.findViewById(R.id.favFoodRating)
        val favFoodImage: ImageView = view.findViewById(R.id.favFoodImage)
        val favFoodLike: ImageButton = view.findViewById(R.id.btnFavouriteAlready)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourite_single_row,parent,false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {

        val food = foodList[position]
        holder.favFoodName.text = food.foodName
        holder.favFoodCost.text = food.foodCost
        holder.favFoodRating.text = food.foodRating
        Picasso.get().load(food.foodImage).error(R.drawable.food).into(holder.favFoodImage)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }
}