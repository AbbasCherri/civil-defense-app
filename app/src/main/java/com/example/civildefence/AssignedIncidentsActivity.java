package com.example.civildefence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class AssignedIncidentsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> incidentsList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_incidents);

        listView = findViewById(R.id.list_view);

        Toast.makeText(this,
                "API: GET /incidents?assigned_to=me&status=active",
                Toast.LENGTH_SHORT).show();

        incidentsList = new ArrayList<>();
        incidentsList.add("🔥 INC-001: Fire - Downtown Building\nPriority: HIGH | Status: Active");
        incidentsList.add("🚑 INC-045: Medical Emergency - Highway 5\nPriority: CRITICAL | Status: En Route");
        incidentsList.add("🏗️ INC-078: Building Collapse - Industrial Area\nPriority: HIGH | Status: On Scene");

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, incidentsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AssignedIncidentsActivity.this,
                        "Opening Incident Detail (Responder View)\nAPI: GET /incidents/{id}",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AssignedIncidentsActivity.this, IncidentDetailActivity.class));
            }
        });
    }
}