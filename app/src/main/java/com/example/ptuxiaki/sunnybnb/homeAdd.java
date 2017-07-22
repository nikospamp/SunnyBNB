package com.example.ptuxiaki.sunnybnb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

public class homeAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_add);

        getSupportActionBar().setTitle("Add Your House");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
