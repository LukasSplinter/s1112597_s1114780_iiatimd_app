package com.example.iiatimd_app_project;

public class insertActiviteitTask implements Runnable {

    AppDatabase db;
    Activiteit act;
    Boolean df;

    public insertActiviteitTask(AppDatabase db, Activiteit act, boolean deleteFirst){
        this.db = db;
        this.act = act;
        this.df = deleteFirst;
    }

    @Override
    public void run() {
        if (df){
            db.activiteitDAO().deleteFirst();
        }
        db.activiteitDAO().insertActiviteit(this.act);
    }
}
