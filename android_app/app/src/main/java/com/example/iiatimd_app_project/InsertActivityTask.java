package com.example.iiatimd_app_project;

import android.util.Log;

public class InsertActivityTask implements Runnable{

    AppDatabase db;
    SettingRowEntry settingRowEntry;

    public InsertActivityTask(AppDatabase db, SettingRowEntry settingRowEntry){
        this.db = db;
        this.settingRowEntry = settingRowEntry;
    }

    @Override
    public void run(){
        db.settingRowEntryDAO().insertEntry(this.settingRowEntry);
        String insert = db.settingRowEntryDAO().getAll().get(0).getActivity();
        Log.d("insertTask", insert);
    }
}
