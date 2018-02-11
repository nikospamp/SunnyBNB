package com.example.ptuxiaki.sunnybnb.ui.Messages


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ptuxiaki.sunnybnb.R
import com.example.ptuxiaki.sunnybnb.ui.Utilities.GetTimeAgo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.friend_single.view.*

/**
 * Created by Pampoukidis on 11/2/2018.
 */
class ChatAdapter(var chatUsersList: List<String>?, var listener: ((friendUid: String) -> Unit)?, var context: Context?)
    : RecyclerView.Adapter<ChatAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.friend_single, parent, false)
        val customViewHolder = CustomViewHolder(itemView, context)

        itemView.setOnClickListener({
            val item = chatUsersList?.getOrNull(customViewHolder.adapterPosition)
                    ?: return@setOnClickListener
            listener?.invoke(item)
        })

        return customViewHolder
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {

        val currentUser = FirebaseAuth.getInstance().currentUser

        val friendUID = chatUsersList?.get(position)

        val ref = FirebaseDatabase.getInstance().reference.child("USERS").child(friendUID)

        val lastMessageReference = FirebaseDatabase.getInstance().reference.child("MESSAGES")
                .child(currentUser?.uid).child(friendUID)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("FriendsAdapter", snapshot.toString())
                val friendName = snapshot.child("displayName").value.toString()
                val friendImage = snapshot.child("photoUrl").value.toString()
                val friendOnlineStatus = snapshot.child("online").value.toString().toBoolean()


                lastMessageReference.orderByKey().limitToLast(1).addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {

                    }

                    override fun onDataChange(messageSnapshot: DataSnapshot) {
                        messageSnapshot.children
                                .map { it.child("time").value.toString() }
                                .forEach { holder?.bindTo(friendName, friendImage, friendOnlineStatus, it) }
                    }
                })
            }

        })


    }

    override fun getItemCount(): Int = chatUsersList?.count() ?: 0

    class CustomViewHolder(var view: View, private val context: Context?) : RecyclerView.ViewHolder(view) {
        fun bindTo(friendName: String?, friendImage: String, friendOnlineStatus: Boolean, lastMessageTimeAgo: String) {
            val timeAgo = GetTimeAgo()
            view.friend_single_name.text = friendName
            val timeAgo1 = "Last message " + timeAgo.getTimeAgo(lastMessageTimeAgo.toLong())
            view.friend_single_date.text = timeAgo1
            Picasso.with(context).load(friendImage)
                    .placeholder(R.drawable.default_profile_image).into(view.friend_single_image)
            if (friendOnlineStatus) {
                view.friend_single_online_status.visibility = View.VISIBLE
            } else {
                view.friend_single_online_status.visibility = View.INVISIBLE
            }

        }
    }
}
