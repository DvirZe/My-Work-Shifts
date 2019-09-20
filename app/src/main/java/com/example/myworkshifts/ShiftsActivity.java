package com.example.myworkshifts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.classes.Register;
import com.example.classes.Shift;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;

public class ShiftsActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private DatabaseReference myRef;
    private LocalDate selectedDate;
    private FirebaseDatabase database;
    private TextView tvStart, tvEnd,tvWorkHours, twWorkIncome;
    private LinearLayout workInfo, welcomeNote;
    private FloatingActionButton fab;
    private Register user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();

        DatabaseReference myRefToUser = database.getReference().child(currentUser.getUid()).child("general");

        myRefToUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(Register.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        selectedDate = LocalDate.now();

        tvStart = findViewById(R.id.tvStartHour);
        tvEnd = findViewById(R.id.tvEndHour);
        workInfo = findViewById(R.id.work_info);
        welcomeNote = findViewById(R.id.welcome_note);
        fab = findViewById(R.id.fabAddShift);
        tvWorkHours = findViewById(R.id.twWorkHours);
        twWorkIncome = findViewById(R.id.twWorkIncome);

        CalendarView cv = findViewById(R.id.calendarView);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = LocalDate.of(year,month+1,dayOfMonth);
                myRef = database.getReference().child(currentUser.getUid())
                                                .child("workHours")
                                                .child(String.valueOf(year))
                                                .child(String.valueOf(month+1))
                                                .child(String.valueOf(dayOfMonth));

                fab.setVisibility(View.VISIBLE);
                welcomeNote.setVisibility(View.GONE);

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Shift shift = dataSnapshot.getValue(Shift.class);
                        if (shift == null) {
                            handleNonExistsShift();
                        } else {
                            handelExistsShift(shift);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.miEdit) {
            Intent intent = new Intent(this, EditInfoActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.miExit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void handleNonExistsShift() {
        workInfo.setVisibility(View.INVISIBLE);
        fab.setImageResource(R.drawable.ic_pencil);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShift(v);
            }
        });
    }

    private void handelExistsShift(Shift shift) {
        tvStart.setText(shift.getStart());
        tvEnd.setText(shift.getEnd());
        workInfo.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.ic_eraser);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeShift(v);
            }
        });
        LocalTime workTime = calcWorkTime(shift);
        tvWorkHours.setText("Total work time on this day: " + workTime.toString());
        updateEarned(workTime);
    }

    private void updateEarned(LocalTime workTime) {
        DecimalFormat df = new DecimalFormat("0.00");
        Double earn = (Double.parseDouble(user.wage) * workTime.getHour()) + (Double.parseDouble(user.wage) * ((workTime.getMinute()*1.0)/60));
        twWorkIncome.setText("You earned " + df.format(earn) + " this shift.");
    }

    private LocalTime calcWorkTime(Shift shift) {
        LocalTime start = LocalTime.parse(shift.getStart());
        LocalTime end = LocalTime.parse(shift.getEnd());
        int minutes = end.getMinute() - start.getMinute();
        int hours = end.getHour()-start.getHour();
        if (minutes < 0) {
            minutes = minutes + 60;
            hours = hours - 1;
        }
        return LocalTime.of(hours,minutes);
    }

    public void addShift(View view) {

        Intent intent = new Intent(view.getContext(), AddShiftActivity.class);
        intent.putExtra("NEW_SHIFT_DATE", selectedDate.toString());
        startActivity(intent);
    }

    private void removeShift(View view) {
        myRef.removeValue();
        handleNonExistsShift();
    }

}
