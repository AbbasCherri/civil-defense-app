package com.example.civildefence.models;

import com.google.gson.annotations.SerializedName;

public class Incident {
    private int id;

    @SerializedName("citizen_id")
    private int citizenId;

    private String category;
    private String priority;
    private String status;
    private double latitude;
    private double longitude;
    private String description;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("closed_at")
    private String closedAt;

    public int getId() { return id; }
    public int getCitizenId() { return citizenId; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getDescription() { return description; }
    public String getCreatedAt() { return createdAt; }
    public String getClosedAt() { return closedAt; }
}