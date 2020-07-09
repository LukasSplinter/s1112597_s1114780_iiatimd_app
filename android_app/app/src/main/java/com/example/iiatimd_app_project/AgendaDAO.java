package com.example.iiatimd_app_project;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import org.json.JSONArray;

import java.util.List;

@Dao
public interface AgendaDAO {

    @Query("DELETE FROM AgendaPunt")
    void deleteAll();

    @Insert
    void insertAllAgendapunten(List<AgendaPunt> agendaPunten);

    @Query("SELECT * FROM AgendaPunt")
    List<AgendaPunt> getAll();

    @Insert
    void insertAgendaPunt(AgendaPunt agendaPunt);

    @Delete
    void delete(AgendaPunt agendaPunt);

    @Query("DELETE FROM AgendaPunt WHERE uuid = 1")
    void deleteFirst();

    @Query("SELECT * FROM AgendaPunt WHERE uuid = 1")
    AgendaPunt getFirst();
}
