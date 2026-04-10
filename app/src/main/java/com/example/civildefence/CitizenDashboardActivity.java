package com.example.civildefence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CitizenDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private FloatingActionButton fabReportIncident;
    private Button btnMyReports, btnAlerts, btnProfile, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_dashboard);

        tvWelcome = findViewById(R.id.tv_welcome);
        fabReportIncident = findViewById(R.id.fab_report_incident);
        btnMyReports = findViewById(R.id.btn_my_reports);
        btnAlerts = findViewById(R.id.btn_alerts);
        btnProfile = findViewById(R.id.btn_profile);
        btnLogout = findViewById(R.id.btn_logout);

        // Set welcome message
        String email = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getString("user_email", "Citizen");
        tvWelcome.setText("Welcome, " + email);

        // FAB: Report Incident
        fabReportIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CitizenDashboardActivity.this, ReportIncidentActivity.class));
            }
        });

        // My Reports button
        btnMyReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CitizenDashboardActivity.this,
                        "Navigating to My Reports\nAPI: GET /incidents?reported_by=me",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CitizenDashboardActivity.this, MyReportsActivity.class));
            }
        });

        // Alerts button
        btnAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CitizenDashboardActivity.this,
                        "Navigating to Emergency Alerts\nAPI: GET /notifications",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CitizenDashboardActivity.this, AlertsListActivity.class));
            }
        });

        // Profile button
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CitizenDashboardActivity.this, ProfileActivity.class));
            }
        });

        // Logout button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CitizenDashboardActivity.this,
                        "API: POST /auth/logout - Invalidating session",
                        Toast.LENGTH_SHORT).show();

                getSharedPreferences("app_prefs", MODE_PRIVATE)
                        .edit()
                        .putBoolean("is_logged_in", false)
                        .apply();

                startActivity(new Intent(CitizenDashboardActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}