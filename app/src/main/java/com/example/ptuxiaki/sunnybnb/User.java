package com.example.ptuxiaki.sunnybnb;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pampoukidis on 7/7/2017.
 */

public class User {
    private String uid;
    private String displayName;
    private String email;
    private String photoUrl;
    private String provider;
    private String phoneNumber;
    private String msgToken;

    public User() {
    }

    public User(String uid, String displayName, String email, String photoUrl, String provider, String phoneNumber) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.provider = provider;
        this.phoneNumber = phoneNumber;

    }

    public String getUid() {
        return uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getProvider() {
        return provider;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMsgToken() {
        return msgToken;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setMsgToken(String msgToken) {
        this.msgToken = msgToken;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> userObject = new HashMap<>();

        userObject.put("uid", uid);
        userObject.put("displayName", displayName);
        userObject.put("email", email);
        userObject.put("photoUrl", photoUrl);
        userObject.put("provider", provider);
        userObject.put("phoneNumber", phoneNumber);
        userObject.put("msgToken", msgToken);

        return userObject;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", provider='" + provider + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", msgToken='" + msgToken + '\'' +
                '}';
    }
}
