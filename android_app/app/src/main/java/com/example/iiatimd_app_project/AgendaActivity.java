package com.example.iiatimd_app_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AgendaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        final ImageButton homeButton = findViewById(R.id.homeButton);
        final ImageButton settingsButton = findViewById(R.id.settingsButton);

        final Button addButton = findViewById(R.id.add_button);

        //navigation
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(v.getContext(), MainActivity.class);
                startActivity(homeIntent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(v.getContext(), SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });

        //button to redirect to add agenda page
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Intent addAgendaIntent = new Intent(v.getContext(), AddAgenda.class);
                startActivity(addAgendaIntent);
            }
        });

        //create db
        final AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        //array for saving agenda items later on;
        JSONArray agendaItems;

        //get day/year/month for api call
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        Date d = new Date();
        cal.setTime(d);
        final int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        final int currentMonth = cal.get(Calendar.MONTH);
        final int currentYear = cal.get(Calendar.YEAR);

        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String url = "https://dey-iiatimd.herokuapp.com/api/";

        JsonArrayRequest planningRequest = new JsonArrayRequest
                (Request.Method.GET, (url + "agenda/" + currentDay + "/" + (currentMonth + 1) + "/" + currentYear), null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("api", response.toString());
                        new Thread(new insertAllAgendaPuntenTask(db, response, true));
                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new Thread(new getAllAgendaPuntenTask(db));
                    }
                });
        planningRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 20000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 20000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {}
        });

        VolleySingleton.getInstance(this).addToRequestQueue(planningRequest);
    }

}
