package com.example.civildefence.models;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class DailyReport {
    private String date;

    @SerializedName("total_incidents")
    private int totalIncidents;

    @SerializedName("status_breakdown")
    private Map<String, Integer> statusBreakdown;

    @SerializedName("category_breakdown")
    private Map<String, Integer> categoryBreakdown;

    @SerializedName("priority_breakdown")
    private Map<String, Integer> priorityBreakdown;

    public String getDate() { return date; }
    public int getTotalIncidents() { return totalIncidents; }
    public Map<String, Integer> getStatusBreakdown() { return statusBreakdown; }
    public Map<String, Integer> getCategoryBreakdown() { return categoryBreakdown; }
    public Map<String, Integer> getPriorityBreakdown() { return priorityBreakdown; }
}