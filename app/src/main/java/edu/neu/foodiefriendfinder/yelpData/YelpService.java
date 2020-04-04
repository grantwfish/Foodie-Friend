package edu.neu.foodiefriendfinder.yelpData;

import edu.neu.foodiefriendfinder.models.YelpDataClass;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface YelpService {

    @GET("businesses/search")
    Call<YelpDataClass> searchRestaurants(@Header("Authorization") String authHeader,
                                          @Query("term") String cuisine,
                                          @Query("latitude") double latitude,
                                          @Query("longitude") double longitude,
                                          @Query("radius") int distance,
                                          @Query("price") String price);
}