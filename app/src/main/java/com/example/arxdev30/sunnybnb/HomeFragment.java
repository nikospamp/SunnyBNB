package com.example.arxdev30.sunnybnb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Pampoukidis on 29/5/2017.
 */

public class HomeFragment extends android.support.v4.app.Fragment {

    private DatabaseReference mDatabase;
    private Button mButton;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mButton = (Button) view.findViewById(R.id.testBtn);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("BTN", "onClick: ");

//                Map<String, Object> dataObj = new HashMap<>();
//                dataObj.put("Name", "Nick");
//                dataObj.put("Surname", "Pampoukidis");
//                dataObj.put("Age", "24");
//
//                mDatabase.child("Users").child("1").updateChildren(dataObj);
            }
        });

        return view;
    }

}
