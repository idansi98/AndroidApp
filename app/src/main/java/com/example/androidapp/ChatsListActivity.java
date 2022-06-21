package com.example.androidapp;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.androidapp.adapters.ChatsListAdapter;
import com.example.androidapp.api.ContactApi;
import com.example.androidapp.classes.AppDB;
import com.example.androidapp.classes.ChatDao;
import com.example.androidapp.classes.EncodedImage;
import com.example.androidapp.classes.EncodedImageDao;
import com.example.androidapp.classes.MessageDao;
import com.example.androidapp.classes.User;
import com.example.androidapp.classes.UserDao;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ChatsListActivity extends AppCompatActivity {
    private AppDB db;
    private UserDao userDao;
    private ChatDao chatDao;
    private MessageDao messageDao;
    private EncodedImageDao imageDao;
    private Semaphore mutex = new Semaphore(1);
    public static final String NOTIFY_ACTIVITY_ACTION = "notify_activity";
    private BroadcastReceiver broadcastReceiver;
    private ChatsListAdapter adapter;
    private ContactApi contactApi;
    private SwipeRefreshLayout layout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_list);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "OurDB")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao = db.userDao();
        chatDao = db.chatDao();
        messageDao = db.messageDao();
        imageDao = db.encodedImageDao();


        // get a firebase token
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(
                ChatsListActivity.this, instanceIdResult -> {
                    String newToken = instanceIdResult.getToken();
                    Thread thread = new Thread(){
                        public void run(){
                            try {
                                // cookie manager
                                CookieHandler cookieHandler = new CookieManager();

                                // make a new session
                                OkHttpClient client = new OkHttpClient.Builder()
                                        .cookieJar(new JavaNetCookieJar(cookieHandler))
                                        .connectTimeout(15, TimeUnit.SECONDS)
                                        .readTimeout(20, TimeUnit.SECONDS)
                                        .writeTimeout(20, TimeUnit.SECONDS)
                                        .retryOnConnectionFailure(true)
                                        .build();
                                tryLogin(client);
                                String json = "\"" + newToken + "\"";

                                RequestBody body = RequestBody.create(
                                        MediaType.parse("application/json"), json);
                                Request request = new Request.Builder()
                                        .url("http://"+userDao.index().get(0).getDefaultServerAdr()+"/api/firebase")
                                        .post(body)
                                        .build();

                                okhttp3.Call call = client.newCall(request);
                                okhttp3.Response response = call.execute();
                                Log.d("Firebase_Logging", "Firebase new id attempt: " + response.message() + response.code());
                            } catch (IOException ex) {
                                Log.d("Firebase_Logging", "Couldn't upload token: " + ex.getMessage());
                            }

                        }
                    };
                    thread.start();
                    Log.d("Firebase_Logging", "The token is " + newToken);
                });

        RecyclerView chatsList = findViewById(R.id.chatsList);
        ImageView signOffButton = findViewById(R.id.signOffButton);
        ImageView addContact = findViewById(R.id.addContact);
        ImageView chatListPFP = findViewById(R.id.chatListPFP);
        adapter = new ChatsListAdapter(this);
        chatsList.setAdapter(adapter);
        chatsList.setLayoutManager(new LinearLayoutManager(this));
        contactApi = new ContactApi(userDao, chatDao, messageDao);
        adapter.setMessageDao(messageDao);
        adapter.setChats(chatDao.index());
        // refresh initially
        layout = findViewById(R.id.refreshLayout);
        ChatsListActivity.RefreshingTask task = new ChatsListActivity.RefreshingTask();
        task.setAdapter(adapter);
        task.setContactApi(contactApi);
        task.setLayout(layout);
        task.execute();
        // refresh with a swipe
        layout.setOnRefreshListener(() -> {
            ChatsListActivity.RefreshingTask task2 = new ChatsListActivity.RefreshingTask();
            task2.setAdapter(adapter);
            task2.setContactApi(contactApi);
            task2.setLayout(layout);
            task2.execute();
        });
        // Add contact
        addContact.setOnClickListener(v -> {
            Intent i = new Intent(this, AddContactActivity.class);
            startActivity(i);
        });
        // Sign off
        signOffButton.setOnClickListener(v -> {
// remove the firebase token
            Thread thread = new Thread(){
                public void run(){
                    try {

                        // cookie manager
                        CookieHandler cookieHandler = new CookieManager();

                        // make a new session
                        OkHttpClient client = new OkHttpClient.Builder()
                                .cookieJar(new JavaNetCookieJar(cookieHandler))
                                .connectTimeout(15, TimeUnit.SECONDS)
                                .readTimeout(20, TimeUnit.SECONDS)
                                .writeTimeout(20, TimeUnit.SECONDS)
                                .retryOnConnectionFailure(true)
                                .build();
                        tryLogin(client);
                        String json = "\"" + "bye bye" + "\"";

                        RequestBody body = RequestBody.create(
                                MediaType.parse("application/json"), json);

                        Request request = new Request.Builder()
                                .url("http://"+userDao.index().get(0).getDefaultServerAdr()+"/api/firebase")
                                .post(body)
                                .build();

                        okhttp3.Call call = client.newCall(request);
                        okhttp3.Response response = call.execute();
                        Log.d("Firebase_Logging", "Firebase log out attempt: " + response.message() + response.code());
                    } catch (IOException ex) {
                        Log.d("Firebase_Logging", "Couldn't log out: " + ex.getMessage());
                    }

                }
            };
            thread.start();
            try {
                thread.join();
            } catch (Exception ex) {
                // something
            }
            // reset the DB
            messageDao.resetTable();
            chatDao.resetTable();
            userDao.resetTable();

            // go to the main screen
            Intent i = new Intent(ChatsListActivity.this, LoginActivity.class);
            startActivity(i);
        });

        // set the PFP
        List<EncodedImage> images = imageDao.index();
        String userName = userDao.index().get(0).getUserName();
        boolean useDefaultPFP = true;

        for (EncodedImage im: images) {
            if (im.getContactName().contentEquals(userName)){
                useDefaultPFP = false;
                Uri uri = Uri.parse(im.getUriString());
                ContentResolver contentResolver;
                getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                chatListPFP.setImageURI(uri);
            }
        }
        if (useDefaultPFP) {
            chatListPFP.setImageResource(R.drawable.logo);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(NOTIFY_ACTIVITY_ACTION)) {
                    ChatsListActivity.RefreshingTask task = new ChatsListActivity.RefreshingTask();
                    task.setAdapter(adapter);
                    task.setContactApi(contactApi);
                    task.setLayout(layout);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(ChatsListActivity.this);
    }

    private void tryLogin(OkHttpClient client)  {
        try {
            User user = userDao.index().get(0);
            String json = "{\"username\":\""+user.getUserName()+"\",\"password\":\""+user.getPassword()+"\"}";

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"), json);

            Request request = new Request.Builder()
                    .url("http://"+ userDao.index().get(0).getDefaultServerAdr()+"/api/login")
                    .post(body)
                    .build();

            okhttp3.Call call = client.newCall(request);
            okhttp3.Response response = call.execute();
            Log.d("API_LOGGING","response body:" + response.body().string());
        } catch (IOException ex) {
            Log.e("API_LOGGING",ex.getMessage());
        }

    }

    private class RefreshingTask extends AsyncTask<String, String, String> {
        private ContactApi contactApi2;
        private ChatsListAdapter adapter2;
        private SwipeRefreshLayout layout;
        @Override
        protected String doInBackground(String... params) {
            try {
                mutex.acquire();
                contactApi2.get();
            } catch (InterruptedException e) {
                Log.d("ChatList_Logging", "Mutex ex:  " + e.getMessage());
            } finally {
                mutex.release();
            }
            return "idk";
        }
        @Override
        protected void onPostExecute(String result) {
            adapter2.setChats(chatDao.index());
            layout.setRefreshing(false);
        }


        public void setAdapter(ChatsListAdapter adapter) {
            this.adapter2 = adapter;
        }

        public void setContactApi(ContactApi contactApi) {
            this.contactApi2 = contactApi;
        }

        public void setLayout(SwipeRefreshLayout layout) {
            this.layout = layout;
        }
    }

}