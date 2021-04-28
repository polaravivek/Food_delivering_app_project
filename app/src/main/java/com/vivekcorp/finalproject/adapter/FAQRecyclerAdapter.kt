package com.vivekcorp.finalproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vivekcorp.finalproject.R
import com.vivekcorp.finalproject.model.FAQ

class FAQRecyclerAdapter(val context: Context, private val faqList: ArrayList<FAQ>) :
    RecyclerView.Adapter<FAQRecyclerAdapter.FAQViewHolder>() {

    class FAQViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val question: TextView = view.findViewById(R.id.question)
        val answer: TextView = view.findViewById(R.id.answer)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_faq_single_row, parent, false)

        return FAQViewHolder(view)
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {

        val faq = faqList[position]
        holder.question.text = faq.question
        holder.answer.text = faq.answer
    }

    override fun getItemCount(): Int {
        return faqList.size
    }
}