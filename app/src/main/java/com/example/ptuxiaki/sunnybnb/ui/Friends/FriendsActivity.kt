package com.example.ptuxiaki.sunnybnb.ui.Friends

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.example.ptuxiaki.sunnybnb.BaseActivity
import com.example.ptuxiaki.sunnybnb.R
import com.example.ptuxiaki.sunnybnb.ui.Profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_friends.*

class FriendsActivity : BaseActivity() {

    private lateinit var friendsDb: DatabaseReference

    private var currentUser: FirebaseUser? = null


    private lateinit var friendsList: MutableList<String>

    private lateinit var datesList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
        supportActionBar?.title = "Friends"

        friends_main_rec.layoutManager = LinearLayoutManager(this)
        friends_main_rec.setHasFixedSize(true)

        currentUser = FirebaseAuth.getInstance().currentUser

        friendsDb = FirebaseDatabase.getInstance().reference.child("FRIENDS").child(currentUser!!.uid)

        friendsDb.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                friendsList = mutableListOf()
                datesList = mutableListOf()

                snapshot?.children!!.mapTo(friendsList) { it.key }
                snapshot.children!!.mapTo(datesList) { it.child("date").value.toString() }

                if (friendsList.isEmpty()) {
                    friends_main_rec.visibility = View.INVISIBLE
                    friends_no_text.visibility = View.VISIBLE
                } else {
                    friends_main_rec.visibility = View.VISIBLE
                    friends_no_text.visibility = View.INVISIBLE
                    val friendsAdapter = FriendsAdapter(friends = friendsList,
                            dates = datesList,
                            listener = { friendUid ->
                                val houseDetailsIntent = Intent(this@FriendsActivity, ProfileActivity::class.java)
                                houseDetailsIntent.putExtra("from_user_id", friendUid)
                                startActivity(houseDetailsIntent)
                            },
                            context = applicationContext)

                    friends_main_rec.adapter = friendsAdapter
                }


            }

            override fun onCancelled(error: DatabaseError?) {
                Log.d("FriendsActivity", error?.message)
            }
        })

    }
}
