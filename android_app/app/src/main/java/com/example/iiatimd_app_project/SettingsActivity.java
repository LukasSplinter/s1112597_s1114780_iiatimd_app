package com.example.iiatimd_app_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private SettingRowAdapter mAdapter;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final ImageButton agendaButton = findViewById(R.id.agendaButton);
        final ImageButton homeButton = findViewById(R.id.homeButton);
        final Button addActivityButton = findViewById(R.id.toAddBtn);

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

        addActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(v.getContext(), addSettingRowActivity.class);
                startActivity(addIntent);
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
                settingRowEntries.add(new SettingRowEntry("3x15 push-ups", 2));
                settingRowEntries.add(new SettingRowEntry("Boek lezen", 3));
                settingRowEntries.add(new SettingRowEntry("Brief schrijven voor Opa", 4));
                settingRowEntries.add(new SettingRowEntry("Kamer schoon maken", 5));
                mAdapter.notifyDataSetChanged();
            }
        });
        jsonObjectRequest1.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest1);

        mAdapter = new SettingRowAdapter(settingRowEntries);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new SettingRowAdapter.onItemClickListener() {
            @Override
            public void onDeleteClick(final int position) {

                final String settingPosistion = String.valueOf(position);

                StringRequest jsonArrayRequestDelete = new StringRequest(Request.Method.POST, "https://dey-iiatimd.herokuapp.com/api/activiteiten/delete",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                settingRowEntries.remove(position);
                                mAdapter.notifyDataSetChanged();
                                Log.d("deleteWerkt", "We outa here");
                                for (int i = 0; i<settingRowEntries.size(); i++){
                                    Log.d("wat", settingRowEntries.toString());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("deleteWerktNiet", "Stay a while and listen");
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("position", settingPosistion);

                        return params;
                    }
                };
                jsonArrayRequestDelete.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequestDelete);
            }
        });
    }
}
