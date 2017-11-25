package com.example.ptuxiaki.sunnybnb.ui.Booking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ptuxiaki.sunnybnb.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;

public class BookingActivity extends AppCompatActivity {

    private static final String TAG = "BookingActivity";

    private DatabaseReference bookings;

    private MaterialCalendarView calendar;

    private ArrayList<String> dates = new ArrayList<>();

    String HOUSE_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Intent intent = getIntent();

        HOUSE_ID = intent.getStringExtra("HOUSE_ID");

        calendar = (MaterialCalendarView) findViewById(R.id.calendarView);

        bookings = FirebaseDatabase.getInstance().getReference().child("RESERVATIONS").child(HOUSE_ID);

        bookings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    dates.add(d.getKey());
                }

                calendar.addDecorator(new DayViewDecorator() {
                    @Override
                    public boolean shouldDecorate(CalendarDay day) {
                        String date = day.getYear() + "-" + (day.getMonth() + 1) + "-" + day.getDay();
                        return dates.contains(date);
                    }

                    @Override
                    public void decorate(DayViewFacade view) {
                        view.setDaysDisabled(true);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
