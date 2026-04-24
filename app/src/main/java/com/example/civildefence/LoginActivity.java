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
import com.example.civildefence.api.ApiClient;
import com.example.civildefence.api.ApiService;
import com.example.civildefence.models.LoginRequest;
import com.example.civildefence.models.TokenResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this,
                            "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                performLogin(email, password);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }

    private void performLogin(String email, String password) {
        ApiService apiService = ApiClient.getApiService(this);
        LoginRequest request = new LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getAccessToken();

                    // Save token
                    SharedPreferences prefs = getSharedPreferences(
                            "civil_defense_prefs", MODE_PRIVATE);

                    int selectedRoleId = rgRole.getCheckedRadioButtonId();
                    String role = (selectedRoleId == R.id.rb_citizen) ?
                            "citizen" : "responder";

                    prefs.edit()
                            .putBoolean("is_logged_in", true)
                            .putString("access_token", token)
                            .putString("user_role", role)
                            .putString("user_email", email)
                            .apply();

                    Toast.makeText(LoginActivity.this,
                            "Login successful!", Toast.LENGTH_SHORT).show();

                    // Navigate based on role
                    if ("citizen".equals(role)) {
                        startActivity(new Intent(LoginActivity.this,
                                CitizenDashboardActivity.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this,
                                ResponderDashboardActivity.class));
                    }
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,
                        "Connection error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}