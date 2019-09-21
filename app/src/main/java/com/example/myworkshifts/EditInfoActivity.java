package com.example.myworkshifts;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.classes.Register;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditInfoActivity extends AppCompatActivity {

    private EditText esFName, esLName, esCompany, esWage;
    private Register user;
    private DatabaseReference myRefToUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);

        esFName = findViewById(R.id.esFName);
        esLName = findViewById(R.id.esLNAME);
        esCompany = findViewById(R.id.esCompany);
        esWage = findViewById(R.id.esWage);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        myRefToUser = database.getReference().child(currentUser.getUid()).child("general");

        myRefToUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(Register.class);
                esFName.setText(user.firstName);
                esLName.setText(user.lastName);
                esCompany.setText(user.company);
                esWage.setText(user.wage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() { }

    public void cancel(View view) {
        finish();
        Toast.makeText(view.getContext(), "Edit not saved",
                Toast.LENGTH_SHORT).show();
    }

    public void saveEdit(View view) {
        user.company = esCompany.getText().toString();
        user.wage = esWage.getText().toString();
        myRefToUser.setValue(user);
        finish();
        Toast.makeText(view.getContext(), "Edit saved",
                Toast.LENGTH_SHORT).show();
    }
}
