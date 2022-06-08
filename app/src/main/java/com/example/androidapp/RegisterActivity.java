package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    EditText userName, displayName, password, passwordValidator;
    Button registerButton, toLoginButton;

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
        toLoginButton = findViewById(R.id.toLoginButton);
        toLoginButton.setOnClickListener(v -> {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        });
        registerButton.setOnClickListener(v -> {
            if (!isValid(userName.getText().toString(), password.getText().toString(),
                    passwordValidator.getText().toString(), displayName.getText().toString())) {
                return;
            }
            //try to add user.
            boolean result = createUser(userName.getText().toString(), displayName.getText().
                            toString(), password.getText().toString());

            Intent i = new Intent(this, RegisterActivity.class);
            //i.putExtra();
            startActivity(i);
        });
    }

    private boolean createUser(String userName, String displayName, String password) {
        try {
            return true;
        } catch (Exception e) {
            Log.e("Fetch", e.getMessage().toLowerCase(Locale.ROOT));
            return false;
        }

    }

    private boolean isValid(String userName, String password, String passwordValidator,
                            String displayName) {
        if (userName.isEmpty() || password.isEmpty() || passwordValidator.isEmpty()
                || displayName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill in all the fields!",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.length() < 8) {
            Toast.makeText(getApplicationContext(), "The password is too short!",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.length() > 14) {
            Toast.makeText(getApplicationContext(), "The password is too long!",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (!password.equals(passwordValidator)) {
            Toast.makeText(getApplicationContext(), "The passwords are not the same!",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}