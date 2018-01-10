package com.example.ptuxiaki.sunnybnb.ui.Friends

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ptuxiaki.sunnybnb.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.friend_single.view.*

/**
 * Created by Pampoukidis on 9/1/2018.
 */
class FriendsAdapter(var friends: List<String>?, var dates: List<String>?, var listener: ((friendUid: String) -> Unit)?, var context: Context?) : RecyclerView.Adapter<FriendsAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.friend_single, parent, false)
        val customViewHolder = CustomViewHolder(itemView, context)

        itemView.setOnClickListener({
            val item = friends?.getOrNull(customViewHolder.adapterPosition) ?: return@setOnClickListener
            listener?.invoke(item)
        })

        return customViewHolder
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val friendUID = friends?.get(position)
        val dateAdded = dates?.get(position)

        val ref = FirebaseDatabase.getInstance().reference.child("USERS").child(friendUID)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("FriendsAdapter", snapshot.toString())
                val friendName = snapshot.child("displayName").value.toString()
                val friendImage = snapshot.child("photoUrl").value.toString()
                holder?.bindTo(friendName, dateAdded, friendImage)
            }

        })


    }

    override fun getItemCount(): Int = friends?.count() ?: 0

    class CustomViewHolder(var view: View, private val context: Context?) : RecyclerView.ViewHolder(view) {
        fun bindTo(friendName: String?, dateAdded: String?, friendImage: String) {
            view.friend_single_name.text = friendName
            view.friend_single_date.text = dateAdded
            Picasso.with(context).load(friendImage)
                    .placeholder(R.drawable.default_profile_image).into(view.friend_single_image)

        }
    }

}
