package com.example.androidapp;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.androidapp.classes.AppDB;
import com.example.androidapp.classes.User;
import com.example.androidapp.classes.UserDao;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private AppDB db;
    private UserDao userDao;
    EditText userName, password;
    Button loginButton, toRegisterButton;
    ImageView  goToSettingsButton;
    boolean login_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "OurDB")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao = db.userDao();
        login_status = false;
        userName = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.loginButton);
        toRegisterButton = findViewById(R.id.toRegButton);
        goToSettingsButton = findViewById(R.id.goToSettingsButton);

        //if logged in already -> go to chats
        if(userDao.index().size()!=0) {
            Intent i = new Intent(this, ChatsListActivity.class);
            startActivity(i);
        }
        loginButton.setOnClickListener(v -> {
            try {
                login(userName.getText().toString(), password.getText().toString());
            } catch (InterruptedException e) {
                Log.d("Login_Logging","GOT AN EXCEPTION");
                e.printStackTrace();
            }
        });
        toRegisterButton.setOnClickListener(v -> {
            Intent i = new Intent(this, RegisterActivity.class);
            Intent thisIntent = getIntent();
            if (thisIntent.hasExtra("server_adr")) {
                String server_adr = thisIntent.getStringExtra("server_adr");
                i.putExtra("server_adr",server_adr);
            }
            startActivity(i);
        });
        goToSettingsButton.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, SettingsActivity.class);
            Intent thisIntent = getIntent();
            if (thisIntent.hasExtra("server_adr")) {
                String server_adr = thisIntent.getStringExtra("server_adr");
                i.putExtra("server_adr",server_adr);
            }
            startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isLoggedAlready()) {
            Intent i = new Intent(LoginActivity.this, ChatsListActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(isLoggedAlready()) {
            Intent i = new Intent(LoginActivity.this, ChatsListActivity.class);
            startActivity(i);
        }
    }

    private boolean isLoggedAlready() {
        return (userDao.index().size() >0);
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
                String urlString;
                Intent thisIntent = getIntent();
                if (thisIntent.hasExtra("server_adr")) {
                     urlString = thisIntent.getStringExtra("server_adr");
                } else {
                     urlString = "10.0.2.2:25565";
                }
                URL url = new URL("http://" + urlString+"/api/login");
                Log.d("Login_Logging", "Trying to connect to: " + url.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("content-type", "application/json");
                String currentUserName = params[0];
                String currentPassword= params[1];

                /* Payload support */
                con.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes("{\"username\":\""+currentUserName+"\"," +
                        "\"password\":\""+currentPassword+"\"}");
                Log.d("Fetch_Login", "{\"username\":\""+currentUserName
                        +"\",\"password\":\""+currentPassword+"\"}");
                out.flush();
                out.close();

                int status = con.getResponseCode();
                con.disconnect();
                login_status =  (status >= 200 && status < 300);
                Log.d("Login_Logging", "Async method finished, status code is " +
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
            // assuming we are logging in ONLY if no user is set in DAO
            if(login_status) {
                // insert the current user -> display name isn't updated yet!
                Intent thisIntent = getIntent();
                if (thisIntent.hasExtra("server_adr")) {
                    String server_adr = thisIntent.getStringExtra("server_adr");
                    userDao.insert(new User(1,userName.getText().toString(),userName.getText().toString(),password.getText().toString(),server_adr));
                } else {
                    userDao.insert(new User(1,userName.getText().toString(),userName.getText().toString(),password.getText().toString()));
                }
                Log.d("Login_Logging", "Entered logging clause");
                Intent i = new Intent(LoginActivity.this, ChatsListActivity.class);
                startActivity(i);
            } else {
                //something
            }
        }



    }
}
