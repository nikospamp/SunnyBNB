package com.example.ptuxiaki.sunnybnb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ptuxiaki.sunnybnb.Data.ServiceDataSource;
import com.example.ptuxiaki.sunnybnb.Data.ServicesItem;
import com.example.ptuxiaki.sunnybnb.logic.Controller;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class homeAdd extends AppCompatActivity implements ViewInterface {

    private List<ServicesItem> listOfData;
    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private Controller controller;

    private DatabaseReference houseReference;
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;

    private String HOUSES = "HOUSES";

    private TextInputEditText homeAddHouseName;
    private TextInputEditText homeAddDescription;
    private TextInputEditText homeAddPrice;
    private TextInputEditText homeAddNumberOfGuests;
    private TextInputEditText homeAddCountry;
    private TextInputEditText homeAddCity;
    private TextInputEditText homeAddPostalCode;
    private CircleImageView homeAddCircleImage;

    private Uri imageUri;
    private boolean dividerFlag = true;
    private ProgressDialog mProgressBar;

    private Object services_code[][] = {{"aircondition", 0},
            {"balcony", 0}, {"breakfast", 0}, {"cafe_bar_restaurant", 0}
            , {"child_keeping", 0}, {"clothes_laundry", 0}
            , {"conference_rooms", 0}, {"dinner", 0}, {"doctor_support", 0}
            , {"elevator", 0}, {"hair_dryer", 0}, {"in_room_safebox", 0}
            , {"iron_ironing_board", 0}, {"minibar", 0}
            , {"newspaper_delivery", 0}, {"parking", 0}
            , {"private_bath", 0}, {"reception_24", 0}
            , {"room_service", 0}, {"soundproof_walls", 0}
            , {"telephone", 0}, {"wifi", 0}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_add);

        getSupportActionBar().setTitle("Add Your HousesModel");

        mAuth = FirebaseAuth.getInstance();
        houseReference = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();

//        servicesPin = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        homeAddDescription = (TextInputEditText) findViewById(R.id.homeAddHouseDescInput);
        homeAddHouseName = (TextInputEditText) findViewById(R.id.homeAddNameTInput);
        homeAddPrice = (TextInputEditText) findViewById(R.id.homeAddPriceInput);
        homeAddNumberOfGuests = (TextInputEditText) findViewById(R.id.homeAddMaxPeopleInput);
        homeAddCountry = (TextInputEditText) findViewById(R.id.homeAddCountryInput);
        homeAddCity = (TextInputEditText) findViewById(R.id.homeAddCityInput);
        homeAddPostalCode = (TextInputEditText) findViewById(R.id.homeAddPostalInput);
        homeAddCircleImage = (CircleImageView) findViewById(R.id.homeAddImage);

        homeAddCircleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(homeAdd.this);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_home_add);
        layoutInflater = getLayoutInflater();
        controller = new Controller(this, new ServiceDataSource());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                Picasso.with(getApplicationContext()).load(imageUri)
                        .placeholder(R.drawable.default_profile_image).into(homeAddCircleImage);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.homeAddUploadBtn) {
            mProgressBar = new ProgressDialog(this);
            mProgressBar.setTitle("Setting Up Your HousesModel");
            mProgressBar.setMessage("Please wait while we upload your house!");
            mProgressBar.setCanceledOnTouchOutside(false);
            mProgressBar.show();

            final HashMap<String, Object> houseObject = new HashMap<>();

            houseObject.put("city", homeAddCity.getText().toString());
            houseObject.put("country", homeAddCountry.getText().toString());
            houseObject.put("description", homeAddDescription.getText().toString());
            houseObject.put("house_name", homeAddHouseName.getText().toString());
            houseObject.put("longitude", "52.25854");
            houseObject.put("latitude", "123.17477");
            houseObject.put("max_people", homeAddNumberOfGuests.getText().toString());
            houseObject.put("price", homeAddPrice.getText().toString());
            houseObject.put("uid", mAuth.getCurrentUser().getUid());

            HashMap<String, String> services_obj;
            services_obj = populateServicesObject(services_code);

            houseObject.put("services", services_obj);

            final DatabaseReference tempRef = houseReference.child(HOUSES).push();
            houseObject.put("hid", tempRef.getKey());

            StorageReference filePath = mStorageReference.child("house_images")
                    .child(tempRef.getKey() + ".jpg");

            filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        String download_url = task.getResult().getDownloadUrl().toString();
                        houseObject.put("mainFoto", download_url);
                        tempRef.setValue(houseObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mProgressBar.dismiss();
                                Toast.makeText(homeAdd.this, "HousesModel Uploaded!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(homeAdd.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(homeAdd.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    private HashMap<String, String> populateServicesObject(Object[][] services_code) {
        int col = services_code[0].length;
        HashMap<String, String> services_obj = new HashMap<>();
        String service_code = "";
        String service_status = "";
        for (Object[] aServices_code : services_code) {
            for (int j = 0; j < col; j++) {
                switch (j) {
                    case 0:
                        service_code = aServices_code[j].toString();
                        break;
                    case 1:
                        switch (aServices_code[j].toString()) {
                            case "1":
                                service_status = "Y";
                                break;
                            case "0":
                                service_status = "N";
                                break;
                        }
                        break;
                }
            }
            services_obj.put(service_code, service_status);
        }
        return services_obj;
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new CustomAdapter();

        if (dividerFlag) {
            DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
            itemDecoration.setDrawable(ContextCompat.getDrawable(homeAdd.this, R.drawable.devider_white));
            recyclerView.addItemDecoration(itemDecoration);
            dividerFlag = false;
        }
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

            int TempValue = (int) services_code[position][1];

            Log.d("TAG", "onBindViewHolder: " + TempValue + " Position: " + position);

            if (TempValue == 0)
                holder.container.setBackgroundColor(getResources().getColor(R.color.material_red_a200));
            else
                holder.container.setBackgroundColor(getResources().getColor(R.color.material_green_300));

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
//                ServicesItem servicesItem = listOfData.get(this.getAdapterPosition());
                int TempValue = (int) services_code[this.getAdapterPosition()][1];

                if (TempValue == 0)
                    services_code[this.getAdapterPosition()][1] = 1;
                else
                    services_code[this.getAdapterPosition()][1] = 0;

                controller = new Controller(homeAdd.this, new ServiceDataSource());
//                controller.onListItemClick(servicesItem);
            }
        }

    }

    /**
     * Utilities
     */
    private void printTable(Object[][] servicesPin) {
        for (int i = 0; i < 22; i++) {
            for (int j = 0; j < 2; j++) {
                Log.d("TAG", "printTable: " + servicesPin[i][j]);
            }
        }
        Log.d("", "");

    }
}
