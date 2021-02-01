package com.example.triptogether;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText descriptionEdition;
    private Button submitEdition;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    public TextView profileDesc;
    public String profileDescContent;

    Firebase reference1;
    ImageView mImageView;
    Button mChooseBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference myRef = db.getReference("users").child(UserDetails.username).child("description");

        profileDesc = findViewById(R.id.profileDescription);
        profileDescContent = profileDesc.getText().toString();
        profileDescContent =  db.getReference("users").child(UserDetails.username).child("description").toString();
        profileDesc.setText(profileDescContent);


        Firebase.setAndroidContext(this);
        reference1 = new Firebase(profileDescContent);

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String descp = dataSnapshot.getValue(String.class);

                profileDesc.setText(descp);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });

        mImageView = findViewById(R.id.imageView);
        mChooseBtn = findViewById(R.id.imageButton);

        mChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permission, PERMISSION_CODE);
                    }
                    else {
                        pickImageFromGallery();

                        }
                    }
                else {
                    pickImageFromGallery();
                }
                }
        });
    }

    private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }


    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                }
            }
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            mImageView.setImageURI(data.getData());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item2:
                Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, Login.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        if (v.getId() == R.id.editDescriptionButton) {
            editDescriptionDialog();
        }
    }



    public void editDescriptionDialog() { //for creating new dialog
        dialogBuilder = new AlertDialog.Builder(this);
        final View editDescriptionPopupView = getLayoutInflater().inflate(R.layout.editdescriptionpopup, null);
        descriptionEdition = editDescriptionPopupView.findViewById(R.id.editDescriptionText);

        submitEdition = (Button) editDescriptionPopupView.findViewById(R.id.submitEditButton);

        dialogBuilder.setView(editDescriptionPopupView);
        dialog = dialogBuilder.create();
        dialog.show();


        submitEdition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String edit_text_string = descriptionEdition.getText().toString();


                FirebaseDatabase db = FirebaseDatabase.getInstance();

                DatabaseReference myRef = db.getReference("users").child(UserDetails.username).child("description");


                //profileDescContent = edit_text_string;
                myRef.setValue((edit_text_string));


                profileDesc.setText(edit_text_string);

                dialog.dismiss();
            }
        });

    }





}