package com.example.civildefence;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MyReportsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> reportsList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);

        listView = findViewById(R.id.list_view);

        // Toast: Simulating API call
        Toast.makeText(this,
                "API: GET /incidents?reported_by=me - Fetching user's reports",
                Toast.LENGTH_SHORT).show();

        // Sample data
        reportsList = new ArrayList<>();
        reportsList.add("🔥 Fire - Residential Building\nStatus: Resolved | 2026-04-01");
        reportsList.add("🚗 Traffic Accident - Highway\nStatus: Active | 2026-04-05");
        reportsList.add("🏥 Medical Emergency\nStatus: Closed | 2026-03-28");

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, reportsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MyReportsActivity.this,
                        "Opening Incident Detail\nAPI: GET /incidents/{id}",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}