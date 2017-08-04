package com.example.ptuxiaki.sunnybnb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.ptuxiaki.sunnybnb.Data.ServiceDataSource;
import com.example.ptuxiaki.sunnybnb.Data.ServicesItem;
import com.example.ptuxiaki.sunnybnb.logic.Controller;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class homeAdd extends AppCompatActivity implements ViewInterface {

    private static final String EXTRA_SERVICE = "EXTRA_SERVICE";
    private int servicesPin[];

    private List<ServicesItem> listOfData;
    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private Controller controller;
    private DatabaseReference houseReference;

    private String HOUSES = "HOUSES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_add);

        getSupportActionBar().setTitle("Add Your House");

        houseReference = FirebaseDatabase.getInstance().getReference();

        servicesPin = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        recyclerView = (RecyclerView) findViewById(R.id.recycler_home_add);
        layoutInflater = getLayoutInflater();
        controller = new Controller(this, new ServiceDataSource());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        HashMap<String, Object> houseObject = new HashMap<>();
        houseObject.put("city", "Thessaloniki");
        houseObject.put("country", "Greece");
        houseObject.put("description", "Mpla mpla mple");

        DatabaseReference tempRef = houseReference.child(HOUSES).push();
        tempRef.setValue(houseObject);
        Log.d("keyDb", "onOptionsItemSelected: "+tempRef.getKey());

        return super.onOptionsItemSelected(item);
    }

    /**
     * RecyclerView
     */
    @Override
    public void addService(String service) {
        Log.d("TAG", "addService: ");
    }

    @Override
    public void setUpAdapterAndView(List<ServicesItem> listOfData) {
        this.listOfData = listOfData;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

        @Override
        public CustomAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.service_item, parent, false);
            return new CustomViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            ServicesItem current = listOfData.get(position);

            holder.service.setText(current.getService());
        }

        @Override
        public int getItemCount() {
            return listOfData.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView service;
            private ViewGroup container;

            public CustomViewHolder(View itemView) {
                super(itemView);
                this.service = (TextView) itemView.findViewById(R.id.single_service_txt);
                this.container = (ViewGroup) itemView.findViewById(R.id.single_service_container);

                this.container.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: ");
                ServicesItem servicesItem = listOfData.get(this.getAdapterPosition());
                servicesPin[this.getAdapterPosition()] = 1;
                controller.onListItemClick(servicesItem);
            }
        }
    }

    /**
     * Utilities
     */
    private void printTable(int[] servicesPin) {
        for (int i = 0; i < servicesPin.length; i++) {
            Log.d("printTag", "printTable: " + servicesPin[i]);
        }
        Log.d("", "");

    }
}
