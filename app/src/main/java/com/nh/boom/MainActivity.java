package com.nh.boom;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlertsAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    Map<String, Integer> alertCount = new HashMap<>();

    private DatabaseReference mPostReference;
    private ValueEventListener mPostListener;
    private Toolbar toolbar;
    private Map<String, Alert> alerts = new HashMap<>();

    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onStart() {
        super.onStart();
        mPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                alertCount.put("critical", 0);
                alertCount.put("warning", 0);
                alertCount.put("info", 0);
                mAdapter.setData(dataSnapshot);
                for (Alert alert : mAdapter.getAlerts()) {
                    alerts.put(alert.getEntityId(), alert);
                    alertCount.put(alert.getSeverity(), alertCount.get(alert.getSeverity()) + 1);
                }
                mAdapter.notifyDataSetChanged();
                ((TextView)findViewById(R.id.fab_info)).setText(alertCount.get("info").toString());
                ((TextView)findViewById(R.id.fab_error)).setText(alertCount.get("critical").toString());
                ((TextView)findViewById(R.id.fab_warn)).setText(alertCount.get("warning").toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };

        mPostReference.addValueEventListener(mPostListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mPostReference.removeEventListener(mPostListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new AlertsAdapter(this);
        recyclerView.setAdapter(mAdapter);

        mPostReference = FirebaseDatabase.getInstance().getReference().child("alerts");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        SearchView search = findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                Map<String, Alert> alerts1 = new HashMap<>();
                for(Map.Entry<String, Alert> alertMap: alerts.entrySet()) {
                    if( alertMap.getValue().getTitle().contains(s) ) {
                        alerts1.put(alertMap.getValue().getEntityId(), alertMap.getValue());
                    }
                }
                mAdapter.setAlert(alerts1);
                mAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(mAdapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                if(s.trim().equals("")) {
                    mAdapter.setAlert(alerts);
                    mAdapter.notifyDataSetChanged();
                    mAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(mAdapter);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
