package com.example.androidapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.androidapp.classes.AppDB;
import com.example.androidapp.classes.ChatDao;
import com.example.androidapp.classes.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddContactActivity extends AppCompatActivity {
    ImageView toChatsList;
    private AppDB db;
    private ChatDao chatDao;
    private Button addContact;
    private EditText nickName, userName, serverAddress;
    private ImageView backArrow;
    private String Nickname;
    private String ContactName;
    private String ServerAdr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "OurDB")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        chatDao = db.chatDao();
        nickName = findViewById(R.id.nickName);
        userName = findViewById(R.id.userName1);
        serverAddress = findViewById(R.id.serverAddress);
        backArrow = findViewById(R.id.backArrow2);
        addContact = findViewById(R.id.addContactButton);
        backArrow.setOnClickListener(v -> {
            Intent i = new Intent(this, ChatsListActivity.class);
            startActivity(i);
        });
        addContact.setOnClickListener(v ->  {
            Nickname = nickName.getText().toString();
            ContactName = userName.getText().toString();
            ServerAdr = serverAddress.getText().toString();
            AddContactActivity.ContactAddingTask task = new ContactAddingTask();
            task.execute();
        });
        //            chatDao.insert(new Chat(1, userName.getText().toString(),
//                    nickName.getText().toString(), serverAddress.getText().toString()))
    }
    private class ContactAddingTask extends AsyncTask<String, String, String> {
        private boolean taskSuccessful = false;
        @Override
        protected String doInBackground(String... params) {
                // same connection cookies
                CookieManager cookieManager = new CookieManager();
                CookieHandler.setDefault(cookieManager);

                User currentUser = db.userDao().index().get(0);


                // send the message to the other server
                try {

                    // TODO: check
                    String replaced = ServerAdr.replace("localhost","10.0.2.2");
                    String url_to_send  = "http://"+replaced+"/api/invitations";
                    URL url = new URL(url_to_send);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("content-type", "application/json");

                    /* Payload support */
                    con.setDoOutput(true);
                    DataOutputStream out = new DataOutputStream(con.getOutputStream());
                    out.writeBytes("{\"from\":\""+currentUser.getUserName()+"\",\"to\":\""+ContactName+"\",\"server\":\""+ServerAdr+"\"}");
                    out.flush();
                    out.close();

                    int status = con.getResponseCode();
                    if (status <200 || status >300) {
                        Log.d("AddContact_Logging", "Status returned in invite is an error: STATUS:" + status);
                        Log.d("AddContact_Logging", url.toString());
                        Log.d("AddContact_Logging", "{\"from\":\""+currentUser.getUserName()+"\",\"to\":\""+ContactName+"\",\"server\":\""+ServerAdr+"\"}");
                        return "idk";
                    }
                    con.disconnect();

                    // send the message to this server
                    String replaced2 = db.userDao().index().get(0).getDefaultServerAdr().replace("localhost","10.0.2.2");
                    String url_to_send2  = "http://"+ replaced2 + "/api/contacts";
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
                    out2.writeBytes("{\"id\":\""+ContactName+"\",\"name\":\""+Nickname+"\",\"server\":\""+ServerAdr+"\"}");
                    out2.flush();
                    out2.close();

                    int status2 = con2.getResponseCode();
                    if (status2 <200 || status2 >300) {
                        Log.d("AddContact_Logging", "Status returned in new contact is an error: STATUS:" + status2);
                        Log.d("AddContact_Logging", url2.toString());
                        Log.d("AddContact_Logging", "{\"id\":\""+ContactName+"\",\"name\":\""+Nickname+"\",\"server\":\""+ServerAdr+"\"}");
                        return "idk";
                    }
                    con2.disconnect();
                    taskSuccessful=true;
                }catch (Exception ex) {
                    Log.d("Chat_Logging", "Exception on transfer, error is: " + ex.getMessage());
                }
            return "idk";
        }
        @Override
        protected void onPostExecute(String result) {
            if(taskSuccessful) {
                Intent intent = new Intent(AddContactActivity.this, ChatsListActivity.class);
                startActivity(intent);
            }
        }
    }
    private void tryLogin() {
        try {
            User user = db.userDao().index().get(0);
            URL url = new URL("http://" + user.getDefaultServerAdr() + "/api/login");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("content-type", "application/json");

            /* Payload support */
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes("{\"username\":\"" + user.getUserName() + "\",\"password\":\"" + user.getPassword() + "\"}");
            out.flush();
            out.close();

            int status = con.getResponseCode();
        } catch (IOException ex) {
            Log.e("API_LOGGING", ex.getMessage());
        }
    }
}