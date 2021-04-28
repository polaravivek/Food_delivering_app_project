package com.vivekcorp.finalproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vivekcorp.finalproject.R
import com.vivekcorp.finalproject.model.AllDetails

class OrderHistoryAdapter(val context: Context, val allDetails: ArrayList<AllDetails>) :
    RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {

    class OrderHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val restaurantName: TextView = view.findViewById(R.id.restaurantName)
        val dateOfOrderHistory: TextView = view.findViewById(R.id.dateOfOrder)
        val cartItem: RecyclerView = view.findViewById(R.id.itemsRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        return OrderHistoryViewHolder(
            LayoutInflater.from(context).inflate(R.layout.recycler_order_history_single_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val alldetails = allDetails[position]
        holder.restaurantName.text = alldetails.restaurantName
        holder.dateOfOrderHistory.text = alldetails.dateOfOrder

        cartItemRecycler(holder.cartItem,alldetails.menuItem)
    }

    override fun getItemCount(): Int {
        return allDetails.size
    }

    fun cartItemRecycler(recyclerView: RecyclerView,cartItemList: ArrayList<com.vivekcorp.finalproject.model.Menu>){
        val cartItemRecyclerAdapter= CartHistoryRecyclerAdapter(context, cartItemList)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        recyclerView.adapter = cartItemRecyclerAdapter
    }
}