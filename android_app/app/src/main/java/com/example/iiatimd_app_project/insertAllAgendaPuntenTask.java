package com.example.iiatimd_app_project;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

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
        for (int i = 0; i < agendaPunten.length(); i++){
            try {
                AgendaPunt agendaPunt = new AgendaPunt(agendaPunten.getJSONObject(i).getString("agenda_item"),
                        agendaPunten.getJSONObject(i).getString("datum"),
                        Integer.valueOf(agendaPunten.getJSONObject(i).getString("agenda_id")));

                db.agendaDAO().insertAgendaPunt(agendaPunt);

            } catch (JSONException e) {
                Log.e("agendaTaskError", e.getMessage());
            }
        }
    }
}
