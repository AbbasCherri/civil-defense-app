package com.example.civildefence;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LiveMapActivity extends AppCompatActivity {

    private Button btnRefresh, btnToggleRiskZones, btnToggleVehicles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_map);

        btnRefresh = findViewById(R.id.btn_refresh);
        btnToggleRiskZones = findViewById(R.id.btn_toggle_risk_zones);
        btnToggleVehicles = findViewById(R.id.btn_toggle_vehicles);

        Toast.makeText(this,
                "API: GET /vehicles/locations\nAPI: GET /incidents?active=true\nLoading map with real-time data...",
                Toast.LENGTH_LONG).show();

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LiveMapActivity.this,
                        "Refreshing map data...\nAPI: GET /vehicles/locations (polling every 5s)",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnToggleRiskZones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LiveMapActivity.this,
                        "Toggling high-risk zone overlay\nShowing flood zones, industrial hazard areas",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnToggleVehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LiveMapActivity.this,
                        "Toggling real-time vehicle tracking\nShowing Fire Engine 1, Ambulance 5, Police Unit 2",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}