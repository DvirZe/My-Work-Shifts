package com.example.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.database.DatabaseConnection;
import com.example.database.Register;
import com.example.database.Shift;
import com.example.classes.ShiftHours;
import com.example.classes.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;

public class ShiftsActivity extends AppCompatActivity {

    private static DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    private DatabaseReference myRef;
    private LocalDate selectedDate;
    private TextView tvStart, tvEnd,tvWorkHours, twWorkIncome;
    private LinearLayout workInfo, welcomeNote;
    private FloatingActionButton fab;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseReference myRefToUser = DatabaseConnection
                                        .getDatabase()
                                        .getReference()
                                        .child(DatabaseConnection.getCurrentUser().getUid())
                                        .child("general");

        //  Get user info
        myRefToUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = new User(dataSnapshot.getValue(Register.class));
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

        //  Update screen when date changes
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = LocalDate.of(year,month+1,dayOfMonth);
                myRef = databaseConnection.getDatabase().getReference()
                        .child(databaseConnection.getCurrentUser().getUid())
                        .child("workHours")
                        .child(String.valueOf(year))
                        .child(String.valueOf(month+1))
                        .child(String.valueOf(dayOfMonth));

                fab.setVisibility(View.VISIBLE);
                welcomeNote.setVisibility(View.GONE);

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Shift data = dataSnapshot.getValue(Shift.class);
                        if (data == null) {
                            handleNonExistsShift(null);
                        } else {
                            ShiftHours shift = new ShiftHours(data);
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

        //  Open edit info activity
        if (id == R.id.miEdit) {
            Intent intent = new Intent(this, EditInfoActivity.class);
            intent.putExtra("USER_INFO", user);
            startActivity(intent);
            return true;
        }

        if (id == R.id.miExit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    //Cancel the option to click back in this Activity
    public void onBackPressed() { }

    /*  This method change the work hours section
        and update the icon */
    private void handleNonExistsShift(View view) {

        //  !Null = came after deleting shift
        if (view != null) {
            //  Fade animation
            Animation animation = new AlphaAnimation(1, 0);
            animation.setStartOffset(0);
            animation.setDuration(1000);
            workInfo.startAnimation(animation);
            final Handler handler = new Handler();
            //  Set delay to make the fade effect noticeable
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    workInfo.setVisibility(View.INVISIBLE);
                    fab.setImageResource(R.drawable.ic_pencil);
                }
            }, 800);
        } else {
            workInfo.setVisibility(View.INVISIBLE);
            fab.setImageResource(R.drawable.ic_pencil);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShift(v);
            }
        });
    }

    /*  This method change the work hours section
        and update the icon */
    private void handelExistsShift(ShiftHours shift) {
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
        tvWorkHours.setText(getString(R.string.total_work_hours, workTime.toString()));
        updateEarned(workTime);
    }

    //  After loading new shift calculate and make visible the shift wage
    private void updateEarned(LocalTime workTime) {
        DecimalFormat df = new DecimalFormat("0.00");
        Double earn = (Double.parseDouble(user.getWage()) * workTime.getHour()) + (Double.parseDouble(user.getWage()) * ((workTime.getMinute()*1.0)/60));
        twWorkIncome.setText(getString(R.string.total_earn_for_shift ,df.format(earn)));
    }

    //  Calculate the length of the shift
    private LocalTime calcWorkTime(ShiftHours shift) {
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
        handleNonExistsShift(view);
    }

}
