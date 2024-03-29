package com.example.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.database.DatabaseConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;

import com.example.database.Register;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {

    private static DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

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

        databaseConnection.initUser();

        firstName   = findViewById(R.id.etFirstName);
        lastName    = findViewById(R.id.etLastName);
        companyName = findViewById(R.id.etCompany);
        hourlyWage  = findViewById(R.id.etHourlyWage);
        email = findViewById(R.id.etrEmail);
        password = findViewById(R.id.etrPassword);

    }

    @Override
    //Cancel the option to click back in this Activity
    public void onBackPressed() { }

    /*  This method will save the log in email and password
        when user finish the registration.  */
    public void finish(final View view) {

        if (!checkFields()) {
            Toasty.info(view.getContext(), R.string.form_incomplete, Toast.LENGTH_SHORT).show();
        } else {

            databaseConnection.getmAuth().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                                startActivity(intent);
                                saveUserInfo(view);
                            } else {
                                Toasty.error(view.getContext(), R.string.email_password_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private void saveUserInfo(View view){

        DatabaseReference myRef = DatabaseConnection.getDatabase().getReference().child(DatabaseConnection.getCurrentUser().getUid()).child("general");

        //  Save user info to the Database
        myRef.setValue(new Register(DatabaseConnection.getCurrentUser().getEmail(),
                firstName.getText().toString().trim(),
                lastName.getText().toString().trim(),
                companyName.getText().toString().trim(),
                hourlyWage.getText().toString().trim()));

        Toasty.success(view.getContext(), R.string.reg_complete, Toast.LENGTH_SHORT).show();

        //  Open the next stage - main screen of the app
        Intent intent = new Intent(this, ShiftsActivity.class);
        startActivity(intent);

        finish();
    }


    //Return false if the TextEdit is not full
    private boolean checkFields() {
        return !(firstName.getText().toString().trim().equals("") ||
                lastName.getText().toString().trim().equals("") ||
                companyName.getText().toString().trim().equals("") ||
                hourlyWage.getText().toString().trim().equals("") ||
                email.getText().toString().trim().equals("") ||
                password.getText().toString().trim().equals(""));
    }
}
