package com.example.androidapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidapp.adapters.ChatAdapter;
import com.example.androidapp.adapters.ChatsListAdapter;
import com.example.androidapp.api.ContactApi;
import com.example.androidapp.classes.AppDB;
import com.example.androidapp.classes.ChatDao;
import com.example.androidapp.classes.MessageDao;
import com.example.androidapp.classes.UserDao;

public class ChatActivity extends AppCompatActivity {
    private AppDB db;
    private UserDao userDao;
    private ChatDao chatDao;
    private MessageDao messageDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "OurDB")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao = db.userDao();
        chatDao = db.chatDao();
        messageDao = db.messageDao();



        RecyclerView chat = findViewById(R.id.chatRecyclerView);
        final ChatAdapter adapter = new ChatAdapter(this);
        chat.setAdapter(adapter);
        chat.setLayoutManager(new LinearLayoutManager(this));
        ContactApi contactApi = new ContactApi(userDao, chatDao, messageDao);
        Thread thread = new Thread(){
            public void run(){
                contactApi.get();
            }
        };
        thread.start();
        adapter.setMessageDao(messageDao);
        adapter.setMessages(messageDao.index());
    }
}