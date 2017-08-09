package com.example.ptuxiaki.sunnybnb;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikos on 22/7/2017.
 */

public class House {
    private String availability;
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
    private String postalCode;
    //Services
    private String aircondition;
    private String balcony;
    private String breakfast;
    private String cafe_bar_restaurant;
    private String child_keeping;
    private String clothes_laundry;
    private String conference_rooms;
    private String dinner;
    private String doctor_support;
    private String elevator;
    private String hair_dryer;
    private String in_room_safebox;
    private String iron_ironing_board;
    private String minibar;
    private String newspaper_delivery;
    private String parking;
    private String private_bath;
    private String reception_24;
    private String room_service;
    private String soundproof_walls;
    private String wifi;
    private String uid;
    private Object user_reviews;

    public House() {
    }

    public House(String availability, String city, String country, String description, Object fotos, String hid, String house_name, String langitude, String longitude, String mainFoto, String max_people, String price, String rating, String postalCode, String aircondition, String balcony, String breakfast, String cafe_bar_restaurant, String child_keeping, String clothes_laundry, String conference_rooms, String dinner, String doctor_support, String elevator, String hair_dryer, String in_room_safebox, String iron_ironing_board, String minibar, String newspaper_delivery, String parking, String private_bath, String reception_24, String room_service, String soundproof_walls, String wifi, String uid, Object user_reviews) {
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
        this.aircondition = aircondition;
        this.balcony = balcony;
        this.breakfast = breakfast;
        this.cafe_bar_restaurant = cafe_bar_restaurant;
        this.child_keeping = child_keeping;
        this.clothes_laundry = clothes_laundry;
        this.conference_rooms = conference_rooms;
        this.dinner = dinner;
        this.doctor_support = doctor_support;
        this.elevator = elevator;
        this.hair_dryer = hair_dryer;
        this.in_room_safebox = in_room_safebox;
        this.iron_ironing_board = iron_ironing_board;
        this.minibar = minibar;
        this.newspaper_delivery = newspaper_delivery;
        this.parking = parking;
        this.private_bath = private_bath;
        this.reception_24 = reception_24;
        this.room_service = room_service;
        this.soundproof_walls = soundproof_walls;
        this.wifi = wifi;
        this.uid = uid;
        this.user_reviews = user_reviews;
        this.postalCode = postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFotos(Object fotos) {
        this.fotos = fotos;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public void setHouse_name(String house_name) {
        this.house_name = house_name;
    }

    public void setLangitude(String langitude) {
        this.langitude = langitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setMainFoto(String mainFoto) {
        this.mainFoto = mainFoto;
    }

    public void setMax_people(String max_people) {
        this.max_people = max_people;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setAircondition(String aircondition) {
        this.aircondition = aircondition;
    }

    public void setBalcony(String balcony) {
        this.balcony = balcony;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public void setCafe_bar_restaurant(String cafe_bar_restaurant) {
        this.cafe_bar_restaurant = cafe_bar_restaurant;
    }

    public void setChild_keeping(String child_keeping) {
        this.child_keeping = child_keeping;
    }

    public void setClothes_laundry(String clothes_laundry) {
        this.clothes_laundry = clothes_laundry;
    }

    public void setConference_rooms(String conference_rooms) {
        this.conference_rooms = conference_rooms;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public void setDoctor_support(String doctor_support) {
        this.doctor_support = doctor_support;
    }

    public void setElevator(String elevator) {
        this.elevator = elevator;
    }

    public void setHair_dryer(String hair_dryer) {
        this.hair_dryer = hair_dryer;
    }

    public void setIn_room_safebox(String in_room_safebox) {
        this.in_room_safebox = in_room_safebox;
    }

    public void setIron_ironing_board(String iron_ironing_board) {
        this.iron_ironing_board = iron_ironing_board;
    }

    public void setMinibar(String minibar) {
        this.minibar = minibar;
    }

    public void setNewspaper_delivery(String newspaper_delivery) {
        this.newspaper_delivery = newspaper_delivery;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public void setPrivate_bath(String private_bath) {
        this.private_bath = private_bath;
    }

    public void setReception_24(String reception_24) {
        this.reception_24 = reception_24;
    }

    public void setRoom_service(String room_service) {
        this.room_service = room_service;
    }

    public void setSoundproof_walls(String soundproof_walls) {
        this.soundproof_walls = soundproof_walls;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUser_reviews(Object user_reviews) {
        this.user_reviews = user_reviews;
    }

    public String getAvailability() {
        return availability;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getDescription() {
        return description;
    }

    public Object getFotos() {
        return fotos;
    }

    public String getHid() {
        return hid;
    }

    public String getHouse_name() {
        return house_name;
    }

    public String getLangitude() {
        return langitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getMainFoto() {
        return mainFoto;
    }

    public String getMax_people() {
        return max_people;
    }

    public String getPrice() {
        return price;
    }

    public String getRating() {
        return rating;
    }

    public String getAircondition() {
        return aircondition;
    }

    public String getBalcony() {
        return balcony;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public String getCafe_bar_restaurant() {
        return cafe_bar_restaurant;
    }

    public String getChild_keeping() {
        return child_keeping;
    }

    public String getClothes_laundry() {
        return clothes_laundry;
    }

    public String getConference_rooms() {
        return conference_rooms;
    }

    public String getDinner() {
        return dinner;
    }

    public String getDoctor_support() {
        return doctor_support;
    }

    public String getElevator() {
        return elevator;
    }

    public String getHair_dryer() {
        return hair_dryer;
    }

    public String getIn_room_safebox() {
        return in_room_safebox;
    }

    public String getIron_ironing_board() {
        return iron_ironing_board;
    }

    public String getMinibar() {
        return minibar;
    }

    public String getNewspaper_delivery() {
        return newspaper_delivery;
    }

    public String getParking() {
        return parking;
    }

    public String getPrivate_bath() {
        return private_bath;
    }

    public String getReception_24() {
        return reception_24;
    }

    public String getRoom_service() {
        return room_service;
    }

    public String getSoundproof_walls() {
        return soundproof_walls;
    }

    public String getWifi() {
        return wifi;
    }

    public String getUid() {
        return uid;
    }

    public Object getUser_reviews() {
        return user_reviews;
    }

    @Override
    public String toString() {
        return "House{" +
                "availability='" + availability + '\'' +
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
                ", postalCode='" + postalCode + '\'' +
                ", aircondition='" + aircondition + '\'' +
                ", balcony='" + balcony + '\'' +
                ", breakfast='" + breakfast + '\'' +
                ", cafe_bar_restaurant='" + cafe_bar_restaurant + '\'' +
                ", child_keeping='" + child_keeping + '\'' +
                ", clothes_laundry='" + clothes_laundry + '\'' +
                ", conference_rooms='" + conference_rooms + '\'' +
                ", dinner='" + dinner + '\'' +
                ", doctor_support='" + doctor_support + '\'' +
                ", elevator='" + elevator + '\'' +
                ", hair_dryer='" + hair_dryer + '\'' +
                ", in_room_safebox='" + in_room_safebox + '\'' +
                ", iron_ironing_board='" + iron_ironing_board + '\'' +
                ", minibar='" + minibar + '\'' +
                ", newspaper_delivery='" + newspaper_delivery + '\'' +
                ", parking='" + parking + '\'' +
                ", private_bath='" + private_bath + '\'' +
                ", reception_24='" + reception_24 + '\'' +
                ", room_service='" + room_service + '\'' +
                ", soundproof_walls='" + soundproof_walls + '\'' +
                ", wifi='" + wifi + '\'' +
                ", uid='" + uid + '\'' +
                ", user_reviews=" + user_reviews +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> houseObject = new HashMap<>();

        houseObject.put("city", city);
        houseObject.put("country", country);
        houseObject.put("description", description);
        houseObject.put("fotos", fotos);
        houseObject.put("hid", hid);
        houseObject.put("house_name", house_name);
        houseObject.put("langitude", langitude);
        houseObject.put("longitude", longitude);
        houseObject.put("mainFoto", mainFoto);
        houseObject.put("max_people", max_people);
        houseObject.put("price", price);
        houseObject.put("rating", rating);
        houseObject.put("postalCode", postalCode);
        houseObject.put("aircondition", aircondition);
        houseObject.put("balcony", balcony);
        houseObject.put("breakfast", breakfast);
        houseObject.put("cafe_bar_restaurant", cafe_bar_restaurant);
        houseObject.put("child_keeping", child_keeping);
        houseObject.put("clothes_laundry", clothes_laundry);
        houseObject.put("conference_rooms", conference_rooms);
        houseObject.put("dinner", dinner);
        houseObject.put("doctor_support", doctor_support);
        houseObject.put("elevator", elevator);
        houseObject.put("hair_dryer", hair_dryer);
        houseObject.put("in_room_safebox", in_room_safebox);
        houseObject.put("iron_ironing_board", iron_ironing_board);
        houseObject.put("minibar", minibar);
        houseObject.put("newspaper_delivery", newspaper_delivery);
        houseObject.put("parking", parking);
        houseObject.put("private_bath", private_bath);
        houseObject.put("reception_24", reception_24);
        houseObject.put("room_service", room_service);
        houseObject.put("soundproof_walls", soundproof_walls);
        houseObject.put("wifi", wifi);
        houseObject.put("uid", uid);
        houseObject.put("user_reviews", user_reviews);

        return houseObject;
    }
}
