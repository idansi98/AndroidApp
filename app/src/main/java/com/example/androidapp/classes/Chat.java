package com.example.androidapp.classes;

import java.util.ArrayList;

public class Chat {
    private ArrayList<Message> messages;
    private int id;

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public int getId() {
        return id;
    }

    public Chat(ArrayList<Message> messages, int id) {
        this.messages = messages;
        this.id = id;
    }
    private void addMessage(Message message) {
        messages.add(message);
    }

    private Message getLastMessage() {
        if (this.messages.size() != 0) {
            return this.messages.get(this.messages.size() - 1);
        }
        return null;
    }
}
