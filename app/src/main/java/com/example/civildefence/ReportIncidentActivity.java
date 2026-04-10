package com.example.civildefence;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ReportIncidentActivity extends AppCompatActivity {

    private Spinner spinnerType, spinnerPriority;
    private EditText etDescription, etLocation;
    private Button btnPickLocation, btnAttachPhoto, btnAttachVideo, btnRecordVoice, btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);

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
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.incident_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(this,
                R.array.priority_levels, android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(priorityAdapter);

        // Pick Location button
        btnPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReportIncidentActivity.this,
                        "Opening map to select location\nGPS coordinates will be auto-captured",
                        Toast.LENGTH_SHORT).show();
                etLocation.setText("📍 Selected: 33.8938, 35.5018 (Beirut Central District)");
            }
        });

        // Attach Photo button
        btnAttachPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReportIncidentActivity.this,
                        "Opening gallery/camera to attach photo\nAPI: POST /incidents/{id}/media (multipart)",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Attach Video button
        btnAttachVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReportIncidentActivity.this,
                        "Opening camera to record video\nMax size: 50MB, Format: MP4",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Record Voice button
        btnRecordVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReportIncidentActivity.this,
                        "Starting voice recording\nAudio will be attached to incident",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = spinnerType.getSelectedItem().toString();
                String priority = spinnerPriority.getSelectedItem().toString();
                String description = etDescription.getText().toString();
                String location = etLocation.getText().toString();

                if (description.isEmpty()) {
                    Toast.makeText(ReportIncidentActivity.this,
                            "Please enter incident description", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(ReportIncidentActivity.this,
                        "API: POST /incidents\n" +
                                "Creating incident:\n" +
                                "Type: " + type + "\n" +
                                "Priority: " + priority + "\n" +
                                "Location: " + location + "\n" +
                                "Status: Waiting (pending assignment)",
                        Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }
}