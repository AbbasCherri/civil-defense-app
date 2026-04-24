package com.example.civildefence;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.civildefence.api.ApiClient;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLoginStatus();
            }
        }, 2000);
    }

    private void checkLoginStatus() {
        SharedPreferences prefs = getSharedPreferences("civil_defense_prefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
        String token = prefs.getString("access_token", "");

        if (isLoggedIn && !token.isEmpty()) {
            // Verify token with backend
            ApiClient.getApiService(SplashActivity.this)
                    .verifyToken("Bearer " + token)
                    .enqueue(new Callback<Map<String, Object>>() {
                        @Override
                        public void onResponse(Call<Map<String, Object>> call,
                                               Response<Map<String, Object>> response) {
                            if (response.isSuccessful()) {
                                // Token valid, navigate to dashboard
                                navigateToDashboard();
                            } else {
                                // Token invalid, go to login
                                navigateToLogin();
                            }
                        }

                        @Override
                        public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                            // Network error, but user was logged in
                            // Navigate to dashboard (offline mode)
                            navigateToDashboard();
                        }
                    });
        } else {
            navigateToLogin();
        }
    }

    private void navigateToDashboard() {
        SharedPreferences prefs = getSharedPreferences("civil_defense_prefs", MODE_PRIVATE);
        String role = prefs.getString("user_role", "citizen");

        Intent intent;
        if ("responder".equals(role)) {
            intent = new Intent(SplashActivity.this, ResponderDashboardActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, CitizenDashboardActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private void navigateToLogin() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }
}