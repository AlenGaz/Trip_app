package com.example.triptogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class Search extends AppCompatActivity {


    RecyclerView recview;
    myadapter adapter;
    private AlertDialog.Builder dialogBuilder, dialogUserTripBuilder;
    private AlertDialog dialog;
    private EditText fromCity, toCity, date, time, confirmUser;
    private Button addTrip, cancel,deleteTrips, submitUsername;
    public String gotUser;

    ArrayList<String> alist = new ArrayList<>();
    int totaleUsers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("");

        recview= (RecyclerView) findViewById(R.id.recview);




        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users").orderByChild("name").startAt("from "), model.class)
                        .build();

        adapter=new myadapter(options);
        recview.setAdapter(adapter);

        recview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("KOSKESHKURWA", String.valueOf(recview.getChildItemId(v)));
                recview.getChildItemId(v);
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.searchmenu,menu);

        MenuItem item=menu.findItem(R.id.search);

        SearchView searchView=(SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    private void processsearch(String s)
    {
        FirebaseRecyclerOptions<model> options =
               new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users").orderByChild("trips").startAt("").endAt(s+"\uf8ff"), model.class)
                        .build();

        adapter=new myadapter(options);
        adapter.startListening();
        recview.setAdapter(adapter);

    }


    public void onClick(View v) {
        if (v.getId() == R.id.chatButton) {
            Intent i = new Intent(this, Users.class);
            startActivity(i);
        }
        if (v.getId() == R.id.profileButton) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        if (v.getId() == R.id.searchButton) {
            Intent i = new Intent(this, Search.class);
            startActivity(i);
        }
        if (v.getId() == R.id.addButton){
            createNewTripDialog();
        }
       /* if (v.getId() == R.id.button5){

            //tripUserPopupDialog();
            //recview.getChildItemId(recview);
            Log.d("KOSKESHKURWA", String.valueOf(recview.getChildItemId(v)));

        }*/
    }



    public void tripUserPopupDialog() { //for creating new dialog

        dialogBuilder = new AlertDialog.Builder(this);
        final View userTripPopupView = getLayoutInflater().inflate(R.layout.confirmuserpopup, null);

        confirmUser = (EditText) userTripPopupView.findViewById(R.id.editConfirmUser);

        dialogBuilder.setView(userTripPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        submitUsername = (Button) userTripPopupView.findViewById(R.id.confirmUserButton);

        submitUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // define cancelbutton here



                CallChatActivity(confirmUser.getText().toString());
                dialog.dismiss();
            }
        });



    }

    public void CallChatActivity(String uname){
        Intent i = new Intent(this, Chat.class);
        UserDetails.chatWith = uname;
        startActivity(i);

    }


    public void createNewTripDialog() { //for creating new dialog
        dialogBuilder = new AlertDialog.Builder(this);
        final View tripPopupView = getLayoutInflater().inflate(R.layout.popup, null);

        toCity = (EditText) tripPopupView.findViewById(R.id.toCity);
        toCity = (EditText) tripPopupView.findViewById(R.id.toCity);
        fromCity = (EditText) tripPopupView.findViewById(R.id.fromCity);
        date = (EditText) tripPopupView.findViewById(R.id.date);
        time = (EditText) tripPopupView.findViewById(R.id.time);

        addTrip = (Button) tripPopupView.findViewById(R.id.addTripButton);
        cancel = (Button) tripPopupView.findViewById(R.id.cancelButton);
        deleteTrips = (Button) tripPopupView.findViewById(R.id.deleteTripsButton);


        dialogBuilder.setView(tripPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        Login loginref = new Login();

        DatabaseReference myRef = db.getReference("users").child(UserDetails.username).child("trips");


        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //define savebutton here

                String fromto = "from " + toCity.getText().toString() + " to " + fromCity.getText().toString();
                String dateTrip = date.getText().toString() ;
                String timeTrip = time.getText().toString();

                String all_ofthe_trip = fromto + dateTrip + timeTrip;
                DatabaseReference myRef0 = db.getReference("users").child(UserDetails.username).child("trips");
                myRef.setValue(all_ofthe_trip);

                DatabaseReference myRef = db.getReference("users").child(UserDetails.username).child("name");
                myRef.setValue(fromto);

                DatabaseReference myRef1 = db.getReference("users").child(UserDetails.username).child("course");
                myRef1.setValue(dateTrip + ", at time: " + timeTrip);

                DatabaseReference myRef2 = db.getReference("users").child(UserDetails.username).child("email");
                myRef2.setValue(UserDetails.username);

                dialog.dismiss();
            }


        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // define cancelbutton here
                dialog.dismiss();
            }
        });


        deleteTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // define cancelbutton here

                DatabaseReference myRef0 = db.getReference("users").child(UserDetails.username).child("trips");
                myRef.setValue("");

                DatabaseReference myRef = db.getReference("users").child(UserDetails.username).child("name");
                myRef.setValue("");

                DatabaseReference myRef1 = db.getReference("users").child(UserDetails.username).child("course");
                myRef1.setValue("");

                DatabaseReference myRef2 = db.getReference("users").child(UserDetails.username).child("email");
                myRef2.setValue("");
                dialog.dismiss();
            }
        });
    }




}