package com.example.androidapp.classes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MessageDao {

    @Query("SELECT * FROM message")
    List<Message> index();

    @Query("SELECT * FROM message WHERE userID = :userID")
    List<Message> get(String userID);
    @Insert
    void insert(Message... message);
    @Update
    void update(Message... message);
    @Delete
    void delete(Message... message);
}
