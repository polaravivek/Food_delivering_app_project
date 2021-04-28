package com.vivekcorp.finalproject.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.squareup.picasso.Picasso
import com.vivekcorp.finalproject.R
import com.vivekcorp.finalproject.activity.RestaurantMenuActivity
import com.vivekcorp.finalproject.model.Food
import com.vivekcorp.finalproject.database.Entities
import com.vivekcorp.finalproject.database.FoodDatabase
import java.util.*
import kotlin.collections.ArrayList

class HomeRecyclerAdapter(
    val context: Context,
    itemList: ArrayList<Food>,
    val bundle: Bundle
) :
    RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>(), Filterable {

    var itemList: ArrayList<Food>
    var itemListAll: ArrayList<Food>

    init {
        this.itemList = itemList
        itemListAll = ArrayList()
        itemListAll.addAll(itemList)
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val foodName: TextView = view.findViewById(R.id.foodName)
        val foodCost: TextView = view.findViewById(R.id.foodCost)
        val foodRating: TextView = view.findViewById(R.id.foodRating)
        val foodImage: ImageView = view.findViewById(R.id.foodImage)
        val foodFavourite: ImageButton = view.findViewById(R.id.btnfavoriteBorder)
        val llcontent: LinearLayout = view.findViewById(R.id.llcontent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_home_single_row, parent, false)

        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        var foodId: String? = "100"

        val context: Context = holder.itemView.context
        val food = itemList[position]
        val concateStr = "Rs. " + food.foodCost + "/person"
        holder.foodName.text = food.foodName
        holder.foodCost.text = concateStr
        holder.foodRating.text = food.foodRating
        Picasso.get().load(food.foodImage).error(R.drawable.food).into(holder.foodImage)

        foodId = food.foodId
        val foodEntities = Entities(
            foodId.toInt(),
            holder.foodName.text.toString(),
            holder.foodCost.text.toString(),
            holder.foodRating.text.toString(),
            food.foodImage
        )

        val checkFav = DBAsyncTask(context, foodEntities, 1).execute()
        val isFav = checkFav.get()

        if (isFav) {
            holder.foodFavourite.setImageResource(R.drawable.ic_filled_favorite)

        } else {
            holder.foodFavourite.setImageResource(R.drawable.ic_border_favorite)
        }

        holder.foodFavourite.setOnClickListener {

            if (!DBAsyncTask(context, foodEntities, 1).execute()
                    .get()
            ) {
                val async =
                    DBAsyncTask(context, foodEntities, 2).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(
                        context,
                        "food added to favourites",
                        Toast.LENGTH_SHORT
                    ).show()

                    holder.foodFavourite.setImageResource(R.drawable.ic_filled_favorite)
                } else {
                    Toast.makeText(
                        context,
                        "Some error occurred!",
                        Toast.LENGTH_SHORT
                    ).show()

                    holder.foodFavourite.setImageResource(R.drawable.ic_border_favorite)
                }
            } else {
                val async =
                    DBAsyncTask(context, foodEntities, 3).execute()
                val result = async.get()

                if (result) {
                    Toast.makeText(
                        context,
                        "food removed from favorites",
                        Toast.LENGTH_LONG
                    ).show()

                    holder.foodFavourite.setImageResource(R.drawable.ic_border_favorite)
                } else {
                    Toast.makeText(
                        context,
                        "Some error occured",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        holder.llcontent.setOnClickListener {
            val intent = Intent(context, RestaurantMenuActivity::class.java)
            intent.putExtra("details", bundle)
            intent.putExtra("restaurant_id", food.foodId)
            intent.putExtra("restaurant_name", food.foodName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class DBAsyncTask(
        val context: Context,
        private val foodEntity: Entities,
        private val mode: Int
    ) :
        AsyncTask<Void, Void, Boolean>() {

        /*
            Mode 1 -> Check DB if the book is favourite or not
            Mode 2 -> Save the book into DB as favourite
            Mode 3 -> Remove the favourite book
             */

        val db = Room.databaseBuilder(context, FoodDatabase::class.java, "foods-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {
                1 -> {
//                    Mode 1 -> Check DB if the book is favourite or not
                    val book: Entities = db.foodDao().getFoodById(foodEntity.food_id.toString())
                    db.close()
                    return book != null
                }
                2 -> {
//                    Mode 2 -> Save the book into DB as favourite
                    db.foodDao().insertFood(foodEntity)
                    db.close()
                    return true
                }
                3 -> {
//                    Mode 3 -> Remove the favourite book
                    db.foodDao().deleteFood(foodEntity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    itemList = itemListAll
                } else {
                    val resultList = ArrayList<Food>()
                    for (row in itemListAll) {
                        if (row.foodName.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    itemList = resultList
                    println(itemList)
                }
                val filterResults = FilterResults()
                filterResults.values = itemList
                println(filterResults.values)
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                itemList = results?.values as ArrayList<Food>
                notifyDataSetChanged()
            }
        }
    }
}