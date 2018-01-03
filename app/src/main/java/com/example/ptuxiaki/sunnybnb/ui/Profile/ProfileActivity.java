package com.example.ptuxiaki.sunnybnb.ui.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ptuxiaki.sunnybnb.R;
import com.example.ptuxiaki.sunnybnb.ui.Settings.Settings2Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.DateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final String FRIENDS = "friends";
    private static final String NOT_FRIENDS = "not_friends";
    private static final String REQ_SENT = "sent";
    private static final String REQ_RECEIVED = "received";

    private String profile = "Profile";
    private String users = "USERS";
    private String photo = "photoUrl";
    private String displayName = "displayName";
    private String status = "status";
    private String houses = "houses";
    private String visitors = "visitors";
    private String friends = "friends";

    private Button sendRequestBtn;
    private Button declineRequestBtn;
    private Button settingsBtn;

    private String displayingUser;
    private String mCurrent_state;

    private CircleImageView image;

    private FirebaseUser currentUser;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mFriendsRequestReference;
    private DatabaseReference mFriendsDatabaseReference;
    private StorageReference mStorageReference;
    private ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle(profile);

        sendRequestBtn = findViewById(R.id.profile_send_friend_request_btn);
        settingsBtn = findViewById(R.id.profile_settings_btn);
        declineRequestBtn = findViewById(R.id.profile_decline_friend_request_btn);

        image = findViewById(R.id.profileCircleImage);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        mCurrent_state = NOT_FRIENDS;

        mProgressBar = new ProgressDialog(this);
        mProgressBar.setTitle("Getting User Profile");
        mProgressBar.setMessage("Please wait while we get your data!");
        mProgressBar.setCanceledOnTouchOutside(false);
        mProgressBar.show();

        displayingUser = getIntent().getStringExtra("Current_User");

        if (currentUser != null) {
            if (!currentUser.getUid().equals(displayingUser)) {
                settingsBtn.setVisibility(View.GONE);
                sendRequestBtn.setVisibility(View.VISIBLE);
                declineRequestBtn.setVisibility(View.VISIBLE);
            } else {
                settingsBtn.setVisibility(View.VISIBLE);
                sendRequestBtn.setVisibility(View.GONE);
                declineRequestBtn.setVisibility(View.GONE);
            }
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(users).child(displayingUser);

        mFriendsRequestReference = FirebaseDatabase.getInstance().getReference().child("FRIEND_REQ");
        mFriendsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("FRIENDS");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
                SharedPreferences.Editor editor = prefs.edit();

                String user_name = dataSnapshot.child(displayName).getValue().toString();
                editor.putString("display_name_text", user_name);
                editor.apply();
                String user_status = dataSnapshot.child(status).getValue().toString();
                editor.putString("display_status_text", user_status);
                editor.apply();
                String user_image = dataSnapshot.child(photo).getValue().toString();
                String user_houses = dataSnapshot.child(houses).getValue().toString();
                String user_visitors = dataSnapshot.child(visitors).getValue().toString();
                String user_friends = dataSnapshot.child(friends).getValue().toString();

                TextView nameTV = findViewById(R.id.profileDisplayName);
                TextView statusTV = findViewById(R.id.profileStatus);
                TextView housesTV = findViewById(R.id.profileHouseCounter);
                TextView visitorsTV = findViewById(R.id.profileVisitorsCounter);
                TextView friendsTV = findViewById(R.id.profileFriendsCounter);

                nameTV.setText(user_name);
                statusTV.setText(user_status);
                housesTV.setText(user_houses);
                visitorsTV.setText(user_visitors);
                friendsTV.setText(user_friends);

                Picasso.with(getApplicationContext()).load(user_image)
                        .placeholder(R.drawable.default_profile_image).into(image);

                mFriendsRequestReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(displayingUser)) {
                            String request_type = dataSnapshot.child(displayingUser).child("request_type").getValue().toString();
                            if (request_type.equals(REQ_RECEIVED)) {

                                mCurrent_state = REQ_RECEIVED;
                                sendRequestBtn.setText(getApplicationContext().getResources().getString(R.string.accept_friend_request));

                            } else if (request_type.equals(REQ_SENT)) {

                                mCurrent_state = REQ_SENT;
                                sendRequestBtn.setText(getApplicationContext().getResources().getString(R.string.cancel_friend_request));

                            }
                        }
                        mProgressBar.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        image.setOnClickListener(v -> {
            Log.d("Circle", "onClick: ");
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(ProfileActivity.this);
        });

        sendRequestBtn.setOnClickListener(view -> {

            /*
             * Should Handle Remove Request
             * */

            if (mCurrent_state.equals(NOT_FRIENDS)) {

                mFriendsRequestReference.child(currentUser.getUid()).child(displayingUser).child("request_type")
                        .setValue("sent").addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mFriendsRequestReference.child(displayingUser).child(currentUser.getUid()).child("request_type")
                                .setValue("received").addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {

                                mCurrent_state = REQ_SENT;

                                sendRequestBtn.setText(getApplicationContext().getResources().getString(R.string.cancel_friend_request));

                                Toast.makeText(ProfileActivity.this, "Friend Request Sent", Toast.LENGTH_LONG).show();
                            }

                        });

                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed sending friend request", Toast.LENGTH_LONG).show();
                    }
                });

            }

            if (mCurrent_state.equals(REQ_SENT)) {

                mFriendsRequestReference.child(currentUser.getUid()).child(displayingUser).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mFriendsRequestReference.child(displayingUser).child(currentUser.getUid()).removeValue().addOnCompleteListener(task1 -> {

                            mCurrent_state = NOT_FRIENDS;
                            sendRequestBtn.setText(getApplicationContext().getResources().getString(R.string.send_friend_request));

                        });
                    }
                });

            }

            if (mCurrent_state.equals(REQ_RECEIVED)) {

                String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                mFriendsDatabaseReference.child(currentUser.getUid()).child(displayingUser).setValue(currentDate).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        mFriendsDatabaseReference.child(displayingUser).child(currentUser.getUid()).setValue(currentDate).addOnSuccessListener(aVoid -> {

                            mFriendsRequestReference.child(currentUser.getUid()).child(displayingUser).removeValue().addOnCompleteListener(task1 -> {
                                if (task.isSuccessful()) {
                                    mFriendsRequestReference.child(displayingUser).child(currentUser.getUid()).removeValue().addOnCompleteListener(task2 -> {

                                        mCurrent_state = FRIENDS;
                                        sendRequestBtn.setText(getApplicationContext().getResources().getString(R.string.remove_friend));

                                    });
                                }
                            });


                        });

                    }
                });

            }

        });
    }

    public void settingsActivity(View view) {
        Intent settingsIntent = new Intent(ProfileActivity.this, Settings2Activity.class);
        settingsIntent.putExtra("fragToLoad", "general");
        startActivity(settingsIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mProgressBar.setTitle("Uploading Image");
                mProgressBar.setMessage("Please wait while we upload your image!");
                mProgressBar.setCanceledOnTouchOutside(false);
                mProgressBar.show();
                Uri resultUri = result.getUri();
                StorageReference filepath = mStorageReference.child("profile_images")
                        .child(displayingUser + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            String download_url = task.getResult().getDownloadUrl().toString();
                            mDatabaseReference.child("photoUrl").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mProgressBar.dismiss();
                                    Toast.makeText(ProfileActivity.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            mProgressBar.dismiss();
                            Toast.makeText(ProfileActivity.this, "Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
