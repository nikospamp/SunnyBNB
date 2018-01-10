package com.example.ptuxiaki.sunnybnb

import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Pampoukidis on 10/1/2018.
 */
abstract class BaseActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mUserDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
            .child("USERS").child(mAuth.currentUser?.uid)

    override fun onResume() {
        super.onResume()
        if (mAuth.currentUser != null) {
            mUserDatabase.child("online").setValue(true)
        }
    }

    override fun onPause() {
        super.onPause()
        if (mAuth.currentUser != null) {
            mUserDatabase.child("online").setValue(false)
        }
    }
}