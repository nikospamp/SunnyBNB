package com.example.ptuxiaki.sunnybnb.Models;

import java.util.HashMap;

/**
 * Created by nikos on 22/7/2017.
 */

public class House {

    private Object availability;
    private String city;
    private String country;
    private String description;
    private Object fotos;
    private String hid;
    private String house_name;
    private String langitude;
    private String longitude;
    private String mainFoto;
    private String max_people;
    private String price;
    private double rating;
    private Object services;
    private Object uid;
    private Object user_reviews;
    private String address;
    private String phoneNumber;


    public House() {
    }

    public House(Object availability, String city, String country, String description, Object fotos, String hid, String house_name,
                 String langitude, String longitude, String mainFoto, String max_people, String price, double rating,
                 Object services, Object uid, Object user_reviews, String address, String phoneNumber) {
        this.availability = availability;
        this.city = city;
        this.country = country;
        this.description = description;
        this.fotos = fotos;
        this.hid = hid;
        this.house_name = house_name;
        this.langitude = langitude;
        this.longitude = longitude;
        this.mainFoto = mainFoto;
        this.max_people = max_people;
        this.price = price;
        this.rating = rating;
        this.services = services;
        this.uid = uid;
        this.user_reviews = user_reviews;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Object getAvailability() {
        return availability;
    }

    public void setAvailability(Object availability) {
        this.availability = availability;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getFotos() {
        return fotos;
    }

    public void setFotos(Object fotos) {
        this.fotos = fotos;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getHouse_name() {
        return house_name;
    }

    public void setHouse_name(String house_name) {
        this.house_name = house_name;
    }

    public String getLangitude() {
        return langitude;
    }

    public void setLangitude(String langitude) {
        this.langitude = langitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMainFoto() {
        return mainFoto;
    }

    public void setMainFoto(String mainFoto) {
        this.mainFoto = mainFoto;
    }

    public String getMax_people() {
        return max_people;
    }

    public void setMax_people(String max_people) {
        this.max_people = max_people;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Object getServices() {
        return services;
    }

    public void setServices(Object services) {
        this.services = services;
    }

    public Object getUid() {
        return uid;
    }

    public void setUid(Object uid) {
        this.uid = uid;
    }

    public Object getUser_reviews() {
        return user_reviews;
    }

    public void setUser_reviews(Object user_reviews) {
        this.user_reviews = user_reviews;
    }

    @Override
    public String toString() {
        return "House{" +
                "availability=" + availability +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", description='" + description + '\'' +
                ", fotos=" + fotos +
                ", hid='" + hid + '\'' +
                ", house_name='" + house_name + '\'' +
                ", langitude='" + langitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", mainFoto='" + mainFoto + '\'' +
                ", max_people='" + max_people + '\'' +
                ", price='" + price + '\'' +
                ", rating='" + rating + '\'' +
                ", services=" + services +
                ", uid=" + uid +
                ", user_reviews=" + user_reviews +
                '}';
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> houseObject = new HashMap<>();
        houseObject.put("house_name", getHouse_name());
        houseObject.put("description", getDescription());
        houseObject.put("city", getCity());
        houseObject.put("country", getCountry());
        houseObject.put("hid", getHid());
        houseObject.put("latitude", "<SOON>");
        houseObject.put("longitude", "<SOON>");
        houseObject.put("max_people", getMax_people());
        houseObject.put("price", getPrice());
        houseObject.put("uid", getUid());
        houseObject.put("address", getAddress());
        houseObject.put("phone", getPhoneNumber());
        return houseObject;
    }
}
