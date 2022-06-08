package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.classes.LoginRunner;

public class LoginActivity extends AppCompatActivity {
    EditText userName, password;
    Button loginButton, toRegisterButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.loginButton);
        toRegisterButton = findViewById(R.id.toRegButton);
        loginButton.setOnClickListener(v -> {
            try {
                login(userName.getText().toString(), password.getText().toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        toRegisterButton.setOnClickListener(v -> {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });

        /*loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserName = userName.getText().toString();
                String Password = password.getText().toString();

            }
        });*/
    }
    private boolean login (String userName, String password) throws InterruptedException {
        LoginRunner foo = new LoginRunner();
        foo.setValues(userName, password);
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        //int value = foo.getValue();
        Log.d("Fetch_Login", "Here");
        return foo.getValue();
    }
}
