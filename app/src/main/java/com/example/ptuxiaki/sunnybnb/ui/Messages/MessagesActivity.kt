package com.example.ptuxiaki.sunnybnb.ui.Messages

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import com.example.ptuxiaki.sunnybnb.BaseActivity
import com.example.ptuxiaki.sunnybnb.Models.Message
import com.example.ptuxiaki.sunnybnb.R
import com.example.ptuxiaki.sunnybnb.ui.Utilities.GetTimeAgo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_messages.*
import kotlinx.android.synthetic.main.chat_app_bar.*
import java.util.*

class MessagesActivity : BaseActivity() {

    private lateinit var userId: String
    private lateinit var userName: String
    private lateinit var currentUser: FirebaseUser
    private lateinit var rootDb: DatabaseReference
    private lateinit var mChatToolbar: Toolbar
    private val messagesList = ArrayList<Message>()
    private lateinit var msgAdapter: MessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        mChatToolbar = findViewById(R.id.chat_app_bar)
        setSupportActionBar(mChatToolbar)

        userId = intent.getStringExtra(USER_ID_TAG)
        userName = intent.getStringExtra(USER_NAME_TAG)

        rootDb = FirebaseDatabase.getInstance().reference
        currentUser = FirebaseAuth.getInstance().currentUser!!

        val actionBar = supportActionBar

        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowCustomEnabled(true)

        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val actionBarView = inflater.inflate(R.layout.chat_app_bar, null)
        actionBar?.customView = actionBarView

        custom_bar_name.text = userName

        msgAdapter = MessagesAdapter(messagesList, applicationContext, currentUser.uid, userId)

        messages_rec.adapter = msgAdapter

        loadMessages()

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

        rootDb.child("CHAT").child(currentUser.uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.hasChild(userId)) {
                    val chatMap = hashMapOf<String, Any>()
                    chatMap.put("seen", false)
                    chatMap.put("timestamp", ServerValue.TIMESTAMP)

                    val chatUserMap = hashMapOf<String, Any>()
                    chatUserMap.put("CHAT/" + currentUser.uid + "/" + userId, chatMap)
                    chatUserMap.put("CHAT/" + userId + "/" + currentUser.uid, chatMap)

                    rootDb.updateChildren(chatUserMap) { error, ref ->
                        if (error != null) {
                            Log.d("MessagesActivity", error.message)
                        }
                    }
                }
            }
        })

        messages_send.setOnClickListener({
            sendMessage()
        })

    }

    private fun loadMessages() {
        rootDb.child("MESSAGES").child(currentUser.uid).child(userId).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError?) {
            }

            override fun onChildMoved(snapshot: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot?, p1: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
                try {
                    val message = snapshot.getValue(Message::class.java)
                    Log.d("MSG_LOG", message.toString())

                    if (message != null) {
                        messagesList.add(message)

                        messages_rec.post({
                            msgAdapter.messages = messagesList
                            msgAdapter.notifyDataSetChanged()
                        })
                    }

                } catch (e: Exception) {
                    Log.d("MSG_LOG", e.toString())
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot?) {
            }
        })
    }

    private fun sendMessage() {
        val message = messages_main_body.text.toString().trim()

        if (!TextUtils.isEmpty(message)) {
            val currentUserRef = "MESSAGES/${currentUser.uid}/$userId"
            val displayingUserRef = "MESSAGES/$userId/${currentUser.uid}"

            val messagePushIdRef = rootDb.child("MESSAGES").child(currentUser.uid)
                    .child(userId).push()

            val pushId = messagePushIdRef.key

            val messageMap = hashMapOf<String, Any>()
            messageMap.put("message", message)
            messageMap.put("seen", false)
            messageMap.put("type", "text")
            messageMap.put("time", ServerValue.TIMESTAMP)
            messageMap.put("from", currentUser.uid)

            val messageUserMap = hashMapOf<String, Any>()
            messageUserMap.put("$currentUserRef/$pushId", messageMap)
            messageUserMap.put("$displayingUserRef/$pushId", messageMap)

            rootDb.updateChildren(messageUserMap) { error, ref ->
                if (error != null) {
                    Log.d("MessagesActivity", error.message)
                }

                messages_main_body.setText("")
            }
        }
    }


    companion object {
        private val TAG = "MessagesActivity"
        private val USER_ID_TAG = "from_user_id"
        private val USER_NAME_TAG = "from_user_name"
    }
}
