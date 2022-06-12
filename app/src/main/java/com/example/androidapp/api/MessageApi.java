package com.example.androidapp.api;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.MyApplication;
import com.example.androidapp.R;
import com.example.androidapp.classes.Chat;
import com.example.androidapp.classes.ChatDao;
import com.example.androidapp.classes.Message;
import com.example.androidapp.classes.MessageDao;
import com.example.androidapp.classes.ServerContact;
import com.example.androidapp.classes.ServerMessage;
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

public class MessageApi {
    private MutableLiveData<List<ServerContact>> contactListData;
    private UserDao userDao;
    private ChatDao chatDao;
    private MessageDao messageDao;

    Retrofit retrofit;
    MessageServiceApi messageServiceApi;
    private final OkHttpClient client;

    public MessageApi(UserDao userDao, ChatDao chatDao ,MessageDao messageDao) {
        this.userDao = userDao;
        this.chatDao = chatDao;
        this.messageDao = messageDao;

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

        messageServiceApi = retrofit.create(MessageServiceApi.class);
    }

    public void get(String contactID) {
        Chat chat = chatDao.get(contactID);
        if (chat == null) {
            Log.d("API_LOGGING", "Didn't find contact");
            return;
        }
        // login using the new session
        tryLogin();
        // get all the messages of the contact
        Call<List<ServerMessage>> call = messageServiceApi.getAllMessages(contactID);
        call.enqueue(new Callback<List<ServerMessage>>() {
            @Override
            public void onResponse(@NonNull Call<List<ServerMessage>> call, @NonNull Response<List<ServerMessage>> response) {
                List<ServerMessage> messages = response.body();
                assert messages != null;
                List<Message> pastMessages = messageDao.get(chat.getUserName());
                for (ServerMessage message : messages) {
                    long timeCalculated = convertToTimeFormat(message.getCreated());
                    boolean toUpdate = false;
                    Message convertedMessage = new Message(message.getId(),chat.getUserName(),message.getContent(), timeCalculated, message.getSent());
                    for (Message pastMessage: pastMessages) {
                        if (pastMessage.getId().equals(message.getId())) {
                            toUpdate = true;
                            break;
                        }
                    }
                    if (toUpdate) {
                        messageDao.update(convertedMessage);
                    } else {
                        messageDao.insert(convertedMessage);
                    }


                }

            }

            @Override
            public void onFailure(@NonNull Call<List<ServerMessage>> contact, @NonNull Throwable t){
                Log.d("API_LOGGING", "Failure / no messages\t Error code: " + t.getMessage());
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

    private long convertToTimeFormat(String str) {
        return 0;
//        try {
//            // TODO: fix the time thing
////            String pattern = "yyyy-MM-ddTHH:mm:ss.fffffff";
////            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
////            Date date = simpleDateFormat.parse(str);
////            return date.getTime();
//        } catch (Exception ex) {
//            Log.d("API_LOGGING","Exception at converting time "+ ex.getMessage());
//            return 0;
//        }

    }
}
