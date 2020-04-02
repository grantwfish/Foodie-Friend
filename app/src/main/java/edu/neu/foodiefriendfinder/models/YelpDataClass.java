package edu.neu.foodiefriendfinder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class YelpDataClass {
    @SerializedName("total")
    @Expose
    public Integer total;
    @SerializedName("businesses")
    @Expose
    public ArrayList<YelpRestaurant> restaurants;
}
