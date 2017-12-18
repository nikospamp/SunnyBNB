package com.example.ptuxiaki.sunnybnb.ui.Search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ptuxiaki.sunnybnb.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.util.List;


public class CalendarFragment extends Fragment {

    private static final String TAG = "CalendarFragment";

    private PassCalendarInterface passCalendarInterface;


    public CalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        MaterialCalendarView calendarView = view.findViewById(R.id.search_calendar);

        calendarView.setSelectionMode(3);

        calendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                passCalendarInterface.calendarDatesForSearch(dates);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            passCalendarInterface = (PassCalendarInterface) context;
        } catch (Exception e) {
            Log.d(TAG, "onAttach: Booking Activity Must Implement ~PassDatesInterface~");
        }
    }

    public interface PassCalendarInterface {
        void calendarDatesForSearch(List<CalendarDay> dates);
    }

}
