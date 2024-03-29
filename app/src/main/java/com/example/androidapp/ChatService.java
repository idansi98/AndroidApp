package com.example.androidapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ChatService extends FirebaseMessagingService {
    private static final String Channel_ID = "1";
    private NotificationManagerCompat notificationManager;
    private boolean isInitialized = false;
    public ChatService() {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (!isInitialized) {
            createNotificationChannel();
            isInitialized = true;
        }
        String contactDisplayName = message.getNotification().getTitle();
        String messageBody = message.getNotification().getBody();
        Log.d("Firebase_Logging", "Got a message from " +contactDisplayName + "\nContents: " + messageBody);
        makeNotification(contactDisplayName, messageBody);


        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ChatActivity.NOTIFY_ACTIVITY_ACTION);
        sendBroadcast(broadcastIntent);
    }

    private void createNotificationChannel() {
        notificationManager = NotificationManagerCompat.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // idk if check for version is needed
            CharSequence name = "ContactsNotification";
            String description = "Contains all the new messages from the app";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Channel_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    private void makeNotification(String contactDisplayName, String message) {
        // create a pending intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(this,ChatActivity.class);
            intent.putExtra("username",contactDisplayName);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Channel_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(contactDisplayName)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(message))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            int notificationID = contactDisplayName.hashCode();
            notificationManager.notify(notificationID,builder.build());
        }
    }

}