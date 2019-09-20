package com.example.myworkshifts;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.classes.Shift;
import com.example.listeners.OnFocusChangeAddShift;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;

public class AddShiftActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private Shift shift;
    private LocalDate selectedDate;
    private EditText etStart, etEnd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shift);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        TextView addShiftTitle = findViewById(R.id.addShiftTitle);

        shift = new Shift();

        selectedDate = LocalDate.parse(getIntent().getExtras().getString("NEW_SHIFT_DATE"));

        addShiftTitle.setText("Please enter the work hours for " + selectedDate.getDayOfWeek() + " the " + selectedDate.getDayOfMonth() + " in " + selectedDate.getMonth() + " ," + selectedDate.getYear());

        etStart = findViewById(R.id.etStart);
        etEnd = findViewById(R.id.etEnd);

        etStart.setOnFocusChangeListener(new OnFocusChangeAddShift(etStart).listener());
        etEnd.setOnFocusChangeListener(new OnFocusChangeAddShift(etEnd).listener());


    }

    public void saveShift(View view) {
        if (etStart.getText().toString().equals("") || etEnd.getText().toString().equals(""))
        {
            Toast.makeText(view.getContext(), "Fill te fields correctly before saving",Toast.LENGTH_SHORT).show();
        }else {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child(currentUser.getUid())
                    .child("workHours")
                    .child(String.valueOf(selectedDate.getYear()))
                    .child(String.valueOf(selectedDate.getMonthValue()))
                    .child(String.valueOf(selectedDate.getDayOfMonth()));

            shift.start = etStart.getText().toString();
            shift.end = etEnd.getText().toString();

            myRef.setValue(shift);
            Toast.makeText(view.getContext(), "Shift saved",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void cancel(View view) {
        finish();
        Toast.makeText(view.getContext(), "Shift not saved",
                Toast.LENGTH_SHORT).show();
    }
}
