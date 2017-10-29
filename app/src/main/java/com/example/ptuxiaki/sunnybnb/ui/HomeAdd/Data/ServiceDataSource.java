package com.example.ptuxiaki.sunnybnb.ui.HomeAdd.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pampoukidis on 4/8/2017.
 */

public class ServiceDataSource implements DataSourceInterface {

    //22
    private final String services[] = {"Air Condition", "Balcony", "Breakfast", "Cafe/Bar & Restaurant", "Child Keeping", "Laundry", "Conference Rooms"
            , "Dinner", "Doctor Support", "Elevator", "Hair Dryer", "Safe Box", "Iron & Ironing Board", "Minibar", "Newspaper Delivery"
            , "Parking", "Private Bath", "Reception 24/7", "Room Service", "Soundproof Walls", "Telephone", "Wifi"};

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
