package com.example.civildefence;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.materialswitch.MaterialSwitch;
import androidx.appcompat.app.AppCompatActivity;
import com.example.civildefence.api.ApiClient;
import com.example.civildefence.models.Incident;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResponderDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome, tvActiveIncidents;
    private MaterialSwitch swAvailability;
    private MaterialCardView btnAssignedIncidents, btnLiveMap, btnNotifications, btnProfile;
    private Button btnLogout;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_dashboard);

        prefs = getSharedPreferences("civil_defense_prefs", MODE_PRIVATE);

        tvWelcome = findViewById(R.id.tv_welcome);
        tvActiveIncidents = findViewById(R.id.tv_active_incidents);
        swAvailability = findViewById(R.id.sw_availability);
        btnAssignedIncidents = findViewById(R.id.btn_assigned_incidents);
        btnLiveMap = findViewById(R.id.btn_live_map);
        btnNotifications = findViewById(R.id.btn_notifications);
        btnProfile = findViewById(R.id.btn_profile);
        btnLogout = findViewById(R.id.btn_logout);

        String email = prefs.getString("user_email", "Responder");
        tvWelcome.setText("Responder: " + email);

        // Load active incidents count
        loadActiveIncidents();

        // Availability toggle
        swAvailability.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String status = isChecked ? "Available" : "Busy";
            Toast.makeText(this, "Status: " + status, Toast.LENGTH_SHORT).show();

            // Update availability via API
            updateAvailabilityStatus(isChecked);
        });

        // Navigation buttons
        btnAssignedIncidents.setOnClickListener(v -> {
            startActivity(new Intent(this, AssignedIncidentsActivity.class));
        });

        btnLiveMap.setOnClickListener(v -> {
            startActivity(new Intent(this, LiveMapActivity.class));
        });

        btnNotifications.setOnClickListener(v -> {
            startActivity(new Intent(this, NotificationsActivity.class));
        });

        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            prefs.edit()
                    .putBoolean("is_logged_in", false)
                    .remove("access_token")
                    .apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadActiveIncidents();
    }

    private void loadActiveIncidents() {
        String token = "Bearer " + prefs.getString("access_token", "");

        ApiClient.getApiService(this).getIncidents(token, 0, 100)
                .enqueue(new Callback<List<Incident>>() {
                    @Override
                    public void onResponse(Call<List<Incident>> call,
                                           Response<List<Incident>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            int activeCount = 0;
                            for (Incident inc : response.body()) {
                                if ("Active".equals(inc.getStatus()) ||
                                        "Waiting".equals(inc.getStatus())) {
                                    activeCount++;
                                }
                            }
                            tvActiveIncidents.setText(activeCount + " Active Incidents");
                        } else {
                            tvActiveIncidents.setText("0 Active Incidents");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Incident>> call, Throwable t) {
                        tvActiveIncidents.setText("Connection error");
                    }
                });
    }

    private void updateAvailabilityStatus(boolean isAvailable) {
        // In production, this would call an API endpoint to update responder status
        String token = "Bearer " + prefs.getString("access_token", "");

        // For now, we just show the status change
        // Future: PATCH /api/v1/responders/me/status with {"status": "available"/"busy"}
    }
}