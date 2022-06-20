package com.example.androidapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.androidapp.adapters.ChatsListAdapter;
import com.example.androidapp.api.ContactApi;
import com.example.androidapp.classes.AppDB;
import com.example.androidapp.classes.ChatDao;
import com.example.androidapp.classes.MessageDao;
import com.example.androidapp.classes.User;
import com.example.androidapp.classes.UserDao;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
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
    private Semaphore mutex = new Semaphore(1);

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
            }
        });

        RecyclerView chatsList = findViewById(R.id.chatsList);
        ImageView signOffButton = findViewById(R.id.signOffButton);

        final ChatsListAdapter adapter = new ChatsListAdapter(this);
        chatsList.setAdapter(adapter);
        chatsList.setLayoutManager(new LinearLayoutManager(this));
        ContactApi contactApi = new ContactApi(userDao, chatDao, messageDao);
        adapter.setMessageDao(messageDao);
        adapter.setChats(chatDao.index());
        // refresh initially
        SwipeRefreshLayout layout = findViewById(R.id.refreshLayout);
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
        // signing off
        signOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // reset the DB
                messageDao.resetTable();
                chatDao.resetTable();
                userDao.resetTable();
                // go to the main screen
                Intent i = new Intent(ChatsListActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

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