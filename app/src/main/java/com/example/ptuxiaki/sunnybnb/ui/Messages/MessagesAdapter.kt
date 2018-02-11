package com.example.ptuxiaki.sunnybnb.ui.Messages

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ptuxiaki.sunnybnb.Models.Message
import com.example.ptuxiaki.sunnybnb.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.message_single.view.*

/**
 * Created by Pampoukidis on 16/1/2018.
 */
class MessagesAdapter(var messages: List<Message>?,
                      var context: Context?,
                      var currentUser: String?,
                      var friendUser: String,
                      var imageListener: ((friendUid: String) -> Unit)?) : RecyclerView.Adapter<MessagesAdapter.CustomViewHolder>() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.message_single, parent, false)

        return CustomViewHolder(itemView, context)

    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val message = messages?.get(position)
        holder?.bindTo(message, auth, currentUser, friendUser, imageListener)
    }

    override fun getItemCount(): Int = messages?.count() ?: 0

    class CustomViewHolder(var view: View, private val context: Context?) : RecyclerView.ViewHolder(view) {

        private lateinit var ref: DatabaseReference


        fun bindTo(message: Message?, auth: FirebaseAuth, currentUser: String?, friendUser: String, imageListener: ((friendUid: String) -> Unit)?) {

            ref = FirebaseDatabase.getInstance().reference.child("USERS")



            view.message_body.text = message?.message
            if (auth.currentUser?.uid.equals(message?.from)) {
                view.message_single_image_left.visibility = View.GONE
                view.message_single_image_right.visibility = View.VISIBLE

                ref.child(currentUser).addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError?) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val image = dataSnapshot.child("photoUrl").value.toString()
                        Picasso.with(context).load(image)
                                .placeholder(R.drawable.default_profile_image).into(view.message_single_image_right)
                    }
                })
            } else {
                view.message_single_image_left.visibility = View.VISIBLE
                view.message_single_image_right.visibility = View.GONE

                view.message_body.setBackgroundResource(R.drawable.message_background_alternative)
                view.message_body.setTextColor(Color.BLACK)

                ref.child(friendUser).addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError?) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val image = dataSnapshot.child("photoUrl").value.toString()
                        Picasso.with(context).load(image)
                                .placeholder(R.drawable.default_profile_image).into(view.message_single_image_left)

                        view.message_single_image_left.setOnClickListener({
                            imageListener?.invoke(friendUser)
                        })
                    }
                })
            }

        }
    }

}
