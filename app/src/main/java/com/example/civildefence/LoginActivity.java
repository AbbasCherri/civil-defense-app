package com.example.civildefence;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private RadioGroup rgRole;
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        rgRole = findViewById(R.id.rg_role);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Toast: Simulating API authentication
                Toast.makeText(LoginActivity.this,
                        "API: POST /auth/login - Authenticating user", Toast.LENGTH_SHORT).show();

                // Determine selected role
                int selectedRoleId = rgRole.getCheckedRadioButtonId();
                String role = (selectedRoleId == R.id.rb_citizen) ? "citizen" : "responder";

                // Save login state (demo only)
                SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
                prefs.edit()
                        .putBoolean("is_logged_in", true)
                        .putString("user_role", role)
                        .putString("user_email", email)
                        .apply();

                // Navigate based on role
                if ("citizen".equals(role)) {
                    startActivity(new Intent(LoginActivity.this, CitizenDashboardActivity.class));
                } else {
                    startActivity(new Intent(LoginActivity.this, ResponderDashboardActivity.class));
                }
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }
}