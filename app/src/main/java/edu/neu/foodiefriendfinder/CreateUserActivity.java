package edu.neu.foodiefriendfinder;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.neu.foodiefriendfinder.models.User;


public class CreateUserActivity extends AppCompatActivity {

    private Spinner genderDropDown;


    private TextView languageSelected;
    private TextView cuisineSelected;

    private String[] languagesBank;
    private boolean[] checkedLanguages;
    private ArrayList<Integer> languagesIndex = new ArrayList<>();
    private ArrayList<String> userLanguages = new ArrayList<>();

    private String[] cuisineBank;
    private boolean[] checkedCuisine;
    private ArrayList<Integer> cuisineIndex = new ArrayList<>();
    private ArrayList<String> userCuisine = new ArrayList<>();

    private EditText userId;
    private EditText userFirstName;
    private EditText userLastName;
    private EditText userPhone;
    private EditText userDob;
    private EditText userEmail;


    private DatabaseReference usersReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        usersReference = FirebaseDatabase.getInstance().getReference();

        userId = findViewById(R.id.userId);
        userFirstName = findViewById(R.id.firstName);
        userLastName = findViewById(R.id.lastName);
        userPhone = findViewById(R.id.phone);
        userDob = findViewById(R.id.dobId);
        userEmail = findViewById(R.id.emailId);

        genderDropDown = findViewById(R.id.genderSpinner);
        languageSelected = findViewById(R.id.languageId);
        cuisineSelected = findViewById(R.id.cuisineId);

        // gender select
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(CreateUserActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.genderOptions));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderDropDown.setAdapter(myAdapter);


        // cuisine select
        cuisineBank = getResources().getStringArray(R.array.cuisineOptions);
        checkedCuisine = new boolean[cuisineBank.length];

        ImageButton cuisineSelection = findViewById(R.id.selectCuisineButton);
        cuisineSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSelectDialog(cuisineBank, checkedCuisine, cuisineIndex, userCuisine, cuisineSelected).show();
            }
        });

        // language select
        languagesBank = getResources().getStringArray(R.array.languageOptions);
        checkedLanguages = new boolean[languagesBank.length];

        ImageButton languageSelection = findViewById(R.id.languageSelectButton);
        languageSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSelectDialog(languagesBank, checkedLanguages, languagesIndex, userLanguages, languageSelected).show();
            }
        });


        // create user
        Button register = findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewUser();
            }
        });

        // go login
        Button goLogin = findViewById(R.id.goLoginButton);
        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLoginActivity();
            }
        });
    }



    private Dialog makeSelectDialog(final String[] itemBank, boolean[] checkedItems,
                                        final ArrayList<Integer> itemIndex,
                                        final ArrayList<String> userItems,
                                        final TextView itemSelected) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CreateUserActivity.this);
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

    private void createNewUser() {
        String id = userId.getText().toString();
        String firstName = userFirstName.getText().toString();
        String lastName = userLastName.getText().toString();
        String email = userEmail.getText().toString();
        String phone = userPhone.getText().toString();
        String dob = userDob.getText().toString();
        String gender = genderDropDown.getSelectedItem().toString();

        if (!id.equals("") && !firstName.equals("") && !lastName.equals("") && !email.equals("") &&
                !phone.equals("") && !dob.equals("") && !gender.equals("") && userCuisine.size() > 0
        && userLanguages.size() > 0) {
            User newUser = new User(id, firstName, lastName, email, phone, userCuisine, dob, userLanguages, gender);
            usersReference.child("Users").child(id).setValue(newUser);
            Toast.makeText(this, "Success!!!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something unfilled!!!", Toast.LENGTH_SHORT).show();

        }
    }

    private void goLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }



}
