package com.example.roadresq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import com.google.firebase.database.ValueEventListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

        public class Form extends AppCompatActivity {


            String userName,userPhoneNumber;


            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_form);
                FirebaseApp.initializeApp(this);

                EditText subjecttxt = findViewById(R.id.subtext);
                EditText bodytxt = findViewById(R.id.complainttext);
                MaterialButton button = findViewById(R.id.submitbutton);

                Intent intent = getIntent();
                String category = intent.getStringExtra("CATEGORY");

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String subject = subjecttxt.getText().toString();
                        String body = bodytxt.getText().toString();

                        if (!(subject.isEmpty()) && !(body.isEmpty())) {

                            // Get the currently logged-in user's UID
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            // Get a reference to your Firebase Realtime Database
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


                            // Create a child reference for the new complaint under the "Complaints" node
                            DatabaseReference complaintsReference = databaseReference.child("complaints");

                            // Generate a unique key for the new complaint
                            String complaintId = complaintsReference.push().getKey();
                            DatabaseReference userReference = databaseReference.child("users").child(uid);


                            MyDatabaseHelper dbHelper = new MyDatabaseHelper(Form.this);
                            SQLiteDatabase db = dbHelper.getReadableDatabase();

                            // Define the columns you want to retrieve
                            String[] columns = {"name", "phoneNumber"};

                            // Define any WHERE clause if needed (e.g., to filter by phoneNumber)
                            String selection = null; // You can set this to "phoneNumber = ?" if needed
                            String[] selectionArgs = null; // You can set this to an array of arguments if needed

                            Cursor cursor = db.query("user_data", columns, selection, selectionArgs, null, null, null);

                            if (cursor != null && cursor.moveToFirst()) {
                                userName = cursor.getString(cursor.getColumnIndex("name"));
                                userPhoneNumber = cursor.getString(cursor.getColumnIndex("phoneNumber"));

                                // Use the retrieved data as needed
                                cursor.close();
                            }

                            // Close the database
                            db.close();







                            // Get the current date and time
                            Calendar calendar = Calendar.getInstance();
                            Date currentDate = calendar.getTime();

                            // Define the date and time format
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                            // Format the current date and time
                            String formattedDate = dateFormat.format(currentDate);
                            String formattedTime = timeFormat.format(currentDate);

                            // Set the data for the new complaint
                            DatabaseReference newComplaintReference = complaintsReference.child(complaintId);
                            newComplaintReference.child("phoneNumber").setValue(userPhoneNumber);
                            newComplaintReference.child("name").setValue(userName);
                            newComplaintReference.child("date").setValue(formattedDate);
                            newComplaintReference.child("time").setValue(formattedTime);
                            newComplaintReference.child("category").setValue(category);
                            newComplaintReference.child("subject").setValue(subject);
                            newComplaintReference.child("body").setValue(body);
                            newComplaintReference.child("status").setValue("Under Review");



                            button.setBackgroundColor(Color.parseColor("#82DE7C"));
                            button.setText("Submitted");
                            Drawable iconDrawable = ResourcesCompat.getDrawable(getResources(),R.drawable.round_done_24,null);
                            button.setIcon(iconDrawable);
                            button.setEnabled(false);




                        } else {
                            Toast.makeText(com.example.roadresq.Form.this, "Fill all fields", Toast.LENGTH_SHORT).show();

                        }
                    }
                    });




                }





        }


