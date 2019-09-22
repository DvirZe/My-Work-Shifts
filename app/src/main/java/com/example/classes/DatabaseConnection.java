package com.example.classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseConnection {
    private static final DatabaseConnection ourInstance = new DatabaseConnection();
    private static FirebaseAuth mAuth;
    private static FirebaseUser currentUser;
    private static FirebaseDatabase database;

    public static DatabaseConnection getInstance() {
        return ourInstance;
    }

    private DatabaseConnection() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public void initUser() {
        currentUser = mAuth.getCurrentUser();
    }

    public static FirebaseAuth getmAuth() {
        return mAuth;
    }

    public static FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public static FirebaseDatabase getDatabase() {
        return database;
    }
}
