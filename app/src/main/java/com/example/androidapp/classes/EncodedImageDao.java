package com.example.androidapp.classes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EncodedImageDao {
    @Query("SELECT * FROM encodedimage")
    List<EncodedImage> index();
    @Query("SELECT * FROM encodedimage WHERE contactName = :contactName")
    List<EncodedImage> get(String contactName);
    @Insert
    void insert(EncodedImage... encodedImages);
    @Update
    void update(EncodedImage... encodedImages);
    @Delete
    void delete(EncodedImage... encodedImages);
    @Query("DELETE FROM encodedimage")
    void resetTable();
}
