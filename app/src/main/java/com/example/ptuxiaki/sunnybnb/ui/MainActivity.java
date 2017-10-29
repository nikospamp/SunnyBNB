package com.example.ptuxiaki.sunnybnb.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ptuxiaki.sunnybnb.Models.House;
import com.example.ptuxiaki.sunnybnb.Models.User;
import com.example.ptuxiaki.sunnybnb.R;
import com.example.ptuxiaki.sunnybnb.ui.AllCities.CitiesFragment;
import com.example.ptuxiaki.sunnybnb.ui.Favourites.FavouritesFragment;
import com.example.ptuxiaki.sunnybnb.ui.HomeAdd.homeAdd;
import com.example.ptuxiaki.sunnybnb.ui.Messages.MessagesFragment;
import com.example.ptuxiaki.sunnybnb.ui.Profile.ProfileActivity;
import com.example.ptuxiaki.sunnybnb.ui.Settings.Settings2Activity;
import com.example.ptuxiaki.sunnybnb.ui.TopDestinations.TopDestinationsFragment;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseHouses;
    private static final int RC_SIGN_IN = 123;

    private String uid;
    private String displayName;
    private String email;
    private String photoUrl;
    private String provider;
    private String phoneNumber;
    private String status;
    private String token;
    private String houses;
    private String visitors;
    private String friends;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseHouses = FirebaseDatabase.getInstance().getReference().child("HOUSES");

        recyclerView = (RecyclerView) findViewById(R.id.mainRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setTheme(R.style.DarkTheme)
//                            .setLogo(R.mipmap.ic_launcher)
                            .setAvailableProviders(
                                    Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                    ))
                            .build(),
                    RC_SIGN_IN);
        }

        FirebaseRecyclerAdapter<House, HousesViewHolder> mHousesRecyclerAdapter = new FirebaseRecyclerAdapter<House, HousesViewHolder>(
                House.class,
                R.layout.house_single,
                HousesViewHolder.class,
                mDatabaseHouses
        ) {

            @Override
            protected void populateViewHolder(HousesViewHolder viewHolder, House model, int position) {
                viewHolder.setHouseName(model.getHouse_name());
                viewHolder.setCity(model.getCity(), model.getCountry());
                viewHolder.setImage(model.getMainFoto(), getApplicationContext());
                viewHolder.setPrice(model.getPrice());
                final String houseId = getRef(position).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent houseDetailsIntent = new Intent(MainActivity.this, HouseDetailsActivity.class);
                        houseDetailsIntent.putExtra("house_id", houseId);
                        startActivity(houseDetailsIntent);
                    }
                });
            }
        };

        recyclerView.setAdapter(mHousesRecyclerAdapter);
    }

    private static class HousesViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public HousesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        void setHouseName(String house_name) {
            TextView house_name_tv = (TextView) mView.findViewById(R.id.single_house_name_tv);
            house_name_tv.setText(house_name);
        }

        public void setCity(String city, String country) {
            TextView city_tv = (TextView) mView.findViewById(R.id.single_house_location_tv);
            String finalString = city + "," + country;
            city_tv.setText(finalString);
        }


        public void setImage(String mainFoto, Context context) {
            ImageView main_image = (ImageView) mView.findViewById(R.id.single_house_image);
            Picasso.with(context).load(mainFoto).placeholder(R.drawable.common_google_signin_btn_icon_dark_normal).into(main_image);
        }

        public void setPrice(String price) {
            TextView price_tv = (TextView) mView.findViewById(R.id.single_house_price);
            String finalText = price + "€";
            price_tv.setText(finalText);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            final FirebaseUser signedUser = mAuth.getCurrentUser();

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Time now = new Time();
                    Log.d(TAG, "onDataChange: TIME " + now.toString());
                    Log.d(TAG, "onDataChange: " + dataSnapshot.child(getString(R.string.users)).child(signedUser.getUid()).exists());
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    if (!dataSnapshot.child(getString(R.string.users)).child(signedUser.getUid()).exists()) {
                        Log.d(TAG, "onDataChange: REGISTER");
                        uid = setNullToDefaultValue(signedUser.getUid());
                        status = "default_status";
                        displayName = setNullToDefaultValue(signedUser.getDisplayName());
                        email = setNullToDefaultValue(signedUser.getEmail());
                        photoUrl = setNullToDefaultValue(signedUser.getPhotoUrl().toString());
                        provider = setNullToDefaultValue(signedUser.getProviderId());
                        phoneNumber = setNullToDefaultValue(signedUser.getPhoneNumber());
                        token = setNullToDefaultValue(null);
                        houses = "0";
                        visitors = "0";
                        friends = "0";

                        Log.d(TAG, "onActivityResult: User Signed In!");
                        User mUser = new User(uid, status, displayName, email,
                                photoUrl, provider, phoneNumber, token, houses,
                                visitors, friends);
                        Map<String, Object> userMap = mUser.toMap();
                        userMap.put("register_date", format.toString());

                        mDatabase.child(getString(R.string.users)).child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);
                    } else {
                        Log.d(TAG, "onDataChange: LOGIN");
                        mDatabase.child(getString(R.string.users)).child(mAuth.getCurrentUser().getUid()).child("last_login").setValue(format.toLocalizedPattern());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            };

            mDatabase.addListenerForSingleValueEvent(postListener);

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Top right corner
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logOut_settings) {
            AuthUI.getInstance()
                    .signOut(MainActivity.this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "onComplete: " + "User logged out!");
                            mAuth = FirebaseAuth.getInstance();
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser == null) {
                                Log.d(TAG, "onComplete: User is null");
                                startActivityForResult(
                                        AuthUI.getInstance()
                                                .createSignInIntentBuilder()
                                                .setTheme(R.style.DarkTheme)
                                                .setAvailableProviders(
                                                        Arrays.asList(
                                                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                                                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                                        ))
                                                .build(),
                                        RC_SIGN_IN);
                            }
                        }
                    });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int id) {
        android.support.v4.app.Fragment fragment = null;
        switch (id) {
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.nav_profile:
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                break;
            case R.id.nav_favourites:
                fragment = new FavouritesFragment();
                break;
            case R.id.nav_top_destinations:
                fragment = new TopDestinationsFragment();
                break;
            case R.id.nav_cities:
                fragment = new CitiesFragment();
                break;
            case R.id.nav_messages:
                fragment = new MessagesFragment();
                break;
            case R.id.nav_settings:
                Intent settingsIntent = new Intent(MainActivity.this, Settings2Activity.class);
                startActivity(settingsIntent);
                break;
            case R.id.nav_add_house:
                Intent intent = new Intent(getApplicationContext(), homeAdd.class);
                startActivity(intent);
                break;

        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);

        return true;
    }

    //Database Housekeeping
    public String setNullToDefaultValue(String s) {
        String defaultValue = "default_value";
        if (s == null)
            return defaultValue;
        return s;
    }
}

