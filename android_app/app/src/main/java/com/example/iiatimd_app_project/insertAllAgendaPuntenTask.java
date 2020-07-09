package com.example.iiatimd_app_project;

import org.json.JSONArray;

import java.util.List;

public class insertAllAgendaPuntenTask implements Runnable {

    AppDatabase db;
    JSONArray agendaPunten;
    boolean deleteFirst;

    public insertAllAgendaPuntenTask(AppDatabase db, JSONArray agendaPunten, boolean deleteFirst){
        this.db = db;
        this.agendaPunten = agendaPunten;
        this.deleteFirst = deleteFirst;
    }

    @Override
    public void run() {
        if (deleteFirst){
            db.agendaDAO().deleteAll();
        }
        db.agendaDAO().insertAllAgendapunten((List<AgendaPunt>) agendaPunten);
    }
}
