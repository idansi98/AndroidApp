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
import com.example.androidapp.classes.User;
import com.example.androidapp.classes.UserDao;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
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
                                    .url("http://10.0.2.2:25565/api/firebase")
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
    private void tryLogin(OkHttpClient client)  {
        try {
            User user = userDao.index().get(0);
            String json = "{\"username\":\""+user.getUserName()+"\",\"password\":\""+user.getPassword()+"\"}";

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"), json);

            Request request = new Request.Builder()
                    .url("http://10.0.2.2:25565/api/login")
                    .post(body)
                    .build();

            okhttp3.Call call = client.newCall(request);
            okhttp3.Response response = call.execute();
            Log.d("API_LOGGING","response body:" + response.body().string());
        } catch (IOException ex) {
            Log.e("API_LOGGING",ex.getMessage());
        }

    }

}