package com.example.androidapp.classes;

import java.util.ArrayList;

public class Chat {
    private final String userName; // is the key but if doesn't work can use a new key field
    private final String displayName;
    private final String serverAdr;
    private final ArrayList<Message> messages;

    public Chat(String userName, String displayName, String serverAdr) {
        this.userName = userName;
        this.displayName = displayName;
        this.serverAdr = serverAdr;
        this.messages = new ArrayList<>();
    }
    private void addMessage(Message message) {
        messages.add(message);
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }
    private Message getLastMessage() {
        if (this.messages.size() != 0) {
            return this.messages.get(this.messages.size() - 1);
        }
        return null;
    }
    public String getUserName() {
        return userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getServerAdr() {
        return serverAdr;
    }
}
