package com.example.ptuxiaki.sunnybnb.ui.TopDestinations

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.ptuxiaki.sunnybnb.R
import com.google.firebase.database.*


class TopDestinationsActivity : AppCompatActivity() {

    private lateinit var topDestinationsDb: DatabaseReference
    private val map: HashMap<String, Int> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_destinations)



        topDestinationsDb = FirebaseDatabase.getInstance().reference
                .child("HOUSES")

        val query = topDestinationsDb.orderByChild("city")

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snap: DataSnapshot) {
                for (tempSnap in snap.children) run {

                    val currentCity: String = tempSnap.child("city").value.toString()

                    if (map.containsKey(currentCity)) {
                        val counter: Int? = map[currentCity]
                        map.put(key = currentCity, value = counter!!.plus(1))
                    } else {
                        map.put(key = currentCity, value = 1)

                    }
                }

                Log.d("TopOnDataChange", map.toString())
            }
        })
    }
}
