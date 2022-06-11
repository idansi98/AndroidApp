package com.example.androidapp.classes;

public class ServerMessage {
    private String id;
    private String content;
    private String created;
    private Boolean sent;

    public ServerMessage(String id, String content, String created, Boolean sent) {
        this.id = id;
        this.content = content;
        this.created = created;
        this.sent = sent;
    }

    public ServerMessage() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }
}
