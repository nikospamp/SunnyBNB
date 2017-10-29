package com.example.ptuxiaki.sunnybnb.ui.TopDestinations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ptuxiaki.sunnybnb.R;

/**
 * Created by Pampoukidis on 1/6/2017.
 */

public class TopDestinationsFragment extends android.support.v4.app.Fragment {
    private Button mButton;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Top Destinations");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.destinations, container, false);

        mButton = (Button) view.findViewById(R.id.destinationsBtn);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BTN", "onClick: ");
            }
        });

        return view;

    }
}
