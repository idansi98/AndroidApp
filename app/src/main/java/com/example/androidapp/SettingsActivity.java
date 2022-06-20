package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

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
        if (getIntent().hasExtra("server_adr")) {
            serverId.setText(getIntent().getStringExtra("server_adr"));
        }
        changeButton = findViewById(R.id.changeButton);
        changeButton.setOnClickListener(v -> {
            Intent intent=new Intent(SettingsActivity.this, LoginActivity.class);
            intent.putExtra("server_adr",serverId.getText().toString());
            startActivity(intent);
        });
        leftArrow =  findViewById(R.id.backArrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(SettingsActivity.this, LoginActivity.class);
                Intent thisIntent = getIntent();
                if (thisIntent.hasExtra("server_adr")) {
                    String server_adr = thisIntent.getStringExtra("server_adr");
                    i.putExtra("server_adr",server_adr);
                }
                startActivity(i);
            }
        });

    }
}