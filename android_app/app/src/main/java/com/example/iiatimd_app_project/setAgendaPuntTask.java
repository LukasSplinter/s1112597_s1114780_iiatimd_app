package com.example.iiatimd_app_project;

import android.widget.TextView;

public class setAgendaPuntTask implements Runnable {
    AppDatabase db;
    TextView tv;

    public setAgendaPuntTask(AppDatabase db, TextView tv){
        this.db = db;
        this.tv = tv;
    }

    @Override
    public void run() {
        tv.setText(db.agendaDAO().getFirst().getOmschrijving());
    }
}
