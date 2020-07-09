package com.example.iiatimd_app_project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Locale;
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

        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "iiatimd").build();

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

        //get day/year/month for api call
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(d);
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        int currentMonth = cal.get(Calendar.MONTH);
        final int currentYear = cal.get(Calendar.YEAR);

        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String url = "https://dey-iiatimd.herokuapp.com/api/";

        Log.d("api", (url + "activiteiten/random"));
        JsonObjectRequest activiteitRequest = new JsonObjectRequest
                (Request.Method.GET, (url + "activiteiten/random"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("api-act", response.toString());
                            activiteit.setText(response.getString("activiteit_omschrijving"));
                            //todo: zet planning in room db - DONE
//                            db.activiteitDAO().deleteFirst();
//                            db.activiteitDAO().insertActiviteit(new Activiteit(response.getString("activiteit_omschrijving"), 1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("api-act-error", error.toString());
                        //todo: als api call foutgaat, haal laatste planning uit room db - DONE
//                        activiteit.setText(db.activiteitDAO().getFirst().getOmschrijving());
                    }
                });
        //request voor planning/agendapunten
        //url + dag/maand/jaar
        JsonArrayRequest planningRequest = new JsonArrayRequest
                (Request.Method.GET, (url + "agenda/" + currentDay + "/" + (currentMonth + 1) + "/" + currentYear), null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("api-agenda", response.toString());
                            //if size of reponse array == 0 > no activities for today
                            if (response.length() == 0){
                                planning.setText("Niks vandaag");
                            }else{
                                planning.setText(response.getJSONObject(0).getString("agenda_item"));
                            }
                            //todo: zet planning in room db - KAN PAS VERDER ALS API AANSPREEKBAAR IS
//                            db.agendaDAO().deleteFirst();
//                            db.agendaDAO().insertAgendaPunt(new AgendaPunt([[agenda-omschrijving]], [[agenda-datum]], 1));
                        } catch (Exception e) {
                            Log.e("api-agenda-error", e.toString());
                        }
                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("api-agenda-error", error.toString());
                        //todo: als api call foutgaat, haal laatste planning uit room db - DONE (DENKIK, TESTEN!)
//                        planning.setText(db.agendaDAO().getFirst().getOmschrijving());
                    }
                });
        activiteitRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        planningRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(activiteitRequest);
        VolleySingleton.getInstance(this).addToRequestQueue(planningRequest);



    }
}
