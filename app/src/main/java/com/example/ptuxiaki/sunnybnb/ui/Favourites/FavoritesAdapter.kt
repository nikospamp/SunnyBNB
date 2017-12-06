package com.example.ptuxiaki.sunnybnb.ui.Favourites

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * Created by Pampoukidis on 6/12/2017.
 */

class FavoritesAdapter(var items: List<String>, var listener: ((houseId: String) -> Unit)?) : RecyclerView.Adapter<FavoritesAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class CustomViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    }
}