package com.example.ptuxiaki.sunnybnb.ui.Reviews

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.ptuxiaki.sunnybnb.Models.Review
import com.example.ptuxiaki.sunnybnb.R
import com.example.ptuxiaki.sunnybnb.ui.Utilities.GetTimeAgo
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.activity_reviews.*
import kotlinx.android.synthetic.main.review_single.view.*

class ReviewsActivity : AppCompatActivity() {

    private lateinit var reviewRef: Query
    private lateinit var houseId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)

        supportActionBar?.title = "House Reviews"

        val incomingIntent = intent

        houseId = incomingIntent.getStringExtra("HOUSE_ID")

        reviewRef = FirebaseDatabase.getInstance().reference.child("REVIEWS").child(houseId).orderByChild("time")

        val reviewAdapter = object : FirebaseRecyclerAdapter<Review, ReviewsViewHolder>(
                Review::class.java,
                R.layout.review_single,
                ReviewsViewHolder::class.java,
                reviewRef) {
            override fun populateViewHolder(viewHolder: ReviewsViewHolder?, model: Review?, position: Int) {
                val reviewId = getRef(position).key
                Log.d("ReviewsCheck", reviewId)

                viewHolder?.setReviewTitle(model?.title)

                viewHolder?.setReviewTime(model?.time.toString())

                viewHolder?.setReviewBody(model?.body)

                viewHolder?.setReviewStars(model?.stars)

            }

        }

        reviews_main_rec.adapter = reviewAdapter
    }

}

class ReviewsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    fun setReviewTitle(title: String?) {
        itemView.review_title.text = title
    }

    fun setReviewTime(time: String?) {
        val timeAgo = GetTimeAgo()
        itemView.review_time.text = timeAgo.getTimeAgo(time?.toLong()!!)
    }

    fun setReviewBody(body: String?) {
        itemView.review_body.text = body
    }

    fun setReviewStars(stars: Int?) {
        itemView.review_stars.text = stars.toString()

    }

}

