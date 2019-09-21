package com.example.myworkshifts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    //  Open register activity
    public void register(final View view) {
        Intent intent = new Intent(view.getContext(), RegisterActivity.class);
        startActivity(intent);
    }

    //  Login option not open new activity(only new content view)
    public void login(final View view) {

        email       = findViewById(R.id.etEmail);
        password    = findViewById(R.id.etPassword);

        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(view.getContext(), ShiftsActivity.class);
                            startActivity(intent);
                            Toasty.success(view.getContext(), R.string.success, Toast.LENGTH_SHORT, true).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toasty.error(view.getContext(), R.string.auth_failed, Toast.LENGTH_SHORT, true).show();
                        }

                        // ...
                    }
                });
    }

    public void openLogin(View view) {
        setContentView(R.layout.activity_login);
    }
}
