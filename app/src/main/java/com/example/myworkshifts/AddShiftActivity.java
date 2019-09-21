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

import es.dmoral.toasty.Toasty;

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

        //  Add listener to the hours fields
        etStart.setOnFocusChangeListener(new OnFocusChangeAddShift(etStart).listener());
        etEnd.setOnFocusChangeListener(new OnFocusChangeAddShift(etEnd).listener());

    }

    @Override
    //Cancel the option to click back in this Activity
    public void onBackPressed() { }

    public void saveShift(View view) {
        if (etStart.getText().toString().equals("") || etEnd.getText().toString().equals(""))
        {
            Toasty.info(view.getContext(), R.string.empty_fields_add_shift,Toast.LENGTH_SHORT).show();
        }else {

            //  requestFocus to trig the 'OnFocusChange' listener
            etStart.requestFocus();
            etEnd.requestFocus();

            shift.start = etStart.getText().toString();
            shift.end = etEnd.getText().toString();

            //  Make sure that the end hour is later then the start hour
            if (shift.compereHours() == 0) {
                Toasty.error(view.getContext(), getString(R.string.same_hour_error),
                        Toast.LENGTH_LONG).show();
            } else if (shift.compereHours() == 1) {
                Toasty.error(view.getContext(), getString(R.string.hour_error),
                        Toast.LENGTH_LONG).show();
            } else {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child(currentUser.getUid())
                        .child("workHours")
                        .child(String.valueOf(selectedDate.getYear()))
                        .child(String.valueOf(selectedDate.getMonthValue()))
                        .child(String.valueOf(selectedDate.getDayOfMonth()));
                myRef.setValue(shift);
                Toasty.success(view.getContext(), R.string.shift_saved, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    //  Close this activity when click cancel
    public void cancel(View view) {
        finish();
        Toasty.info(view.getContext(), R.string.shift_not_saved, Toast.LENGTH_SHORT).show();
    }
}
