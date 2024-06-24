package com.example.tutorial

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerViewAdapter: RecyclerView.Adapter<MyViewHolder>() {
    val fruitSalads = listOf<String>("Glass","Stick-o","Mango", "Ice", "Sugar", "Milk", "Sago", "Water", "Yogurt")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_item, parent, false)
        Log.i("RecyclerView", "onCreateViewHolder called")
        return MyViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        Log.i("RecyclerView", "getItemCount called")
        return fruitSalads.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val fruit = fruitSalads[position]
        holder.myTextView.text = fruit
        Log.i("RecyclerView", "onBindViewHolder called for position $position")
    }
}


class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
    val myTextView: TextView = view.findViewById(R.id.tvName)

}