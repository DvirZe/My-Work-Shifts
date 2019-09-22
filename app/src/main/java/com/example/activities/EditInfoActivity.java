package com.example.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.database.DatabaseConnection;
import com.example.database.Register;
import com.example.classes.User;
import com.google.firebase.database.DatabaseReference;

import es.dmoral.toasty.Toasty;

public class EditInfoActivity extends AppCompatActivity {

    private EditText esFName, esLName, esCompany, esWage;
    private User user;
    private DatabaseReference myRefToUser;
    private static DatabaseConnection databaseConnection = DatabaseConnection.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);

        esFName = findViewById(R.id.esFName);
        esLName = findViewById(R.id.esLNAME);
        esCompany = findViewById(R.id.esCompany);
        esWage = findViewById(R.id.esWage);

        user = (User) getIntent().getSerializableExtra("USER_INFO");

        esFName.setText(user.getFirstName());
        esLName.setText(user.getLastName());
        esCompany.setText(user.getCompany());
        esWage.setText(user.getWage());

        myRefToUser = databaseConnection.getDatabase().getReference()
                        .child(databaseConnection.getCurrentUser().getUid())
                        .child("general");
    }

    @Override
    //Cancel the option to click back in this Activity
    public void onBackPressed() { }

    public void cancel(View view) {
        finish();
        Toasty.info(view.getContext(), R.string.edit_not_saved, Toast.LENGTH_SHORT).show();
    }

    //  Update info in the Database before finish the activity
    public void saveEdit(View view) {
        user.setCompany(esCompany.getText().toString());
        user.setWage(esWage.getText().toString());
        myRefToUser.setValue(new Register(user));
        finish();
        Toasty.success(view.getContext(), R.string.edit_saved, Toast.LENGTH_SHORT).show();
    }
}
