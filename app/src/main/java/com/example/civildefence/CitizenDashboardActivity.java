package com.example.civildefence;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.civildefence.api.ApiClient;
import com.example.civildefence.api.ApiService;
import com.example.civildefence.models.DailyReport;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CitizenDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome, tvActiveAlerts, tvMyReportsCount;
    private ExtendedFloatingActionButton fabReportIncident;
    private MaterialCardView btnMyReports, btnAlerts, btnProfile, btnLogout;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_dashboard);

        prefs = getSharedPreferences("civil_defense_prefs", MODE_PRIVATE);

        tvWelcome = findViewById(R.id.tv_welcome);
        tvActiveAlerts = findViewById(R.id.tv_active_alerts_count);
        tvMyReportsCount = findViewById(R.id.tv_my_reports_count);
        fabReportIncident = findViewById(R.id.fab_report_incident);
        btnMyReports = findViewById(R.id.btn_my_reports);
        btnAlerts = findViewById(R.id.btn_alerts);
        btnProfile = findViewById(R.id.btn_profile);
        btnLogout = findViewById(R.id.btn_logout);

        String email = prefs.getString("user_email", "Citizen");
        tvWelcome.setText("Welcome, " + email);

        // Load dashboard stats
        loadDashboardStats();

        fabReportIncident.setOnClickListener(v -> {
            startActivity(new Intent(CitizenDashboardActivity.this,
                    ReportIncidentActivity.class));
        });

        btnMyReports.setOnClickListener(v -> {
            startActivity(new Intent(CitizenDashboardActivity.this,
                    MyReportsActivity.class));
        });

        btnAlerts.setOnClickListener(v -> {
            startActivity(new Intent(CitizenDashboardActivity.this,
                    AlertsListActivity.class));
        });

        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(CitizenDashboardActivity.this,
                    ProfileActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            prefs.edit()
                    .putBoolean("is_logged_in", false)
                    .remove("access_token")
                    .apply();
            startActivity(new Intent(CitizenDashboardActivity.this,
                    LoginActivity.class));
            finish();
        });
    }

    private void loadDashboardStats() {
        String token = "Bearer " + prefs.getString("access_token", "");
        ApiService apiService = ApiClient.getApiService(this);

        // Get daily report for stats
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());

        apiService.getDailyReport(token, today).enqueue(new Callback<DailyReport>() {
            @Override
            public void onResponse(Call<DailyReport> call, Response<DailyReport> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DailyReport report = response.body();
                    tvActiveAlerts.setText(String.valueOf(report.getTotalIncidents()));
                }
            }

            @Override
            public void onFailure(Call<DailyReport> call, Throwable t) {
                tvActiveAlerts.setText("0");
                tvMyReportsCount.setText("0");
            }
        });
    }
}