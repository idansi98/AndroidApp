package com.example.androidapp.classes;

import java.util.ArrayList;

public class User {
    private final String userName;
    private final String displayName;
    private final String password;
    private final String defaultServerAdr;
    private final ArrayList<Chat> chats;

    public User(String userName, String displayName, String password) {
        this.userName = userName;
        this.displayName = displayName;
        this.password = password;
        this.chats = new ArrayList<Chat>();
        this.defaultServerAdr =  "10.0.2.2";
    }

    private void addChat(Chat chat) {
        chats.add(chat);
    }

    private Chat searchChat(String userName) {
        for (Chat chat: this.chats) {
            if (chat.getUserName() == userName) {
                return chat;
            }
        }
        return null;
    }
    public String getUserName() {
        return userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPassword() {
        return password;
    }

    public String getDefaultServerAdr() {
        return defaultServerAdr;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }
/*
    private void sendMessage(String to, String text) {
    }*/

}