package com.example.iiatimd_app_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    //NOTE:
    // ja ik weet dat er flink wat dingen dubbel instaan, komt omdat api eerst heel kut deed met post requests, en het hosten te rot was ze allemaal weg te halen lol
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.page_intro, R.anim.page_outro);
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

        //loading symbol and animation for during api loading
        final ImageView loading = findViewById(R.id.loading);
        Animation rotating = AnimationUtils.loadAnimation(this, R.anim.rotation);
        loading.startAnimation(rotating);

        //create db
        final AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        //recyclerview for agenda items
        recyclerView = findViewById(R.id.agenda_points_container);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.hasFixedSize();

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
                (Request.Method.GET, (url + "agenda"), null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //set loading invis
                        loading.setAlpha(0f);
                        loading.setVisibility(View.GONE);

                        //insert response into room db
                        new Thread(new insertAllAgendaPuntenTask(db, response, true)).start();

                        //seed recyclerview
                        recyclerViewAdapter = new AgendaPuntAdapter(response);
                        recyclerView.setAdapter(recyclerViewAdapter);

                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //if api fails to get anything: load last saved agenda points to recycleview
                        new Thread(new getAllAgendaPuntenTask(db, recyclerViewAdapter, recyclerView)).start();
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
