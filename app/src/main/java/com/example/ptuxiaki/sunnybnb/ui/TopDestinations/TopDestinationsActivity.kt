package com.example.ptuxiaki.sunnybnb.ui.TopDestinations

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.ptuxiaki.sunnybnb.Models.City
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

                val list = mutableListOf<City>()

                for (tempSnap in snap.children) run {

                    val currentCity: String = tempSnap.child("city").value.toString()

                    if (map.containsKey(currentCity)) {
                        val counter: Int? = map[currentCity]
                        map.put(key = currentCity, value = counter!!.plus(1))
                    } else {
                        map.put(key = currentCity, value = 1)
                    }
                }

                for (city in map) {
                    list.add(City(name = city.key, rooms = city.value))
                }

                Log.d("CityList", list.toString())

                Collections.sort(list) { city1, city2 ->
                    city1?.rooms!!.compareTo(city2?.rooms!!)
                }

                val topDestinationsAdapter: TopDestinationsAdapter?

                topDestinationsAdapter = TopDestinationsAdapter(list.reversed(), listener = { chosenCity ->
                    val intent = Intent(this@TopDestinationsActivity, CityRoomsActivity::class.java)
                    intent.putExtra("CITY", chosenCity)
                    startActivity(intent)

                })

                topRec.adapter = topDestinationsAdapter

                topDestinationsAdapter.notifyDataSetChanged()

            }
        })
    }
}
