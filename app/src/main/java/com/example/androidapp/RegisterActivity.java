package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    EditText userName, displayName, password, passwordValidator;
    Button registerButton;
    List<String> stringList;
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
        registerButton.setOnClickListener(v -> {
            if (!valid()) {
                return;
            }
            //try to add user.
            boolean result = createUser(userName.getText().toString(), displayName.getText().toString(),
                    password.getText().toString());

            Intent i = new Intent(this, RegisterActivity.class);
            //i.putExtra();
            startActivity(i);
        });
    }
    private boolean createUser (String userName, String displayName, String password) {
        try {
            return true;
        }
        catch (Exception e) {
            Log.e("Fetch", e.getMessage().toLowerCase(Locale.ROOT));
            return false;
        }

    }
    private boolean valid () {
        return true;
    }
}