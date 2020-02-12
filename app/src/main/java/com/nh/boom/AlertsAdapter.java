package com.nh.boom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
import com.nh.boom.http.AlertClientTask;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Alert> alerts = new ArrayList<>();
    private Map<String, Alert> alertMap = new TreeMap<>();
    Map<String, List<DataSnapshot>> dataSnapshotMap = new HashMap<>();
    private DataSnapshot data;
    private Gson gson = new Gson();

    public void setAlert(Map<String, Alert> alerts) {
        this.alertMap = alerts;
        this.alerts.clear();
        this.alerts.addAll(alerts.values());
    }

    public DataSnapshot getData() {
        return data;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public Map<String, Alert> getAlertMap() {
        return alertMap;
    }

    public void setAlertMap(Map<String, Alert> alertMap) {
        this.alertMap = alertMap;
    }

    public Map<String, List<DataSnapshot>> getDataSnapshotMap() {
        return dataSnapshotMap;
    }

    public void setDataSnapshotMap(Map<String, List<DataSnapshot>> dataSnapshotMap) {
        this.dataSnapshotMap = dataSnapshotMap;
    }

    public void setData(DataSnapshot data) {
        this.data = data;
        dataSnapshotMap.clear();
        alertMap.clear();
        alerts.clear();
        for (DataSnapshot child : data.getChildren()) {
            Log.i("TAG", child.getKey() + " - " + child.getValue());
            final Alert alert = child.getValue(Alert.class);
            final String entityId = alert.getEntityId();
            alertMap.put(alert.getEntityId(), alert);

            // set data to data snapshot
            if (dataSnapshotMap.containsKey(entityId)) {
                dataSnapshotMap.get(entityId).add(child);
            } else {
                final List<DataSnapshot> dataSnapshots = new ArrayList<>();
                dataSnapshots.add(child);
                dataSnapshotMap.put(alert.getEntityId(), dataSnapshots);
            }
        }
        alerts.addAll(alertMap.values());
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
    public void onBindViewHolder(@NonNull final AlertsAdapter.MyViewHolder holder, int position) {
        final AlertsAdapter my = this;
        final Alert alert = alerts.get(position);
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
        if (alert.getSeverity().equals("warning")) {
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
                Log.i("Resolve", alert.getEntityId());
                new AlertClientTask(my, alert, AlertClientTask.Action.RESOLVE, "admin", "Nutanix.123").execute();
            }
        });
        holder.acknowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();
                new AlertClientTask(my, alert, AlertClientTask.Action.ACKNOWLEDGE, "admin", "Nutanix.123").execute();
            }
        });
        holder.fixIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();
                Toast.makeText(mContext, "fix it worked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }
}
