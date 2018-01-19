package com.example.ptuxiaki.sunnybnb.ui.AllCities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.example.ptuxiaki.sunnybnb.BaseActivity
import com.example.ptuxiaki.sunnybnb.Models.City
import com.example.ptuxiaki.sunnybnb.R
import com.example.ptuxiaki.sunnybnb.ui.TopDestinations.CityRoomsActivity
import com.example.ptuxiaki.sunnybnb.ui.TopDestinations.TopDestinationsAdapter
import com.google.firebase.database.*
import java.util.*

class CitiesActivity : BaseActivity() {

    private lateinit var topDestinationsDb: DatabaseReference
    private var map: HashMap<String, Int> = hashMapOf()
    private lateinit var allRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)

        supportActionBar?.title = "All Cities"

        allRecycler = findViewById(R.id.all_cities_rec)

        topDestinationsDb = FirebaseDatabase.getInstance().reference
                .child("HOUSES")

        val query = topDestinationsDb.orderByChild("city")

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
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

                val topDestinationsAdapter: TopDestinationsAdapter?

                topDestinationsAdapter = TopDestinationsAdapter(list, listener = { chosenCity ->
                    val intent = Intent(this@CitiesActivity, CityRoomsActivity::class.java)
                    intent.putExtra("CITY", chosenCity)
                    startActivity(intent)

                })

                allRecycler.adapter = topDestinationsAdapter

                topDestinationsAdapter.notifyDataSetChanged()

            }
        })

    }

}
