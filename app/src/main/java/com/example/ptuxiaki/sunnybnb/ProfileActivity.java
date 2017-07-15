package com.example.ptuxiaki.sunnybnb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProfileActivity extends AppCompatActivity {

    private String profile = "Profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle(profile);
    }
}
