package com.example.ptuxiaki.sunnybnb.ui.Reviews

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.ptuxiaki.sunnybnb.Models.Review
import com.example.ptuxiaki.sunnybnb.R
import com.example.ptuxiaki.sunnybnb.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.android.synthetic.main.activity_write_review.*

class WriteReviewActivity : AppCompatActivity() {

    private lateinit var reviewRef: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private lateinit var stars: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_review)

        val intent = intent

        val houseId = intent.getStringExtra("HOUSE_ID")

        currentUser = FirebaseAuth.getInstance().currentUser!!

        reviewRef = FirebaseDatabase.getInstance().reference.child("REVIEWS").child(houseId)

        val reviewId = reviewRef.push().key

        write_review_stars.setOnRatingBarChangeListener { ratingBar, rating, b ->
            stars = rating.toInt().toString()
        }

        write_review_submit.setOnClickListener({
            val reviewTitle = write_review_title.text.toString()
            val reviewBody = write_review_body.text.toString()
            val time = ServerValue.TIMESTAMP[".sv"]

            val mReview = Review(reviewBody, currentUser.uid, ServerValue.TIMESTAMP, stars.toInt(), reviewTitle)

            val reviewMap = mReview.toMap()

            reviewRef.child(reviewId).updateChildren(reviewMap).addOnCompleteListener({
                startActivity(Intent(this@WriteReviewActivity, MainActivity::class.java))
                finish()
            })
        })
    }
}
