package com.example.ptuxiaki.sunnybnb

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue

/**
 * Created by Pampoukidis on 11/1/2018.
 */
class ForegroundBackgroundListener : LifecycleObserver {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mUserDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
            .child("USERS").child(mAuth.currentUser?.uid)

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startSomething() {
        Log.v("ProcessLog", "APP IS ON FOREGROUND")
        if (mAuth.currentUser != null) {
            mUserDatabase.child("online").setValue(true)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopSomething() {
        Log.v("ProcessLog", "APP IS IN BACKGROUND")
        if (mAuth.currentUser != null) {
            mUserDatabase.child("online").setValue(ServerValue.TIMESTAMP)
        }
    }
}