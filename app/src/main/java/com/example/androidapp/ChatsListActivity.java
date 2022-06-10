package com.example.androidapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.adapters.ChatsListAdapter;
import com.example.androidapp.classes.Chat;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

public class ChatsListActivity extends AppCompatActivity {

//    private ChatViewModel chatViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_list);

        // get a firebase token
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(ChatsListActivity.this, new OnSuccessListener<InstanceIdResult>() {
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

        List<Chat> chats = new ArrayList<>();
        chats.add(new Chat(1, "alice", "Alice", "x"));
        chats.add(new Chat(2, "bob", "Bob", "x"));
        chats.add(new Chat(3, "charlie", "Charlie", "x"));
        adapter.setChats(chats);
    }
//        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
//
//        chatViewModel.get().observe(this, new Observer<List<Chat>>() {
//            @Override
//            public void onChanged(List<Chat> chats) {
//                adapter.setChats(chats);
//            }
//        });

}