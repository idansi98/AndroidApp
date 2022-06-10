package com.example.androidapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    EditText userName, password;
    Button loginButton, toRegisterButton;
    boolean login_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_status = false;
        userName = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.loginButton);
        toRegisterButton = findViewById(R.id.toRegButton);
        loginButton.setOnClickListener(v -> {
            try {
                login(userName.getText().toString(), password.getText().toString());
            } catch (InterruptedException e) {
                Log.d("Log-in-logging","GOT AN EXCEPTION");
                e.printStackTrace();
            }
        });
        toRegisterButton.setOnClickListener(v -> {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });
    }
    private void login (String userName, String password) throws InterruptedException {
        Log.d("Login_Logging", "Trying to log in");
        LoggingTask task = new LoggingTask();
        task.execute(userName, password);
        Log.d("Login_Logging", "Called the async method");

    }
    private class LoggingTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d("Login_Logging", "Async method started running");
            try {
                URL url = new URL("http://10.0.2.2:25565/api/login");
                Log.d("Login_Logging", "Trying to connect to: " + url.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("content-type", "application/json");

                /* Payload support */
                con.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes("{\"username\":\""+userName.getText().toString()+"\"," +
                        "\"password\":\""+password.getText().toString()+"\"}");
                Log.d("Fetch_Login", "{\"username\":\""+userName.getText().toString()
                        +"\",\"password\":\""+password.getText().toString()+"\"}");
                out.flush();
                out.close();

                int status = con.getResponseCode();
                con.disconnect();
                login_status =  (status >= 200 && status < 300);
                Log.d("Login_Logging", "Async method finished, login_status is " +
                        status);
                return "Maybe?";
            }
            catch (Exception e) {
                login_status = false;
                Log.d("Login_Logging", "Async method finished with an error: " + e.toString());
                return "False";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d("Login_Logging", "On post-execute");
            if(login_status) {
                Log.d("Login_Logging", "Entered logging clause");
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        }



    }
}
