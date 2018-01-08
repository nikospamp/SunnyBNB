package com.example.ptuxiaki.sunnybnb.Services

import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import com.example.ptuxiaki.sunnybnb.R
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by Pampoukidis on 8/1/2018.
 */

class FirebaseMessagingService : com.google.firebase.messaging.FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        val mBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("New Friend Request")
                .setContentText("A user has sent you request!")


        val mNotificationId = System.currentTimeMillis().toInt()

        val mNotifyMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotifyMgr.notify(mNotificationId, mBuilder.build())
    }

    companion object {
        private val TAG = "FirebaseMessaging"
    }
}
