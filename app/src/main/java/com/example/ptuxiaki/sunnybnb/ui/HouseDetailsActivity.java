package com.example.ptuxiaki.sunnybnb.ui;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
 import android.widget.TextView;

import com.example.ptuxiaki.sunnybnb.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
public class HouseDetailsActivity extends AppCompatActivity {

    private ImageView mainImageView;
    private TextView houseName;
    private TextView houseLocation;


     private DatabaseReference housesData;
    private FirebaseAuth mAuth;

    public String userId;
    public String ownerId;
    public String houseId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_details);

        houseId = getIntent().getStringExtra("house_id");

        mAuth = FirebaseAuth.getInstance();

        userId = this.mAuth.getCurrentUser().getUid();

        housesData = FirebaseDatabase.getInstance().getReference().child("HOUSES").child(houseId);

        mainImageView = (ImageView) findViewById(R.id.house_detail_photo);
        houseName = (TextView) findViewById(R.id.house_detail_name);
        houseLocation = (TextView) findViewById(R.id.house_detail_location);



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