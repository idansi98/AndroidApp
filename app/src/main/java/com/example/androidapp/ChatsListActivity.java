package com.example.androidapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.adapters.ChatsListAdapter;
import com.example.androidapp.classes.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_list);

        RecyclerView chatsList = findViewById(R.id.chatsList);
        final ChatsListAdapter adapter = new ChatsListAdapter(this);
        chatsList.setAdapter(adapter);
        chatsList.setLayoutManager(new LinearLayoutManager(this));

        List<Chat> chats = new ArrayList<>();
        chats.add(new Chat(1,"alice","Alice","x"));
        chats.add(new Chat(2,"bob","Bob","x"));
        chats.add(new Chat(3,"charlie","Charlie","x"));
        adapter.setChats(chats);
    }
}