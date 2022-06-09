package com.example.androidapp.classes;

import java.time.LocalDateTime;

public class Message {
    private String text;
    private LocalDateTime localDateTime;
    private boolean userSent;

    public Message(String text, LocalDateTime localDateTime, boolean userSent) {
        this.text = text;
        this.localDateTime = localDateTime;
        this.userSent = userSent;
    }
}
