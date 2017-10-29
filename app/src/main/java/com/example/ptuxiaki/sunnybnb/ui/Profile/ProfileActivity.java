package com.example.ptuxiaki.sunnybnb.ui.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

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
    private CircleImageView image;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mCurrentUser;
    private StorageReference mStorageReference;
    private ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle(profile);

        image = (CircleImageView) findViewById(R.id.profileCircleImage);

        mProgressBar = new ProgressDialog(this);
        mProgressBar.setTitle("Getting User Profile");
        mProgressBar.setMessage("Please wait while we get your data!");
        mProgressBar.setCanceledOnTouchOutside(false);
        mProgressBar.show();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        /**Edo arxikopoio ena reference to opoio deixne sto root tis vasis -> users -> currentUser
         * */
        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(users).child(mCurrentUser.getUid());

        /**Edo trabaei ta stoixeia pou 8elo kai tautoxrona "akouo" gia tuxon allages
         * */
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                /**Edo px pairno displayName tou currentUser apo th vasi
                 * Omoios pairno kai oti allo 8elo
                 * */
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

                TextView nameTV = (TextView) findViewById(R.id.profileDisplayName);
                TextView statusTV = (TextView) findViewById(R.id.profileStatus);
                TextView housesTV = (TextView) findViewById(R.id.profileHouseCounter);
                TextView visitorsTV = (TextView) findViewById(R.id.profileVisitorsCounter);
                TextView friendsTV = (TextView) findViewById(R.id.profileFriendsCounter);
                /**Kai edo vazo to onoma poy trabiksa apo ti vasi
                 * se ena textView sto profile mou
                 * */
                nameTV.setText(user_name);
                statusTV.setText(user_status);
                housesTV.setText(user_houses);
                visitorsTV.setText(user_visitors);
                friendsTV.setText(user_friends);

                /**H' edo, pernao tin eikona pou traviksa apo ti vasi
                 * */
                Picasso.with(getApplicationContext()).load(user_image)
                        .placeholder(R.drawable.default_profile_image).into(image);

                mProgressBar.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Circle", "onClick: ");
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(ProfileActivity.this);
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
                        .child(mCurrentUser.getUid() + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            String download_url = task.getResult().getDownloadUrl().toString();
                            mDatabaseReference.child("photoUrl").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mProgressBar.dismiss();
                                    Toast.makeText(ProfileActivity.this, "Images Uploaded!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
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
