package com.example.iiatimd_app_project;

import android.util.Log;

public class getActivitiesTask implements Runnable {

    AppDatabase db;
    public getActivitiesTask(AppDatabase db){
        this.db = db;
    }
    @Override
    public void run(){
        String name = db.settingRowEntryDAO().getAll().get(0).getActivity();
        Log.d("TaskTest", name);
    }
}
