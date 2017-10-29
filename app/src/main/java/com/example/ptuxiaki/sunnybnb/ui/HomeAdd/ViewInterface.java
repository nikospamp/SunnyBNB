package com.example.ptuxiaki.sunnybnb.ui.HomeAdd;

import com.example.ptuxiaki.sunnybnb.ui.HomeAdd.Data.ServicesItem;

import java.util.List;

/**
 * Created by Pampoukidis on 4/8/2017.
 */

public interface ViewInterface {
    void addService(String service);

    void setUpAdapterAndView(List<ServicesItem> listOdData);
}
