package com.sky21.liquor_app.models;

public class FilterModel {
    boolean isSelected;
    String item_name;

    public FilterModel(String item_name) {
        this.item_name = item_name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
