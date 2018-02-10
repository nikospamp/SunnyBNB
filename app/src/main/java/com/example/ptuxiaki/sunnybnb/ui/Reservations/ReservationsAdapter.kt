package com.example.ptuxiaki.sunnybnb.ui.Reservations

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ptuxiaki.sunnybnb.Models.Review
import com.example.ptuxiaki.sunnybnb.R
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.reservation_single.view.*

/**
 * Created by Pampoukidis on 18/1/2018.
 */
class ReservationsAdapter(private var reservationDates: List<String>?,
                          private var houseIds: List<String>?,
                          private var visitorsIds: List<String>?,
                          private var context: Context?,
                          var listener: ((chosenCity: String) -> Unit)?)
    : RecyclerView.Adapter<ReservationsAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.reservation_single, parent, false)

        val customViewHolder = CustomViewHolder(itemView)

        itemView.setOnClickListener({
            val item = reservationDates?.getOrNull(customViewHolder.adapterPosition)
                    ?: return@setOnClickListener
            listener?.invoke(item)
        })

        itemView.reservation_review_btn.setOnClickListener {
            Log.d("ReservationsAdapter", "Clicked")
            var reviewRef = FirebaseDatabase.getInstance().reference.child("REVIEWS").child(houseIds?.getOrNull(customViewHolder.adapterPosition))

            val mReview = Review("This is a review!", "123", "16:30", 5, "A great house review")

            val reviewMap = mReview.toMap()

            val reviewId = reviewRef.push().key

            reviewRef.child(reviewId).updateChildren(reviewMap).addOnCompleteListener({
                Toast.makeText(context, "Review Added", Toast.LENGTH_SHORT).show()
                reviewRef.child(reviewId).addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {

                    }

                    override fun onDataChange(snap: DataSnapshot?) {
                        val readReview = snap?.getValue(Review::class.java)
                        Log.d("ReservationsAdapter", readReview.toString())
                    }
                })
            })

        }

        return customViewHolder
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val date = reservationDates?.get(position)
        val houseId = houseIds?.get(position)
        val visitorId = visitorsIds?.get(position)
        holder?.bindTo(date, houseId, visitorId, context)
    }

    override fun getItemCount(): Int = reservationDates?.count() ?: 0

    class CustomViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var rootRef: DatabaseReference


        fun bindTo(date: String?, houseId: String?, visitorId: String?, context: Context?) {

            rootRef = FirebaseDatabase.getInstance().reference

            rootRef.child("HOUSES").child(houseId).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {

                }

                override fun onDataChange(snapshot: DataSnapshot?) {
                    val houseName = snapshot?.child("house_name")?.value.toString()
                    val houseImage = snapshot?.child("mainFoto")?.value.toString()

                    view.single_house_name_tv.text = houseName

                    Picasso.with(context).load(houseImage)
                            .placeholder(R.drawable.default_profile_image).into(view.single_house_image)

                }
            })

            rootRef.child("USERS").child(visitorId).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {

                }

                override fun onDataChange(snapshot: DataSnapshot?) {
                    val visitorsName = snapshot?.child("displayName")?.value.toString()
                    view.single_house_price.text = visitorsName
                }
            })

            view.single_house_location_tv.text = date

        }

    }

}