package com.example.ptuxiaki.sunnybnb.ui.Favourites

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.ptuxiaki.sunnybnb.R
import com.example.ptuxiaki.sunnybnb.ui.HouseDetails.HouseDetailsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_favorites.*
import java.util.*

class FavoritesActivity : AppCompatActivity() {

    private lateinit var userFavHousesDb: DatabaseReference

    private val favoriteHousesAdapter: FavoritesAdapter = FavoritesAdapter(null, null, null)

    private var currentUser: FirebaseUser? = null

    private val favHousesList = ArrayList<String>()

    private lateinit var favHousesRec: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        supportActionBar?.title = "Favorites"

        favHousesRec = findViewById(R.id.favorites_main_rec)

        favHousesRec.adapter = favoriteHousesAdapter

        favoriteHousesAdapter.context = applicationContext

        favoriteHousesAdapter.listener = { houseId ->
            Log.d("favoriteHousesAdapter", houseId)
            val houseDetailsIntent = Intent(this@FavoritesActivity, HouseDetailsActivity::class.java)
            houseDetailsIntent.putExtra("house_id", houseId)
            startActivity(houseDetailsIntent)
        }

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

                    if (favHousesList.size > 0) {
                        favorites_no_fav_txt.visibility = View.GONE
                        favHousesRec.visibility = View.VISIBLE
                    } else {
                        favorites_no_fav_txt.visibility = View.VISIBLE
                        favHousesRec.visibility = View.GONE
                    }

                    favHousesRec.post({
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
