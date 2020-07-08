package com.example.iiatimd_app_project;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AgendaPunt {

    @ColumnInfo
    private String omschrijving;
    @ColumnInfo
    private String datum;

    @PrimaryKey
    private int uuid;

    public AgendaPunt(String omschrijving, String datum, int uuid){
        this.omschrijving = omschrijving;
        this.datum = datum;
        this.uuid = uuid;
    }

    public String getOmschrijving(){
        return this.omschrijving;
    }

    public String getDatum(){
        return this.omschrijving;
    }

    public int getUuid(){
        return this.uuid;
    }
}
