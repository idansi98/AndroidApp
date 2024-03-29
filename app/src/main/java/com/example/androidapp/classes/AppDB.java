package com.example.androidapp.classes;

import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {User.class, Chat.class, Message.class, EncodedImage.class}, version = 13)
public abstract class AppDB extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ChatDao chatDao();
    public abstract MessageDao messageDao();
    public abstract EncodedImageDao encodedImageDao();
}
