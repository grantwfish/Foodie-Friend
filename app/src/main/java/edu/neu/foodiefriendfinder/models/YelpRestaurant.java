package edu.neu.foodiefriendfinder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YelpRestaurant {
    @SerializedName("rating")
    @Expose
    public double rating;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("is_closed")
    @Expose
    public Boolean isClosed;
    @SerializedName("categories")
    @Expose
    public List<YelpRestaurantCategory> categories = null;
    @SerializedName("review_count")
    @Expose
    public Integer reviewCount;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("coordinates")
    @Expose
    public YelpRestaurantCoor coordinates;
    @SerializedName("image_url")
    @Expose
    public String imageUrl;
    @SerializedName("location")
    @Expose
    public YelpRestaurantLocation location;
    @SerializedName("distance")
    @Expose
    public Double distanceInMeters;

    public YelpRestaurant() {
    }

    public String meterToMile(){
        double mi = distanceInMeters * 0.00062137;
        return String.format("%.1f", mi) + " Mi";
    }

    public String getRestaurantName() {
        return this.name;
    }



}
