package com.example.androidapp.classes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userName;
    private String displayName;
    private String password;
    private String defaultServerAdr;

    public User(int id, String userName, String displayName, String password) {
        this.id = id;
        this.userName = userName;
        this.displayName = displayName;
        this.password = password;
        this.defaultServerAdr =  "10.0.2.2";
    }


    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDefaultServerAdr() {
        return defaultServerAdr;
    }

    public void setDefaultServerAdr(String defaultServerAdr) {
        this.defaultServerAdr = defaultServerAdr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}