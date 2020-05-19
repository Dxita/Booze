package com.sky21.liquor_app.models;

import java.util.HashMap;
import java.util.List;

public class ProductModel {
    HashMap<String,String> mapList;

    String id;
    int quantity;
    String totalCost;

    public ProductModel() {
    }

    public ProductModel(HashMap<String, String> mapList) {
        this.mapList = mapList;
    }

    public HashMap<String, String> getMapList() {
        return mapList;
    }

    public void setMapList(HashMap<String, String> mapList) {
        this.mapList = mapList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

}
