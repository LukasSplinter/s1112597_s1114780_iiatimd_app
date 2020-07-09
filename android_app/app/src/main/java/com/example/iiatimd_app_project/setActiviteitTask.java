package com.example.iiatimd_app_project;

import android.widget.TextView;

public class setActiviteitTask implements Runnable{
    AppDatabase db;
    TextView tv;

    public setActiviteitTask(AppDatabase db, TextView tv){
        this.db = db;
        this.tv = tv;
    }

    @Override
    public void run() {
        tv.setText(db.activiteitDAO().getFirst().getOmschrijving());
    }
}
