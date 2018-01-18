package com.example.ptuxiaki.sunnybnb.Models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

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
    private Boolean online;
    private String houses;
    private long visitors;
    private String friends;

    public User() {
    }

    public User(String uid, String status, String displayName, String email, String photoUrl, String provider, String phoneNumber, String msgToken, Boolean online, String houses, long visitors, String friends) {
        this.uid = uid;
        this.status = status;
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.provider = provider;
        this.phoneNumber = phoneNumber;
        this.msgToken = msgToken;
        this.online = online;
        this.houses = houses;
        this.visitors = visitors;
        this.friends = friends;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMsgToken() {
        return msgToken;
    }

    public void setMsgToken(String msgToken) {
        this.msgToken = msgToken;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getHouses() {
        return houses;
    }

    public void setHouses(String houses) {
        this.houses = houses;
    }

    public long getVisitors() {
        return visitors;
    }

    public void setVisitors(long visitors) {
        this.visitors = visitors;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
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
