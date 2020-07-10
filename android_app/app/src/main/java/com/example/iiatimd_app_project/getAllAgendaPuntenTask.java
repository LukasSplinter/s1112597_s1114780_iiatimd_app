package com.example.iiatimd_app_project;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class getAllAgendaPuntenTask implements Runnable {

    AppDatabase db;
    RecyclerView RV;
    RecyclerView.Adapter RVA;

    public getAllAgendaPuntenTask(AppDatabase db, RecyclerView.Adapter RVA, RecyclerView RV){
        this.db = db;
        this.RVA = RVA;
        this.RV = RV;
    }


    @Override
    public void run() {
        JSONArray jsonArray = new JSONArray();

        //convert agendapunten array to JSONarray so we can run the recyclerviewadapter
        List<AgendaPunt> agendaPunten = db.agendaDAO().getAll();
        for (int i = 0; i < agendaPunten.size(); i++){
            try {
                JSONObject agendapunt = new JSONObject();
                agendapunt.put("datum",agendaPunten.get(i).getDatum());
                agendapunt.put("agenda_item", agendaPunten.get(i).getOmschrijving());
                jsonArray.put(agendapunt);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        RVA = new AgendaPuntAdapter(jsonArray);
        RV.setAdapter(RVA);
    }
}
