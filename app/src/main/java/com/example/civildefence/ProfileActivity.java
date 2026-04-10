package com.example.civildefence;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvRole, tvDepartment;
    private Button btnEditProfile, btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        tvRole = findViewById(R.id.tv_role);
        tvDepartment = findViewById(R.id.tv_department);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnChangePassword = findViewById(R.id.btn_change_password);

        Toast.makeText(this,
                "API: GET /users/me - Fetching profile data",
                Toast.LENGTH_SHORT).show();

        // Load profile from SharedPreferences
        String email = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getString("user_email", "user@example.com");
        String role = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getString("user_role", "citizen");

        tvEmail.setText("Email: " + email);
        tvRole.setText("Role: " + role);

        if ("responder".equals(role)) {
            tvName.setText("Name: John Responder");
            tvDepartment.setText("Department: Fire & Rescue");
        } else {
            tvName.setText("Name: Citizen User");
            tvDepartment.setVisibility(View.GONE);
        }

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this,
                        "API: PATCH /users/me - Updating profile information",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this,
                        "API: POST /auth/change-password",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}