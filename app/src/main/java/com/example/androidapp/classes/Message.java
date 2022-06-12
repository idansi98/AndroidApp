package com.example.androidapp.classes;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Message {
    @PrimaryKey @NonNull
    private String id;
    private String userID;
    private String text;
    private long timeInMS;
    private boolean userSent;

    public Message(@NonNull String id, String userID, String text, long timeInMS, boolean userSent) {
        this.id = id;
        this.userID = userID;
        this.text = text;
        this.timeInMS = timeInMS;
        this.userSent = userSent;
    }

    public Message() {
    }

    public String getText() {
        return text;
    }


    public boolean isUserSent() {
        return userSent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setText(String text) {
        this.text = text;
    }


    public void setUserSent(boolean userSent) {
        this.userSent = userSent;
    }

    public long getTimeInMS() {
        return timeInMS;
    }

    public void setTimeInMS(long timeInMS) {
        this.timeInMS = timeInMS;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}

