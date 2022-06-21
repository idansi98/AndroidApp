package com.example.androidapp.classes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Chat {

    @PrimaryKey (autoGenerate = true)
    private int id;
    private String userName;
    private String displayName;
    private String serverAdr;
    //private long  lastDate;

    public Chat(int id, String userName, String displayName, String serverAdr) {
        this.id = id;
        this.userName = userName;
        this.displayName = displayName;
        this.serverAdr = serverAdr;
    }

    public Chat() {
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

    public String getServerAdr() {
        return serverAdr;
    }

    public void setServerAdr(String serverAdr) {
        this.serverAdr = serverAdr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
