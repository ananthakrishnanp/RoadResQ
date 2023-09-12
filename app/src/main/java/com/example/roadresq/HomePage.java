package com.example.roadresq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity {
    private String name,formattedPhoneNumber;
    ImageButton accident,rules,road,other,profile;
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Intent intent=getIntent();
        name=intent.getStringExtra("NAME");
        formattedPhoneNumber=intent.getStringExtra("PHONE");
        message=findViewById(R.id.textmessage);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
           // Toast.makeText(this, "Auth", Toast.LENGTH_SHORT).show();
            // Check if user data is already written to the database
            checkUserDataInDatabase(currentUser.getUid());
        } else {

            Toast.makeText(this, "Authentication Error", Toast.LENGTH_SHORT).show();
        }








        accident=findViewById(R.id.accidentbt);
        rules=findViewById(R.id.rulesbt);
        road=findViewById(R.id.roadbt);
        other=findViewById(R.id.otherbt);


        setonclick(accident);
        setonclick(rules);
        setonclick(road);
        setonclick(other);

        profile=findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });














    }

    private void setonclick(ImageButton button) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Form.class);
                if(button.getId()==R.id.accidentbt)
                {
                    intent.putExtra("CATEGORY","accident");
                } else if (button.getId()==R.id.rulesbt) {
                    intent.putExtra("CATEGORY","rules");
                }
                else if (button.getId()==R.id.roadbt) {
                    intent.putExtra("CATEGORY","road");
                }
                else if (button.getId()==R.id.otherbt) {
                    intent.putExtra("CATEGORY","other");
                }
                startActivity(intent);
            }
        });
    }


    // Check if user data is already written to the database
    private void checkUserDataInDatabase(String uid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userReference = databaseReference.child("users").child(uid);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User data exists in the database
                    String namefromdb = dataSnapshot.child("name").getValue(String.class);
                    if (namefromdb != null) {
                        String[] nameArray2 = namefromdb.split(" ");
                        message.setText("Hi, " + nameArray2[0]);
                    }
                } else {
                    // User data does not exist in the database, so write it
                    writeUserDataToDatabase(uid);
                    if (name != null) {
                        String[] nameArray = name.split(" ");
                        message.setText("Hi, " + nameArray[0]);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur
                Log.e("DatabaseError", "Error checking user data: " + databaseError.getMessage());
            }
        });
    }



    // Write user data to the database
    private void writeUserDataToDatabase(String uid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userReference = databaseReference.child("users").child(uid);

        userReference.child("phoneNumber").setValue(formattedPhoneNumber);
        userReference.child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Data was successfully written

                } else {
                    // Handle the error

                    Toast.makeText(HomePage.this, "Error submitting complaint", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();

    }
}

