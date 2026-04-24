package com.example.civildefence;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.civildefence.api.ApiClient;
import com.example.civildefence.models.Incident;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignedIncidentsActivity extends AppCompatActivity {

    private ListView listView;
    private List<Incident> assignedIncidents;
    private IncidentAdapter adapter;
    private SharedPreferences prefs;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_incidents);

        prefs = getSharedPreferences("civil_defense_prefs", MODE_PRIVATE);

        listView = findViewById(R.id.list_view);
        tvEmpty = findViewById(R.id.tv_empty);

        assignedIncidents = new ArrayList<>();
        adapter = new IncidentAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Incident inc = assignedIncidents.get(position);
            Intent intent = new Intent(AssignedIncidentsActivity.this,
                    IncidentDetailActivity.class);
            intent.putExtra("incident_id", inc.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAssignedIncidents();
    }

    private void loadAssignedIncidents() {
        String token = "Bearer " + prefs.getString("access_token", "");

        ApiClient.getApiService(this).getIncidents(token, 0, 100)
                .enqueue(new Callback<List<Incident>>() {
                    @Override
                    public void onResponse(Call<List<Incident>> call,
                                           Response<List<Incident>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            assignedIncidents.clear();

                            // Filter active incidents
                            for (Incident inc : response.body()) {
                                if (!"Closed".equals(inc.getStatus())) {
                                    assignedIncidents.add(inc);
                                }
                            }

                            adapter.notifyDataSetChanged();

                            if (assignedIncidents.isEmpty()) {
                                tvEmpty.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                            } else {
                                tvEmpty.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Incident>> call, Throwable t) {
                        Toast.makeText(AssignedIncidentsActivity.this,
                                "Failed to load incidents: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                        tvEmpty.setVisibility(View.VISIBLE);
                        tvEmpty.setText("Connection error");
                    }
                });
    }

    // Custom Adapter
    private class IncidentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return assignedIncidents.size();
        }

        @Override
        public Incident getItem(int position) {
            return assignedIncidents.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(AssignedIncidentsActivity.this)
                        .inflate(R.layout.item_incident, parent, false);
                holder = new ViewHolder();
                holder.tvTitle = convertView.findViewById(R.id.tv_incident_title);
                holder.tvDetails = convertView.findViewById(R.id.tv_incident_details);
                holder.tvStatus = convertView.findViewById(R.id.tv_incident_status);
                holder.btnView = convertView.findViewById(R.id.btn_view_incident);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Incident inc = getItem(position);

            String emoji = getEmoji(inc.getCategory());
            holder.tvTitle.setText(emoji + " INC-" + inc.getId() + " - " + inc.getCategory());
            holder.tvDetails.setText(inc.getDescription());

            String statusText = "Status: " + inc.getStatus();
            holder.tvStatus.setText(statusText);

            if ("Active".equals(inc.getStatus())) {
                holder.tvStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            } else if ("Waiting".equals(inc.getStatus())) {
                holder.tvStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            } else {
                holder.tvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            }

            final int pos = position;
            holder.btnView.setOnClickListener(v -> {
                Intent intent = new Intent(AssignedIncidentsActivity.this,
                        IncidentDetailActivity.class);
                intent.putExtra("incident_id", inc.getId());
                startActivity(intent);
            });

            return convertView;
        }
    }

    private String getEmoji(String category) {
        if (category == null) return "⚠️";
        switch (category) {
            case "Fire": return "🔥";
            case "Medical": return "🏥";
            case "Traffic": return "🚗";
            case "Accident": return "💥";
            case "Flood": return "🌊";
            default: return "⚠️";
        }
    }

    static class ViewHolder {
        TextView tvTitle;
        TextView tvDetails;
        TextView tvStatus;
        Button btnView;
    }
}