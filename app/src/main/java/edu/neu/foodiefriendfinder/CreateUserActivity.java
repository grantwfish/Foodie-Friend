package edu.neu.foodiefriendfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinner;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateUserActivity extends AppCompatActivity {

    Spinner cuisineDropDown;
    Spinner languagesDropDown;
    Spinner genderDropDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        cuisineDropDown = (Spinner) findViewById(R.id.cuisineSpinner);
        genderDropDown = (Spinner) findViewById(R.id.genderSpinner);
        languagesDropDown = (Spinner) findViewById(R.id.languagesSpinner);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(CreateUserActivity.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.genderOptions));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderDropDown.setAdapter(myAdapter);
    }
}
