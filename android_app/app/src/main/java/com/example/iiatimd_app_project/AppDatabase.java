package com.example.iiatimd_app_project;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SettingRowEntry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SettingRowEntryDAO settingRowEntryDAO();
}
