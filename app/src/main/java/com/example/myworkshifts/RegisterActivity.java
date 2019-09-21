package com.example.myworkshifts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
    private EditText email;
    private EditText password;

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
        email = findViewById(R.id.etrEmail);
        password = findViewById(R.id.etrPassword);

    }

    @Override
    public void onBackPressed() { }

    public void finish(final View view) {

        if (firstName.getText().toString().equals("") ||
            lastName.getText().toString().equals("") ||
            companyName.getText().toString().equals("") ||
            hourlyWage.getText().toString().equals("") ||
            email.getText().toString().equals("") ||
            password.getText().toString().equals(""))
        {
            Toast.makeText(view.getContext(), "You have to complete the form first.",
                    Toast.LENGTH_SHORT).show();
        } else {

            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                                startActivity(intent);
                                ifUserRegisterOf(view);
                            } else {
                                Toast.makeText(view.getContext(), "Error in mail or password.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private void ifUserRegisterOf(View view){

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
