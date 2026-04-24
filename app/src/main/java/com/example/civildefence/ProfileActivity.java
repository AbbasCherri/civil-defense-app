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
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvRole, tvDepartment, tvContactInfo, tvMemberSince;
    private Button btnEditProfile, btnChangePassword, btnLogout;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prefs = getSharedPreferences("civil_defense_prefs", MODE_PRIVATE);

        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        tvRole = findViewById(R.id.tv_role);
        tvDepartment = findViewById(R.id.tv_department);
        tvContactInfo = findViewById(R.id.tv_contact_info);
        tvMemberSince = findViewById(R.id.tv_member_since);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnLogout = findViewById(R.id.btn_logout);

        // Load profile from stored data and verify with backend
        loadProfile();

        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(this, "Edit profile feature coming soon",
                    Toast.LENGTH_SHORT).show();
        });

        btnChangePassword.setOnClickListener(v -> {
            Toast.makeText(this, "Change password feature coming soon",
                    Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> {
            performLogout();
        });
    }

    private void loadProfile() {
        String email = prefs.getString("user_email", "Not available");
        String role = prefs.getString("user_role", "citizen");
        String token = "Bearer " + prefs.getString("access_token", "");

        tvEmail.setText("Email: " + email);
        tvRole.setText("Role: " + formatRole(role));

        // Verify token and get user info from backend
        ApiClient.getApiService(this).verifyToken(token)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call,
                                           Response<Map<String, Object>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Map<String, Object> user = (Map<String, Object>)
                                    response.body().get("user");

                            if (user != null) {
                                String name = (String) user.getOrDefault("name", "Not available");
                                String contactInfo = (String) user.getOrDefault("contact_info", "Not set");

                                tvName.setText("Name: " + name);
                                tvContactInfo.setText("Contact: " + contactInfo);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        tvName.setText("Name: " + email);
                    }
                });

        // For responders, show department info
        if ("responder".equals(role)) {
            tvDepartment.setVisibility(View.VISIBLE);
            tvDepartment.setText("Department: Fire & Rescue");
        } else {
            tvDepartment.setVisibility(View.GONE);
        }

        tvMemberSince.setText("Member since: 2026");
    }

    private String formatRole(String role) {
        if (role == null) return "Unknown";
        return role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase();
    }

    private void performLogout() {
        // Clear all saved preferences
        prefs.edit()
                .putBoolean("is_logged_in", false)
                .remove("access_token")
                .remove("user_role")
                .apply();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Navigate to login
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}