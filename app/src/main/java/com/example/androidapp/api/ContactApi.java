package com.example.androidapp.api;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.MyApplication;
import com.example.androidapp.R;
import com.example.androidapp.classes.Chat;
import com.example.androidapp.classes.ChatDao;
import com.example.androidapp.classes.ServerContact;
import com.example.androidapp.classes.User;
import com.example.androidapp.classes.UserDao;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactApi  {
    private MutableLiveData<List<ServerContact>> contactListData;
    private ChatDao chatDao;
    private UserDao userDao;

    Retrofit retrofit;
    ContactServiceApi contactServiceApi;
    private final OkHttpClient client;

    public ContactApi(UserDao userDao ,ChatDao chatDao) {
//        this.contactListData = contactListData;
        this.chatDao = chatDao;
        this.userDao = userDao;

        // init cookie manager
        CookieHandler cookieHandler = new CookieManager();

        // make a new session
        client = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        // create a retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        contactServiceApi = retrofit.create(ContactServiceApi.class);
    }

    public void get() {
        // login using the new session
        tryLogin();
        // get all the contacts
        Call<List<ServerContact>> call = contactServiceApi.getServerContacts();
        call.enqueue(new Callback<List<ServerContact>>() {
            @Override
            public void onResponse(@NonNull Call<List<ServerContact>> call, @NonNull Response<List<ServerContact>> response) {
                List<ServerContact> contacts = response.body();
                assert contacts != null;
                for (ServerContact contact : contacts) {
                    Chat chat = new Chat();
                    chat.setUserName(contact.getId());
                    chat.setDisplayName(contact.getName());
                    chat.setServerAdr(contact.getServer());
                    chatDao.insert(chat);
                    //TODO: Save the response into OurDB
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ServerContact>> contact, @NonNull Throwable t){
                Log.d("API_LOGGING", "Failure / no contacts\t Error code: " + t.getMessage());
            }
        });
    }

    // this method can be used in other API's
    private void tryLogin()  {
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
