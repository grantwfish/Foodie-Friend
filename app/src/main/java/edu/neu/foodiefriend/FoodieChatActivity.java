package edu.neu.foodiefriend;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.github.library.bubbleview.BubbleTextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

import edu.neu.foodiefriend.models.Chat;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static edu.neu.foodiefriend.LoginActivity.loginUser;
import static edu.neu.foodiefriend.FoodieResultActivity.matchedFoodie;
import static edu.neu.foodiefriend.R.layout.item_chat;

public class FoodieChatActivity extends AppCompatActivity {

    EmojiconEditText emojiconEditText;
    ImageView emojiButton,submitButton;
    EmojIconActions emojIconActions;
    private FirebaseListAdapter<Chat> adapter;
    RelativeLayout activity_chat;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodie_chat);

        reference = FirebaseDatabase.getInstance().getReference();

        activity_chat = findViewById(R.id.activity_foodie_chat);

        emojiButton = findViewById(R.id.emoji_button);
        submitButton = findViewById(R.id.submit_button);
        emojiconEditText = findViewById(R.id.emojicon_edit_text);
        emojIconActions = new EmojIconActions(getApplicationContext(),activity_chat,emojiButton,emojiconEditText);
        emojIconActions.ShowEmojicon();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messageBody = emojiconEditText.getText().toString();
                String fullName = loginUser.getFirstName() + " " + loginUser.getLastName();

//                if (!messageBody.equals("")) {
//                    Map<String, String> map = new HashMap<>();
//                    map.put("message", messageBody);
//                    map.put("user", fullName);
//                    reference.push().setValue(map);
//                }


                FirebaseDatabase.getInstance().getReference().child("Messages").child(loginUser.getUserId()).push().setValue(
                        new Chat(emojiconEditText.getText().toString(), fullName
                ));
                emojiconEditText.setText("");
                emojiconEditText.requestFocus();
            }
        });
//        reference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
//                String messageBody = map.get("message").toString();
//                String userSent = map.get("user").toString();
//                String loginUserFullName = loginUser.getFirstName() + " " + loginUser.getLastName();
//
//                if(userSent.equals(loginUserFullName)) {
//                    displayChatMessage("You: \n" + messageBody, 1);
//                }
//                else {
//                    String matchedFoodieName = matchedFoodie.getFirstName() + " " + matchedFoodie.getLastName();
//                    displayChatMessage(matchedFoodieName + "\n" + messageBody, 2 );
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        displayChatMessage();
    }

    private void displayChatMessage() {
        ListView listofMessage = findViewById(R.id.list_of_message);
        Query query = FirebaseDatabase.getInstance().getReference().child("Chats");
        // https://stackoverflow.com/questions/47690974/firebaselistadapter-not-working
        FirebaseListOptions<Chat> options = new FirebaseListOptions.Builder<Chat>()
                .setQuery(query, Chat.class)
                .setLayout(item_chat)
                .build();

        adapter = new FirebaseListAdapter<Chat>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Chat model, int position) {
                TextView messageText, messageUser, messageTime;
                messageText = (BubbleTextView) v.findViewById(R.id.message_text);
                messageUser = v.findViewById(R.id.message_user);
                messageTime = v.findViewById(R.id.message_time);

//                if (type == 1) {
//                    messageText.setBackgroundResource(R.drawable.chat_bubble_blue);
//                } else {
//                    messageText.setBackgroundResource(R.drawable.chat_bubble_green);
//                }
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
            }
        };
        listofMessage.setAdapter(adapter);
    }
}
