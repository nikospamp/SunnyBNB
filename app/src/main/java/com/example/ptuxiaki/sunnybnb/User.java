package com.example.ptuxiaki.sunnybnb;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Pampoukidis on 7/7/2017.
 */

public class User {
    private String uid;
    private String status;
    private String displayName;
    private String email;
    private String photoUrl;
    private String provider;
    private String phoneNumber;
    private String msgToken;
    private String houses;
    private String visitors;
    private String friends;

    public User() {
    }

    public User(String uid, String status, String displayName, String email, String photoUrl, String provider, String phoneNumber, String msgToken, String houses, String visitors, String friends) {
        this.uid = uid;
        this.status = status;
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.provider = provider;
        this.phoneNumber = phoneNumber;
        this.msgToken = msgToken;
        this.houses = houses;
        this.visitors = visitors;
        this.friends = friends;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public void setHouses(String houses) {
        this.houses = houses;
    }

    public void setVisitors(String visitors) {
        this.visitors = visitors;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getUid() {
        return uid;
    }

    public String getStatus() {
        return status;
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

    public String getHouses() {
        return houses;
    }

    public String getVisitors() {
        return visitors;
    }

    public String getFriends() {
        return friends;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> userObject = new HashMap<>();

        userObject.put("uid", uid);
        userObject.put("status", status);
        userObject.put("displayName", displayName);
        userObject.put("email", email);
        userObject.put("photoUrl", photoUrl);
        userObject.put("provider", provider);
        userObject.put("phoneNumber", phoneNumber);
        userObject.put("msgToken", msgToken);
        userObject.put("houses", houses);
        userObject.put("visitors", visitors);
        userObject.put("friends", friends);

        return userObject;
    }


}
