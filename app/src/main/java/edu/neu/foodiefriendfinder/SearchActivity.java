package edu.neu.foodiefriendfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import java.util.ArrayList;

import edu.neu.foodiefriendfinder.models.YelpDataClass;
import edu.neu.foodiefriendfinder.models.YelpRestaurant;
import edu.neu.foodiefriendfinder.yelpData.YelpService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    private Spinner cuisineDropDown;
    private Spinner distanceDropDown;
    private Spinner priceDropDown;

    private String[] priceBank;
    private boolean[] checkedPrice;
    private ArrayList<Integer> priceIndex = new ArrayList<>();
    private ArrayList<String> userPrice = new ArrayList<>();


    private RestaurantsAdapter adapter;
    private static final String BASE_URL = "https://api.yelp.com/v3/";
    private static final String API_KEY = BuildConfig.API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        // cuisine select
        cuisineDropDown = findViewById(R.id.cusineSpinner);
        ArrayAdapter<String> cusineAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.cuisineInSearch));

        cusineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cuisineDropDown.setAdapter(cusineAdapter);


        //Radius selection
        distanceDropDown = findViewById(R.id.distanceSpinner);
        ArrayAdapter<String> radiusAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.distanceOptions));

        radiusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceDropDown.setAdapter(radiusAdapter);

        // price select
        priceDropDown = findViewById(R.id.priceSpinner);
        ArrayAdapter<String> priceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.priceOptions));

        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceDropDown.setAdapter(priceAdapter);

        Button search = findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Yelp Params
                String cuisine = cuisineDropDown.getSelectedItem().toString();
                int meter = getDistance();
                String priceRange = getPriceRange();

                RecyclerView recyclerView;

                ArrayList<YelpRestaurant> restaurants = new ArrayList<YelpRestaurant>();
                adapter = new RestaurantsAdapter(R.layout.item_restaurant, SearchActivity.this);
                recyclerView = findViewById(R.id.rvRestaurants);
                recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                recyclerView.setAdapter(adapter);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                YelpService yelpService = retrofit.create(YelpService.class);
                yelpService.searchRestaurants(API_KEY,cuisine, 42.3398,
                        -71.0892, meter, priceRange).enqueue(new Callback<YelpDataClass>() {

                    private static final String TAG = "search";

                    @Override
                    public void onResponse(Call<YelpDataClass> call, Response<YelpDataClass> response) {
                        int statusCode = response.code();
                        YelpDataClass yelpDataClass = response.body();
                        if (yelpDataClass == null) {
                            Log.w(TAG, "Did not receive valid response from Yelp API");
                            return;
                        }
                        restaurants.addAll(yelpDataClass.restaurants);
                        adapter.setRestaurants(restaurants);

                    }

                    @Override
                    public void onFailure(Call<YelpDataClass> call, Throwable t) {
                        // Log error here since request failed

                    }
                });

            }
        });
    }

    public String getPriceRange() {
        String price = priceDropDown.getSelectedItem().toString();
        if (price.equals("Any Range")) {
            return "1, 2, 3, 4";
        }
        else if (price.equals("$")) {
            return "1";
        }
        else if (price.equals("$$")) {
            return "2";
        }
        else if (price.equals("$$$")) {
            return "3";
        }
        else {
            return "4";
        }
    }

    //get use selected radius and covert to meters
    //The max value of YELP can take which is 40000 meters (about 25 miles)
    public int getDistance() {
        double mi = 0;
        String miles = distanceDropDown.getSelectedItem().toString();
        if (miles.equals("Any distance")) {
            return 40000;
        }
        else if (miles.equals("0.3 mi")) {
            mi = 0.3;
        }
        else if (miles.equals("1 mi")) {
            mi = 1;
        }
        else if (miles.equals("5 mi")) {
            mi = 5;
        }
        else if (miles.equals("20 mi")) {
            mi = 20;
        }

        return (int) Math.round(mi * 1609.34);

    }
}
