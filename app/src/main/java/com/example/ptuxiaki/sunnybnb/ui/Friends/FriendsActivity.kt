package com.example.ptuxiaki.sunnybnb.ui.Friends

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.ptuxiaki.sunnybnb.Models.Friends
import com.example.ptuxiaki.sunnybnb.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_friends.*
import kotlinx.android.synthetic.main.friend_single.view.*

class FriendsActivity : AppCompatActivity() {

    private lateinit var friendsDb: DatabaseReference

    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
        supportActionBar?.title = "Friends"

        friends_main_rec.layoutManager = LinearLayoutManager(this)
        friends_main_rec.setHasFixedSize(true)


        currentUser = FirebaseAuth.getInstance().currentUser

        friendsDb = FirebaseDatabase.getInstance().reference.child("FRIENDS")
                .child(currentUser!!.uid)

        val friendsRecyclerViewAdapter = object : FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(
                Friends::class.java,
                R.layout.friend_single,
                FriendsViewHolder::class.java,
                friendsDb) {

            override fun populateViewHolder(viewHolder: FriendsViewHolder, model: Friends, position: Int) {
                Log.d("FriendsList", model.toString())
                viewHolder.setDate(model.date)
            }

        }

        friends_main_rec.adapter = friendsRecyclerViewAdapter
    }


    class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setDate(date: String) {
            itemView.friend_single_date.text = date
            Log.d("FriendsList", date)
        }

    }
}
