package com.example.iiatimd_app_project;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ActiviteitDAO {

    @Query("SELECT * FROM Activiteit")
    List<Activiteit> getAll();

    @Insert
    void insertActiviteit(Activiteit activiteit);

    @Delete
    void delete(Activiteit activiteit);

    @Query("DELETE FROM Activiteit WHERE uuid = 1")
     void deleteFirst();

    @Query("SELECT * FROM Activiteit WHERE uuid = 1")
    Activiteit getFirst();
}
