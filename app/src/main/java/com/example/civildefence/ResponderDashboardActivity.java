package com.example.civildefence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ResponderDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome, tvActiveIncidents;
    private Switch swAvailability;
    private Button btnAssignedIncidents, btnLiveMap, btnNotifications, btnProfile, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_dashboard);

        tvWelcome = findViewById(R.id.tv_welcome);
        tvActiveIncidents = findViewById(R.id.tv_active_incidents);
        swAvailability = findViewById(R.id.sw_availability);
        btnAssignedIncidents = findViewById(R.id.btn_assigned_incidents);
        btnLiveMap = findViewById(R.id.btn_live_map);
        btnNotifications = findViewById(R.id.btn_notifications);
        btnProfile = findViewById(R.id.btn_profile);
        btnLogout = findViewById(R.id.btn_logout);

        String email = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getString("user_email", "Responder");
        tvWelcome.setText("Responder: " + email);

        // Availability toggle
        swAvailability.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String status = isChecked ? "Available" : "Busy";
                Toast.makeText(ResponderDashboardActivity.this,
                        "API: PATCH /employees/me/status - Status: " + status,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Assigned Incidents button
        btnAssignedIncidents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ResponderDashboardActivity.this,
                        "Navigating to Assigned Incidents\nAPI: GET /incidents?assigned_to=me&status=active",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ResponderDashboardActivity.this, AssignedIncidentsActivity.class));
            }
        });

        // Live Map button
        btnLiveMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ResponderDashboardActivity.this,
                        "Opening Live Map\nAPI: GET /vehicles/locations, GET /incidents?active=true",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ResponderDashboardActivity.this, LiveMapActivity.class));
            }
        });

        // Notifications button
        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ResponderDashboardActivity.this,
                        "Navigating to Notifications\nAPI: GET /notifications",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ResponderDashboardActivity.this, NotificationsActivity.class));
            }
        });

        // Profile button
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResponderDashboardActivity.this, ProfileActivity.class));
            }
        });

        // Logout button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ResponderDashboardActivity.this,
                        "API: POST /auth/logout - Ending shift",
                        Toast.LENGTH_SHORT).show();

                getSharedPreferences("app_prefs", MODE_PRIVATE)
                        .edit()
                        .putBoolean("is_logged_in", false)
                        .apply();

                startActivity(new Intent(ResponderDashboardActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}