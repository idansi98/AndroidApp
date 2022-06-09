package com.example.androidapp.classes;

import java.time.LocalDateTime;

public class Message {
    private final String text;
    private final LocalDateTime localDateTime;
    private final boolean userSent;

    public Message(String text, LocalDateTime localDateTime, boolean userSent) {
        this.text = text;
        this.localDateTime = localDateTime;
        this.userSent = userSent;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public boolean isUserSent() {
        return userSent;
    }

}