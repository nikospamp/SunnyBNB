package com.example.ptuxiaki.sunnybnb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private String profile = "Profile";
    private String users = "USERS";
    private String photo = "photoUrl";
    private String displayName = "displayName";
    private String status = "status";
    private String houses = "houses";
    private String visitors = "visitors";
    private String friends = "friends";
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mCurrentUser;
    private ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle(profile);

        mProgressBar = new ProgressDialog(this);
        mProgressBar.setTitle("Getting User Profile");
        mProgressBar.setMessage("Please wait while we get your data!");
        mProgressBar.setCanceledOnTouchOutside(false);
        mProgressBar.show();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(users).child(mCurrentUser.getUid());

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user_name = dataSnapshot.child(displayName).getValue().toString();
                String user_status = dataSnapshot.child(status).getValue().toString();
                String user_image = dataSnapshot.child(photo).getValue().toString();
                String user_houses = dataSnapshot.child(houses).getValue().toString();
                String user_visitors = dataSnapshot.child(visitors).getValue().toString();
                String user_friends = dataSnapshot.child(friends).getValue().toString();

                TextView nameTV = (TextView) findViewById(R.id.profileDisplayName);
                TextView statusTV = (TextView) findViewById(R.id.profileStatus);
                TextView housesTV = (TextView) findViewById(R.id.profileHouseCounter);
                TextView visitorsTV = (TextView) findViewById(R.id.profileVisitorsCounter);
                TextView friendsTV = (TextView) findViewById(R.id.profileFriendsCounter);
                nameTV.setText(user_name);
                statusTV.setText(user_status);
                housesTV.setText(user_houses);
                visitorsTV.setText(user_visitors);
                friendsTV.setText(user_friends);

                CircleImageView image = (CircleImageView) findViewById(R.id.profileCircleImage);
                Picasso.with(getApplicationContext()).load(user_image)
                        .placeholder(R.drawable.default_profile_image).into(image);

                mProgressBar.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void settingsActivity(View view) {
        Intent settingsIntent = new Intent(ProfileActivity.this, Settings2Activity.class);
        settingsIntent.putExtra("fragToLoad", "general");
        startActivity(settingsIntent);
    }
}
