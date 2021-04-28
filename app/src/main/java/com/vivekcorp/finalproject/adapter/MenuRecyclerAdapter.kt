package com.vivekcorp.finalproject.adapter

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.vivekcorp.finalproject.R
import com.vivekcorp.finalproject.database.Entities
import com.vivekcorp.finalproject.database.FoodDatabase
import com.vivekcorp.finalproject.database.MenuEntities
import com.vivekcorp.finalproject.database.menuItemDatabase
import com.vivekcorp.finalproject.model.Menu

class MenuRecyclerAdapter(val context: Context, val itemList: ArrayList<Menu>,val bundle: Bundle?) :
    RecyclerView.Adapter<MenuRecyclerAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val foodName: TextView = view.findViewById(R.id.txtFoodItem)
        val foodPrice: TextView = view.findViewById(R.id.txtFoodPrice)
        val itemNum: TextView = view.findViewById(R.id.txtItemNum)
        val add: TextView = view.findViewById(R.id.txtAdd)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_menu_item_single_row, parent, false)

        return MenuViewHolder(view)

    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {

        var menuId: String? = "100"

        val menu = itemList[position]
        val price = "Rs. "+menu.foodPrice
        holder.foodName.text = menu.foodName
        holder.foodPrice.text = price
        holder.itemNum.text = menu.num
        menuId = menu.id

        val menuItemEntities = MenuEntities(
            menuId.toInt(),
            holder.foodName.text.toString(),
            menu.foodPrice,
        )
        DBAsyncTask(context,menuItemEntities,4).execute()

        val checkAdd = DBAsyncTask(context, menuItemEntities, 1).execute()
        val isAdd = checkAdd.get()

        if (isAdd) {
            holder.add.text = "Remove"
            holder.add.setBackgroundColor(ContextCompat.getColor(context,R.color.yellow))
            holder.add.setTextColor(ContextCompat.getColor(context,R.color.white))
        } else {
            holder.add.text = "Add"
            holder.add.setBackgroundColor(ContextCompat.getColor(context,R.color.teal_200))
            holder.add.setTextColor(ContextCompat.getColor(context,R.color.white))
        }

        holder.add.setOnClickListener {

            if (!DBAsyncTask(context, menuItemEntities, 1).execute()
                    .get()
            ) {
                val async =
                    DBAsyncTask(context, menuItemEntities, 2).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(
                        context,
                        "menu item added",
                        Toast.LENGTH_SHORT
                    ).show()

                    holder.add.text = "Remove"
                    holder.add.setBackgroundColor(ContextCompat.getColor(context,R.color.yellow))
                    holder.add.setTextColor(ContextCompat.getColor(context,R.color.white))
                } else {
                    Toast.makeText(
                        context,
                        "Some error occurred!",
                        Toast.LENGTH_SHORT
                    ).show()

                    holder.add.text = "Add"
                    holder.add.setBackgroundColor(ContextCompat.getColor(context,R.color.teal_200))
                    holder.add.setTextColor(ContextCompat.getColor(context,R.color.white))
                }
            } else {
                val async =
                    DBAsyncTask(context, menuItemEntities, 3).execute()
                val result = async.get()

                if (result) {
                    Toast.makeText(
                        context,
                        "menu item removed",
                        Toast.LENGTH_SHORT
                    ).show()

                    holder.add.text = "Add"
                    holder.add.setBackgroundColor(ContextCompat.getColor(context,R.color.teal_200))
                    holder.add.setTextColor(ContextCompat.getColor(context,R.color.white))
                } else {
                    Toast.makeText(
                        context,
                        "Some error occured",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class DBAsyncTask(
        val context: Context,
        private val menuEntity: MenuEntities,
        private val mode: Int
    ) :
        AsyncTask<Void, Void, Boolean>() {

        /*
            Mode 1 -> Check DB if the book is favourite or not
            Mode 2 -> Save the book into DB as favourite
            Mode 3 -> Remove the favourite book
             */

        val db = Room.databaseBuilder(context, menuItemDatabase::class.java, "menuItems-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {
                1 -> {
//                    Mode 1 -> Check DB if the book is favourite or not
                    val book: MenuEntities = db.menuItemDao().getMenuItemsById(menuEntity.menuItem_id.toString())
                    db.close()
                    return book != null
                }
                2 -> {
//                    Mode 2 -> Save the book into DB as favourite
                    db.menuItemDao().insertMenuItem(menuEntity)
                    db.close()
                    return true
                }
                3 -> {
//                    Mode 3 -> Remove the favourite book
                    db.menuItemDao().deleteMenuItem(menuEntity)
                    db.close()
                    return true
                }
                4 -> {
                    db.menuItemDao().deleteTable()
                    db.close()
                    return true
                }
            }
            return false
        }
    }
}