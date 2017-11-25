package com.example.ptuxiaki.sunnybnb.ui.Booking;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseDatesFragment extends Fragment {

    private static final String TAG = "ChooseDatesFragment";

    private DatabaseReference bookings;

    private MaterialCalendarView calendar;

    private ArrayList<String> dates = new ArrayList<>();

    private String HOUSE_ID = "";

    private int dateToPass = -1;

    private PassDatesInterface arrivalInterface;

    public ChooseDatesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dates, container, false);

        HOUSE_ID = getArguments().getString("HOUSE_ID");

        dateToPass = getArguments().getInt("DATE");

        calendar = view.findViewById(R.id.calendarView);

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
                        int year = day.getYear();

                        int month = day.getMonth() + 1;
                        String monthExtraZero = "";
                        if (month < 10)
                            monthExtraZero = "0";

                        int monthDay = day.getDay();
                        String dayExtraZero = "";
                        if (monthDay < 10)
                            dayExtraZero = "0";

                        String date = year + "-" + monthExtraZero + month + "-" + dayExtraZero + monthDay;
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

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (dateToPass == 0) {
                    arrivalInterface.arrivalDate(date.getDay(), date.getMonth() + 1, date.getYear());
                } else if (dateToPass == 1) {
                    arrivalInterface.departureDate(date.getDay(), date.getMonth() + 1, date.getYear());
                }
            }
        });

        return view;
    }

    public interface PassDatesInterface {
        void arrivalDate(int day, int month, int year);

        void departureDate(int day, int month, int year);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            arrivalInterface = (PassDatesInterface) context;
        } catch (Exception e) {
            Log.d(TAG, "onAttach: Booking Activity Must Implement ~PassDatesInterface~");
        }
    }
}
