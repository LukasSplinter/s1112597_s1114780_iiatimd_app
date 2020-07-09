package com.example.iiatimd_app_project;

public class InsertAgendaPuntTask implements Runnable {

    AppDatabase db;
    AgendaPunt ap;
    boolean df;

    public InsertAgendaPuntTask(AppDatabase db, AgendaPunt ap, boolean deleteFirst){
        this.db = db;
        this.ap = ap;
        this.df = deleteFirst;
    }

    @Override
    public void run() {
        if (df){
            db.agendaDAO().deleteFirst();
        }
        db.agendaDAO().insertAgendaPunt(this.ap);
    }
}
