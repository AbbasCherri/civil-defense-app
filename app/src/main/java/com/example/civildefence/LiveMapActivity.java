package com.example.civildefence;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.civildefence.api.ApiClient;
import com.example.civildefence.models.Incident;
import com.example.civildefence.models.Resource;
import java.util.List;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveMapActivity extends AppCompatActivity {

    private FrameLayout mapContainer;
    private LinearLayout infoPanel;
    private Button btnRefresh;
    private SharedPreferences prefs;
    private List<Incident> activeIncidents;
    private List<Resource> resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_map);

        prefs = getSharedPreferences("civil_defense_prefs", MODE_PRIVATE);

        mapContainer = findViewById(R.id.map_container);
        infoPanel = findViewById(R.id.info_panel);
        btnRefresh = findViewById(R.id.btn_refresh);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshMapData();
            }
        });

        // Load data when map is ready
        mapContainer.post(new Runnable() {
            @Override
            public void run() {
                refreshMapData();
            }
        });
    }

    private void refreshMapData() {
        Toast.makeText(this, "Loading map data...", Toast.LENGTH_SHORT).show();
        String token = "Bearer " + prefs.getString("access_token", "");

        // Load incidents
        ApiClient.getApiService(this).getIncidents(token, 0, 100)
                .enqueue(new Callback<List<Incident>>() {
                    @Override
                    public void onResponse(Call<List<Incident>> call,
                                           Response<List<Incident>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            activeIncidents = response.body();
                            drawMap();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Incident>> call, Throwable t) {
                        Toast.makeText(LiveMapActivity.this,
                                "Failed to load map data", Toast.LENGTH_SHORT).show();
                    }
                });

        // Load resources (vehicles)
        ApiClient.getApiService(this).getResources(token, 0, 100)
                .enqueue(new Callback<List<Resource>>() {
                    @Override
                    public void onResponse(Call<List<Resource>> call,
                                           Response<List<Resource>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            resources = response.body();
                            drawMap();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Resource>> call, Throwable t) {}
                });
    }

    private void drawMap() {
        // Clear previous markers
        mapContainer.removeAllViews();
        infoPanel.removeAllViews();

        int mapWidth = mapContainer.getWidth();
        int mapHeight = mapContainer.getHeight();

        // If map hasn't been measured yet, use reasonable defaults
        if (mapWidth == 0) mapWidth = 600;
        if (mapHeight == 0) mapHeight = 800;

        Random random = new Random();

        if (activeIncidents == null || activeIncidents.isEmpty()) {
            // Show empty map message
            TextView emptyText = new TextView(this);
            emptyText.setText("🗺️ No active incidents\n\nTap Refresh to reload");
            emptyText.setTextSize(18);
            emptyText.setGravity(Gravity.CENTER);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.gravity = Gravity.CENTER;
            emptyText.setLayoutParams(params);
            mapContainer.addView(emptyText);

            // Add legend anyway
            addLegend();
            return;
        }

        // Draw incident markers
        for (final Incident inc : activeIncidents) {
            if ("Closed".equals(inc.getStatus())) continue;

            // Create marker
            TextView marker = new TextView(this);
            String emoji = getIncidentEmoji(inc.getCategory());
            marker.setText(emoji);
            marker.setTextSize(24);
            marker.setGravity(Gravity.CENTER);

            // Random position on the map (simulating GPS coordinates)
            int leftMargin = random.nextInt(mapWidth - 100) + 20;
            int topMargin = random.nextInt(mapHeight - 200) + 50;

            FrameLayout.LayoutParams markerParams = new FrameLayout.LayoutParams(80, 80);
            markerParams.leftMargin = leftMargin;
            markerParams.topMargin = topMargin;
            marker.setLayoutParams(markerParams);

            // Set background color based on status
            if ("Active".equals(inc.getStatus())) {
                marker.setBackgroundColor(Color.argb(100, 255, 0, 0));
            } else if ("Waiting".equals(inc.getStatus())) {
                marker.setBackgroundColor(Color.argb(100, 255, 165, 0));
            } else {
                marker.setBackgroundColor(Color.argb(100, 76, 175, 80));
            }
            marker.setPadding(8, 8, 8, 8);

            // Click listener to show incident info
            marker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showIncidentInfo(inc);
                }
            });

            mapContainer.addView(marker);

            // Draw ID label below marker
            TextView label = new TextView(this);
            label.setText("#" + inc.getId());
            label.setTextSize(10);
            label.setTextColor(Color.BLACK);
            FrameLayout.LayoutParams labelParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            labelParams.leftMargin = leftMargin + 15;
            labelParams.topMargin = topMargin + 82;
            label.setLayoutParams(labelParams);
            mapContainer.addView(label);
        }

        // Draw vehicle markers if resources available
        if (resources != null) {
            for (final Resource res : resources) {
                if ("Vehicle".equals(res.getType()) &&
                        !"Maintenance".equals(res.getStatus())) {

                    TextView vehicleMarker = new TextView(this);
                    vehicleMarker.setText("🚒");
                    vehicleMarker.setTextSize(20);
                    vehicleMarker.setGravity(Gravity.CENTER);

                    int leftMargin = random.nextInt(mapWidth - 80) + 20;
                    int topMargin = random.nextInt(mapHeight - 150) + 50;

                    FrameLayout.LayoutParams vParams = new FrameLayout.LayoutParams(60, 60);
                    vParams.leftMargin = leftMargin;
                    vParams.topMargin = topMargin;
                    vehicleMarker.setLayoutParams(vParams);

                    vehicleMarker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showResourceInfo(res);
                        }
                    });

                    mapContainer.addView(vehicleMarker);
                }
            }
        }

        // Add legend
        addLegend();
    }

    private String getIncidentEmoji(String category) {
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

    private void showIncidentInfo(Incident inc) {
        infoPanel.removeAllViews();

        // Title
        TextView titleText = new TextView(this);
        titleText.setText("📌 Incident #" + inc.getId());
        titleText.setTextSize(16);
        titleText.setTypeface(null, Typeface.BOLD);
        titleText.setPadding(8, 8, 8, 8);
        titleText.setTextColor(Color.parseColor("#1A237E"));
        infoPanel.addView(titleText);

        // Details
        addInfoLine("📂 Type: " + inc.getCategory());
        addInfoLine("⚡ Priority: " + inc.getPriority());
        addInfoLine("📊 Status: " + inc.getStatus());
        addInfoLine("📍 Location: " + String.format("%.4f, %.4f",
                inc.getLatitude(), inc.getLongitude()));
        addInfoLine("📝 " + inc.getDescription());
        addInfoLine("🕐 Created: " + inc.getCreatedAt());
    }

    private void showResourceInfo(Resource res) {
        infoPanel.removeAllViews();

        TextView titleText = new TextView(this);
        titleText.setText("🚒 " + res.getName());
        titleText.setTextSize(16);
        titleText.setTypeface(null, Typeface.BOLD);
        titleText.setPadding(8, 8, 8, 8);
        titleText.setTextColor(Color.parseColor("#1A237E"));
        infoPanel.addView(titleText);

        addInfoLine("📦 Type: " + res.getType());
        addInfoLine("📊 Status: " + res.getStatus());

        if (res.getFuelUsage() > 0) {
            addInfoLine("⛽ Fuel: " + res.getFuelUsage() + "L");
        }

        if (res.getLastInspection() != null) {
            addInfoLine("🔧 Last Inspection: " + res.getLastInspection());
        }
    }

    private void addInfoLine(String text) {
        TextView textView = new TextView(this);
        textView.setText("  " + text);
        textView.setTextSize(12);
        textView.setPadding(8, 2, 8, 2);
        textView.setTextColor(Color.BLACK);
        infoPanel.addView(textView);
    }

    private void addLegend() {
        // Add separator
        View separator = new View(this);
        separator.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 2));
        separator.setBackgroundColor(Color.parseColor("#BDBDBD"));
        infoPanel.addView(separator);

        // Legend title
        TextView legendTitle = new TextView(this);
        legendTitle.setText("🗺️ Legend:");
        legendTitle.setTextSize(12);
        legendTitle.setTypeface(null, Typeface.BOLD);
        legendTitle.setPadding(8, 4, 8, 4);
        infoPanel.addView(legendTitle);

        // Legend items
        addLegendItem("🔴 = Active Incident");
        addLegendItem("🟠 = Waiting Incident");
        addLegendItem("🟢 = Resolved Incident");
        addLegendItem("🚒 = Vehicle");
        addLegendItem("⚠️ = Risk Zone");
    }

    private void addLegendItem(String text) {
        TextView textView = new TextView(this);
        textView.setText("  " + text);
        textView.setTextSize(11);
        textView.setPadding(8, 1, 8, 1);
        textView.setTextColor(Color.DKGRAY);
        infoPanel.addView(textView);
    }
}