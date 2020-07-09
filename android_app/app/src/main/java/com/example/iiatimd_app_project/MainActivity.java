package com.example.iiatimd_app_project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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

        final TextView activiteit = findViewById(R.id.textview__activiteit);
        final TextView planning = findViewById(R.id.textview__planning);

        final TextView meerInPlanning = findViewById(R.id.textview__meerInPlanning);

        //create db
        final AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        //navigation buttons
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
        String dayOfTheWeek = sdf.format(d).toLowerCase();
            //set textview to said date of the week
            //dutch days because java simpledateformat doesnt have dutch
        //done by getting the string resource name with varuable dayOfTheWeek
        today.setText(getString(getResources().getIdentifier(dayOfTheWeek, "string", getPackageName())));

        //get day/year/month for api call
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(d);
        final int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        final int currentMonth = cal.get(Calendar.MONTH);
        final int currentYear = cal.get(Calendar.YEAR);

        //define and assign requestqueue from VolleySingleton
        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String url = "https://dey-iiatimd.herokuapp.com/api/";

        JsonObjectRequest activiteitRequest = new JsonObjectRequest
                (Request.Method.GET, (url + "activiteiten/random"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("api", "got response");
                            //set activity text to "activiteit" textview
                            activiteit.setText(response.getString("activiteit_omschrijving"));

                            //push activity to room db
                            Activiteit act = new Activiteit(response.getString("activiteit_omschrijving"), 1);
                            new Thread(new insertActiviteitTask(db, act, true)).start();

                            //exception
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //exception
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //in case of timeout or other exceptions, load activity saved in room db
                        new Thread(new setActiviteitTask(db, activiteit)).start();
                    }
                });
        //request for agenda items
        //url + day/month/year
        JsonArrayRequest planningRequest = new JsonArrayRequest
                (Request.Method.GET, (url + "agenda/" + currentDay + "/" + (currentMonth + 1) + "/" + currentYear), null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //if size of reponse array == 0 > no activities for today
                            if (response.length() == 0){
                                planning.setText(getString(R.string.niksVandaag));
                            }else{
                                //if there are more than 1 items for the day, set a banner which signals to user that there are more items in "planning" page
                                if (response.length() > 1){
                                    meerInPlanning.setVisibility(View.VISIBLE);
                                }
                                //set item text to planning textview
                                planning.setText(response.getJSONObject(0).getString("agenda_item"));

                                //push activity to room db
                                AgendaPunt ap = new AgendaPunt(response.getJSONObject(0).getString("agenda_item"),
                                        response.getJSONObject(0).getString("datum"),
                                        1);
                                new Thread(new InsertAgendaPuntTask(db, ap, true)).start();
                            }


                            //exception
                        } catch (Exception e) {
                            Log.e("api-agenda-error", e.toString());
                        }
                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //in case of an exception, load last saved item into "planning" textview
                        new Thread(new setAgendaPuntTask(db, planning)).start();
                    }
                });

        //retrypolicies increased because the api is quite ((really damn)) slow, to prevent early timeout.
        activiteitRequest.setRetryPolicy(new RetryPolicy() {
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

        VolleySingleton.getInstance(this).addToRequestQueue(activiteitRequest);
        VolleySingleton.getInstance(this).addToRequestQueue(planningRequest);



    }
}
