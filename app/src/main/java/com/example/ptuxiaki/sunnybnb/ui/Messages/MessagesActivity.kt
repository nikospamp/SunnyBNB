package com.example.ptuxiaki.sunnybnb.ui.Messages

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import com.example.ptuxiaki.sunnybnb.BaseActivity
import com.example.ptuxiaki.sunnybnb.R
import com.example.ptuxiaki.sunnybnb.ui.Utilities.GetTimeAgo
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chat_app_bar.*

class MessagesActivity : BaseActivity() {

    private lateinit var userId: String
    private lateinit var userName: String
    private lateinit var rootDb: DatabaseReference

    private lateinit var mChatToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        mChatToolbar = findViewById(R.id.chat_app_bar)
        setSupportActionBar(mChatToolbar)

        userId = intent.getStringExtra(USER_ID_TAG)
        userName = intent.getStringExtra(USER_NAME_TAG)

        rootDb = FirebaseDatabase.getInstance().reference

        val actionBar = supportActionBar

        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowCustomEnabled(true)

        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val actionBarView = inflater.inflate(R.layout.chat_app_bar, null)
        actionBar?.customView = actionBarView

        custom_bar_name.text = userName

        rootDb.child("USERS").child(userId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(snapshot: DataSnapshot?) {
                val online = snapshot?.child("online")?.value.toString()
                val image = snapshot?.child("photoUrl")?.value.toString()

                if (online == "true")
                    custom_bar_last_seen.text = "Online"
                else {
                    val timeAgo = GetTimeAgo()
                    val lastTime = online.toLong()
                    val lastSeenTime = timeAgo.getTimeAgo(lastTime)
                    custom_bar_last_seen.text = lastSeenTime
                }

                Picasso.with(applicationContext).load(image)
                        .placeholder(R.drawable.default_profile_image).into(custom_bar_image)
            }
        })

    }


    companion object {
        private val TAG = "MessagesActivity"
        private val USER_ID_TAG = "from_user_id"
        private val USER_NAME_TAG = "from_user_name"
    }
}
