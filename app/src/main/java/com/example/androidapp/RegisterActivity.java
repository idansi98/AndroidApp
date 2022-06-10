package com.example.androidapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.androidapp.classes.AppDB;
import com.example.androidapp.classes.User;
import com.example.androidapp.classes.UserDao;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {
    EditText userName, displayName, password, passwordValidator;
    Button registerButton, toLoginButton;
    boolean register_status;
    private AppDB db;
    private UserDao userDao;
    /*
    FirebaseAuth auth;
    DatabaseReference ref;*/
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_status = false;
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "OurDB")
                .allowMainThreadQueries().build();
        userDao = db.userDao();
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
            createUser(userName.getText().toString(), displayName.getText().
                            toString(), password.getText().toString());
        });
    }

    private void createUser(String userName, String displayName, String password) {
        try {
            Log.d("Register_Logging", "Trying to register");
            RegisterActivity.RegisteringTask task = new RegisterActivity.RegisteringTask();
            task.execute(userName, password, displayName);
            Log.d("Register_Logging", "Called the async method");
        } catch (Exception e) {
            Log.e("Register_Logging", e.getMessage());
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

    private class RegisteringTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d("Register_Logging", "Async method started running");
            try {
                URL url = new URL("http://10.0.2.2:25565/api/register");
                Log.d("Register_Logging", "Connecting to: " + url.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("content-type", "application/json");
                String currentUserName = params[0];
                String currentPassword = params[1];
                String currentDisplayName = params[2];

                /* Payload support */
                con.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes("{\"ID\":\""+currentUserName+"\",\"Password\":\""+currentPassword+"\",\"DisplayName\":\""+currentDisplayName+"\"}");
                Log.d("Register_Logging", "Trying to send: " + "{\"ID\":\""+currentUserName+"\",\"Password\":\""+currentPassword+"\",\"DisplayName\":\""+currentDisplayName+"\"}");
                out.flush();
                out.close();

                int status = con.getResponseCode();
                con.disconnect();
                register_status =  (status >= 200 && status < 300);
                Log.d("Register_Logging", "Async method finished, status code is " +
                        status);
                return "Maybe?";
            }
            catch (Exception e) {
                register_status = false;
                Log.d("Register_Logging", "Async method finished with an error: " + e.toString());
                return "False";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d("Register_Logging", "On post-execute");
            // assuming we are logging in ONLY if no user is set in DAO
            if(register_status) {
                // insert the current user -> display name isn't updated yet!
                userDao.insert(new User(1,userName.getText().toString(),displayName.getText().toString(),password.getText().toString()));
                Log.d("Register_Logging", "Added a new user to DB");
                Intent i = new Intent(RegisterActivity.this, ChatsListActivity.class);
                startActivity(i);
            } else {
                //something
            }
        }



    }
}