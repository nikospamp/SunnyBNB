package com.example.ptuxiaki.sunnybnb.ui.TopDestinations

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.example.ptuxiaki.sunnybnb.R
import com.google.firebase.database.*
import java.util.*


class TopDestinationsActivity : AppCompatActivity() {

    private lateinit var topDestinationsDb: DatabaseReference
    private var map: HashMap<String, Int> = hashMapOf()
    private lateinit var topRec: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_destinations)

        supportActionBar?.title = "Top Destinations"

        topRec = findViewById(R.id.top_destinations_rec)

        topDestinationsDb = FirebaseDatabase.getInstance().reference
                .child("HOUSES")

        val query = topDestinationsDb.orderByChild("city")

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snap: DataSnapshot) {

                map = hashMapOf()

                for (tempSnap in snap.children) run {

                    val currentCity: String = tempSnap.child("city").value.toString()

                    if (map.containsKey(currentCity)) {
                        val counter: Int? = map[currentCity]
                        map.put(key = currentCity, value = counter!!.plus(1))
                    } else {
                        map.put(key = currentCity, value = 1)
                    }
                }

                val citiesList = ArrayList<String>()
                val citiesCounterList = ArrayList<Int>()

                val downMap = hashMapOf<String, String>()

                for (x in map) {
                    downMap.put(x.value.toString(), x.key)
                }

                val tree = TreeMap(downMap).descendingMap()

                for ((index, value) in tree) {
                    Log.d("Tree", "$index $value")
                    citiesList.add(value)
                    citiesCounterList.add(index.toInt())
                }

                val topDestinationsAdapter: TopDestinationsAdapter?

                topDestinationsAdapter = TopDestinationsAdapter(citiesList, citiesCounterList, listener = { chosenCity ->
                    Toast.makeText(this@TopDestinationsActivity, chosenCity, Toast.LENGTH_LONG).show()
                })

                topRec.adapter = topDestinationsAdapter

                topDestinationsAdapter.notifyDataSetChanged()

            }
        })


    }
}
