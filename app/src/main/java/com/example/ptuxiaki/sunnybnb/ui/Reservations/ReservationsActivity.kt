package com.example.ptuxiaki.sunnybnb.ui.Reservations

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.ptuxiaki.sunnybnb.R
import com.example.ptuxiaki.sunnybnb.ui.Reservations.fragments.MyHousesReservationsFragment
import com.example.ptuxiaki.sunnybnb.ui.Reservations.fragments.PersonalReservationsFragment
import kotlinx.android.synthetic.main.activity_reservations.*

class ReservationsActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservations)
        supportActionBar?.title = "Reservations"

        pagerAdapter = ViewPagerAdapter(supportFragmentManager)

        pagerAdapter.addFragments(PersonalReservationsFragment(), "Personal")
        pagerAdapter.addFragments(MyHousesReservationsFragment(), "On My Houses")

        reservations_view_pager.adapter = pagerAdapter

        reservations_tab_layout.setupWithViewPager(reservations_view_pager)
    }
}
