package com.example.ptuxiaki.sunnybnb.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pampoukidis on 4/8/2017.
 */

public class ServiceDataSource implements DataSourceInterface {

    //22
    private final String services[] = {"aircondition", "balcony", "breakfast", "cafe_bar_restaurant", "child_keeping", "clothes_laundry", "conference_rooms"
            , "dinner", "doctor_support", "elevator", "hair_dryer", "in_room_safebox", "iron_ironing_board", "minibar", "newspaper_delivery"
            , "parking", "private_bath", "reception_24", "room_service", "soundproof_walls", "telephone", "wifi"};

    @Override
    public List<ServicesItem> getListOfData() {
        ArrayList<ServicesItem> listOfData = new ArrayList<>();
        for (int i = 0; i < services.length; i++) {
            ServicesItem servicesItem = new ServicesItem(services[i]);
            listOfData.add(servicesItem);
        }
        return listOfData;
    }
}
