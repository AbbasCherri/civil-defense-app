package com.example.civildefence;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class AlertsListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> alertsList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts_list);

        listView = findViewById(R.id.list_view);

        Toast.makeText(this,
                "API: GET /notifications - Fetching emergency alerts",
                Toast.LENGTH_SHORT).show();

        alertsList = new ArrayList<>();
        alertsList.add("🚨 CRITICAL: Wildfire spreading near residential area\nEvacuate immediately");
        alertsList.add("⚠️ HIGH: Flood warning - Stay away from low-lying areas");
        alertsList.add("📢 INFO: Scheduled emergency drill - 2026-04-15");

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, alertsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AlertsListActivity.this,
                        "Marking alert as read\nAPI: PATCH /notifications/{id}/read",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}