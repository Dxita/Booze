package com.sky21.liquor_app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> Categories = new ArrayList<String>();
        Categories.add("Beer");
        Categories.add("Wine");
        Categories.add("Whisky");
        Categories.add("Vodka");
        Categories.add("Gin");
        Categories.add("Rum");
        Categories.add("Brandy");
        Categories.add("Tequila");






        List<String> basketball = new ArrayList<String>();
        basketball.add("Low to High");
        basketball.add("High to low");


        List<String> football = new ArrayList<String>();
        football.add("Tuborg");
        football.add("Bira 91");
        football.add("Kingfisher");
        football.add("Corona");
        football.add("Carlsberg");
        football.add("Budweiser");
        football.add("Heineken");
        football.add("Foster's");



        expandableListDetail.put("Categories", Categories);
        expandableListDetail.put("Brands", football);
        expandableListDetail.put("Price", basketball);
        return expandableListDetail;
    }
}