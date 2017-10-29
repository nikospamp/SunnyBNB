package com.example.ptuxiaki.sunnybnb.ui.Favourites;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ptuxiaki.sunnybnb.R;

/**
 * Created by Pampoukidis on 1/6/2017.
 */

public class FavouritesFragment extends android.support.v4.app.Fragment {
    private Button mButton;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Favourites");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourites, container, false);

        mButton = (Button) view.findViewById(R.id.favBtn);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                String temp = prefs.getString("example_list", "not_found");
                Toast.makeText(getContext(), temp, Toast.LENGTH_SHORT).show();

            }
        });

        return view;

    }
}
