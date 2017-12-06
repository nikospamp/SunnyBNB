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

    private val favoriteHousesAdapter: FavoritesAdapter = FavoritesAdapter(null, null)

    private var currentUser: FirebaseUser? = null

    private val favHousesList = ArrayList<String>()

    private lateinit var favHousesRec: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        favHousesRec = findViewById(R.id.favorites_main_rec)

        favHousesRec.adapter = favoriteHousesAdapter

        currentUser = FirebaseAuth.getInstance().currentUser

        userFavHousesDb = FirebaseDatabase.getInstance().reference
                .child("USERS")
                .child(currentUser!!.uid)
                .child("favorites")

        userFavHousesDb.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {

                    snapshot.children.mapTo(favHousesList) { it.key }

                    favHousesRec!!.post({
                        favoriteHousesAdapter.items = favHousesList
                        favoriteHousesAdapter.notifyItemRangeInserted(0, favHousesList.count())
                    })

                }

            }

        })


    }

    companion object {

        private val TAG = "FavoritesActivity"

    }

}
