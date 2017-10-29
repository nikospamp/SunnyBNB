package com.example.ptuxiaki.sunnybnb.ui.HomeAdd.logic;

import com.example.ptuxiaki.sunnybnb.ui.HomeAdd.Data.DataSourceInterface;
import com.example.ptuxiaki.sunnybnb.ui.HomeAdd.Data.ServicesItem;
import com.example.ptuxiaki.sunnybnb.ui.HomeAdd.ViewInterface;

/**
 * Created by Pampoukidis on 4/8/2017.
 */

public class Controller {
    private ViewInterface view;
    private DataSourceInterface dataSource;

    public Controller(ViewInterface view, DataSourceInterface dataSource) {
        this.view = view;
        this.dataSource = dataSource;

        getListFromDataSource();
    }

    public void getListFromDataSource() {
        view.setUpAdapterAndView(dataSource.getListOfData());
    }

    public void onListItemClick(ServicesItem Item) {
        view.addService(Item.getService());
    }
}
