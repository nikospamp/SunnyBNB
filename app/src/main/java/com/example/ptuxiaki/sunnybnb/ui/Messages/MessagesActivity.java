package com.example.ptuxiaki.sunnybnb.ui.Messages;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ptuxiaki.sunnybnb.BaseActivity;
import com.example.ptuxiaki.sunnybnb.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MessagesActivity extends BaseActivity {

    private static final String TAG = "MessagesActivity";

    private RecyclerView servicesRec;

    private DatabaseReference servicesReference;
    List<String> servicesListNames = new ArrayList<>();
    List<Integer> servicesListImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        ButterKnife.bind(this);

        servicesRec = findViewById(R.id.rec_services);

        servicesReference = FirebaseDatabase.getInstance().getReference()
                .child("HOUSES")
                .child("-KxdERGkDFg9MDa4YPab")
                .child("services");

        servicesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Object value = snap.getValue();
                    if (value.equals("Y")) {
                        switch (snap.getKey()) {
                            case "aircondition":
                                servicesListNames.add("Air Condition");
                                servicesListImages.add(R.drawable.ic_air_conditioner);
                                break;
                            case "balcony":
                                servicesListNames.add("Balcony");
                                servicesListImages.add(R.drawable.ic_antique_balcony);
                                break;
                        }
                    }
                }
                servicesRec.setAdapter(new CustomAdapter(servicesListNames, servicesListImages));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<String> servicesListNames;
        private List<Integer> servicesListImages;

        CustomAdapter(List<String> servicesListNames, List<Integer> servicesListImages) {
            this.servicesListNames = servicesListNames;
            this.servicesListImages = servicesListImages;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String serviceName = servicesListNames.get(position);
            int serviceImage = servicesListImages.get(position);

            holder.setName(serviceName);
            holder.setImage(serviceImage);
        }

        @Override
        public int getItemCount() {
            return servicesListNames.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(Object currentService) {
            TextView serviceName = mView.findViewById(R.id.service_name);
            String text = (String) currentService;
            serviceName.setText(text);
        }

        public void setImage(Object currentService) {
            ImageView serviceImage = mView.findViewById(R.id.service_image);
            int image = (int) currentService;
            serviceImage.setImageResource(image);
        }
    }
}
