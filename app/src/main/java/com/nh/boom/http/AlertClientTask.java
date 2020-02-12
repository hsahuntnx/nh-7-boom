package com.nh.boom.http;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.common.net.HttpHeaders;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
import com.nh.boom.Alert;
import com.nh.boom.AlertsAdapter;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AlertClientTask extends AsyncTask<Void, Void, String> {

    public enum Action {
        RESOLVE,
        ACKNOWLEDGE
    }

    private Alert alert;
    private String url = "http://10.36.240.196:9080/PrismGateway/services/rest/v1/alerts/%s";
    private static Gson gson = new Gson();
    private String username;
    private String password;
    private AlertsAdapter adapter;
    private Action action;

    public AlertClientTask(AlertsAdapter adapter, Alert alert, Action action, String username, String password) {
        this.adapter = adapter;
        this.alert = alert;
        this.action = action;
        this.url = String.format(url, action == Action.RESOLVE ? "resolve_list" : "acknowledge_list");
        this.username = username;
        this.password = password;
    }

    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        List<String> entityIds = new ArrayList<>();
        entityIds.add(alert.getEntityId());

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(entityIds));

        String basicAuth = null;
        try {
            basicAuth = Base64.getEncoder().encodeToString(String.format("%s:%s", username, password).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader(HttpHeaders.CONTENT_TYPE, mediaType.toString())
                .addHeader(HttpHeaders.AUTHORIZATION, "Basic " + basicAuth)
                .build();

        try {
            Response response = client.newCall(request).execute();
            Log.i("RESPONSE", Integer.toString(response.code()));

            if (action.equals(Action.RESOLVE)) {
                List<DataSnapshot> dataSnapshots =  adapter.getDataSnapshotMap().get(alert.getEntityId());
                if (dataSnapshots != null) {
                    for (final DataSnapshot dataSnapshot : dataSnapshots) {
                        dataSnapshot.getRef().removeValue();
                    }
                }
            }
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(adapter.getmContext(), action+ " done.", Toast.LENGTH_SHORT).show();
    }
}
