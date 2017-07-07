package com.example.ptuxiaki.sunnybnb;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static final int RC_SIGN_IN = 123;

    String uid;
    String displayName;
    String email;
    String photoUrl;
    String provider;
    String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Log.d("Here", "2");
        displaySelectedScreen(R.id.nav_home);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            FirebaseUser signedUser = mAuth.getCurrentUser();

            uid = setNullToDefaultValue(signedUser.getUid());
            displayName = setNullToDefaultValue(signedUser.getDisplayName());
            email = setNullToDefaultValue(signedUser.getEmail());
            photoUrl = setNullToDefaultValue(signedUser.getPhotoUrl().toString());
            provider = setNullToDefaultValue(signedUser.getProviderId());
            phoneNumber = setNullToDefaultValue(signedUser.getPhoneNumber());

            Log.d(TAG, "onActivityResult: User Signed In!");
            User mUser = new User(uid, displayName, email, photoUrl, provider, phoneNumber);
            Map<String, Object> userMap = mUser.toMap();
            mDatabase.child(getString(R.string.users)).child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);
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
                fragment = new HomeFragment();
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
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
            default:
                fragment = new HomeFragment();
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
