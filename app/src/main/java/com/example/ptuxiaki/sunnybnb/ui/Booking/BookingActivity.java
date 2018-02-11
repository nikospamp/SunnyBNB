package com.example.ptuxiaki.sunnybnb.ui.Booking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ptuxiaki.sunnybnb.BaseActivity;
import com.example.ptuxiaki.sunnybnb.R;
import com.example.ptuxiaki.sunnybnb.ui.Payment.PaymentActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

public class BookingActivity extends BaseActivity implements ChooseDatesFragment.PassDatesInterface {

    private static final String TAG = "BookingActivity";

    private DatabaseReference bookingReference;
    private DatabaseReference ownerReference;

    private int arrivalDay = -1;
    private int arrivalMonth = -1;
    private int arrivalYear = -1;

    private int departureDay = -1;
    private int departureMonth = -1;
    private int departureYear = -1;

    private String HOUSE_ID = "";
    private String UID = "";
    private String OWNER = "";

    private TextView arrivalTv;

    private TextView departureTv;

    private Button submitBookingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(this);

        arrivalTv = findViewById(R.id.booking_arrival);

        departureTv = findViewById(R.id.booking_departure);

        submitBookingBtn = findViewById(R.id.booking_submit_book_request_btn);

        submitBookingBtn.setOnClickListener(view -> BookNow());

        Intent intent = getIntent();
        HOUSE_ID = intent.getStringExtra("HOUSE_ID");
        UID = intent.getStringExtra("UID");
        OWNER = intent.getStringExtra("OWNER_ID");

        bookingReference = FirebaseDatabase.getInstance().getReference().child("RESERVATIONS").child(HOUSE_ID);
        ownerReference = FirebaseDatabase.getInstance().getReference().child("USERS").child(OWNER);

        ChooseDatesFragment fragmentArrival = new ChooseDatesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("HOUSE_ID", HOUSE_ID);
        bundle.putInt("DATE", 0);
        fragmentArrival.setArguments(bundle);

        loadFragment(fragmentArrival);

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.booking_fragments_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void arrivalDate(int day, int month, int year) {
        Log.d(TAG, "Calendar!! arrivalDate: ");
        arrivalDay = day;
        arrivalMonth = month;
        arrivalYear = year;
        String arrivalDate = day + "/" + month + "/" + year;
        arrivalTv.setText(arrivalDate);
        ChooseDatesFragment fragmentDeparture = new ChooseDatesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("HOUSE_ID", HOUSE_ID);
        bundle.putInt("DATE", 1);
        fragmentDeparture.setArguments(bundle);
        loadFragment(fragmentDeparture);
    }

    @Override
    public void departureDate(int day, int month, int year) {
        Log.d(TAG, "Calendar!! departureDate: ");
        departureDay = day;
        departureMonth = month;
        departureYear = year;
        String departureDate = day + "/" + month + "/" + year;
        departureTv.setText(departureDate);

    }

    private List<String> getDates(String dateString1, String dateString2) {
        ArrayList<String> dates = new ArrayList<>();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            int day = cal1.get(Calendar.DAY_OF_MONTH);
            String dayExtraZero = "";
            if (day < 10)
                dayExtraZero = "0";

            int month = cal1.get(Calendar.MONTH) + 1;
            String monthExtraZero = "";
            if (month < 10)
                monthExtraZero = "0";

            int year = cal1.get(Calendar.YEAR);
            dates.add(year + "-" + monthExtraZero + month + "-" + dayExtraZero + day);
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    void BookNow() {
        if (arrivalYear > 0 && arrivalMonth > 0 && arrivalDay > 0
                && departureYear > 0 && departureMonth > 0 && departureDay > 0) {
            Map<String, Object> bookingMap = new HashMap<>();
            Map<String, Object> bookingInfo = new HashMap<>();

            bookingInfo.put("visitor", UID);
            bookingInfo.put("owner", OWNER);

            List<String> dates = getDates(arrivalYear + "-" + arrivalMonth + "-" + arrivalDay,
                    departureYear + "-" + departureMonth + "-" + departureDay);

            for (String date : dates)
                bookingMap.put(date, bookingInfo);

            if (bookingMap.size() > 0) {
                bookingReference.updateChildren(bookingMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        ownerReference.child("visitors").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d(TAG, "onDataChange: " + dataSnapshot.toString());
                                long value = (long) dataSnapshot.getValue();
                                value = value + 1;
                                dataSnapshot.getRef().setValue(value).addOnCompleteListener(task1 -> {
                                    //Todo: Fix lazy implementation
                                    Intent intent = new Intent(BookingActivity.this, PaymentActivity.class);
                                    intent.putExtra("DAYS_LENGTH", dates.size());
                                    intent.putExtra("HOUSE_ID", HOUSE_ID);
                                    startActivity(intent);
                                    finish();
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                });
            }
        } else {
            Toast.makeText(getApplicationContext(), "Fill the dates", Toast.LENGTH_SHORT).show();
        }
    }
}
