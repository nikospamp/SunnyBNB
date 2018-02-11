package com.example.ptuxiaki.sunnybnb.ui.Payment

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.ptuxiaki.sunnybnb.R
import com.example.ptuxiaki.sunnybnb.ui.MainActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : AppCompatActivity() {

    private lateinit var houseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val intent = intent

        val dayLength = intent.getIntExtra("DAYS_LENGTH", 0)
        val houseId = intent.getStringExtra("HOUSE_ID")

        houseRef = FirebaseDatabase.getInstance().reference.child("HOUSES").child(houseId)

        houseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(snapshot: DataSnapshot?) {
                val price = snapshot?.child("price")?.value.toString().toInt()

                Log.d("Payments", "Days: $dayLength and houseID: $houseId and the price: $price")

                val finalHeaderText = "The cost for the $dayLength days is: ${dayLength * price}$"
                payment_header_text.text = finalHeaderText
            }
        })

        payment_confirm.setOnClickListener({
            if (!TextUtils.isEmpty(payment_name.text.toString())
                    && !TextUtils.isEmpty(payment_credit_number.text.toString())
                    && !TextUtils.isEmpty(payment_cvc_number.text.toString())) {
                Toast.makeText(this@PaymentActivity, "Booked", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@PaymentActivity, MainActivity::class.java))
                finish()
            }
        })

    }
}
