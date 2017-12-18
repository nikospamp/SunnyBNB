package com.example.ptuxiaki.sunnybnb.ui.Search

/**
 * Created by Pampoukidis on 18/12/2017.
 */

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ptuxiaki.sunnybnb.R
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.house_single.view.*

class SearchAdapter(var items: List<String>?, var listener: ((houseId: String) -> Unit)?, var context: Context?) : RecyclerView.Adapter<SearchAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.house_single, parent, false)
        val customViewHolder = CustomViewHolder(itemView, context)

        itemView.setOnClickListener({
            val item = items?.getOrNull(customViewHolder.adapterPosition) ?: return@setOnClickListener
            listener?.invoke(item)
        })

        return customViewHolder
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val fav = items?.get(position)
        holder?.bindTo(fav)
    }

    override fun getItemCount(): Int = items?.count() ?: 0

    class CustomViewHolder(view: View, private val context: Context?) : RecyclerView.ViewHolder(view) {

        private lateinit var fireBaseRef: DatabaseReference
        private val houseNameTv = view.single_house_name_tv
        private val housePriceTv = view.single_house_price
        private val houseLocationTv = view.single_house_location_tv
        private val housePhotoImg = view.single_house_image
        private val houseFavIcon = view.single_house_favourite_img
        private val houseFavTv = view.single_house_set_favourite_txt

        fun bindTo(favHouseId: String?) {
            fireBaseRef = FirebaseDatabase.getInstance().reference
                    .child("HOUSES")
                    .child(favHouseId)

            fireBaseRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    Toast.makeText(context, "An error occurred!", Toast.LENGTH_LONG).show()
                }

                @SuppressLint("SetTextI18n")
                override fun onDataChange(snap: DataSnapshot?) {
                    if (snap?.value != null) {

                        houseFavIcon.visibility = View.GONE
                        houseFavTv.visibility = View.GONE

                        val houseName: String? = snap.child("house_name").value.toString()
                        val housePrice: String? = snap.child("price").value.toString()
                        val houseCity: String? = snap.child("city").value.toString()
                        val houseCountry: String? = snap.child("country").value.toString()
                        val housePhoto: String? = snap.child("mainFoto").value.toString()

                        houseNameTv.text = houseName
                        housePriceTv.text = housePrice + "€"
                        houseLocationTv.text = houseCity + "," + houseCountry
                        Picasso.with(context).load(housePhoto).placeholder(R.drawable.property_placeholder).into(housePhotoImg)

                    }

                }
            })

        }
    }

}
