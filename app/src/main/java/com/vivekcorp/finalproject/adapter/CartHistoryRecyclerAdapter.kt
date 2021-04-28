package com.vivekcorp.finalproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vivekcorp.finalproject.R

class CartHistoryRecyclerAdapter(val context: Context, val itemList: ArrayList<com.vivekcorp.finalproject.model.Menu>):
RecyclerView.Adapter<CartHistoryRecyclerAdapter.CartHistoryItemViewHolder>(){

    class CartHistoryItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        val cartMenuItemName: TextView = view.findViewById(R.id.txtMenuItemName)
        val cartMenuItemPrice: TextView = view.findViewById(R.id.txtMenuItemPrice)
        val cartMenuItemNumber: TextView = view.findViewById(R.id.txtMenuItemNum)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHistoryItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_cart_item_single_row,parent,false)

        return CartHistoryItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartHistoryItemViewHolder, position: Int) {
        val cartItems = itemList[position]
        val price = "Rs. ${cartItems.foodPrice}"
        val number = position + 1
        holder.cartMenuItemName.text = cartItems.foodName
        holder.cartMenuItemPrice.text = price
        holder.cartMenuItemNumber.text = number.toString()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}