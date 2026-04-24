package com.example.civildefence;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.civildefence.api.ApiClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertsListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> alertsList;
    private ArrayAdapter<String> adapter;
    private List<Map<String, Object>> notifications;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts_list);

        prefs = getSharedPreferences("civil_defense_prefs", MODE_PRIVATE);
        listView = findViewById(R.id.list_view);

        alertsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alertsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (notifications != null && position < notifications.size()) {
                    Map<String, Object> notification = notifications.get(position);
                    Double notificationId = (Double) notification.get("id");

                    String token = "Bearer " + prefs.getString("access_token", "");
                    ApiClient.getApiService(AlertsListActivity.this)
                            .markNotificationRead(token, notificationId.intValue())
                            .enqueue(new Callback<Map<String, Object>>() {
                                @Override
                                public void onResponse(Call<Map<String, Object>> call,
                                                       Response<Map<String, Object>> response) {
                                    Toast.makeText(AlertsListActivity.this,
                                            "Alert marked as read", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Map<String, Object>> call, Throwable t) {}
                            });
                }
            }
        });

        loadAlerts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAlerts();
    }

    private void loadAlerts() {
        String token = "Bearer " + prefs.getString("access_token", "");

        ApiClient.getApiService(this).getNotifications(token)
                .enqueue(new Callback<List<Map<String, Object>>>() {
                    @Override
                    public void onResponse(Call<List<Map<String, Object>>> call,
                                           Response<List<Map<String, Object>>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            notifications = response.body();
                            alertsList.clear();

                            for (Map<String, Object> notification : notifications) {
                                String type = (String) notification.getOrDefault("type", "Alert");
                                String message = (String) notification.getOrDefault("message", "No details");
                                String timestamp = (String) notification.getOrDefault("timestamp", "");

                                String icon = "🔔";
                                if ("emergency".equalsIgnoreCase(type)) {
                                    icon = "🚨";
                                } else if ("warning".equalsIgnoreCase(type)) {
                                    icon = "⚠️";
                                }

                                alertsList.add(icon + " " + type.toUpperCase() + ": " + message +
                                        "\n" + timestamp);
                            }
                            adapter.notifyDataSetChanged();

                            if (alertsList.isEmpty()) {
                                alertsList.add("No alerts at this time");
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                        alertsList.clear();
                        alertsList.add("Failed to load alerts: " + t.getMessage());
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}