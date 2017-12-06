package com.example.ptuxiaki.sunnybnb.ui.Favourites

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ptuxiaki.sunnybnb.R

/**
 * Created by Pampoukidis on 6/12/2017.
 */

class FavoritesAdapter(var items: List<String>?, var listener: ((houseId: String) -> Unit)?) : RecyclerView.Adapter<FavoritesAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.house_single, parent, false)

        Log.d("onCreateViewHolder", items?.size.toString())

        return CustomViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val fav = items?.get(position)
        holder?.bindTo(fav)
    }

    override fun getItemCount(): Int = items?.count() ?: 0

    class CustomViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindTo(fav: String?) {
            Log.d("CustomViewHolder", fav)
        }

    }
}