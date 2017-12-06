package com.example.ptuxiaki.sunnybnb.ui.Favourites

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import butterknife.BindView
import butterknife.ButterKnife
import com.example.ptuxiaki.sunnybnb.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*

class FavoritesActivity : AppCompatActivity() {

    private lateinit var userFavHousesDb: DatabaseReference

    private lateinit var housesDb: DatabaseReference

    private var currentUser: FirebaseUser? = null

    private val favHousesList = ArrayList<String>()

    @BindView(R.id.favorites_main_rec)
    internal var favHousesRec: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        ButterKnife.bind(this)

        currentUser = FirebaseAuth.getInstance().currentUser

        userFavHousesDb = FirebaseDatabase.getInstance().reference
                .child("USERS")
                .child(currentUser!!.uid)
                .child("favorites")

        housesDb = FirebaseDatabase.getInstance().reference
                .child("HOUSES")

        userFavHousesDb.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {

                    snapshot.children.mapTo(favHousesList) { it.key }

                    val query: Query = housesDb.orderByKey().startAt(favHousesList[0]).endAt(favHousesList[favHousesList.size - 1])

                    query.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError?) {
                            Log.d("onDataChange", "Error")
                        }

                        override fun onDataChange(snap: DataSnapshot) {
                            if (snap.value != null) {
                                for (tempSnap in snap.children) {
                                    if (favHousesList.contains(tempSnap.key))
                                        Log.d("onDataChange", tempSnap.key)
                                }
                            } else {
                                Log.d("onDataChange", "Empty")
                            }
                        }

                    })

                }

            }

        })


    }

    companion object {

        private val TAG = "FavoritesActivity"

    }

}
