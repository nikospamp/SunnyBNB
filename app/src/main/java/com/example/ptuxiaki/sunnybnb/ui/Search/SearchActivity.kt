package com.example.ptuxiaki.sunnybnb.ui.Search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.ptuxiaki.sunnybnb.R
import com.example.ptuxiaki.sunnybnb.ui.HouseDetails.HouseDetailsActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_search.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SearchActivity : AppCompatActivity() {

    private val searchHousesAdapter: SearchAdapter = SearchAdapter(null, null, null)

    private lateinit var searchHousesRec: RecyclerView

    private lateinit var searchReference: DatabaseReference

    private var searchHousesList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar?.title = "Search"

        searchHousesRec = findViewById(R.id.search_main_rec)

        searchHousesRec.adapter = searchHousesAdapter

        searchHousesAdapter.context = applicationContext

        searchHousesAdapter.listener = { houseId ->
            Log.d("searchHousesAdapter", houseId)
            val houseDetailsIntent = Intent(this@SearchActivity, HouseDetailsActivity::class.java)
            houseDetailsIntent.putExtra("house_id", houseId)
            startActivity(houseDetailsIntent)
        }


        searchReference = FirebaseDatabase.getInstance().reference.child("RESERVATIONS")

        search_button.setOnClickListener {


            populateRecycler()


        }


    }

    private fun populateRecycler() {
        val dates = getDates("2017-11-08", "2017-11-10")

        searchReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError?) {
                System.out.println(error.toString())
            }

            override fun onDataChange(snap: DataSnapshot?) {
                searchHousesList = arrayListOf()

                for (x in snap?.children!!) {
                    val dataSnapshot = x.value as HashMap<*, *>

                    if (dates.any { dataSnapshot.containsKey(it) }) {

                        Log.d("Search Date", "${x.key}: Reserved")

                    } else {

                        Log.d("Search Date", "${x.key}: No Reservation for these dates ")
                        searchHousesList.add(x.key)

                    }
                }

                if (searchHousesList.size > 0) {
                    search_prompt_txt.visibility = View.GONE
                    searchHousesRec.visibility = View.VISIBLE
                } else {
                    search_prompt_txt.visibility = View.VISIBLE
                    searchHousesRec.visibility = View.GONE
                }

                Log.d("Search Date", "Houses Added: $searchHousesList")


                searchHousesRec.post({
                    searchHousesAdapter.items = searchHousesList
                    searchHousesAdapter.notifyDataSetChanged()
                })
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDates(dateString1: String, dateString2: String): List<String> {
        val dates = ArrayList<String>()
        val df1 = SimpleDateFormat("yyyy-MM-dd")

        var date1: Date? = null
        var date2: Date? = null

        try {
            date1 = df1.parse(dateString1)
            date2 = df1.parse(dateString2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val cal1 = Calendar.getInstance()
        cal1.time = date1


        val cal2 = Calendar.getInstance()
        cal2.time = date2

        while (!cal1.after(cal2)) {
            val day = cal1.get(Calendar.DAY_OF_MONTH)
            var dayExtraZero = ""
            if (day < 10)
                dayExtraZero = "0"

            val month = cal1.get(Calendar.MONTH) + 1
            var monthExtraZero = ""
            if (month < 10)
                monthExtraZero = "0"

            val year = cal1.get(Calendar.YEAR)
            dates.add(year.toString() + "-" + monthExtraZero + month + "-" + dayExtraZero + day)
            cal1.add(Calendar.DATE, 1)
        }
        return dates
    }
}
