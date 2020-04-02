package edu.neu.foodiefriendfinder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YelpRestaurantLocation {
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("address2")
    @Expose
    public String address2;
    @SerializedName("address3")
    @Expose
    public String address3;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("address1")
    @Expose
    public String address1;
    @SerializedName("zip_code")
    @Expose
    public String zipCode;

}

