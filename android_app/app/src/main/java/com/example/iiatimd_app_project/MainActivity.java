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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
        int currentYear = cal.get(Calendar.YEAR);

        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        /*
        * SWITCH URL LATER, DIT IS PUUR VOOR TIJDENS DEVELOPMENT WANT IK KAN NIET STUDIO EN MN API SAMEN RUNNEN :)))
        * */
//        final String url = "http://127.0.0.1:8000/api + "/activiteiten/random"";
        final String url = "https://pokeapi.co/api/v2/pokemon/";

        JsonObjectRequest activiteitRequest = new JsonObjectRequest
                (Request.Method.GET, (url + "ditto"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            activiteit.setText(response.getString("name"));
                            //todo: zet planning in room db - DONE
                            db.activiteitDAO().deleteFirst();
                            db.activiteitDAO().insertActiviteit(new Activiteit(response.getString("omschrijving"), 1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("api", error.toString());
                        //todo: als api call foutgaat, haal laatste planning uit room db - DONE
                        activiteit.setText(db.activiteitDAO().getFirst().getOmschrijving());
                    }
                });
        //request voor planning/agendapunten
        //url + dag/maand/jaar
        //todo: als ik api kan aanspreken: verander api naar onderstaand
        //(url + /agenda/ + currentDay + "/" + currentMonth + "/" + currentYear)
        JsonObjectRequest planningRequest = new JsonObjectRequest
                (Request.Method.GET, (url + "pikachu"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            planning.setText(response.getString("name"));

                            //todo: zet planning in room db - KAN PAS VERDER ALS API AANSPREEKBAAR IS
//                            db.agendaDAO().deleteFirst();
//                            db.agendaDAO().insertAgendaPunt(new AgendaPunt([[agenda-omschrijving]], [[agenda-datum]], 1));
                        } catch (JSONException e) {
                            //if no json gets returned, there are no pointers for that day, so we can replace the string with "niks vandaag"
                            planning.setText("Niks vandaag");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("api", error.toString());
                        //todo: als api call foutgaat, haal laatste planning uit room db - DONE (DENKIK, TESTEN!)
                        planning.setText(db.agendaDAO().getFirst().getOmschrijving());
                    }
                });

        VolleySingleton.getInstance(this).addToRequestQueue(activiteitRequest);
        VolleySingleton.getInstance(this).addToRequestQueue(planningRequest);



    }
}
