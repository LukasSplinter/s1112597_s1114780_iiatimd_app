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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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

        final SettingRowEntry[] settingRowEntries = new SettingRowEntry[5];
        settingRowEntries[0] = new SettingRowEntry("Rondje lopen", 1);
        settingRowEntries[1] = new SettingRowEntry("3x15 push-ups", 2);
        settingRowEntries[2] = new SettingRowEntry("Boek lezen", 3);
        settingRowEntries[3] = new SettingRowEntry("Brief schrijven voor Opa", 4);
        settingRowEntries[4] = new SettingRowEntry("Kamer schoon maken", 5);

        JSONObject jsonObject = new JSONObject();
        try {
            for (int i =0; i < settingRowEntries.length; i++) {
                jsonObject.put("activity", settingRowEntries[i].getActivity());
                jsonObject.put("id", settingRowEntries[i].getUuid());
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://10.0.2.2:8000/api/activiteiten/update", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("lukt", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("lukt niet", error.getMessage());
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        recyclerViewAdapter = new SettingRowAdapter(settingRowEntries);
        recyclerView.setAdapter(recyclerViewAdapter);

//        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
//
//        new Thread(new InsertActivityTask(db, settingRowEntries[4])).start();
//        //new Thread(new getActivitiesTask(db)).start();
    }
}
