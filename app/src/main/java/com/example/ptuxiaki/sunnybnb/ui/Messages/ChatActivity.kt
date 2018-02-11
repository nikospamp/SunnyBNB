package com.example.ptuxiaki.sunnybnb.ui.Messages

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.ptuxiaki.sunnybnb.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private lateinit var currentUser: FirebaseUser
    private lateinit var chatReference: DatabaseReference
    private lateinit var chatUsersList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.title = "Chat History"

        currentUser = FirebaseAuth.getInstance().currentUser!!
        chatReference = FirebaseDatabase.getInstance().reference.child("CHAT").child(currentUser.uid)

        chatReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                chatUsersList = mutableListOf()

                snapshot.children.mapTo(chatUsersList) { it.key }

                val chatAdapter = ChatAdapter(chatUsersList,
                        listener = { friendUid ->
                            val userReference = FirebaseDatabase.getInstance().reference.child("USERS").child(friendUid)
                            userReference.addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError?) {
                                }

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val userName = snapshot.child("displayName").value!!.toString()
                                    val msgIntent = Intent(applicationContext, MessagesActivity::class.java)
                                    msgIntent.putExtra("from_user_id", friendUid)
                                    msgIntent.putExtra("from_user_name", userName)
                                    startActivity(msgIntent)
                                }

                            })

                        }, context = applicationContext)

                chat_main_rec.adapter = chatAdapter
            }
        })
    }
}
