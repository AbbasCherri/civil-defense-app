package com.example.civildefence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class IncidentDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvDetails, tvStatus;
    private Button btnUpdateStatus, btnGetDirections, btnTeamChat, btnUploadMedia, btnAddVoiceNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_detail);

        tvTitle = findViewById(R.id.tv_title);
        tvDetails = findViewById(R.id.tv_details);
        tvStatus = findViewById(R.id.tv_status);
        btnUpdateStatus = findViewById(R.id.btn_update_status);
        btnGetDirections = findViewById(R.id.btn_get_directions);
        btnTeamChat = findViewById(R.id.btn_team_chat);
        btnUploadMedia = findViewById(R.id.btn_upload_media);
        btnAddVoiceNote = findViewById(R.id.btn_add_voice_note);

        Toast.makeText(this,
                "API: GET /incidents/{id} - Fetching incident details",
                Toast.LENGTH_SHORT).show();

        // Sample incident data
        tvTitle.setText("Incident #INC-001");
        tvDetails.setText("Type: Fire\nPriority: HIGH\nLocation: Beirut Central District\nReported: 2026-04-10 14:30\nAssigned Team: Alpha Squad");
        tvStatus.setText("Status: Active");

        // Update Status button
        btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(IncidentDetailActivity.this,
                        "API: PATCH /incidents/{id}/status\n" +
                                "Valid transitions: Waiting → Active → In Progress → Resolved → Closed",
                        Toast.LENGTH_LONG).show();
            }
        });

        // Get Directions button
        btnGetDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(IncidentDetailActivity.this,
                        "Calculating route to incident location\nOpening external maps app with destination: 33.8938, 35.5018",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Team Chat button
        btnTeamChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(IncidentDetailActivity.this,
                        "Opening team chat for incident #INC-001",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(IncidentDetailActivity.this, ChatActivity.class));
            }
        });

        // Upload Media button
        btnUploadMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(IncidentDetailActivity.this,
                        "API: POST /incidents/{id}/media\nUploading evidence photo/video (max 50MB)",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Add Voice Note button
        btnAddVoiceNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(IncidentDetailActivity.this,
                        "Starting voice recording for incident update\nAudio will be attached to incident record",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}