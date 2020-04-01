package edu.neu.foodiefriendfinder;

import com.google.gson.Gson;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class YelpFilters {

    private YelpFusionApiFactory apiFactory;
    private YelpFusionApi yelpFusionApi;
    private HashMap<String, String> searchParamsMap = new HashMap<>();

    // https://stackoverflow.com/questions/35722904/saving-the-api-key-in-gradle-properties
    private static final String API_KEY = BuildConfig.API_KEY;

    public YelpFilters() throws IOException {
        /* Empty Constructor to access helper methods. */
    }

    public YelpFilters(YelpFusionApi yelpFusionApi) throws IOException {
        this.yelpFusionApi = yelpFusionApi;
    }

    /**
     * Call this from the Activity to create the initial FusionAPI Object with the Key.
     *
     * @return YelpFusionApi Object to access methods.
     * @throws IOException Defined in the model.
     */
    public YelpFusionApi setUp() throws IOException {
        return apiFactory.createAPI(API_KEY);
    }

    /**
     * Helper Function
     * Call from the SearchActivity and used to build the HashMap that gets inputted into the YelpFusionSearchCall().
     *
     * @param cuisineType The type of food used for searching the restaurants.
     * @param latitude    Gathered from Location sensor, Yelp API uses this as a decimal.
     * @param longitude   Gathered from Location sensor, Yelp API uses this as a decimal.
     * @param radius      Entered by user, Yelp API uses this as an int. Max range is 25 miles.
     * @return HashMap to be used for API calls.
     */
    public Map<String, String> buildSearchParams(String cuisineType, String latitude, String longitude, String radius) {
        searchParamsMap.put("term", cuisineType);
        searchParamsMap.put("latitude", latitude);
        searchParamsMap.put("longitude", longitude);
        searchParamsMap.put("radius", radius);
        return searchParamsMap;
    }

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.yelp.com/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build();

    CreateUserActivity userActivity = retrofit.create(CreateUserActivity.class);

    // Synchronous
    Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(searchParamsMap);

    try
    {
        Response<SearchResponse> response = call.execute();
        SearchResponse searchResponse = response.body();
        System.out.println(searchResponse);
    } catch(
    Exception e)

    {
        e.printStackTrace();
    }


//    // Async
//    Callback<SearchResponse> callback = new Callback<SearchResponse>() {
//        @Override
//        public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
//            SearchResponse searchResponse = response.body();
//            // Update UI text with the searchResponse.
//        }
//        @Override
//        public void onFailure(Call<SearchResponse> call, Throwable t) {
//            // HTTP error happened, do something to handle it.
//        }
//    };
//
//call.enqueue(callback);








}
