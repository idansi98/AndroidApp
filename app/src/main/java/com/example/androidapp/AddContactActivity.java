package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.androidapp.classes.AppDB;
import com.example.androidapp.classes.Chat;
import com.example.androidapp.classes.ChatDao;
import com.example.androidapp.classes.User;
import com.example.androidapp.classes.UserDao;

public class AddContactActivity extends AppCompatActivity {
    ImageView toChatsList;
    private AppDB db;
    private ChatDao chatDao;
    private Button addContact;
    private EditText nickName, userName, serverAddress;
    private ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "OurDB")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        chatDao = db.chatDao();
        nickName = findViewById(R.id.nickName);
        userName = findViewById(R.id.userName1);
        serverAddress = findViewById(R.id.serverAddress);
        backArrow = findViewById(R.id.backArrow2);
        addContact = findViewById(R.id.addContactButton);
        backArrow.setOnClickListener(v -> {
            Intent i = new Intent(this, ChatsListActivity.class);
            startActivity(i);
        });
        addContact.setOnClickListener(v -> chatDao.insert(new Chat(1, userName.getText().toString(),
                nickName.getText().toString(), serverAddress.getText().toString())));
    }
}