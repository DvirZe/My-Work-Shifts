package com.example.myworkshifts;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.listeners.OnFocusChangeAddShift;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.classes.Shift;

public class AddShift extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TextView addShiftTitle;
    private Shift shift;
    private LocalDate selectedDate;
    private EditText etStart, etEndDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shift);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        addShiftTitle = findViewById(R.id.addShiftTitle);

        addShiftTitle.setText("Please enter the work hours for ");

        shift = new Shift();

        selectedDate = LocalDate.parse(getIntent().getExtras().getString("NEW_SHIFT_DATE"));

        addShiftTitle.setText("Please enter the work hours for " + selectedDate.getDayOfWeek() + " the " + selectedDate.getDayOfMonth() + " in " + selectedDate.getMonth() + " ," + selectedDate.getYear());

        shift.start = LocalTime.of(0,0);
        shift.end =  LocalTime.of(0,0);

        etStart = findViewById(R.id.etStart);
        etEndDate = findViewById(R.id.etEnd);

        etStart.setOnFocusChangeListener(new OnFocusChangeAddShift(etStart, shift.start).listener());
        etEndDate.setOnFocusChangeListener(new OnFocusChangeAddShift(etEndDate, shift.end).listener());


    }

    public void saveShift(View view) {
        Toast.makeText(view.getContext(), selectedDate.toString(),
                Toast.LENGTH_SHORT).show();
    }
}
