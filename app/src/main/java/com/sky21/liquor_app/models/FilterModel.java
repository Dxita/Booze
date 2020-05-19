package com.sky21.liquor_app.models;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class FilterModel {
    boolean isSelected;
    HashMap<String,String> hashList;

    String id;
    String category;
    String brand;

    public FilterModel(HashMap<String, String> hashList) {
        this.hashList = hashList;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public HashMap<String, String> getHashList() {
        return hashList;
    }

    public void setHashList(HashMap<String, String> hashList) {
        this.hashList = hashList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
