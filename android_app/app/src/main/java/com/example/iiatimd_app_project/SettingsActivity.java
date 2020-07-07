package com.example.iiatimd_app_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class SettingsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final ImageButton agendaButton = findViewById(R.id.agendaButton);
        final ImageButton homeButton = findViewById(R.id.homeButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(v.getContext(), MainActivity.class);
                startActivity(homeIntent);
            }
        });

        agendaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agendaIntent = new Intent(v.getContext(), AgendaActivity.class);
                startActivity(agendaIntent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();

        SettingRowEntry[] settingRowEntries = new SettingRowEntry[5];
        settingRowEntries[0] = new SettingRowEntry("Rondje lopen", 1);
        settingRowEntries[1] = new SettingRowEntry("3x15 push-ups", 2);
        settingRowEntries[2] = new SettingRowEntry("Boek lezen", 3);
        settingRowEntries[3] = new SettingRowEntry("Brief schrijven voor Opa", 4);
        settingRowEntries[4] = new SettingRowEntry("Kamer schoon maken", 5);

        recyclerViewAdapter = new SettingRowAdapter(settingRowEntries);
        recyclerView.setAdapter(recyclerViewAdapter);

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        new Thread(new InsertActivityTask(db, settingRowEntries[4])).start();
        //new Thread(new getActivitiesTask(db)).start();
    }
}
