package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androidapp.classes.AppDB;
import com.example.androidapp.classes.UserDao;

public class SettingsActivity extends AppCompatActivity {
    private AppDB db;
    private UserDao userDao;
    ImageView leftArrow;
    EditText serverId;
    Button changeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "OurDB")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao = db.userDao();
        serverId = findViewById(R.id.serverId);
        changeButton = findViewById(R.id.changeButton);
        changeButton.setOnClickListener(v -> {
            db.userDao().index().get(0).setDefaultServerAdr(serverId.getText().toString());
        });
        leftArrow =  findViewById(R.id.backArrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this, ChatsListActivity.class);
                startActivity(intent);
            }
        });

    }
}