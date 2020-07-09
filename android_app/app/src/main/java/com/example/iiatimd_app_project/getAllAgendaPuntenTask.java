package com.example.iiatimd_app_project;

import android.util.Log;

public class getAllAgendaPuntenTask implements Runnable {

    AppDatabase db;

    public getAllAgendaPuntenTask(AppDatabase db){
        this.db = db;
    }


    @Override
    public void run() {
        Log.d("agenda", db.agendaDAO().getAll().toString());
    }
}
