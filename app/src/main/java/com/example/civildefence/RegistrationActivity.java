package com.example.civildefence;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etFullName, etNationalId, etEmail, etPhone, etPassword, etAddress, etDob;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etFullName = findViewById(R.id.et_full_name);
        etNationalId = findViewById(R.id.et_national_id);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etAddress = findViewById(R.id.et_address);
        etDob = findViewById(R.id.et_dob);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast: Simulating API registration
                Toast.makeText(RegistrationActivity.this,
                        "API: POST /auth/register - Creating citizen account\n" +
                                "Creating: User → Citizen record with national ID",
                        Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }
}