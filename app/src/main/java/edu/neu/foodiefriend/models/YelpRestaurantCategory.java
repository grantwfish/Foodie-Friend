package edu.neu.foodiefriend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YelpRestaurantCategory {
    @SerializedName("title")
    @Expose
    public String title;

}
