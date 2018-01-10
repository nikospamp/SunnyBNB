package com.example.ptuxiaki.sunnybnb.ui.HouseDetails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ptuxiaki.sunnybnb.BaseActivity;
import com.example.ptuxiaki.sunnybnb.R;
import com.example.ptuxiaki.sunnybnb.ui.Booking.BookingActivity;
import com.example.ptuxiaki.sunnybnb.ui.Profile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HouseDetailsActivity extends BaseActivity {

    private ImageView mainImageView;
    private TextView houseName;
    private TextView houseLocation;
    private Button bookNow;
    private Button ownerDetails;


    private DatabaseReference housesData;
    private FirebaseAuth mAuth;

    public String userId;
    public String ownerId;
    public String HOUSE_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_details);

        HOUSE_ID = getIntent().getStringExtra("house_id");

        mAuth = FirebaseAuth.getInstance();

        userId = this.mAuth.getCurrentUser().getUid();

        housesData = FirebaseDatabase.getInstance().getReference().child("HOUSES").child(HOUSE_ID);

        mainImageView = findViewById(R.id.house_detail_photo);
        houseName = findViewById(R.id.house_detail_name);
        houseLocation = findViewById(R.id.house_detail_location);
        bookNow = findViewById(R.id.house_detail_reservation_btn);
        ownerDetails = findViewById(R.id.house_details_owner_details_btn);

        bookNow.setOnClickListener(v -> {
            Intent intent = new Intent(HouseDetailsActivity.this, BookingActivity.class);
            intent.putExtra("HOUSE_ID", HOUSE_ID);
            intent.putExtra("UID", userId);
            startActivity(intent);
            finish();
        });

        ownerDetails.setOnClickListener(v -> {
            Intent profileIntent = new Intent(HouseDetailsActivity.this, ProfileActivity.class);
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null)
                profileIntent.putExtra("from_user_id", ownerId);
            startActivity(profileIntent);
        });


        housesData.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String houseNameStr = dataSnapshot.child("house_name").getValue().toString();
                String houseLocationStr = dataSnapshot.child("city").getValue().toString();
                String housePhoto = dataSnapshot.child("mainFoto").getValue().toString();
                ownerId = dataSnapshot.child("uid").getValue().toString();

                Picasso.with(getApplicationContext()).load(housePhoto).placeholder(R.drawable.common_google_signin_btn_icon_dark_normal).into(mainImageView);
                houseName.setText(houseNameStr);
                houseLocation.setText(houseLocationStr);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}