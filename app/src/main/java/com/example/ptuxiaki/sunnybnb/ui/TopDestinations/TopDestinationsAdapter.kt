package com.example.ptuxiaki.sunnybnb.ui.TopDestinations

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ptuxiaki.sunnybnb.R
import kotlinx.android.synthetic.main.top_destination_single.view.*

/**
 * Created by arxdev30 on 8/12/2017.
 */

class TopDestinationsAdapter(var cities: ArrayList<String>?, var citiesCounter: ArrayList<Int>?, var listener: ((chosenCity: String) -> Unit)?)
    : RecyclerView.Adapter<TopDestinationsAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.top_destination_single, parent, false)

        for ((index, value) in citiesCounter?.withIndex()!!) {
            Log.d("TopDestinationsAdapter", "$index: $value")
        }

        val customViewHolder = CustomViewHolder(itemView)

        itemView.setOnClickListener({
            val item = cities?.getOrNull(customViewHolder.adapterPosition) ?: return@setOnClickListener
            listener?.invoke(item)
        })

        return customViewHolder
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val cityName = cities?.get(position)
        val cityCounter = citiesCounter?.get(position)
        holder?.bindTo(cityName, cityCounter)
    }

    override fun getItemCount(): Int = cities?.count() ?: 0

    class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val cityNameTv: TextView = view.top_destination_single_city_name
        private val cityCounterTv = view.top_destination_single_counter

        fun bindTo(cityName: String?, cityCounter: Int?) {
            cityNameTv.text = cityName
            cityCounterTv.text = cityCounter.toString()
        }

    }

}
