package com.example.androidapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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
                URL url = new URL("https://10.0.2.2:25565/api/login");
                Log.d("Login_Logging", "Trying to connect to: " + url.toString());
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("content-type", "application/json");

                /* Payload support */
                con.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes("{\"username\":\""+userName+"\",\"password\":\""+password+"\"}");
                Log.d("Fetch_Login", "{\"username\":\""+userName+"\",\"password\":\""+password+"\"}");
                out.flush();
                out.close();

                int status = con.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();
                login_status =  (status >= 200 && status < 300);
                Log.d("Login_Logging", "Async method finished");
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