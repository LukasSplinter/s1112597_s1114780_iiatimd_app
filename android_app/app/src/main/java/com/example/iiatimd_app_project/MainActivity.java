package com.example.iiatimd_app_project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageButton agendaButton = findViewById(R.id.agendaButton);
        final ImageButton settingsButton = findViewById(R.id.settingsButton);

        final TextView greeting = findViewById(R.id.textView__greeting);
        final TextView today = findViewById(R.id.textview__vandaag);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(v.getContext(), SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });

        agendaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agendaIntent = new Intent(v.getContext(), AgendaActivity.class);
                startActivity(agendaIntent);
            }
        });

        //change greeting according to time of day:
            //get current hour
        LocalTime currentTime = java.time.LocalTime.now();
        int currentHour = Integer.parseInt(currentTime.toString().split(":")[0]);
            //change textview greeting according to value of currentHour
        if (currentHour > 0 && currentHour < 12){
            greeting.setText(getString(R.string.groet_ochtend));
        }else if (currentHour > 12 && currentHour < 18){
            greeting.setText(getString(R.string.groet_middag));
        }else{
            greeting.setText(getString(R.string.groet_avond));
        }

        //change textview 'vandaag' to day of the week
            //get date
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
            //get day of the week
        String dayOfTheWeek = sdf.format(d);
            //set textview to said date of the week
            //dutch days because java simpledateformat doesnt have dutch yet because idfk :(((
            //this is horrible but bite me im tired
        switch(dayOfTheWeek.toLowerCase()){
            case "monday":
                today.setText("maandag");
                break;
            case "tuesday":
                today.setText("dinsdag");
                break;
            case "wednesday":
                today.setText("woensdag");
                break;
            case "thursday":
                today.setText("donderdag");
                break;
            case "friday":
                today.setText("vrijdag");
                break;
            case "saturday":
                today.setText("zaterdag");
                break;
            case "sunday":
                today.setText("zondag");
                break;
        }
    }
}
