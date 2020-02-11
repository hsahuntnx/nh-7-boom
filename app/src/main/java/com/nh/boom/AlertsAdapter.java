package com.nh.boom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        public TextView severity;
        public Button fixIt;

        public MyViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            desc = v.findViewById(R.id.desc);
            resolve = v.findViewById(R.id.resolve);
            acknowledge = v.findViewById(R.id.acknowledge);
            entity = v.findViewById(R.id.entity_id);
            severity = v.findViewById(R.id.severity);
            fixIt = v.findViewById(R.id.fixIt);
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
        holder.entity.setText(alert.getEntityId());
        if (alert.getSeverity().equals("critical")) {
            holder.severity.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF731C")));
            holder.severity.setText(" Critical ");
        }
        if (alert.getSeverity().equals("warn")) {
            holder.severity.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EDAD1F")));
            holder.severity.setText("Warning");

        }
        if (alert.getSeverity().equals("info")) {
            holder.severity.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#AFD135")));
            holder.severity.setText("     Info     ");
        }
        holder.resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "resolve worked", Toast.LENGTH_SHORT).show();
            }
        });
        holder.acknowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "ack worked", Toast.LENGTH_SHORT).show();
            }
        });
        holder.fixIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "fix it worked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }
}
