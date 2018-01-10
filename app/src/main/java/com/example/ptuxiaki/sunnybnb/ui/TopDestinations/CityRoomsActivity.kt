package com.example.ptuxiaki.sunnybnb.ui.TopDestinations

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.ptuxiaki.sunnybnb.BaseActivity
import com.example.ptuxiaki.sunnybnb.Models.House
import com.example.ptuxiaki.sunnybnb.R
import com.example.ptuxiaki.sunnybnb.ui.HouseDetails.HouseDetailsActivity
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_city_rooms.*
import java.util.*

class CityRoomsActivity : BaseActivity() {

    private lateinit var topDestinationsDb: DatabaseReference
    private lateinit var mDatabase: DatabaseReference
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_rooms)

        val city = intent.getStringExtra("CITY")

        supportActionBar?.title = city

        mainRecycler.layoutManager = LinearLayoutManager(this)

        currentUser = FirebaseAuth.getInstance().currentUser!!

        topDestinationsDb = FirebaseDatabase.getInstance().reference
                .child("HOUSES")

        mDatabase = FirebaseDatabase.getInstance().reference

        val cityHouseQuery: Query = topDestinationsDb.orderByChild("city").equalTo(city)

        val mHousesRecyclerAdapter = object : FirebaseRecyclerAdapter<House, HousesViewHolder>(
                House::class.java,
                R.layout.house_single,
                HousesViewHolder::class.java,
                cityHouseQuery
        ) {
            override fun populateViewHolder(viewHolder: HousesViewHolder, model: House, position: Int) {
                val houseId = getRef(position).key

                viewHolder.setHouseName(model.house_name)
                viewHolder.setCity(model.city, model.country)
                viewHolder.setImage(model.mainFoto, applicationContext)
                viewHolder.setPrice(model.price)
                viewHolder.setFavStatus(houseId, currentUser)
                viewHolder.setContext(applicationContext)

                viewHolder.mView.setOnClickListener({
                    val houseDetailsIntent = Intent(this@CityRoomsActivity, HouseDetailsActivity::class.java)
                    houseDetailsIntent.putExtra("house_id", houseId)
                    startActivity(houseDetailsIntent)
                })

                viewHolder.favIcon.setOnClickListener({
                    val push = mDatabase.child("USERS")
                            .child(currentUser.uid)
                            .child("favorites").child(houseId)

                    push.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.value == null) {
                                val houseFavRef = mDatabase.child("USERS")
                                        .child(currentUser.uid)
                                        .child("favorites")

                                val cal = Calendar.getInstance()
                                val day = cal.get(Calendar.DAY_OF_MONTH)
                                val month = cal.get(Calendar.MONTH) + 1
                                val year = cal.get(Calendar.YEAR)

                                val favMap = HashMap<String, Any>()

                                favMap.put(houseId, day.toString()
                                        + "/" + month
                                        + "/" + year)

                                houseFavRef.updateChildren(favMap)

                                viewHolder.favIcon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_heart))
                            } else {
                                push.removeValue()
                                viewHolder.favIcon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_heart_empty))
                            }

                        }

                        override fun onCancelled(databaseError: DatabaseError) {

                        }
                    })
                })

            }
        }

        mainRecycler.adapter = mHousesRecyclerAdapter

    }

    class HousesViewHolder(internal var mView: View) : RecyclerView.ViewHolder(mView) {
        private var favHouseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        internal var favIcon: ImageView = mView.findViewById(R.id.single_house_favourite_img)
        internal lateinit var context: Context

        internal fun setHouseName(house_name: String) {
            val house_name_tv = mView.findViewById<TextView>(R.id.single_house_name_tv)
            house_name_tv.text = house_name
        }

        internal fun setCity(city: String, country: String) {
            val city_tv = mView.findViewById<TextView>(R.id.single_house_location_tv)
            val finalString = city + "," + country
            city_tv.text = finalString
        }

        internal fun setImage(mainFoto: String, context: Context) {
            val main_image = mView.findViewById<ImageView>(R.id.single_house_image)
            Picasso.with(context).load(mainFoto).placeholder(R.drawable.property_placeholder).into(main_image)
        }

        internal fun setPrice(price: String) {
            val price_tv = mView.findViewById<TextView>(R.id.single_house_price)
            val finalText = price + "€"
            price_tv.text = finalText
        }

        internal fun setFavStatus(houseId: String, currentUser: FirebaseUser?) {
            if (currentUser != null) {
                val uid = currentUser.uid
                val isFav = favHouseRef.child("USERS")
                        .child(uid)
                        .child("favorites").child(houseId)

                isFav.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.value == null) {
                            favIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_empty))
                        } else {
                            favIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart))
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
            }
        }

        fun setContext(applicationContext: Context) {
            context = applicationContext
        }
    }
}
