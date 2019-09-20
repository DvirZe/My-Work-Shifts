package com.example.myworkshifts;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.time.format.DateTimeFormatter;
import com.example.classes.Shift;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddShiftActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TextView addShiftTitle;
    private Shift shift;
    private LocalDate selectedDate;
    private EditText etStart, etEnd;

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

        //shift.start = LocalTime.of(0,0);
        //shift.end =  LocalTime.of(0,0);

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
}