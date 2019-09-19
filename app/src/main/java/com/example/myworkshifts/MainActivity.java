package com.example.myworkshifts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        email       = findViewById(R.id.etEmail);
        password    = findViewById(R.id.etPassword);
    }

    public void register(final View view) {

        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(view.getContext(), "Moving to the next step",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(view.getContext(), "Error in mail or password.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public void login(View view) {



    }
}
