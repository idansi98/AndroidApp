package com.example.androidapp.classes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Message {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String contactName;
    private String text;
    private long timeInMS;
    private boolean userSent;

    public Message(int id, String contactName, String text, long timeInMS, boolean userSent) {
        this.id = id;
        this.contactName = contactName;
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

    public int getId() {
        return id;
    }

    public String getContactName() {
        return contactName;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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
}

