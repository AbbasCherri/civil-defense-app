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
import com.example.civildefence.models.Incident;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class MyReportsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> reportsList;
    private ArrayAdapter<String> adapter;
    private SharedPreferences prefs;
    private List<Incident> incidents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);

        prefs = getSharedPreferences("civil_defense_prefs", MODE_PRIVATE);
        listView = findViewById(R.id.list_view);

        reportsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, reportsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (incidents != null && position < incidents.size()) {
                Intent intent = new Intent(MyReportsActivity.this,
                        IncidentDetailActivity.class);
                intent.putExtra("incident_id", incidents.get(position).getId());
                startActivity(intent);
            }
        });

        loadMyReports();
    }

    private void loadMyReports() {
        String token = "Bearer " + prefs.getString("access_token", "");

        ApiClient.getApiService(this).getIncidents(token, 0, 100)
                .enqueue(new Callback<List<Incident>>() {
                    @Override
                    public void onResponse(Call<List<Incident>> call,
                                           Response<List<Incident>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            incidents = response.body();
                            reportsList.clear();
                            for (Incident inc : incidents) {
                                reportsList.add(inc.getCategory() + " - " +
                                        inc.getDescription() + "\nStatus: " + inc.getStatus() +
                                        " | " + inc.getCreatedAt());
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Incident>> call, Throwable t) {
                        Toast.makeText(MyReportsActivity.this,
                                "Failed to load reports: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}