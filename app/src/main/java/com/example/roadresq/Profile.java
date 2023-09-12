package com.example.roadresq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    RecyclerView recyclerView;
    String userName;
    String phoneNumberToFilter;
    ArrayList<Complaint> arrayList = new ArrayList<>(); // Initialize the list
    CustomAdapter customAdapter = new CustomAdapter(this, arrayList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        MaterialButton logout=findViewById(R.id.logoutbutton);
        recyclerView=findViewById(R.id.recycler);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(Profile.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {"name", "phoneNumber"};

        // Define any WHERE clause if needed (e.g., to filter by phoneNumber)
        String selection = null; //
        String[] selectionArgs = null; //

        Cursor cursor = db.query("user_data", columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndex("name"));
            phoneNumberToFilter = cursor.getString(cursor.getColumnIndex("phoneNumber"));

            // Use the retrieved data as needed
            cursor.close();
        }

        // Close the database
        db.close();



        TextView nametxt=findViewById(R.id.nametxt);
        nametxt.setText(userName);




        DatabaseReference complaintsReference = FirebaseDatabase.getInstance().getReference().child("complaints");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customAdapter);

        complaintsReference.orderByChild("phoneNumber").equalTo(phoneNumberToFilter).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot complaintSnapshot : dataSnapshot.getChildren()) {
                    // Create a Complaint object from the snapshot and add it to the ArrayList
                    Complaint complaint = complaintSnapshot.getValue(Complaint.class);
                    arrayList.add(complaint);
                }




                // Recyclerview you can notify it of the data change:
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the database query
                Toast.makeText(Profile.this, "Error fetching complaints: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });














        customAdapter.setOnItemClickListener(new CustomAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {

                //Toast.makeText(MainActivity.this, arraylist.get(position).getCurrecnyName(), Toast.LENGTH_SHORT).show();

            }
        });




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();

                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish(); // Optional: Finish the main activity to prevent the user from going back.


            }
        });


    }
}