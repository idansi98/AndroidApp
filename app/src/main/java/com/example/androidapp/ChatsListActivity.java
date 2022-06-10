package com.example.androidapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.adapters.ChatsListAdapter;
import com.example.androidapp.classes.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatsListActivity extends AppCompatActivity {
    static final String Channel_ID = "1";


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


        createNotificationChannel();
        
        // create a notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Channel_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentText("Title!!")
                .setContentText("NOTIFICATION WOO")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        int notificationID = 1;
        notificationManager.notify(notificationID,builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // idk if check for version is needed
            CharSequence name = "Hey";
            String description = "DESC";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Channel_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }
}