package com.example.ptuxiaki.sunnybnb.ui.Search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.ptuxiaki.sunnybnb.BaseActivity
import com.example.ptuxiaki.sunnybnb.R
import com.example.ptuxiaki.sunnybnb.ui.HouseDetails.HouseDetailsActivity
import com.google.firebase.database.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.activity_search.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SearchActivity : BaseActivity(), CalendarFragment.PassCalendarInterface {


    private val searchHousesAdapter: SearchAdapter = SearchAdapter(null, null, null)

    private lateinit var searchHousesRec: RecyclerView

    private lateinit var searchReference: DatabaseReference

    private var searchHousesList = ArrayList<String>()

    private var finalDates: MutableList<CalendarDay>? = null

    private val manager = supportFragmentManager

    private var searchBtnFlag = true

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

            if (searchBtnFlag) {
                search_main_rec.visibility = View.GONE
                search_prompt_txt.visibility = View.GONE
                loadCalendar()
            } else {
                if (finalDates != null) {
                    if (finalDates!!.size > 0)
                        populateRecycler(finalDates)
                }
            }
        }
    }

    override fun calendarDatesForSearch(dates: MutableList<CalendarDay>?) {
        searchBtnFlag = false
        search_button.text = "Submit"
        finalDates = dates
    }

    private fun loadCalendar() {
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.search_calendar_container, CalendarFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun populateRecycler(finalDates: MutableList<CalendarDay>?) {
        manager.popBackStackImmediate()

        search_main_rec.visibility = View.VISIBLE

        val firstDay: String = finalDates!![0].year.toString() + "-" +
                (finalDates[0].month + 1) + "-" +
                finalDates[0].day

        val lastDay: String = finalDates[finalDates.size - 1].year.toString() + "-" +
                (finalDates[finalDates.size - 1].month + 1) + "-" +
                finalDates[finalDates.size - 1].day

        val dates = getDates(firstDay, lastDay)

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
                    search_prompt_txt.text = "No available houses for these dates"
                    searchHousesRec.visibility = View.GONE
                }

                Log.d("Search Date", "Houses Added: $searchHousesList")


                searchHousesRec.post({
                    searchHousesAdapter.items = searchHousesList
                    searchHousesAdapter.notifyDataSetChanged()
                })

                searchBtnFlag = true
                search_button.text = "Search Again"
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
