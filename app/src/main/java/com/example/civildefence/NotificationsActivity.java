package com.example.civildefence;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> notificationsList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        listView = findViewById(R.id.list_view);

        Toast.makeText(this,
                "API: GET /notifications - Fetching system notifications",
                Toast.LENGTH_SHORT).show();

        notificationsList = new ArrayList<>();
        notificationsList.add("🔔 NEW ASSIGNMENT: INC-001 - Fire at Downtown Building\nPriority: HIGH | 14:30");
        notificationsList.add("📢 Team Update: Alpha Squad en route to INC-001\nETA: 5 minutes | 14:32");
        notificationsList.add("✅ Incident #INC-045 marked as Resolved\n14:15");

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, notificationsList);
        listView.setAdapter(adapter);
    }
}