package com.example.iiatimd_app_project;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SettingRowEntry {

    @ColumnInfo
    private String activity;

    @PrimaryKey
    private int uuid;

    public SettingRowEntry(String activity, int uuid){
        this.activity = activity;
        this.uuid = uuid;
    }

    public String getActivity(){
        return this.activity;
    }

    public int getUuid() {
        return uuid;
    }
}
