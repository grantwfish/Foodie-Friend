package edu.neu.foodiefriendfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.neu.foodiefriendfinder.models.User;

public class LoginActivity extends AppCompatActivity {

    Button signInButton;

    EditText usernameInput;

    private DatabaseReference userRef;

    public static User loginUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final List<User> users = new ArrayList<User>();

        userRef = FirebaseDatabase.getInstance().getReference();

        signInButton = findViewById(R.id.SignInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                        for (DataSnapshot child: children) {
                            User user = child.getValue(User.class);
                            users.add(user);
                        }
                        int count = 0;
                        for (User user : users) {
                            if (user.getUserId().equals(usernameInput.getText().toString())) {
                                loginUser = user;
                                loginUser.setOnline(true);

                                String currentID = loginUser.getUserId();
                                userRef.child("Users").child(currentID).child("isOnline").setValue(true);

                                Toast.makeText(LoginActivity.this, "Login", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            count++;
                        }
                        if (count == users.size()) {
                            Toast.makeText(LoginActivity.this, "incorrect username", Toast.LENGTH_SHORT).show();
                        } else {
                            // go to another activity
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        usernameInput = findViewById(R.id.usernameInput);
    }


}
