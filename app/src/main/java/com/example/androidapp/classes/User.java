package com.example.androidapp.classes;

import java.util.ArrayList;

public class User {
    private String userName, displayName, password, picture;
    private ArrayList<Chat> chats ;
    public User(String userName, String displayName, String password, String picture) {
        this.userName = userName;
        this.displayName = displayName;
        this.password = password;
        this.picture = picture;
        this.chats = new ArrayList<Chat>();
    }

    private void addChat(Chat chat) {
        chats.add(chat);
    }

    private Chat searchChat(int id) {
        for (Chat chat: this.chats) {
            if (chat.getId() == id) {
                return chat;
            }
        }
        return null;
    }
/*
    private void sendMessage(String to, String text) {

    }*/

}
