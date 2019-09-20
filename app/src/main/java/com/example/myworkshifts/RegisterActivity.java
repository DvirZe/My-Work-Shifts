package com.example.myworkshifts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.classes.Register;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private EditText firstName;
    private EditText lastName;
    private EditText companyName;
    private EditText hourlyWage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        firstName   = findViewById(R.id.etFirstName);
        lastName    = findViewById(R.id.etLastName);
        companyName = findViewById(R.id.etCompany);
        hourlyWage  = findViewById(R.id.etHourlyWage);
    }

    public void finish(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child(currentUser.getUid()).child("general");

        myRef.setValue(new Register(currentUser.getEmail(),
                                    firstName.getText().toString(),
                                    lastName.getText().toString(),
                                    companyName.getText().toString(),
                                    hourlyWage.getText().toString()));

        Toast.makeText(view.getContext(), "Registration complete.",
                Toast.LENGTH_SHORT).show();

        finish();

        Intent intent = new Intent(view.getContext(), ShiftsActivity.class);
        startActivity(intent);

    }
}
