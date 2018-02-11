package com.example.ptuxiaki.sunnybnb.ui.HouseDetails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ptuxiaki.sunnybnb.BaseActivity;
import com.example.ptuxiaki.sunnybnb.R;
import com.example.ptuxiaki.sunnybnb.ui.Booking.BookingActivity;
import com.example.ptuxiaki.sunnybnb.ui.Profile.ProfileActivity;
import com.example.ptuxiaki.sunnybnb.ui.Reviews.ReviewsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HouseDetailsActivity extends BaseActivity {

    private ImageView mainImageView;
    private TextView houseName;
    private TextView houseLocation;
    private TextView houseDescription;
    private Button bookNow;
    private Button reviewsBtn;
    private Button ownerDetails;


    private DatabaseReference housesData;
    private FirebaseAuth mAuth;

    public String userId;
    public String ownerId;
    public String HOUSE_ID;


    private RecyclerView servicesRec;
    List<String> servicesListNames = new ArrayList<>();
    List<Integer> servicesListImages = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_details);

        HOUSE_ID = getIntent().getStringExtra("house_id");

        mAuth = FirebaseAuth.getInstance();

        userId = this.mAuth.getCurrentUser().getUid();

        housesData = FirebaseDatabase.getInstance().getReference().child("HOUSES").child(HOUSE_ID);

        mainImageView = findViewById(R.id.house_detail_photo);
        houseName = findViewById(R.id.house_detail_name);
        houseLocation = findViewById(R.id.house_detail_location);
        houseDescription = findViewById(R.id.house_details_description);
        bookNow = findViewById(R.id.house_detail_reservation_btn);
        reviewsBtn = findViewById(R.id.reviewsButton);
        ownerDetails = findViewById(R.id.house_details_owner_details_btn);
        servicesRec = findViewById(R.id.rec_services);

        bookNow.setOnClickListener(v -> {
            Intent intent = new Intent(HouseDetailsActivity.this, BookingActivity.class);
            intent.putExtra("HOUSE_ID", HOUSE_ID);
            intent.putExtra("UID", userId);
            intent.putExtra("OWNER_ID", ownerId);
            startActivity(intent);
            finish();
        });

        reviewsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(HouseDetailsActivity.this, ReviewsActivity.class);
            intent.putExtra("HOUSE_ID", HOUSE_ID);
            startActivity(intent);
        });

        ownerDetails.setOnClickListener(v -> {
            Intent profileIntent = new Intent(HouseDetailsActivity.this, ProfileActivity.class);
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null)
                profileIntent.putExtra("from_user_id", ownerId);
            startActivity(profileIntent);
        });


        housesData.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String houseNameStr = dataSnapshot.child("house_name").getValue().toString();
                String houseLocationStr = dataSnapshot.child("city").getValue().toString();
                String housePhoto = dataSnapshot.child("mainFoto").getValue().toString();
                String houseDescriptionStr = dataSnapshot.child("description").getValue().toString();
                ownerId = dataSnapshot.child("uid").getValue().toString();

                Picasso.with(getApplicationContext()).load(housePhoto).placeholder(R.drawable.common_google_signin_btn_icon_dark_normal).into(mainImageView);
                houseName.setText(houseNameStr);
                houseLocation.setText(houseLocationStr);
                houseDescription.setText(houseDescriptionStr);


                housesData.child("services").addValueEventListener(new ValueEventListener() {
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
                                    case "breakfast":
                                        servicesListNames.add("Breakfast");
                                        servicesListImages.add(R.drawable.ic_breakfast);
                                        break;
                                    case "cafe_bar_restaurant":
                                        servicesListNames.add("Café-Bar");
                                        servicesListImages.add(R.drawable.ic_coffee_cup);
                                        break;
                                    case "child_keeping":
                                        servicesListNames.add("Child-keeping");
                                        servicesListImages.add(R.drawable.ic_smiling_baby);
                                        break;
                                    case "clothes_laundry":
                                        servicesListNames.add("Laundry");
                                        servicesListImages.add(R.drawable.ic_washing_machine);
                                        break;
                                    case "conference_rooms":
                                        servicesListNames.add("Conference Room");
                                        servicesListImages.add(R.drawable.ic_presentation);
                                        break;
                                    case "dinner":
                                        servicesListNames.add("Dinner");
                                        servicesListImages.add(R.drawable.ic_dinner);
                                        break;
                                    case "doctor_support":
                                        servicesListNames.add("Doctor");
                                        servicesListImages.add(R.drawable.ic_doctor);
                                        break;
                                    case "elevator":
                                        servicesListNames.add("Elevator");
                                        servicesListImages.add(R.drawable.ic_elevator);
                                        break;
                                    case "hair_dryer":
                                        servicesListNames.add("Hair Dryer");
                                        servicesListImages.add(R.drawable.ic_hair_dryer);
                                        break;
                                    case "in_room_safebox":
                                        servicesListNames.add("Safe");
                                        servicesListImages.add(R.drawable.ic_safebox);
                                        break;
                                    case "iron_ironing_board":
                                        servicesListNames.add("Iron");
                                        servicesListImages.add(R.drawable.ic_iron);
                                        break;
                                    case "minibar":
                                        servicesListNames.add("Minibar");
                                        servicesListImages.add(R.drawable.ic_minibar);
                                        break;
                                    case "newspaper_delivery":
                                        servicesListNames.add("Newspaper");
                                        servicesListImages.add(R.drawable.ic_newspaper);
                                        break;
                                    case "parking":
                                        servicesListNames.add("Parking");
                                        servicesListImages.add(R.drawable.ic_parking_sign);
                                        break;
                                    case "private_bath":
                                        servicesListNames.add("Private Bath");
                                        servicesListImages.add(R.drawable.ic_shower);
                                        break;
                                    case "reception_24":
                                        servicesListNames.add("Reception");
                                        servicesListImages.add(R.drawable.ic_reception);
                                        break;
                                    case "soundproof_walls":
                                        servicesListNames.add("Soundproof Walls");
                                        servicesListImages.add(R.drawable.ic_sound_off);
                                        break;
                                    case "telephone":
                                        servicesListNames.add("Telephone");
                                        servicesListImages.add(R.drawable.ic_phone_call);
                                        break;
                                    case "room_service":
                                        servicesListNames.add("Room Service");
                                        servicesListImages.add(R.drawable.ic_room_service);
                                        break;
                                    case "wifi":
                                        servicesListNames.add("WiFi");
                                        servicesListImages.add(R.drawable.ic_wifi);
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


