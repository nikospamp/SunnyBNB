package com.example.ptuxiaki.sunnybnb.Services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.example.ptuxiaki.sunnybnb.R
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by Pampoukidis on 8/1/2018.
 */

class FirebaseMessagingService : com.google.firebase.messaging.FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        val notificationTitle = remoteMessage?.notification?.title
        val notificationBody = remoteMessage?.notification?.body
        val clickAction = remoteMessage?.notification?.clickAction
        val fromUserId = remoteMessage?.data?.get("from_user_id")

        Log.d("FirebaseMessaging", fromUserId)

        val mBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)

        val resultIntent = Intent(clickAction)
        resultIntent.putExtra("Current_User", fromUserId)

        val resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        mBuilder.setContentIntent(resultPendingIntent)

        val mNotificationId = System.currentTimeMillis().toInt()

        val mNotifyMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotifyMgr.notify(mNotificationId, mBuilder.build())


    }

    companion object {
        private val TAG = "FirebaseMessaging"
    }
}
