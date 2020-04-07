package edu.neu.foodiefriendfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;


import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import edu.neu.foodiefriendfinder.models.SelectableRestaurant;
import edu.neu.foodiefriendfinder.models.YelpDataClass;
import edu.neu.foodiefriendfinder.models.YelpRestaurant;
import edu.neu.foodiefriendfinder.yelpData.YelpService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity implements RestaurantsViewHolder.OnItemSelectedListener {

    private Spinner cuisineDropDown;
    private Spinner distanceDropDown;
    private Spinner priceDropDown;
    private Switch statusToggle;

    private String[] priceBank;
    private boolean[] checkedPrice;
    private ArrayList<Integer> priceIndex = new ArrayList<>();
    private ArrayList<String> userPrice = new ArrayList<>();

    private RestaurantsAdapter adapter;

    private static final String BASE_URL = "https://api.yelp.com/v3/";
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String TAG = "search";
    RecyclerView recyclerView;

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

        // status toggle
        statusToggle = findViewById(R.id.statusToggle);

        Button search = findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Used to store restaurants returned from API Call
                ArrayList<YelpRestaurant> restaurants = new ArrayList<>();

                // Yelp Params
                String cuisine = cuisineDropDown.getSelectedItem().toString();
                int meter = getDistance();
                String priceRange = getPriceRange();
                boolean userStatus = getStatus();



//                adapter = new RestaurantsAdapter(R.layout.item_restaurant, SearchActivity.this);


                recyclerView = findViewById(R.id.rvRestaurants);
                recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

                adapter = new RestaurantsAdapter(R.layout.item_restaurant, SearchActivity.this, restaurants, false);

                recyclerView.setAdapter(adapter);

                // Build the HTTP Request
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();



                // API Call to wrap in HTTP Request -- Async callback
                YelpService yelpService = retrofit.create(YelpService.class);
                yelpService.searchRestaurants(API_KEY,cuisine, 42.3398,
                        -71.0892, meter, priceRange).enqueue(new Callback<YelpDataClass>() {

                    @Override
                    public void onResponse(Call<YelpDataClass> call, Response<YelpDataClass> response) {
                        int statusCode = response.code();
                        YelpDataClass yelpDataClass = response.body();

                        if (yelpDataClass == null) {
                            Log.w(TAG, "Did not receive valid response from Yelp API");
                            Log.i(TAG, "statusCode: " + statusCode);
                            return;
                        }

                        // Adds list of restaurants to the YelpRestaurants list to expose needed attributes.
                        restaurants.addAll(yelpDataClass.restaurants);
                        adapter.setRestaurants(restaurants);

                    }

                    @Override
                    public void onFailure(Call<YelpDataClass> call, Throwable t) {
                        Log.e(TAG, "Yelp API failed to return restaurant data.");

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

    public boolean getStatus() {
        final boolean[] currentStatus = {false};
        statusToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            // Default value for toggle switch.

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If checked, true... otherwise false.
                currentStatus[0] = isChecked;
            }
        });
        return currentStatus[0];
    }

    @Override
    public void onItemSelected(SelectableRestaurant restaurant) {
        List<YelpRestaurant> selectedRestaurants = adapter.getSelectedRestaurants();
        Snackbar.make(recyclerView, "Selected restaurants are "  + restaurant.getRestaurantName() +
                ", Total selected restaurant count is " + selectedRestaurants.size(), Snackbar.LENGTH_LONG).show();
    }
    // TODO -- Implement max selection for checkboxes.
    // TODO -- Implement sending list of selected restaurants to firebase to compare to other users who picked the same restaurants.
}
