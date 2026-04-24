package com.example.civildefence;

import android.content.Intent;
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

public class NotificationsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> notificationsList;
    private ArrayAdapter<String> adapter;
    private List<Map<String, Object>> allNotifications;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        prefs = getSharedPreferences("civil_defense_prefs", MODE_PRIVATE);
        listView = findViewById(R.id.list_view);

        notificationsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notificationsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (allNotifications != null && position < allNotifications.size()) {
                    Map<String, Object> notification = allNotifications.get(position);

                    // Check if notification has incident_id
                    Double incidentId = (Double) notification.get("incident_id");
                    if (incidentId != null) {
                        Intent intent = new Intent(NotificationsActivity.this,
                                IncidentDetailActivity.class);
                        intent.putExtra("incident_id", incidentId.intValue());
                        startActivity(intent);
                    }

                    // Mark as read
                    Double notifId = (Double) notification.get("id");
                    String token = "Bearer " + prefs.getString("access_token", "");
                    ApiClient.getApiService(NotificationsActivity.this)
                            .markNotificationRead(token, notifId.intValue())
                            .enqueue(new Callback<Map<String, Object>>() {
                                @Override
                                public void onResponse(Call<Map<String, Object>> call,
                                                       Response<Map<String, Object>> response) {}
                                @Override
                                public void onFailure(Call<Map<String, Object>> call, Throwable t) {}
                            });
                }
            }
        });

        loadNotifications();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotifications();
    }

    private void loadNotifications() {
        String token = "Bearer " + prefs.getString("access_token", "");

        ApiClient.getApiService(this).getNotifications(token)
                .enqueue(new Callback<List<Map<String, Object>>>() {
                    @Override
                    public void onResponse(Call<List<Map<String, Object>>> call,
                                           Response<List<Map<String, Object>>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            allNotifications = response.body();
                            notificationsList.clear();

                            for (Map<String, Object> notification : allNotifications) {
                                String type = (String) notification.getOrDefault("type", "Notification");
                                String message = (String) notification.getOrDefault("message", "");
                                String timestamp = (String) notification.getOrDefault("timestamp", "");

                                String icon = getNotificationIcon(type);
                                notificationsList.add(icon + " " + type + "\n" +
                                        message + "\n" + timestamp);
                            }
                            adapter.notifyDataSetChanged();

                            if (notificationsList.isEmpty()) {
                                notificationsList.add("No notifications");
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                        notificationsList.clear();
                        notificationsList.add("Failed to load notifications");
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private String getNotificationIcon(String type) {
        if (type == null) return "📢";
        switch (type.toLowerCase()) {
            case "assignment": return "📋";
            case "alert": return "🚨";
            case "status_update": return "📊";
            case "message": return "💬";
            default: return "📢";
        }
    }
}