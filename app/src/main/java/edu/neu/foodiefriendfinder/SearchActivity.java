package edu.neu.foodiefriendfinder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private TextView cusineView;
    private TextView distanceView;
    private TextView languageView;
    private TextView priceView;

    private String[] cuisineBank;
    private boolean[] checkedCuisine;
    private ArrayList<Integer> cuisineIndex = new ArrayList<>();
    private ArrayList<String> userCuisine = new ArrayList<>();

    private String[] distanceBank;
    private boolean[] checkedDistance;
    private ArrayList<Integer> distanceIndex = new ArrayList<>();
    private ArrayList<String> userDistance = new ArrayList<>();

    private String[] languagesBank;
    private boolean[] checkedLanguages;
    private ArrayList<Integer> languagesIndex = new ArrayList<>();
    private ArrayList<String> userLanguages = new ArrayList<>();

    private String[] priceBank;
    private boolean[] checkedPrice;
    private ArrayList<Integer> priceIndex = new ArrayList<>();
    private ArrayList<String> userPrice = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        cusineView = findViewById(R.id.cusineId2);
        languageView = findViewById(R.id.languageId2);
        distanceView = findViewById(R.id.radiusId);
        priceView = findViewById(R.id.priceId);

        // cuisine select
        cuisineBank = getResources().getStringArray(R.array.cuisineOptions);
        checkedCuisine = new boolean[cuisineBank.length];


        ImageButton cuisineSelection = findViewById(R.id.selectCuisineButton2);
        cuisineSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSelectDialog(cuisineBank, checkedCuisine, cuisineIndex, userCuisine, cusineView).show();
            }
        });

        //Radius selection
        distanceBank= getResources().getStringArray(R.array.distanceOptions);
        checkedDistance = new boolean[distanceBank.length];

        ImageButton distanceSelection = findViewById(R.id.selectDistanceButton);
        distanceSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSelectDialog(distanceBank, checkedDistance, distanceIndex, userDistance, distanceView).show();
            }
        });

        // language select
        languagesBank = getResources().getStringArray(R.array.languageOptions);
        checkedLanguages = new boolean[languagesBank.length];

        ImageButton languageSelection = findViewById(R.id.selectLanguageButton2);
        languageSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSelectDialog(languagesBank, checkedLanguages, languagesIndex, userLanguages, languageView).show();
            }
        });

        // price select
        priceBank = getResources().getStringArray(R.array.priceOptions);
        checkedPrice = new boolean[priceBank.length];

        ImageButton priceSelection = findViewById(R.id.selectPriceButton);
        priceSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSelectDialog(priceBank, checkedPrice, priceIndex, userPrice, priceView).show();
            }
        });

    }

    private Dialog makeSelectDialog(final String[] itemBank, boolean[] checkedItems,
                                    final ArrayList<Integer> itemIndex,
                                    final ArrayList<String> userItems,
                                    final TextView itemSelected) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SearchActivity.this);
        dialogBuilder.setMultiChoiceItems(itemBank, checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            itemIndex.add(which);
                        } else {
                            itemIndex.remove(Integer.valueOf(which));
                        }

                    }
                });

        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String language = "";
                for (int i = 0; i < itemIndex.size(); i++) {
                    language += itemBank[itemIndex.get(i)];
                    userItems.add(itemBank[itemIndex.get(i)]);
                    if (i != itemIndex.size() - 1) {
                        language += ", ";
                    }
                }
                itemSelected.setText(language);
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        return dialog;
    }
}
