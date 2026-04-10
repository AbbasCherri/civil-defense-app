package com.example.civildefence;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Simulate checking login status (in real app, check SharedPreferences/Token)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // For demo, go to Login. In real app, check if token exists
                boolean isLoggedIn = getSharedPreferences("app_prefs", MODE_PRIVATE)
                        .getBoolean("is_logged_in", false);

                if (isLoggedIn) {
                    String role = getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .getString("user_role", "citizen");
                    if ("responder".equals(role)) {
                        startActivity(new Intent(SplashActivity.this, ResponderDashboardActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, CitizenDashboardActivity.class));
                    }
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 2000);
    }
}