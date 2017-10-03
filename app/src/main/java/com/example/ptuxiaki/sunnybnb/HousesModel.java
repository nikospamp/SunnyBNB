package com.example.ptuxiaki.sunnybnb;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikos on 22/7/2017.
 */

public class HousesModel {

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
    private String rating;
    private Object services;
    private Object uid;
    private Object user_reviews;


    public HousesModel() {
    }

    public HousesModel(Object availability, String city, String country, String description, Object fotos, String hid, String house_name, String langitude, String longitude, String mainFoto, String max_people, String price, String rating, Object services, Object uid, Object user_reviews) {
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
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
        return "HousesModel{" +
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> houseObject = new HashMap<>();
        return houseObject;
    }
}
