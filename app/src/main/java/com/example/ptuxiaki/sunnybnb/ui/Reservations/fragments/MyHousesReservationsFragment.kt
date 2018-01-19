package com.example.ptuxiaki.sunnybnb.ui.Reservations.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.ptuxiaki.sunnybnb.R
import com.example.ptuxiaki.sunnybnb.ui.Reservations.ReservationsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_my_houses_reservations.view.*
import kotlinx.android.synthetic.main.fragment_personal_reservations.view.*

class MyHousesReservationsFragment : Fragment() {

    private lateinit var rootRef: DatabaseReference
    private lateinit var currentUserUid: String

    private lateinit var reservationDates: MutableList<String>
    private lateinit var houseIds: MutableList<String>
    private lateinit var visitorsName: MutableList<String>

    private lateinit var reservationsAdapter: ReservationsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_my_houses_reservations, container, false)

        rootRef = FirebaseDatabase.getInstance().reference

        currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid

        getReservationData(view)

        return view
    }

    private fun getReservationData(view: View) {
        rootRef.child("RESERVATIONS")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        reservationDates = mutableListOf()
                        houseIds = mutableListOf()
                        visitorsName = mutableListOf()

                        for (x in dataSnapshot.children) {
                            val hid = x.key
                            for (y in x.children) {
                                val date = y.key
                                if (y.child("owner").value == currentUserUid) {


                                    reservationDates.add(date)
                                    houseIds.add(hid)
                                    visitorsName.add(y.child("visitor").value.toString())

                                }
                            }
                        }

                        reservationsAdapter = ReservationsAdapter(reservationDates.reversed(),
                                houseIds.reversed(),
                                visitorsName.reversed(), context, listener = { chosenCity ->
                        })

                        view.reservations_my_houses_recycler.adapter = reservationsAdapter

                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
    }

}
