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
        setContentView(R.layout.activity_login);
        RecyclerView chatslist = findViewById(R.id.chatslist);
        ChatsListAdapter adapter = new ChatsListAdapter(this);
        chatslist.setAdapter(adapter);
        chatslist.setLayoutManager(new LinearLayoutManager(this));
        List<Chat> chats = new ArrayList<>();
        chats.add(new Chat("Idan Simai", "IS","Null"));
        chats.add(new Chat("Ido Tziony", "IT","Null"));
        chats.add(new Chat("Hemi Leibo", "HL","Null"));
        adapter.setChats(chats);
    }
}