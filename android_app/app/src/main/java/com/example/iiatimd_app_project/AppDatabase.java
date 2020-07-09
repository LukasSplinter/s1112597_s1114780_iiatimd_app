package com.example.iiatimd_app_project;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {AgendaPunt.class, Activiteit.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AgendaDAO agendaDAO();
    public abstract ActiviteitDAO activiteitDAO();

    private static AppDatabase instance;

    static synchronized AppDatabase getInstance(Context context){
        if (instance == null){
            instance = create(context);
        }
        return instance;
    }

    private static AppDatabase create(final Context context){
        return Room.databaseBuilder(context,AppDatabase.class, "iiatimd").fallbackToDestructiveMigration().build();
    }
}
