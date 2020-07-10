package com.example.iiatimd_app_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddAgenda extends AppCompatActivity {
    //bool to prevent double clicking > double sending post request
    boolean hasSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.page_intro, R.anim.page_outro);
        setContentView(R.layout.activity_add_agenda);

        //bool to prevent double clicking > double sending post request
        hasSent = false;

        Button backButton = findViewById(R.id.back_button);
        final Button saveButton = findViewById(R.id.save_button);

        final DatePicker inputDate = findViewById(R.id.input_date);
        final EditText inputText = findViewById(R.id.input_text);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agendaIntent = new Intent(v.getContext(), AgendaActivity.class);
                startActivity(agendaIntent);
            }
        });

        final RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String url = "https://dey-iiatimd.herokuapp.com/api/agenda/create";

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //prevent double click > double post request
                if (hasSent){return;}
                hasSent = true;
                //set innertext to "..." to notify user that request has been sent
                saveButton.setText("...");
                //pull and parse data from input forms
                final String itemDescription = inputText.getText().toString();
                final String itemDate = inputDate.getYear() + "-" + (inputDate.getMonth() + 1) + "-" + inputDate.getDayOfMonth();

                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            //runs on response from api for succesful post
                            @Override
                            public void onResponse(String response) {
                                //redirect user back to agenda page
                                Intent agendaIntent = new Intent(v.getContext(), AgendaActivity.class);
                                startActivity(agendaIntent);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            //exception
                            public void onErrorResponse(VolleyError error) {
                                Log.d("api", error.toString());
                            }
                        }
                ) {
                    //send data to api
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("datum", itemDate);
                        params.put("agenda_item", itemDescription);

                        return params;
                    }
                };
                postRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(postRequest);




            }
        });
    }

}
