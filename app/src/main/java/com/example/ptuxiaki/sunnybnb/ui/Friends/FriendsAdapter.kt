package com.example.ptuxiaki.sunnybnb.ui.Friends

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * Created by Pampoukidis on 9/1/2018.
 */
class FriendsAdapter(var friends: List<String>?, var dates: List<String>?, var listener: ((friendUid: String) -> Unit)?, var context: Context?) : RecyclerView.Adapter<FriendsAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class CustomViewHolder(view: View, private val context: Context?) : RecyclerView.ViewHolder(view)

}
