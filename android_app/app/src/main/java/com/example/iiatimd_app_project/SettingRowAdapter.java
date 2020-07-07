package com.example.iiatimd_app_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class SettingRowAdapter extends RecyclerView.Adapter<SettingRowAdapter.SettingRowViewHolder> {

    private SettingRowEntry[] activities;

    public SettingRowAdapter(SettingRowEntry[] activities){
        this.activities = activities;
    }

    public static class SettingRowViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;

        public SettingRowViewHolder(View v){
            super(v);
            textView = v.findViewById(R.id.activity);
        }
    }

    @NonNull
    @Override
    public SettingRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_row, parent, false);
        SettingRowViewHolder settingRowViewHolder = new SettingRowViewHolder(v);
        return settingRowViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SettingRowViewHolder holder, int position) {
        holder.textView.setText(activities[position].getActivity());
    }

    @Override
    public int getItemCount() {
        return activities.length;
    }


}
