package edu.neu.foodiefriendfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button createUserButton;
    Button loginButton;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createUserButton = findViewById(R.id.createUserButton);
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToCreateUserActivity();
            }
        });

        loginButton = findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToLoginActivity();
            }
        });

        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSearchActivity();
            }
        });
    }

    private void moveToLoginActivity() {
        Intent findPrimeActivity = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(findPrimeActivity);
    }

    private void moveToCreateUserActivity() {
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }

    private void moveToSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    private void moveToCreateActivity() {
        Intent findPrimeActivity = new Intent(MainActivity.this, CreateUserActivity.class);
        startActivity(findPrimeActivity);

    }
}
