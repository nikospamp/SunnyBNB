package com.example.ptuxiaki.sunnybnb;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import static android.content.ContentValues.TAG;

/**
 * Created by Pampoukidis on 1/6/2017.
 */

public class ChangeAuthStatusFragment extends android.support.v4.app.Fragment {
    private static final int RC_SIGN_IN = 123;
    private Button mButton;
    private FirebaseAuth mAuth;



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("User Log Out");
        AuthUI.getInstance()
                .signOut(getActivity())
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: " + "user logged out!");
                        mAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser == null) {
                            startActivityForResult(
                                    AuthUI.getInstance()
                                            .createSignInIntentBuilder()
                                            .setAvailableProviders(
                                                    Arrays.asList(
                                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                                            new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                                    ))
                                            .build(),
                                    RC_SIGN_IN);
                        }
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loguser, container, false);



        return view;
    }
}
