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
import com.example.civildefence.models.Incident;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncidentDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvDetails, tvStatus;
    private Button btnUpdateStatus, btnGetDirections, btnTeamChat,
            btnUploadMedia, btnAddVoiceNote;
    private SharedPreferences prefs;
    private int incidentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_detail);

        prefs = getSharedPreferences("civil_defense_prefs", MODE_PRIVATE);

        tvTitle = findViewById(R.id.tv_title);
        tvDetails = findViewById(R.id.tv_details);
        tvStatus = findViewById(R.id.tv_status);
        btnUpdateStatus = findViewById(R.id.btn_update_status);
        btnGetDirections = findViewById(R.id.btn_get_directions);
        btnTeamChat = findViewById(R.id.btn_team_chat);
        btnUploadMedia = findViewById(R.id.btn_upload_media);
        btnAddVoiceNote = findViewById(R.id.btn_add_voice_note);

        incidentId = getIntent().getIntExtra("incident_id", 0);

        loadIncidentDetails();

        btnUpdateStatus.setOnClickListener(v -> {
            String token = "Bearer " + prefs.getString("access_token", "");
            Map<String, String> statusUpdate = new HashMap<>();
            statusUpdate.put("status", "Closed");

            ApiClient.getApiService(this)
                    .updateIncidentStatus(token, incidentId, statusUpdate)
                    .enqueue(new Callback<Incident>() {
                        @Override
                        public void onResponse(Call<Incident> call,
                                               Response<Incident> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(IncidentDetailActivity.this,
                                        "Status updated", Toast.LENGTH_SHORT).show();
                                loadIncidentDetails();
                            }
                        }

                        @Override
                        public void onFailure(Call<Incident> call, Throwable t) {
                            Toast.makeText(IncidentDetailActivity.this,
                                    "Failed to update status", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnGetDirections.setOnClickListener(v -> {
            // Open external maps app with coordinates
            Toast.makeText(this, "Opening navigation...", Toast.LENGTH_SHORT).show();
        });

        btnTeamChat.setOnClickListener(v -> {
            Intent intent = new Intent(IncidentDetailActivity.this,
                    ChatActivity.class);
            intent.putExtra("incident_id", incidentId);
            startActivity(intent);
        });

        btnUploadMedia.setOnClickListener(v -> {
            // Open media upload
            Toast.makeText(this, "Select media to upload", Toast.LENGTH_SHORT).show();
        });

        btnAddVoiceNote.setOnClickListener(v -> {
            // Start voice recording
            Toast.makeText(this, "Recording voice note...", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadIncidentDetails() {
        String token = "Bearer " + prefs.getString("access_token", "");

        ApiClient.getApiService(this).getIncidentById(token, incidentId)
                .enqueue(new Callback<Incident>() {
                    @Override
                    public void onResponse(Call<Incident> call, Response<Incident> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Incident inc = response.body();
                            tvTitle.setText("Incident #" + inc.getId());
                            tvStatus.setText("Status: " + inc.getStatus());
                            tvDetails.setText(
                                    "Type: " + inc.getCategory() + "\n" +
                                            "Priority: " + inc.getPriority() + "\n" +
                                            "Location: " + inc.getLatitude() + ", " + inc.getLongitude() + "\n" +
                                            "Description: " + inc.getDescription() + "\n" +
                                            "Reported: " + inc.getCreatedAt()
                            );
                        }
                    }

                    @Override
                    public void onFailure(Call<Incident> call, Throwable t) {
                        Toast.makeText(IncidentDetailActivity.this,
                                "Failed to load incident: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}