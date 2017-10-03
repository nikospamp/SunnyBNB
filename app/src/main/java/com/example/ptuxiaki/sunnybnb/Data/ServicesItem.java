package com.example.ptuxiaki.sunnybnb.Data;

/**
 * Created by Pampoukidis on 4/8/2017.
 */

public class ServicesItem {
    private String service;
    private int imageService;

    public ServicesItem() {
    }

    public ServicesItem(String service) {
        this.service = service;
//        this.imageService = imageService;
    }

    public String getService() {
        return service;
    }

    public int getImageService() {
        return imageService;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setImageService(int imageService) {
        this.imageService = imageService;
    }

    @Override
    public String toString() {
        return "ServicesItem{" +
                "service='" + service + '\'' +
                ", imageService=" + imageService +
                '}';
    }
}
