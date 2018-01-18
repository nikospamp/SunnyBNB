package com.example.ptuxiaki.sunnybnb.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

import com.example.ptuxiaki.sunnybnb.BaseActivity;
import com.example.ptuxiaki.sunnybnb.Models.House;
import com.example.ptuxiaki.sunnybnb.Models.User;
import com.example.ptuxiaki.sunnybnb.R;
import com.example.ptuxiaki.sunnybnb.ui.AddHouse.HomeAdd;
import com.example.ptuxiaki.sunnybnb.ui.AllCities.CitiesActivity;
import com.example.ptuxiaki.sunnybnb.ui.Favourites.FavoritesActivity;
import com.example.ptuxiaki.sunnybnb.ui.Friends.FriendsActivity;
import com.example.ptuxiaki.sunnybnb.ui.HouseDetails.HouseDetailsActivity;
import com.example.ptuxiaki.sunnybnb.ui.Messages.MessagesActivity;
import com.example.ptuxiaki.sunnybnb.ui.Profile.ProfileActivity;
import com.example.ptuxiaki.sunnybnb.ui.Reservations.ReservationsActivity;
import com.example.ptuxiaki.sunnybnb.ui.Search.SearchActivity;
import com.example.ptuxiaki.sunnybnb.ui.Settings.Settings2Activity;
import com.example.ptuxiaki.sunnybnb.ui.TopDestinations.TopDestinationsActivity;
import com.example.ptuxiaki.sunnybnb.ui.WelcomeScreen.WelcomeScreenActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static boolean RUN_ONCE = true;

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mUserDatabase;
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
    private long visitors;
    private String friends;

    private RecyclerView recyclerView;

    @BindView(R.id.main_fab_search)
    FloatingActionButton actionButton;

    @OnClick(R.id.main_fab_search)
    public void showToast(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseHouses = FirebaseDatabase.getInstance().getReference().child("HOUSES");
        if (mAuth.getCurrentUser() != null) {
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("USERS").child(mAuth.getCurrentUser().getUid());
        }

        boolean welcome_screen = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("display_welcome_screen", true);


        if (welcome_screen && RUN_ONCE) {
            RUN_ONCE = false;
            startActivity(new Intent(this, WelcomeScreenActivity.class));
        }

        recyclerView = findViewById(R.id.mainRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        testForResults();
    }

    private void testForResults() {
        mDatabase.child("RESERVATIONS")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String hid;

                        for (DataSnapshot x : dataSnapshot.getChildren()) {
                            hid = x.getKey();
                            for (DataSnapshot y : x.getChildren()) {
                                String date = y.getKey();
                                String visitor = "NBHM6Bpo7CQ8quFOTCKGoHsnx0N2";
                                if (y.child("visitor").getValue().equals(visitor)) {
                                    Log.d("testForResults", "onDataChange: The user: " + visitor + " will visit: " + hid
                                            + " on: " + date);
                                }
                            }
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
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
/*
        else {
            mUserDatabase.child("online").setValue(true);
        }
*/

        FirebaseRecyclerAdapter<House, HousesViewHolder> mHousesRecyclerAdapter =
                new FirebaseRecyclerAdapter<House, HousesViewHolder>(
                        House.class,
                        R.layout.house_single,
                        HousesViewHolder.class,
                        mDatabaseHouses
                ) {
                    @Override
                    protected void populateViewHolder(final HousesViewHolder viewHolder, House model, int position) {
                        final String houseId = getRef(position).getKey();

                        viewHolder.setHouseName(model.getHouse_name());
                        viewHolder.setCity(model.getCity(), model.getCountry());
                        viewHolder.setImage(model.getMainFoto(), getApplicationContext());
                        viewHolder.setPrice(model.getPrice());
                        viewHolder.setFavStatus(houseId, currentUser);
                        viewHolder.setContext(getApplicationContext());

                        viewHolder.mView.setOnClickListener(v -> {
                            Intent houseDetailsIntent = new Intent(MainActivity.this, HouseDetailsActivity.class);
                            houseDetailsIntent.putExtra("house_id", houseId);
                            startActivity(houseDetailsIntent);
                        });

                        viewHolder.favIcon.setOnClickListener(view -> {
                            final DatabaseReference push = mDatabase.child("USERS")
                                    .child(currentUser.getUid())
                                    .child("favorites").child(houseId);

                            push.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() == null) {
                                        DatabaseReference houseFavRef = mDatabase.child("USERS")
                                                .child(currentUser.getUid())
                                                .child("favorites");

                                        Calendar cal = Calendar.getInstance();
                                        int day = cal.get(Calendar.DAY_OF_MONTH);
                                        int month = cal.get(Calendar.MONTH) + 1;
                                        int year = cal.get(Calendar.YEAR);

                                        HashMap<String, Object> favMap = new HashMap<>();

                                        favMap.put(houseId, day
                                                + "/" + month
                                                + "/" + year);

                                        houseFavRef.updateChildren(favMap);

                                        viewHolder.favIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart));
                                    } else {
                                        push.removeValue();
                                        viewHolder.favIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart_empty));
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        });


                    }
                };

        recyclerView.setAdapter(mHousesRecyclerAdapter);
    }

    public static class HousesViewHolder extends RecyclerView.ViewHolder {
        DatabaseReference favHouseRef;
        View mView;
        ImageView favIcon;
        Context context;

        public HousesViewHolder(View itemView) {
            super(itemView);
            favHouseRef = FirebaseDatabase.getInstance().getReference();
            mView = itemView;
            favIcon = mView.findViewById(R.id.single_house_favourite_img);
        }

        void setHouseName(String house_name) {
            TextView house_name_tv = mView.findViewById(R.id.single_house_name_tv);
            house_name_tv.setText(house_name);
        }

        void setCity(String city, String country) {
            TextView city_tv = mView.findViewById(R.id.single_house_location_tv);
            String finalString = city + "," + country;
            city_tv.setText(finalString);
        }

        void setImage(String mainFoto, Context context) {
            ImageView main_image = mView.findViewById(R.id.single_house_image);
            Picasso.with(context).load(mainFoto).placeholder(R.drawable.property_placeholder).into(main_image);
        }

        void setPrice(String price) {
            TextView price_tv = mView.findViewById(R.id.single_house_price);
            String finalText = price + "€";
            price_tv.setText(finalText);
        }

        void setFavStatus(final String houseId, FirebaseUser currentUser) {
            if (currentUser != null) {
                String uid = currentUser.getUid();
                final DatabaseReference isFav = favHouseRef.child("USERS")
                        .child(uid)
                        .child("favorites").child(houseId);

                isFav.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            favIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_empty));
                        } else {
                            favIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }

        public void setContext(Context applicationContext) {
            context = applicationContext;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: Sign/Register");

        if (requestCode == RC_SIGN_IN) {

            final FirebaseUser signedUser = mAuth.getCurrentUser();

            final String currentToken = FirebaseInstanceId.getInstance().getToken();

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Time now = new Time();
                    Log.d(TAG, "onDataChange: TIME " + now.toString());
                    Log.d(TAG, "onDataChange: " + dataSnapshot.child(getString(R.string.users)).child(signedUser.getUid()).exists());
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    if (!dataSnapshot.child(getString(R.string.users)).child(signedUser.getUid()).exists()) {
                        Log.d(TAG, "onActivityResult: Register");

                        uid = setNullToDefaultValue(signedUser.getUid());
                        status = "default_status";
                        displayName = setNullToDefaultValue(signedUser.getDisplayName());
                        email = setNullToDefaultValue(signedUser.getEmail());
                        photoUrl = setNullToDefaultValue(signedUser.getPhotoUrl().toString());
                        provider = setNullToDefaultValue(signedUser.getProviderId());
                        phoneNumber = setNullToDefaultValue(signedUser.getPhoneNumber());
                        token = currentToken;
                        houses = "0";
                        visitors = 0;
                        friends = "0";

                        User mUser = new User(uid, status, displayName, email,
                                photoUrl, provider, phoneNumber, token, true, houses,
                                visitors, friends);
                        Map<String, Object> userMap = mUser.toMap();
                        userMap.put("register_date", format.toString());

                        mDatabase.child(getString(R.string.users)).child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);
                    } else {
                        Log.d(TAG, "onActivityResult: Login");

                        Log.d(TAG, "onActivityResult: Token: " + currentToken);

                        mDatabase.child(getString(R.string.users)).child(mAuth.getCurrentUser().getUid()).child("last_login").setValue(format.toLocalizedPattern());

                        mDatabase.child(getString(R.string.users)).child(mAuth.getCurrentUser().getUid()).child("msgToken")
                                .setValue(currentToken);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds cities to the action bar if it is present.
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
                    .addOnCompleteListener(task -> {
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
                    });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int id) {
        switch (id) {
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.nav_search:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
            case R.id.nav_profile:
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null)
                    profileIntent.putExtra("from_user_id", user.getUid());
                startActivity(profileIntent);
                break;
            case R.id.nav_friends:
                startActivity(new Intent(MainActivity.this, FriendsActivity.class));
                break;
            case R.id.nav_reservation:
                startActivity(new Intent(MainActivity.this, ReservationsActivity.class));
                break;
            case R.id.nav_favourites:
                startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
                break;
            case R.id.nav_top_destinations:
                startActivity(new Intent(MainActivity.this, TopDestinationsActivity.class));
                break;
            case R.id.nav_cities:
                startActivity(new Intent(MainActivity.this, CitiesActivity.class));
                break;
            case R.id.nav_messages:
                startActivity(new Intent(MainActivity.this, MessagesActivity.class));
                break;
            case R.id.nav_settings:
                Intent settingsIntent = new Intent(MainActivity.this, Settings2Activity.class);
                startActivity(settingsIntent);
                break;
            case R.id.nav_add_house:
                Intent intent = new Intent(getApplicationContext(), HomeAdd.class);
                startActivity(intent);
                break;

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

