package com.example.androidapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText userName, displayName, password, passwordValidator;
    Button registerButton;
    /*
    FirebaseAuth auth;
    DatabaseReference ref;*/
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userName = findViewById(R.id.editTextTextPersonName);
        displayName = findViewById(R.id.editTextTextDisplayName);
        password = findViewById(R.id.editTextTextPassword);
        passwordValidator = findViewById(R.id.editTextTextPasswordValidation);
        registerButton = findViewById(R.id.registerButton);
    }
}