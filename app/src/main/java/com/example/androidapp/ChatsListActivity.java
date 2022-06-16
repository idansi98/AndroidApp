package com.example.androidapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidapp.adapters.ChatsListAdapter;
import com.example.androidapp.api.ContactApi;
import com.example.androidapp.classes.AppDB;
import com.example.androidapp.classes.ChatDao;
import com.example.androidapp.classes.MessageDao;
import com.example.androidapp.classes.UserDao;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class ChatsListActivity extends AppCompatActivity {
    private AppDB db;
    private UserDao userDao;
    private ChatDao chatDao;
    private MessageDao messageDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_list);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "OurDB")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao = db.userDao();
        chatDao = db.chatDao();
        messageDao = db.messageDao();


        // get a firebase token
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(
                ChatsListActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.d("Firebase_Logging", "The token is " + newToken);
            }
        });

        RecyclerView chatsList = findViewById(R.id.chatsList);
        final ChatsListAdapter adapter = new ChatsListAdapter(this);
        chatsList.setAdapter(adapter);
        chatsList.setLayoutManager(new LinearLayoutManager(this));
        ContactApi contactApi = new ContactApi(userDao, chatDao, messageDao);
        Thread thread = new Thread(){
            public void run(){
                contactApi.get();
            }
        };
        thread.start();
        adapter.setMessageDao(messageDao);
        adapter.setChats(chatDao.index());

    }


}