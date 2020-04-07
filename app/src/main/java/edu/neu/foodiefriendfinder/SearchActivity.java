package edu.neu.foodiefriendfinder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private Button searchButton;

    private LocationListener locationListener;
    private LocationManager locationManager;

    private String latitude;
    private String longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT <= 24) {
                    LatLonOld();
                }
                LatLon();
            }
        });

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
                yelpService.searchRestaurants(API_KEY, cuisine, 42.3398,
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
        } else if (price.equals("$")) {
            return "1";
        } else if (price.equals("$$")) {
            return "2";
        } else if (price.equals("$$$")) {
            return "3";
        } else {
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
        } else if (miles.equals("0.3 mi")) {
            mi = 0.3;
        } else if (miles.equals("1 mi")) {
            mi = 1;
        } else if (miles.equals("5 mi")) {
            mi = 5;
        } else if (miles.equals("20 mi")) {
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
        Snackbar.make(recyclerView, "Selected restaurants are " + restaurant.getRestaurantName() +
                ", Total selected restaurant count is " + selectedRestaurants.size(), Snackbar.LENGTH_LONG).show();
    }

    // TODO -- Implement max selection for checkboxes.
    // TODO -- Implement sending list of selected restaurants to firebase to compare to other users who picked the same restaurants.


    private void LatLon() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SearchActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            getCurrentLocation();
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } else {
            Toast.makeText(this, "No Access to GPS", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean isGPSOn = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSOn) {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    String coordinates = "Latitude: " + location.getLatitude() + "\n" +
                            "Longitude: " + location.getLongitude();


                    Toast.makeText(SearchActivity.this, coordinates, Toast.LENGTH_LONG).show();

                    locationManager.removeUpdates(locationListener);


                    double lat = location.getLatitude();
                    latitude = Double.toString(lat);

                    double lon = location.getLongitude();
                    longitude = Double.toString(lon);


                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
        }

        if (ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SearchActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {

            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            assert lm != null;
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    String coordinates = "Latitude: " + location.getLatitude() + "\n" +
                            "Longitude: " + location.getLongitude();


                    Toast.makeText(SearchActivity.this, coordinates, Toast.LENGTH_LONG).show();

                    locationManager.removeUpdates(locationListener);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
    }

    private void LatLonOld() {
        LatitudeAndLongitude latitudeAndLongitude = new LatitudeAndLongitude(getApplicationContext());
        Location location = latitudeAndLongitude.getLocation();

        if (location != null) {
            double lat = location.getLatitude();
            latitude = Double.toString(lat);

            double lon = location.getLongitude();
            longitude = Double.toString(lon);

            Toast.makeText(this, "lat: " + latitude + "\nlon: " + longitude, Toast.LENGTH_LONG).show();
        }
    }
}


