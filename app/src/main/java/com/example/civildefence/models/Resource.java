package com.example.civildefence.models;

import com.google.gson.annotations.SerializedName;

public class Resource {
    private int id;
    private String name;
    private String type;
    private String status;

    @SerializedName("fuel_usage")
    private double fuelUsage;

    @SerializedName("last_inspection")
    private String lastInspection;

    @SerializedName("created_at")
    private String createdAt;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public double getFuelUsage() { return fuelUsage; }
    public String getLastInspection() { return lastInspection; }
    public String getCreatedAt() { return createdAt; }
}