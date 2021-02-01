package com.example.triptogether;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class Chat extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    TextView descriptionText;
    Button HideDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = findViewById(R.id.layout1);
        layout_2 = findViewById(R.id.layout2);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://androidchattapp-8f815-default-rtdb.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://androidchattapp-8f815-default-rtdb.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                    messageArea.setTextSize(22);
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                //System.out.println("kkkkkk");

                if(userName.equals(UserDetails.username)){
                    addMessageBox(message, 1);
                    //System.out.println("in message1 box:" + message);
                }
                else{
                    addMessageBox(message, 2);
                    //System.out.println("in message2 box:" + message);
                    makeNot(message, userName);
                    //System.out.println("makeNot method");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Button buttonShowNotification = findViewById(R.id.show);
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.icon3)
                .setContentTitle("New message")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        buttonShowNotification.setOnClickListener(v-> {
            System.out.println("sdwdadas");
            notificationManager.notify(100, builder.build());


            dialogBuilder = new AlertDialog.Builder(this);
            final View userDescrip= getLayoutInflater().inflate(R.layout.othersprofiledescriptionpopup, null);

            descriptionText =  userDescrip.findViewById(R.id.othersDescriptionText);

            dialogBuilder.setView(userDescrip);
            dialog = dialogBuilder.create();
            dialog.show();

            HideDescription = (Button) userDescrip.findViewById(R.id.RemoveDescriptionButton);

            FirebaseDatabase db = FirebaseDatabase.getInstance();

            DatabaseReference myRef = db.getReference("users").child(UserDetails.chatWith).child("description");
            String profileDescContent;

            profileDescContent = descriptionText.getText().toString();
            profileDescContent =  db.getReference("users").child(UserDetails.chatWith).child("description").toString();
            descriptionText.setText(profileDescContent);


            Firebase.setAndroidContext(this);
            reference1 = new Firebase(profileDescContent);

            reference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String descp = dataSnapshot.getValue(String.class);

                    descriptionText.setText(descp);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) { }
            });




            HideDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // define cancelbutton here


                    dialog.dismiss();
                }
            });
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description ="channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void makeNot (String message_m, String name_m){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.icon3)
                .setContentTitle(name_m)
                .setContentText("In text")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message_m))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(100, builder.build());
    }


    public void addMessageBox(String message, int type){
        TextView textView = new TextView(Chat.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 7.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}