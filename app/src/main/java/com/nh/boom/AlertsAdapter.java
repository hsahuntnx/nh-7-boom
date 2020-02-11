package com.nh.boom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Alert> alerts = new ArrayList<>();

    public void setAlert(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView desc;
        public Button resolve;
        public Button acknowledge;
        public TextView entity;
        public com.veinhorn.tagview.TagView tagView;

        public MyViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            desc = v.findViewById(R.id.desc);
            resolve = v.findViewById(R.id.resolve);
            acknowledge = v.findViewById(R.id.acknowledge);
            tagView = v.findViewById(R.id.tagView);
            entity = v.findViewById(R.id.entity_id);
        }
    }

    public AlertsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AlertsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlertsAdapter.MyViewHolder holder, int position) {
        Alert alert = alerts.get(position);
        holder.title.setText(alert.getTitle());
        holder.desc.setText(alert.getDescription());
        if (alert.isAcknowledged()) {
            holder.acknowledge.setClickable(false);
        }
        if (alert.isResolve()) {
            holder.resolve.setClickable(false);
        }
        holder.tagView.setText(alert.getSeverity());
        holder.entity.setText(alert.getEntityId());

        if (alert.getSeverity().toLowerCase().equals("critical")) {
            holder.tagView.setTagColor(R.color.colorAccent);
        } else if (alert.getSeverity().toLowerCase().equals("warn")) {
            holder.tagView.setTagColor(R.color.design_default_color_primary_dark);
        } else {
            holder.tagView.setTagColor(R.color.colorPrimary);
        }
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }
}
