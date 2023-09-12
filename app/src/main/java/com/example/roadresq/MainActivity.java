package com.example.roadresq;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    MaterialButton login;
    private MyDatabaseHelper dbHelper;

    int click=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        dbHelper = new MyDatabaseHelper(this); // Initialize dbHelper in onCreate



        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already authenticated, navigate to the homepage.
            Intent intent = new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);
            finish(); // Optional: Finish the main activity to prevent the user from going back.
        }




        login=findViewById(R.id.loginbutton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                click++;


                EditText nameText=findViewById(R.id.inputtext1);
                EditText editTextPhoneNumber=findViewById(R.id.inputtext2);
                String phoneNumberInput = editTextPhoneNumber.getText().toString();
                String formattedPhoneNumber = formatPhoneNumber(phoneNumberInput);
                String name = nameText.getText().toString();




                if (!(formattedPhoneNumber.isEmpty()))
                {


                    Intent intent=new Intent(getApplicationContext(), Verification.class);
                    intent.putExtra("PHONE",formattedPhoneNumber);
                    intent.putExtra("NAME",name);
                    // After successful login
                    ContentValues values = new ContentValues();
                    values.put("phoneNumber", formattedPhoneNumber);
                    values.put("name", name);

                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    int rowsUpdated = db.update("user_data", values, null, null);

                    if (rowsUpdated == 0) {
                        // No rows were updated; insert a new row
                        db.insert("user_data", null, values);
                    }

                    db.close();



                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(MainActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }




            }
        });

























    }


    public static String formatPhoneNumber(String phoneNumber) {
        // Remove all non-digit characters from the input
        String digitsOnly = phoneNumber.replaceAll("[^\\d]", "");

        // Check if the number starts with "0" (typically used for local numbers in India)
        if (digitsOnly.startsWith("0")) {
            // Remove the leading "0" and add "+91"
            return "+91" + digitsOnly.substring(1);
        }

        // Check if the number already starts with "+91"
        if (digitsOnly.startsWith("+91")) {
            // Number is already correctly formatted
            return digitsOnly;
        }

        // If none of the above conditions match, simply add "+91" to the number
        return "+91" + digitsOnly;
    }

}