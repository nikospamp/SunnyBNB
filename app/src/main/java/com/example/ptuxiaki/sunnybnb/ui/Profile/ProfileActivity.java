package com.example.ptuxiaki.sunnybnb.ui.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ptuxiaki.sunnybnb.R;
import com.example.ptuxiaki.sunnybnb.ui.Settings.Settings2Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

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

    private DatabaseReference mRootDb;
    private DatabaseReference mRootRef;
    private DatabaseReference mFriendsRequestDb;
    private DatabaseReference mFriendsDb;
    private DatabaseReference mNotificationDb;

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

        displayingUser = getIntent().getStringExtra("from_user_id");
        if (displayingUser == null) {
            for (String x : getIntent().getExtras().keySet()) {
                if (x.equals("from_user_id")) {
                    displayingUser = getIntent().getStringExtra(x);
                }
            }
        }
        Log.d("Messaging", displayingUser);

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

        mRootDb = FirebaseDatabase.getInstance().getReference()
                .child(users).child(displayingUser);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mFriendsRequestDb = FirebaseDatabase.getInstance().getReference().child("FRIEND_REQ");
        mFriendsDb = FirebaseDatabase.getInstance().getReference().child("FRIENDS");
        mNotificationDb = FirebaseDatabase.getInstance().getReference().child("NOTIFICATIONS");

        mRootDb.addValueEventListener(new ValueEventListener() {
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

                mFriendsRequestDb.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(displayingUser)) {
                            String request_type = dataSnapshot.child(displayingUser).child("request_type").getValue().toString();
                            if (request_type.equals(REQ_RECEIVED)) {

                                mCurrent_state = REQ_RECEIVED;
                                sendRequestBtn.setText(getApplicationContext().getResources().getString(R.string.accept_friend_request));
                                declineRequestBtn.setVisibility(View.VISIBLE);

                            } else if (request_type.equals(REQ_SENT)) {


                                mCurrent_state = REQ_SENT;
                                sendRequestBtn.setText(getApplicationContext().getResources().getString(R.string.cancel_friend_request));

                                declineRequestBtn.setVisibility(View.GONE);

                            }
                        } else {
                            mFriendsDb.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Object friend = dataSnapshot.child(displayingUser).getValue();

                                    Log.d("FriendsList", "onDataChange: " + dataSnapshot.toString());

                                    if (friend != null) {

                                        mCurrent_state = FRIENDS;
                                        sendRequestBtn.setText(getApplicationContext().getResources().getString(R.string.remove_friend));
                                        declineRequestBtn.setVisibility(View.GONE);

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

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

            if (mCurrent_state.equals(NOT_FRIENDS)) {

                HashMap<String, String> notificationData = new HashMap<>();
                notificationData.put("from", currentUser.getUid());
                notificationData.put("type", "request");

                DatabaseReference newNotificationRef = mRootDb.child("NOTIFICATIONS")
                        .child(displayingUser).push();

                String notificationId = newNotificationRef.getKey();

                HashMap<String, Object> requestMap = new HashMap<>();

                requestMap.put("FRIEND_REQ/" + currentUser.getUid() + "/" + displayingUser + "/request_type", "sent");
                requestMap.put("FRIEND_REQ/" + displayingUser + "/" + currentUser.getUid() + "/request_type", "received");
                requestMap.put("NOTIFICATIONS/" + displayingUser + "/" + notificationId, notificationData);

                mRootRef.updateChildren(requestMap, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        Toast.makeText(this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        mCurrent_state = REQ_SENT;

                        sendRequestBtn.setText(getApplicationContext().getResources().getString(R.string.cancel_friend_request));

                        declineRequestBtn.setVisibility(View.GONE);
                    }
                });

            }

            if (mCurrent_state.equals(REQ_SENT)) {
                HashMap<String, Object> cancelMap = new HashMap<>();
                cancelMap.put("FRIEND_REQ/" + currentUser.getUid() + "/" + displayingUser, null);
                cancelMap.put("FRIEND_REQ/" + displayingUser + "/" + currentUser.getUid(), null);

                mRootRef.updateChildren(cancelMap, (databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        mCurrent_state = NOT_FRIENDS;
                        sendRequestBtn.setText(getApplicationContext().getResources().getString(R.string.send_friend_request));
                        declineRequestBtn.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            if (mCurrent_state.equals(REQ_RECEIVED)) {
                final String currentDate = DateFormat.getDateTimeInstance().format(new Date());
                HashMap<String, Object> friendsMap = new HashMap<>();
                friendsMap.put("FRIENDS/" + currentUser.getUid() + "/" + displayingUser + "/date", currentDate);
                friendsMap.put("FRIENDS/" + displayingUser + "/" + currentUser.getUid() + "/date", currentDate);
                friendsMap.put("FRIEND_REQ/" + currentUser.getUid() + "/" + displayingUser, null);
                friendsMap.put("FRIEND_REQ/" + displayingUser + "/" + currentUser.getUid(), null);

                mRootRef.updateChildren(friendsMap, (databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        mCurrent_state = FRIENDS;
                        sendRequestBtn.setText(getApplicationContext().getResources().getString(R.string.remove_friend));
                        declineRequestBtn.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            if (mCurrent_state.equals(FRIENDS)) {
                HashMap<String, Object> unFriendMap = new HashMap<>();
                unFriendMap.put("FRIENDS/" + currentUser.getUid() + "/" + displayingUser, null);
                unFriendMap.put("FRIENDS/" + displayingUser + "/" + currentUser.getUid(), null);

                mRootRef.updateChildren(unFriendMap, (databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        mCurrent_state = NOT_FRIENDS;
                        sendRequestBtn.setText(getApplicationContext().getResources().getString(R.string.send_friend_request));
                    } else {
                        Toast.makeText(this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
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
                filepath.putFile(resultUri).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String download_url = task.getResult().getDownloadUrl().toString();
                        mRootDb.child("photoUrl").setValue(download_url).addOnCompleteListener(task1 -> {
                            mProgressBar.dismiss();
                            Toast.makeText(ProfileActivity.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        mProgressBar.dismiss();
                        Toast.makeText(ProfileActivity.this, "Try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d("ImageUpload", error.toString());
            }
        }
    }
}
