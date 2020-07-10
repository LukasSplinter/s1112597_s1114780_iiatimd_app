package com.example.iiatimd_app_project;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SettingRowEntryDAO{

    @Query("SELECT * FROM SettingRowEntry")
    List<SettingRowEntry> getAll();

    @Insert
    void insertEntry(SettingRowEntry settingRowEntry);

    @Delete
    void delete(SettingRowEntry settingRowEntry);
}
