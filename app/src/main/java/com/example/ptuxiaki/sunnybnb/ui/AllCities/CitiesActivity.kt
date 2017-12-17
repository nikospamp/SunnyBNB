package com.example.ptuxiaki.sunnybnb.ui.AllCities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.ptuxiaki.sunnybnb.R
import com.google.firebase.database.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class CitiesActivity : AppCompatActivity() {

    private lateinit var searchReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)

        searchReference = FirebaseDatabase.getInstance().reference.child("RESERVATIONS")

        val dates = getDates("2017-11-08", "2017-11-10")

        searchReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError?) {
                System.out.println(error.toString())
            }

            override fun onDataChange(snap: DataSnapshot?) {
                for (x in snap?.children!!) {
                    val dataSnapshot = x.value as HashMap<*, *>

                    if (dates.all { dataSnapshot.containsKey(it) }) {

                        Log.d("Search Date", "${x.key}: Reserved")

                    } else {

                        Log.d("Search Date", "${x.key}: No Reservation for these dates ")

                    }
                }
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