package com.example.ptuxiaki.sunnybnb

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * Created by Pampoukidis on 10/1/2018.
 */
class SunnyBnb : Application() {

    private lateinit var mUserDatabase: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        mAuth = FirebaseAuth.getInstance()
        mUserDatabase = FirebaseDatabase.getInstance().reference
                .child("USERS").child(mAuth.currentUser?.uid)

        mUserDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(snapshot: DataSnapshot?) {
                if (snapshot != null)
                    mUserDatabase.child("online").onDisconnect().setValue(false)
            }
        })
    }
}