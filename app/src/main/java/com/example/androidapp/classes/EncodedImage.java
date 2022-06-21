package com.example.androidapp.classes;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EncodedImage {
    @PrimaryKey
    @NonNull
    private String contactName;
    private String uriString;

    public EncodedImage(String uriString, String contactName) {
        this.uriString = uriString;
        this.contactName = contactName;
    }

    public EncodedImage() {
    }


    public String getUriString() {
        return uriString;
    }

    public void setUriString(String uriString) {
        this.uriString = uriString;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}


