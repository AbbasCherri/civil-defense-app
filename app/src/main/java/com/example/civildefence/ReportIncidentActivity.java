package com.example.civildefence;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.civildefence.api.ApiClient;
import com.example.civildefence.api.ApiService;
import com.example.civildefence.models.IncidentCreate;
import com.example.civildefence.models.Incident;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportIncidentActivity extends AppCompatActivity {

    private Spinner spinnerType, spinnerPriority;
    private EditText etDescription, etLocation;
    private Button btnPickLocation, btnAttachPhoto, btnAttachVideo,
            btnRecordVoice, btnSubmit;
    private SharedPreferences prefs;
    private double selectedLatitude = 0.0;
    private double selectedLongitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);

        prefs = getSharedPreferences("civil_defense_prefs", MODE_PRIVATE);

        spinnerType = findViewById(R.id.spinner_type);
        spinnerPriority = findViewById(R.id.spinner_priority);
        etDescription = findViewById(R.id.et_description);
        etLocation = findViewById(R.id.et_location);
        btnPickLocation = findViewById(R.id.btn_pick_location);
        btnAttachPhoto = findViewById(R.id.btn_attach_photo);
        btnAttachVideo = findViewById(R.id.btn_attach_video);
        btnRecordVoice = findViewById(R.id.btn_record_voice);
        btnSubmit = findViewById(R.id.btn_submit);

        // Setup spinners
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                this, R.array.incident_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(
                this, R.array.priority_levels, android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(priorityAdapter);

        // Pick location (simplified - in production use actual GPS)
        btnPickLocation.setOnClickListener(v -> {
            // In production: Open map activity to select location
            selectedLatitude = 33.8938;  // Example: Beirut
            selectedLongitude = 35.5018;
            etLocation.setText("Location selected: " + selectedLatitude + ", " + selectedLongitude);
        });

        // Attach photo
        btnAttachPhoto.setOnClickListener(v -> {
            // In production: Open image picker
            Toast.makeText(this, "Select photo to attach", Toast.LENGTH_SHORT).show();
        });

        // Attach video
        btnAttachVideo.setOnClickListener(v -> {
            // In production: Open video picker
            Toast.makeText(this, "Select video to attach", Toast.LENGTH_SHORT).show();
        });

        // Record voice
        btnRecordVoice.setOnClickListener(v -> {
            // In production: Start voice recording
            Toast.makeText(this, "Recording voice note...", Toast.LENGTH_SHORT).show();
        });

        // Submit incident
        btnSubmit.setOnClickListener(v -> {
            String description = etDescription.getText().toString().trim();

            if (description.isEmpty()) {
                Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show();
                return;
            }

            submitIncident();
        });
    }

    private void submitIncident() {
        String token = "Bearer " + prefs.getString("access_token", "");
        String category = spinnerType.getSelectedItem().toString();
        String priority = spinnerPriority.getSelectedItem().toString();
        String description = etDescription.getText().toString().trim();

        IncidentCreate incident = new IncidentCreate(
                category, priority, selectedLatitude, selectedLongitude, description);

        ApiClient.getApiService(this).createIncident(token, incident)
                .enqueue(new Callback<Incident>() {
                    @Override
                    public void onResponse(Call<Incident> call, Response<Incident> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(ReportIncidentActivity.this,
                                    "Incident #" + response.body().getId() + " reported!",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(ReportIncidentActivity.this,
                                    "Failed to report incident", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Incident> call, Throwable t) {
                        Toast.makeText(ReportIncidentActivity.this,
                                "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}