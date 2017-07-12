package com.example.ptuxiaki.sunnybnb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


/**
 * Created by Pampoukidis on 29/5/2017.
 */

public class HomeFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "Home Fragment";
    private FirebaseAuth mAuth;
    private Button mButton;
    private static final int RC_SIGN_IN = 123;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);

//        mButton = (Button) view.findViewById(R.id.loginBtn);
        return view;
    }
}


