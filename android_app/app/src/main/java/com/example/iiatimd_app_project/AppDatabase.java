package com.example.iiatimd_app_project;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {AgendaPunt.class, Activiteit.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AgendaDAO agendaDAO();
    public abstract ActiviteitDAO activiteitDAO();
}
