package com.example.iiatimd_app_project;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AgendaPuntAdapter extends RecyclerView.Adapter<AgendaPuntAdapter.AgendaPuntViewHolder> {
    private JSONArray agendaItems;

    public AgendaPuntAdapter(JSONArray data){
        this.agendaItems = data;
    }

    public static class AgendaPuntViewHolder extends RecyclerView.ViewHolder{
        public TextView date;
        public TextView description;
        public TextView dayOfWeek;

        public AgendaPuntViewHolder(View v){
            super(v);
            date = v.findViewById(R.id.textView__date);
            description = v.findViewById(R.id.textView__item);
            dayOfWeek = v.findViewById(R.id.textView__dayOfWeek);
        }
    }

    @NonNull
    @Override
    public AgendaPuntViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.agenda_card, parent, false);
        AgendaPuntViewHolder agendaPuntViewHolder = new AgendaPuntViewHolder(v);
        return agendaPuntViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AgendaPuntViewHolder holder, int position) {
        try {
            holder.date.setText(agendaItems.getJSONObject(position).getString("datum"));
            holder.description.setText(agendaItems.getJSONObject(position).getString("agenda_item"));

            String[] cardSplitDate = agendaItems.getJSONObject(position).getString("datum").split("-");
            //get day of the week to set in cardView
                //get date from response, make calender, parse split response date, convert it to date attr
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.valueOf(cardSplitDate[0]), Integer.valueOf(cardSplitDate[1])-1, Integer.valueOf(cardSplitDate[2]));
            Date cardDate = cal.getTime();
                //get day of week using sdf
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            String dayOfTheWeek = sdf.format(cardDate).toLowerCase();
            Log.d("datum", cardDate.toString());
            Log.d("datum-dag", dayOfTheWeek);
            holder.dayOfWeek.setText(dayOfTheWeek);

        } catch (JSONException e) {
            Log.d("viewholderException", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return agendaItems.length();
    }
}
