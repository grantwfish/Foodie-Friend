package edu.neu.foodiefriendfinder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YelpRestaurantCoor {

    @SerializedName("latitude")
    @Expose
    public Double latitude;
    @SerializedName("longitude")
    @Expose
    public Double longitude;

}
