package com.example.ptuxiaki.sunnybnb;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;


/**
 * Created by Pampoukidis on 1/6/2017.
 */

public class ProfileFragment extends android.support.v4.app.Fragment {

    private Button mButton;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        EditText name_ = (EditText) view.findViewById(R.id.et_name);
        final EditText email_ = (EditText) view.findViewById(R.id.et_email);
        Button save_ = (Button) view.findViewById(R.id.btn_save);

        save_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName("Maria Juan Delos Trelos DiPaquitta")
                        .build();
                FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);
                if(FirebaseAuth.getInstance().getCurrentUser().updateEmail(email_.getText().toString()).isSuccessful()){
                    Log.e("email","email saved");
                }else{
                    Log.e("email","email NOT saved");
                    Log.e("email",email_.getText().toString());

                }
            }
        });
        mRequestQueue = Volley.newRequestQueue(getActivity());
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
        if(user!=null){
            name_.setText(user.getDisplayName());
            email_.setText(user.getEmail());
            NetworkImageView avatar = (NetworkImageView)view.findViewById(R.id.profile_pic);
            String phone = user.getPhoneNumber();
            if(user.getPhotoUrl()!=null){
                avatar.setImageUrl(user.getPhotoUrl().toString(),mImageLoader);
            }else{
               // avatar.setDefaultImageResId(R.drawable.user);
                for(UserInfo mUserInfo : user.getProviderData()){
                    if(mUserInfo.getPhotoUrl() != null){
                        avatar.setImageUrl(mUserInfo.getPhotoUrl().toString(),mImageLoader);
                    }

                }
            }

        }else{
            Log.e("User","null current user");
        }
//        mButton = (Button) view.findViewById(R.id.loginBtn);

//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("BTN", "onClick: ");
//            }
//        });

        return view;

    }
}
