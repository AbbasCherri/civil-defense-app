package com.example.civildefence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.civildefence.api.ApiClient;
import com.example.civildefence.models.User;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etFullName, etNationalId, etEmail, etPhone,
            etPassword, etAddress, etDob;
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
                String fullName = etFullName.getText().toString().trim();
                String nationalId = etNationalId.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                String dob = etDob.getText().toString().trim();

                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || nationalId.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this,
                            "Please fill all required fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                registerUser(fullName, nationalId, email, phone, password, address, dob);
            }
        });
    }

    private void registerUser(String fullName, String nationalId, String email,
                              String phone, String password, String address, String dob) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", fullName);
        userData.put("email", email);
        userData.put("password", password);
        userData.put("role", "Citizen");
        userData.put("contact_info", phone);

        ApiClient.getApiService(this).register(userData).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    Toast.makeText(RegistrationActivity.this,
                            "Registration successful! Welcome " + user.getName(),
                            Toast.LENGTH_LONG).show();

                    // Navigate to login
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    intent.putExtra("registered_email", email);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMsg = "Registration failed";
                    if (response.code() == 400) {
                        errorMsg = "Email already registered";
                    }
                    Toast.makeText(RegistrationActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}