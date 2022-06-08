package com.example.androidapp.classes;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class LoginRunner implements Runnable {
    private volatile boolean result;
    private volatile String userName;
    private volatile String password;

    @Override
    public void run() {
        try {
            URL url = new URL("https://localhost:25565/api/login");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("content-type", "application/json");

            /* Payload support */
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes("{\"username\":\""+userName+"\",\"password\":\""+password+"\"}");
            Log.d("Fetch_Login", "{\"username\":\""+userName+"\",\"password\":\""+password+"\"}");
            out.flush();
            out.close();

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            result =  (status >= 200 && status < 300);
        }
        catch (Exception e) {
            Log.e("Fetch", e.toString());
            result =  false;
        }
    }

    public boolean getValue() {
        return result;
    }

    public void setValues(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
