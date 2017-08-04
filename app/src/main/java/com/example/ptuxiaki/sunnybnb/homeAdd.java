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
import java.util.Objects;

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
    private Object services_code[][] = {{"aircondition", 0},
            {"balcony", 0}, {"breakfast", 0}, {"cafe_bar_restaurant", 0}
            , {"child_keeping", 0}, {"clothes_laundry", 0}
            , {"conference_rooms", 0}, {"dinner", 0}, {"doctor_support", 0}
            , {"elevator", 0}, {"hair_dryer", 0}, {"in_room_safebox", 0}
            , {"iron_ironing_board", 0}, {"minibar", 0}
            , {"newspaper_delivery", 0}, {"parking", 0}
            , {"private_bath", 0}, {"reception_24", 0}
            , {"room_service", 0}, {"soundproof_walls", 0}
            , {"telephone", 0}, {"wifi,0"}};

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
        houseObject.put("hid", tempRef.getKey());
        tempRef.setValue(houseObject);

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
//                controller.onListItemClick(servicesItem);
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
