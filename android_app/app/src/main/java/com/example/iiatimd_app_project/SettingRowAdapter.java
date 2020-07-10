package com.example.iiatimd_app_project;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SettingRowAdapter extends RecyclerView.Adapter<SettingRowAdapter.SettingRowViewHolder> {

    private ArrayList<SettingRowEntry> activities;
    private onItemClickListener mListener;

    public interface onItemClickListener{
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
        Log.d("listener", String.valueOf(mListener));
    }

    public SettingRowAdapter(ArrayList<SettingRowEntry> activities){
        this.activities = activities;
    }

    public static class SettingRowViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public ImageButton deleteButton;

        public SettingRowViewHolder(View v, final onItemClickListener listener){
            super(v);
            textView = v.findViewById(R.id.activity);
            deleteButton = v.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                    Log.d("frickYou", String.valueOf(listener));
                }
            });
        }
    }

    @NonNull
    @Override
    public SettingRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_row, parent, false);
        return new SettingRowViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingRowViewHolder holder, int position) {
        holder.textView.setText(activities.get(position).getActivity());
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }




}
