package com.example.myworkshifts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;

public class ShiftsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference myRef;
    private LocalDate selectedDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        selectedDate = LocalDate.now();

        CalendarView cv = findViewById(R.id.calendarView);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = LocalDate.of(year,month+1,dayOfMonth);
            }
        });

    }

    public void addShift(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(currentUser.getUid()).child("shifts");
        Intent intent = new Intent(view.getContext(), AddShift.class);
        intent.putExtra("NEW_SHIFT_DATE", selectedDate.toString());
        startActivity(intent);
        Toast.makeText(view.getContext(), "Error in mail or password.",
                Toast.LENGTH_SHORT).show();
    }
}
