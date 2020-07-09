package com.example.iiatimd_app_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private SettingRowAdapter mAdapter;

    AppDatabase db;

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


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();


        final ArrayList<SettingRowEntry> settingRowEntries = new ArrayList<>();



        JsonArrayRequest jsonObjectRequest1 = new JsonArrayRequest(Request.Method.GET, "https://dey-iiatimd.herokuapp.com/api/activiteiten", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                            Log.d("Getlukt", response.toString());
                            for (int i = 0; i<response.length(); i++){
                                try {
                                settingRowEntries.add(new SettingRowEntry((String) response.getJSONObject(i).get("activiteit_omschrijving"), (int) response.getJSONObject(i).get("activiteit_id")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("nope", error.getMessage() + "!");
                settingRowEntries.add(new SettingRowEntry("Rondje lopen", 1)) ;
                settingRowEntries.add (new SettingRowEntry("3x15 push-ups", 2));
                settingRowEntries.add (new SettingRowEntry("Boek lezen", 3));
                settingRowEntries.add ( new SettingRowEntry("Brief schrijven voor Opa", 4));
                settingRowEntries.add (new SettingRowEntry("Kamer schoon maken", 5));
                mAdapter.notifyDataSetChanged();
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest1);

        mAdapter = new SettingRowAdapter(settingRowEntries);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new SettingRowAdapter.onItemClickListener() {
            @Override
            public void onDeleteClick(final int position) {
                settingRowEntries.remove(position);
                mAdapter.notifyDataSetChanged();
                JsonArrayRequest jsonArrayRequestDelete = new JsonArrayRequest(Request.Method.GET, "https://dey-iiatimd.herokuapp.com/api/activiteiten/delete", null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d("deleteWerkt", "We outa here");
                                Log.d("wat", settingRowEntries.get(position).getActivity());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("deleteWerktNiet", "Stay a while and listen");
                            }
                        });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequestDelete);
            }
        });





//        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
//
//        new Thread(new InsertActivityTask(db, settingRowEntries[4])).start();
//        new Thread(new getActivitiesTask(db)).start();
    }
}
