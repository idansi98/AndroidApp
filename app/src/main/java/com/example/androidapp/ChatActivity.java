package com.example.androidapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidapp.adapters.ChatAdapter;
import com.example.androidapp.api.ContactApi;
import com.example.androidapp.classes.AppDB;
import com.example.androidapp.classes.Chat;
import com.example.androidapp.classes.ChatDao;
import com.example.androidapp.classes.MessageDao;
import com.example.androidapp.classes.User;
import com.example.androidapp.classes.UserDao;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Semaphore;

public class ChatActivity extends AppCompatActivity {
    private AppDB db;
    private UserDao userDao;
    private ChatDao chatDao;
    private MessageDao messageDao;
    private Semaphore mutex = new Semaphore(1);
    public static final String NOTIFY_ACTIVITY_ACTION = "notify_activity";
    private BroadcastReceiver broadcastReceiver;
    private ChatAdapter adapter;
    private ContactApi contactApi;
    private String username;
    private RecyclerView chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "OurDB")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao = db.userDao();
        chatDao = db.chatDao();
        messageDao = db.messageDao();

        Intent thisIntent = getIntent();
        username = thisIntent.getStringExtra("username");
        chat = findViewById(R.id.chatRecyclerView);
        TextView userNameView = findViewById(R.id.userName);
        userNameView.setText(username);
        adapter = new ChatAdapter(this);
        chat.setAdapter(adapter);
        chat.setLayoutManager(new LinearLayoutManager(this));
        contactApi = new ContactApi(userDao, chatDao, messageDao);

        adapter.setMessages(messageDao.get(username));
        // get new messages
        ChatActivity.RefreshingTask task = new ChatActivity.RefreshingTask();
        task.setAdapter(adapter);
        task.setContactApi(contactApi);
        task.setContactName(username);
        task.execute();

        // send new messages
        TextView toSendText = findViewById(R.id.editTextTextPersonName2);
        ImageView sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> {
            ChatActivity.MessageSendingTask task1 = new ChatActivity.MessageSendingTask();
            task1.setAdapter(adapter);
            task1.setContactApi(contactApi);
            task1.setMessageTextView(toSendText);
            task1.setContactName(username);
            task1.execute();
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(NOTIFY_ACTIVITY_ACTION)) {
                    ChatActivity.RefreshingTask task = new ChatActivity.RefreshingTask();
                    task.setAdapter(adapter);
                    task.setContactApi(contactApi);
                    task.setContactName(username);
                    task.execute();
                }
            }
        };
        IntentFilter filter = new IntentFilter(NOTIFY_ACTIVITY_ACTION);
        registerReceiver(broadcastReceiver, filter);
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    private class RefreshingTask extends AsyncTask<String, String, String> {
        private ContactApi contactApi2;
        private ChatAdapter adapter2;
        private String contact_name;
        @Override
        protected String doInBackground(String... params) {
            try {
                mutex.acquire();
                contactApi2.get();
            } catch (InterruptedException e) {
                Log.d("Chat_Logging", "Mutex ex:  " + e.getMessage());
            } finally {
                mutex.release();
            }
            return "idk";
        }
        @Override
        protected void onPostExecute(String result) {
            adapter2.setMessages(messageDao.get(contact_name));
        }

        public void setAdapter(ChatAdapter adapter) {
            this.adapter2 = adapter;
        }

        public void setContactApi(ContactApi contactApi) {
            this.contactApi2 = contactApi;
        }

        public void setContactName(String username) {
            this.contact_name = username;
        }
    }
    private class MessageSendingTask extends AsyncTask<String, String, String> {
        private TextView messageTextView;
        private String contact_name;
        private ContactApi contactApi2;
        private ChatAdapter adapter2;
        @Override
        protected String doInBackground(String... params) {
            try {
                // in case no message content
                if (messageTextView.getText().toString().equals("")) {
                    return "yey";
                }
                mutex.acquire();
                // same connection cookies
                CookieManager cookieManager = new CookieManager();
                CookieHandler.setDefault(cookieManager);
                // get vars
                Chat chat = chatDao.get(contact_name);
                User currentUser = userDao.index().get(0);
                if (chat == null) {
                    Log.d("Chat_Logging", "Could not find chat for contact name: " + contact_name);
                    return "idk";
                }
                String server_adr = chat.getServerAdr();


                // send the message to the other server
                try {
                    Log.d("Chat_Logging", "Server addr is " + server_adr);
                    // TODO: check
                    server_adr = server_adr.replace("localhost","10.0.2.2");
                    String url_to_send  = "http://"+server_adr+"/api/transfer";

                    URL url = new URL(url_to_send);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("content-type", "application/json");

                    /* Payload support */
                    con.setDoOutput(true);
                    DataOutputStream out = new DataOutputStream(con.getOutputStream());
                    out.writeBytes("{\"from\":\""+currentUser.getUserName()+"\",\"to\":\""+chat.getUserName()+"\",\"content\":\""+messageTextView.getText().toString()+"\"}");
                    out.flush();
                    out.close();

                    int status = con.getResponseCode();
                    if (status <200 || status >300) {
                        Log.d("Chat_Logging", "Status returned in transfer is an error: STATUS:" + status);
                        Log.d("Chat_Logging", url.toString());
                        Log.d("Chat_Logging", "{\"from\":\""+currentUser.getUserName()+"\",\"to\":\""+chat.getUserName()+"\",\"content\":\""+messageTextView.getText().toString()+"\"}");

                        return "idk";
                    }
                    con.disconnect();

                    // send the message to this server
                    String url_to_send2  = "http://"+userDao.index().get(0).getDefaultServerAdr() + "/api/contacts/" + contact_name +"/messages";
                    URL url2 = new URL(url_to_send2);
                    HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
                    // login
                    tryLogin();

                    // send request
                    con2.setRequestMethod("POST");
                    con2.setRequestProperty("content-type", "application/json");

                    /* Payload support */
                    con2.setDoOutput(true);
                    DataOutputStream out2 = new DataOutputStream(con2.getOutputStream());
                    out2.writeBytes("{\"content\":\""+messageTextView.getText().toString()+"\"}");
                    out2.flush();
                    out2.close();

                    int status2 = con2.getResponseCode();
                    if (status2 <200 || status2 >300) {
                        Log.d("Chat_Logging", "Status returned in new message is an error: STATUS:" + status2);
                        Log.d("Chat_Logging", url2.toString());
                        Log.d("Chat_Logging", "{\"content\":\""+messageTextView.getText().toString()+"\"}");


                        return "idk";
                    }
                    con2.disconnect();

                } catch ( Exception ex) {
                    Log.d("Chat_Logging", "Exception on transfer, error is: " + ex.getMessage());


                }
            } catch (InterruptedException e) {
                Log.d("Chat_Logging", "Mutex ex:  " + e.getMessage());
            } finally {
                mutex.release();
            }
            return "idk";
        }
        @Override
        protected void onPostExecute(String result) {
            // when over it calls the refreshing task
            ChatActivity.RefreshingTask task = new ChatActivity.RefreshingTask();
            task.setAdapter(adapter2);
            task.setContactApi(contactApi2);
            task.setContactName(contact_name);
            task.execute();
            // remove the message
            messageTextView.setText("");
            chat.smoothScrollToPosition(chat.FOCUS_DOWN);

        }

        public void setMessageTextView(TextView messageTextView) {
            this.messageTextView = messageTextView;
        }

        public void setContactName(String contact_name){
            this.contact_name = contact_name;
        }
        public void setAdapter(ChatAdapter adapter) {
            this.adapter2 = adapter;
        }

        public void setContactApi(ContactApi contactApi) {
            this.contactApi2 = contactApi;
        }
    }
    private void tryLogin()  {
        try {
            User user = userDao.index().get(0);
            URL url = new URL("http://"+userDao.index().get(0).getDefaultServerAdr()+"/api/login");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("content-type", "application/json");

            /* Payload support */
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes("{\"username\":\""+user.getUserName()+"\",\"password\":\""+user.getPassword()+"\"}");
            out.flush();
            out.close();

            int status = con.getResponseCode();
        } catch (IOException ex) {
            Log.e("API_LOGGING",ex.getMessage());
        }

    }

}