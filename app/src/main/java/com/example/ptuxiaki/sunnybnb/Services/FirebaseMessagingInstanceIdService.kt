package com.example.ptuxiaki.sunnybnb.Services

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by Pampoukidis on 8/1/2018.
 */
class FirebaseMessagingInstanceIdService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        Log.d("MessagingInstance", FirebaseInstanceId.getInstance().token)
    }
}