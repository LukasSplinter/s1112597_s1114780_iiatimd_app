package com.example.iiatimd_app_project;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Activiteit {
    @ColumnInfo
    private String omschrijving;

    @PrimaryKey
    private int uuid;

    public Activiteit(String omschrijving, int uuid){
        this.omschrijving = omschrijving;
        this.uuid = uuid;
    }

    public int getUuid() {
        return uuid;
    }

    public String getOmschrijving() {
        return omschrijving;
    }
}
