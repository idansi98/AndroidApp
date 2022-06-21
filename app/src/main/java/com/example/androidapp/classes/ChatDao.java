package com.example.androidapp.classes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChatDao {

    @Query("SELECT * FROM chat")
    List<Chat> index();
//    @Query("SELECT * FROM message WHERE timeInMS)
//    @Query("SELECT *  FROM chat ORDER BY lastDate DESC, id DESC")
//    List<Chat> getAllChats();
    @Query("SELECT * FROM chat WHERE userName = :userName")
    Chat get(String userName);
    @Insert
    void insert(Chat... chats);
    @Update
    void update(Chat... chats);
    @Delete
    void delete(Chat... chats);
    @Query("DELETE FROM chat")
    void resetTable();
}
